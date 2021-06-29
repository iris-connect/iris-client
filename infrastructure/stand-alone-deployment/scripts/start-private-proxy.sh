source .env

## Variables depending on .env
export PROXY_SETTINGS=conf/proxy/roles/$IRIS_ENV/private-proxy
export PROXY_OP=$PROXY_OP
export PROXY_DOMAIN=$PROXY_SUBDOMAIN
export PROXY_TLS_CERT=$PROXY_TLS_CERT
export PROXY_TLS_CERT_KEY=$PROXY_TLS_CERT_KEY
export PROXY_CLIENT_CERT=$PROXY_CLIENT_CERT
export PROXY_CLIENT_CERT_KEY=$PROXY_CLIENT_CERT_KEY
export PROXY_URL=$PROXY_URL

## Fixed Variables
export IRIS_CLIENT_BFF_ENDPOINT=http://localhost:8092/data-submission-rpc
export PRIVATE_PROXY_EPS_ENDPOINT=https://localhost:7766/jsonrpc

## Run application ( requires proxy to be in PATH )
proxy -level trace run private