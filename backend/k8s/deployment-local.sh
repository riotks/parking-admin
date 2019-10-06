#!/bin/bash
set -e

env_subst() {
  eval "echo \"$(sed 's/\"/\\"/g' "$1")\""
}

sed_subst() {
  sed "s/{$1}/$2/g"
}

props_parse() {
  export PROPERTIES_FILE="$1"
  while IFS=$'\n\r' read -r property; do
    IFS='=' read -r key value <<< "$property"
    export "$key"="$value"
  done < "$PROPERTIES_FILE"
}

encode() {
  base64 -w 0
}

usage() {
  echo "Minikube deploy script for Spring Boot pattern Java applications"
  echo "usage: $0 [project_name] [image_name] [decryption_key] [k8s_namespace]"
  echo ""
  echo "project_name    Name of project or application"
  echo "image_name      Docker image name"
  echo "decryption_key  Kubernetes Secret values decryption key"
  echo "k8s_namespace   Kubernetes namespace (optional)"
  echo ""
  echo "dependencies:   kubectl, sed, openssl, base64"
  exit
}

if [ -z "$3" ]
then
  usage
fi

# Configure Kubernetes base parameters
KUBE_NAMESPACE="$4"
KUBE_CMD=$(command -v kubectl)

KUBE_DEPLOYMENT="deployment.yml"

KUBE_CONFIGMAP="configmap/configmap.yml"
KUBE_CONFIGMAP_PROPERTIES="configmap/values/configmap-dev.properties"

KUBE_SECRET="secret/secret.yml"
KUBE_SECRET_DECRYPTION_KEY="$3"
KUBE_SECRET_VALUES="secret/values/application-dev.yml"

# If namespace not specified, use Minikube default namespace
if [ -z "$KUBE_NAMESPACE" ]; then
  KUBE_NAMESPACE="default"
fi

MINIKUBE_CMD="$KUBE_CMD -v4 --namespace=$KUBE_NAMESPACE"

# Configure application specific parameters
PROJECT_NAME="$1"

# If image not specified, use default image name
export APP_IMAGE="$2"
if [ -z "$APP_IMAGE" ]; then
  export APP_IMAGE="$PROJECT_NAME:latest"
fi

# Read environment variables from properties
props_parse "$KUBE_CONFIGMAP_PROPERTIES"

# Parse secret from YML file
APPLICATION_YML=$(source ./secret/decrypt.sh "$KUBE_SECRET_VALUES" "$KUBE_SECRET_DECRYPTION_KEY" | encode)

# Deploy to local Minikube cluster
echo 'Start minikube'
sed_subst "APPLICATION_YML" "$APPLICATION_YML" < "$KUBE_SECRET" | $MINIKUBE_CMD apply -f -
env_subst "$KUBE_CONFIGMAP" | $MINIKUBE_CMD apply -f -
env_subst "$KUBE_DEPLOYMENT" | $MINIKUBE_CMD apply -f -
sleep 1
$MINIKUBE_CMD rollout status "deployment/$PROJECT_NAME"
echo 'Finished minikube'