package ee.telia.parking.helper.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

@Slf4j
@Configuration
public class InjectFileContentAutoConfiguration {

  @Bean
  public InjectFileContentHandler injectFileContentHandler() {
    return new InjectFileContentHandler();
  }

  static class InjectFileContentHandler implements BeanPostProcessor {

    @Autowired
    ApplicationContext ctx;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {

      Class<?> clazz = AspectUtil.unwrapProxy(bean).getClass();
      do {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
          if (field.isAnnotationPresent(InjectFileContent.class)) {
            log.info("Found '@{}' annotation in class '{}'",
                InjectFileContent.class.getSimpleName(), clazz);
            InjectFileContent source = field.getAnnotation(InjectFileContent.class);
            boolean accessChanged = false;
            try {
              if (!field.isAccessible()) {
                accessChanged = true;
                field.setAccessible(true);
              }
              if (source.value().length() > 0) {
                field.set(bean, loadResource(source.value()));
              }

            } catch (Exception e) {
              throw new RuntimeException(e);
            } finally {
              if (accessChanged) {
                field.setAccessible(false);
              }
            }

          }
        }
        clazz = clazz.getSuperclass();
      }
      while (clazz != null);

      return bean;
    }

    private String loadResource(String fileName) {
      InputStream is = null;
      try {
        Resource r = ctx.getResource(fileName);
        if (!r.exists()) {
          log.error("Unable to load resource '{}' content, resource does not exist !", fileName);
          throw new RuntimeException("Unable to load resource " + fileName);
        } else {
          is = r.getInputStream();
          java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
          return s.hasNext() ? s.next() : "";
        }
      } catch (Exception ignore) {
        throw new RuntimeException(ignore);
      } finally {
        if (null != is) {
          try {
            is.close();
          } catch (IOException ignore) {
          }
        }
      }
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
      return o;
    }
  }
}