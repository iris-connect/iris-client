#!/bin/bash

source .env

## Variables depending on .env
export EPS_SETTINGS=conf/eps/roles/$IRIS_ENV/private-proxy-eps
export PROXY_OP=$PROXY_OP
export PROXY_CLIENT_CERT=$PROXY_CLIENT_CERT
export PROXY_CLIENT_CERT_KEY=$PROXY_CLIENT_CERT_KEY
export PROXY_URL=$PROXY_URL

## Fixed Variables
export PRIVATE_PROXY_ENDPOINT=http://localhost:8877/jsonrpc

## Run application ( requires eps to be in PATH )
eps -level trace server run
