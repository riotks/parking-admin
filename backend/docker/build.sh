#!/bin/bash
set -e

if [ $# -ne 1 ]; then
  echo 'Supply project name as first argument'
  exit 1
fi

export PROJECT_NAME="$1"
PROJECT_TAG="${bamboo_planRepository_branchName:?}-${bamboo_buildNumber:?}"

DOCKER_CMD=$(command -v docker)
DOCKER_HOST="docker.patton.estpak.ee"
IMAGE_FILE="image.txt"
CLEANUP_MAX_COUNT="30"
CLEANUP_MAX_DAYS="30"

if [[ -z "$PROJECT_TAG" ]];
  then PROJECT_TAG="local";
fi

if [[ ${PROJECT_TAG} == master* ]] || [[ ${PROJECT_TAG}  == release* ]];
  then
    CLEANUP_LABEL="MAX_COUNT=${CLEANUP_MAX_COUNT}"
  else
    CLEANUP_LABEL="MAX_DAYS=${CLEANUP_MAX_DAYS}"
fi

if [[ ${PROJECT_TAG} == feature/* ]];
  then
    FORMATTED_TAG=$(echo ${PROJECT_TAG} | tr '[:upper:]' '[:lower:]' | sed 's#feature/##g')
  else
    FORMATTED_TAG=${PROJECT_TAG}
fi

${DOCKER_CMD} build \
--force-rm=true \
-t "$DOCKER_HOST/$PROJECT_NAME:$FORMATTED_TAG" \
-t "$DOCKER_HOST/$PROJECT_NAME:latest" \
--build-arg "version=$FORMATTED_TAG" \
--build-arg "PROJECT_NAME=$PROJECT_NAME" \
--build-arg "${CLEANUP_LABEL}" ./

${DOCKER_CMD} push "$DOCKER_HOST/$PROJECT_NAME:latest" && ${DOCKER_CMD} push "$DOCKER_HOST/$PROJECT_NAME:$FORMATTED_TAG"
echo "$DOCKER_HOST/$PROJECT_NAME:$FORMATTED_TAG" > ${IMAGE_FILE}