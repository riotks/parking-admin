FROM openjdk:11-jre-slim

ARG PROJECT_NAME

ARG MAX_COUNT
ARG MAX_DAYS
LABEL com.jfrog.artifactory.retention.maxCount=${MAX_COUNT}
LABEL com.jfrog.artifactory.retention.maxDays=${MAX_DAYS}

# Add artifact
ENV ARTIFACT_NAME="$PROJECT_NAME.jar" \
    BASE_PATH="/opt/app"

# Add Plumbr Java agent
ADD $ARTIFACT_NAME $BASE_PATH/$ARTIFACT_NAME
ADD "plumbr/" $BASE_PATH/plumbr/

VOLUME /tmp
ENV JAVA_OPTS="-XX:-OmitStackTraceInFastThrow"

EXPOSE 8069

ENTRYPOINT exec java $JAVA_OPTS \
    -Duser.timezone='Europe/Tallinn' \
    -Dplumbr.jvmId=$HOSTNAME-$SPRING_PROFILES_ACTIVE \
    -Dplumbr.clusterId=$PLUMBR_APPNAME \
    -javaagent:$BASE_PATH/plumbr/plumbr.jar \
    -jar $BASE_PATH/$ARTIFACT_NAME