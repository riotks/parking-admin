package ee.telia.parking.helper.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class AspectUtil {

  public static Object unwrapProxy(Object bean) {

    /*
     * If the given object is a proxy, set the return value as the object
     * being proxied, otherwise return the given object.
     */
    if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {

      Advised advised = (Advised) bean;

      try {
        if (null != advised.getTargetSource().getTarget()) {
          bean = advised.getTargetSource().getTarget();
        }
      } catch (Exception e) {
        log.error("Unable to unwrap AOP proxy {}", bean.getClass(), e);
      }
    }

    return bean;
  }
}