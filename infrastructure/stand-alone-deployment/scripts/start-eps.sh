source .env

## Variables depending on .env
export EPS_SETTINGS=conf/eps/roles/$IRIS_ENV/hd       
export EPS_OP=$EPS_OP
export EPS_SD_NAME=$EPS_SD_NAME
export EPS_LS_NAME=$EPS_LS_NAME
export EPS_PP_NAME=$EPS_PP_NAME
export EPS_CLIENT_CERT=$EPS_CLIENT_CERT
export EPS_CLIENT_CERT_KEY=$EPS_CLIENT_CERT_KEY
export EPS_SD_ENDPOINT=$EPS_SD_ENDPOINT
export PROXY_URL=$PROXY_URL

## Fixed Variables
export IRIS_CLIENT_BFF_ENDPOINT=http://localhost:8092/data-submission-rpc

## Run application ( requires eps to be in PATH )
eps -level trace server run 
