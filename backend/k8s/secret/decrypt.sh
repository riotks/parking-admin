#!/bin/bash
#
# Kubernetes Secret decryption script
set -e

usage() {
  echo "Kubernetes Secret decryption wrapper for OpenSSL"
  echo "usage: $0 [file_name] [decryption_key]"
  echo ""
  echo "file_name         Name of encrypted values file"
  echo "decryption_key    Kubernetes Secret values decryption key"
  exit
}

if [ -z "$2" ]
then
  usage
fi

FILE_CONTENT=$(cat "$1")
if [ -z "$FILE_CONTENT" ];
then
  echo "Input file $1 is empty"
  exit
fi

OPENSSL_CMD=$(command -v openssl)

$OPENSSL_CMD enc -d -base64 -aes-256-cbc -md md5 -in "${1}" -pass pass:"${2}"