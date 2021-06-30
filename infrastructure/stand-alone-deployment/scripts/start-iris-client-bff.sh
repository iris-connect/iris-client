source .env

## Variables depending on .env
export SPRING_DATASOURCE_URL=jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$POSTGRES_DB
export SPRING_DATASOURCE_USERNAME=$POSTGRES_USER
export SPRING_DATASOURCE_PASSWORD=$POSTGRES_PASSWORD
export SECURITY_JWT_JWT_SHARED_SECRET=$SECURITY_JWT_JWT_SHARED_SECRET
export SECURITY_AUTH_DB_ADMIN_USER_NAME=$SECURITY_AUTH_DB_ADMIN_USER_NAME
export SECURITY_AUTH_DB_ADMIN_USER_PASSWORD=$SECURITY_AUTH_DB_ADMIN_USER_PASSWORD
export PROXY_SERVICE_TARGET_SUBDOMAIN=$PROXY_SUBDOMAIN
export PROXY_SERVICE_TARGET_PROXY=$EPS_PP_NAME
export IRIS_LOCATION_SERVICE_ENDPOINT=$EPS_LS_NAME

## Fixed Variables
export SPRING_PROFILES_ACTIVE=prod
export PROXY_SERVICE_EPS_NAME=$PROXY_OP
export PROXY_OP=$PROXY_OP
export EPS_CLIENT_EPS_CLIENT_URL=https://localhost:5556/jsonrpc
export EPS_CLIENT_PROXY_CLIENT_URL=https://localhost:7766/jsonrpc
export IRIS_CLIENT_BASEPATH=https://localhost

## Run application

# First we find the jar file with the latest version 
JAR=$(ls -Art bin |sort | tail -n 1)

echo "Running IRIS Client BFF with Jar file $JAR \n"

# Then we run the latest version
java -jar bin/$JAR
