#!/bin/bash
set -e

# Set project variables
export PROJECT_NAME="$1"
PROJECT_TAG="$2"

# Find Docker command
DOCKER_CMD=$(command -v docker)

# If tag not specified, use currently active git branch as tag
if [ -z "$PROJECT_TAG" ]; then
  # Find Git command
  GIT_CMD=$(command -v git)
  # Find Git branch
  PROJECT_TAG=$($GIT_CMD rev-parse --abbrev-ref HEAD)
fi

if [[ ${PROJECT_TAG} == feature/* ]];
  then
    FORMATTED_TAG=$(echo ${PROJECT_TAG} | tr '[:upper:]' '[:lower:]' | sed 's#feature/##g')
  else
    FORMATTED_TAG=${PROJECT_TAG}
fi

# Build to local Docker registry
${DOCKER_CMD} build --force-rm=true -t "$PROJECT_NAME:$FORMATTED_TAG" -t "$PROJECT_NAME:latest" --build-arg version=latest ./