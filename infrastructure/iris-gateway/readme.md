# Iris gateway helm chart

## Environments
There are 3 supported environments: `local`, `staging` & `production`.

### local
A local kubernetes cluster, e.g. minikube. Install with `helm upgrade --install --namespace iris-gateway --create-namespace --set environment=local iris-gateway .`.

#### local docker images on minikube
To use local images (instead of pulling them from docker hub), you have to use minikube's docker environment, 
and build the images there:
1. `eval $(minikube docker-env)`
1. `./mvnw spring-boot:build-image -DskipTests`

#### local database
Postgres is created as a container. Secrets are created automatically; see [secret.yaml](templates/postgres/secret.yaml).

#### local test for public-server
`curl -sk $(minikube service --url --https iris-gateway-public) | jq`

### staging
The staging kubernetes cluster, provided by AKDB. Install with `helm upgrade --install --namespace iris-gateway --create-namespace --set environment=staging iris-gateway .`.

#### staging database
Postgres is created as a container. Secrets have to be created manually _before_ deploying this chart, 
with the name `postgres-staging`. For required secret keys/env vars, see [secret.yaml](templates/postgres/secret.yaml).

### production
The production kubernetes cluster, provided by AKDB. Install with `helm upgrade --install --namespace iris-gateway --create-namespace --set environment=production iris-gateway .`.

#### production database
Postgres is provided outside the kubernetes cluster. Secrets have to be created manually _before_ deploying this chart,
with the name `postgres-production`. For required secret keys/env vars, see [secret.yaml](templates/postgres/secret.yaml).

## TODOs
- persistent storage for postgres container: local?
- health checks
- log configuration (JSON)
