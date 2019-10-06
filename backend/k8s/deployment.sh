#!/bin/bash
#
# Kubernetes deployment script for Java API-s
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
  echo "Kubernetes deploy script for Spring Boot pattern Java applications"
  echo "usage: $0 [project_name] [k8s_namespace] [k8s_token] [k8s_cluster] [decryption_key] [image_name]"
  echo ""
  echo "project_name    Name of project or application"
  echo "k8s_namespace   Kubernetes namespace"
  echo "k8s_token       Kubernetes deployment token"
  echo "k8s_cluster     Kubernetes cluster (skube or vkube)"
  echo "decryption_key  Kubernetes Secret values decryption key"
  echo "image_name      Docker image name (optional)"
  exit
}

if [ -z "$5" ]
then
  usage
fi

# Configure Kubernetes base parameters

KUBE_NAMESPACE="$2"
KUBE_TOKEN="$3"
KUBECTL_CMD=$(command -v kubectl)

KUBE_DEPLOYMENT="deployment.yml"

KUBE_CONFIGMAP="configmap/configmap.yml"
KUBE_CONFIGMAP_PROPERTIES=""

KUBE_SECRET="secret/secret.yml"
KUBE_SECRET_DECRYPTION_KEY="$5"
KUBE_SECRET_VALUES=""

BASE_CMD="$KUBECTL_CMD -v4 --token=$KUBE_TOKEN --insecure-skip-tls-verify=true --namespace=$KUBE_NAMESPACE"

declare -A VKUBE_SERVERS=(
  ["mus-vkube"]="https://mus-vkubemaster.estpak.ee"
  ["adr-vkube"]="https://adr-vkubemaster.estpak.ee"
)
declare -A SKUBE_SERVERS=(
  ["mus-skube"]="https://mus-skubemaster.estpak.ee"
  ["adr-skube"]="https://adr-skubemaster.estpak.ee"
)

KUBE_CLUSTER="$4"
KUBE_SERVERS=""
if [ "$KUBE_CLUSTER" = "skube" ]
then
  KUBE_SERVERS_STRING=$(declare -p SKUBE_SERVERS)
elif [ "$KUBE_CLUSTER" = "vkube" ]
then
  KUBE_SERVERS_STRING=$(declare -p VKUBE_SERVERS)
else
  usage
fi
eval "declare -A KUBE_SERVERS=""${KUBE_SERVERS_STRING#*=}"

# Configure application specific parameters

PROJECT_NAME="$1"
export APP_IMAGE="$6"

if [ -z "$APP_IMAGE" ]
then
  export IMAGE_FILE="image.txt"
  APP_IMAGE=$(cat "$IMAGE_FILE")
  export APP_IMAGE
fi

# Configure environment specific parameters

if [[ $KUBE_NAMESPACE =~ .*dev.* ]]
then
   KUBE_CONFIGMAP_PROPERTIES="configmap-dev.properties"
   KUBE_SECRET_VALUES="application-dev.yml"
fi

if [[ $KUBE_NAMESPACE =~ .*test.* ]]
then
   KUBE_CONFIGMAP_PROPERTIES="configmap-test.properties"
   KUBE_SECRET_VALUES="application-test.yml"
fi

if [[ $KUBE_NAMESPACE =~ .*live.* ]]
then
   KUBE_CONFIGMAP_PROPERTIES="configmap-live.properties"
   KUBE_SECRET_VALUES="application-live.yml"
fi

# Read environment variables from properties

props_parse "configmap/values/$KUBE_CONFIGMAP_PROPERTIES"

# Parse secret from YML file

APPLICATION_YML=$(source ./secret/decrypt.sh "secret/values/$KUBE_SECRET_VALUES" "$KUBE_SECRET_DECRYPTION_KEY" | encode)

# Deploy to all specified servers

for KUBE_SERVER in "${!KUBE_SERVERS[@]}"; do

  KUBE_URL="${KUBE_SERVERS[$KUBE_SERVER]}"
  KUBE_CMD="$BASE_CMD --server=$KUBE_URL"

  echo "Start $KUBE_SERVER"
    echo "Applying secret..."
  if [ -z "$APPLICATION_YML" ]
  then
    echo "Secret $KUBE_SECRET_VALUES content is empty, skipping..."
  else
    sed_subst "APPLICATION_YML" "$APPLICATION_YML" < "$KUBE_SECRET" | $KUBE_CMD apply -f -
  fi
  echo "Applying configmap..."
  env_subst "$KUBE_CONFIGMAP" | $KUBE_CMD apply -f -
  echo "Applying deployment..."
  env_subst "$KUBE_DEPLOYMENT" | $KUBE_CMD apply -f -
  sleep 1
  echo "Watching rollout..."
  $KUBE_CMD rollout status "deployment/$PROJECT_NAME"
  echo "Finished $KUBE_SERVER"

done
