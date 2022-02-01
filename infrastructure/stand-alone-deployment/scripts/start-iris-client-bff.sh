#!/bin/bash

# exit when any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command failed with exit code $?."' ERR

source .env

## Import root certificates to Javas key store
echo "Running 'import-root-cert.sh' to check and import root certificates"

bash scripts/import-root-cert.sh

## Variables depending on .env
export SPRING_DATASOURCE_URL=jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$POSTGRES_DB
export SPRING_DATASOURCE_USERNAME=$POSTGRES_USER
export SPRING_DATASOURCE_PASSWORD=$POSTGRES_PASSWORD
export SECURITY_AUTH_DB_ADMIN_USER_NAME=$SECURITY_AUTH_DB_ADMIN_USER_NAME
export SECURITY_AUTH_DB_ADMIN_USER_PASSWORD=$SECURITY_AUTH_DB_ADMIN_USER_PASSWORD
export PROXY_SERVICE_TARGET_SUBDOMAIN=$PROXY_SUBDOMAIN
export PROXY_SERVICE_EPS_NAME=$PROXY_OP
export PROXY_OP=$PROXY_OP
export EPS_OP=$EPS_OP
export SPRING_PROFILES_ACTIVE=prod,stage_${IRIS_ENV}

## Fixed Variables
export EPS_CLIENT_EPS_CLIENT_URL=https://localhost:5556/jsonrpc
export EPS_CLIENT_PROXY_CLIENT_URL=https://localhost:7766/jsonrpc
export IRIS_CLIENT_BASEPATH=https://localhost

## Run application

# First we find the jar file with the latest version 
JAR=$(ls -Art bin |sort|grep jar| tail -n 1)

echo "Running IRIS Client BFF with Jar file $JAR \n"

# Then we run the latest version
java -jar bin/$JAR
