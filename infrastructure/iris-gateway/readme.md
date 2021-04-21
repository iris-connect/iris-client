# Iris gateway helm chart

## Environments
There are 3 supported environments: `local`, `test` & `production`.

### local
A local kubernetes cluster, e.g. minikube. Install with `helm upgrade --install --namespace iris-gateway --create-namespace --set environment=local iris-gateway .`.

#### local docker images on minikube
To use local images (instead of pulling them from docker hub), you have to use minikube's docker environment, 
and build the images there:
1. `eval $(minikube docker-env)`
1. `./mvnw spring-boot:build-image -DskipTests`

You can also use the python scripts:

| script | purpose |
--- | ---
| `minikube-check-dependencies.py` | test if you have the required packages installed |
| `minikube-start.py` | start the minikube instance, if it's not running yet |
| `minikube-build-docker.py` | build the docker image for the minikube deployment - uses a remote docker connection |
| `minikube-deploy.py` | run the deployment |
| `minikube-undeploy.py` | remove the deployment |
| `minikube-stop.py` | stop the minikube instance |

#### local database
Postgres is created as a container. Secrets are created automatically; see [secret.yaml](templates/postgres/secret.yaml).

#### local test for public-server
`curl -sk $(minikube service --url --https iris-gateway-public) | jq`

### test
The test kubernetes cluster, provided by AKDB. Install with `helm upgrade --install --namespace iris-gateway --set environment=test iris-gateway .`.

#### test database
Postgres is created as a container. Secrets have to be created manually _before_ deploying this chart, 
with the name `postgres-test`. For required secret keys/env vars, see [secret.yaml](templates/postgres/secret.yaml).

### production
The production kubernetes cluster, provided by AKDB. Install with `helm upgrade --install --namespace iris-gateway --set environment=production iris-gateway .`.

#### production database
Postgres is provided outside the kubernetes cluster. Secrets have to be created manually _before_ deploying this chart,
with the name `postgres-production`. For required secret keys/env vars, see [secret.yaml](templates/postgres/secret.yaml).

## TODOs
- persistent storage for postgres container: local?
- health checks
- log configuration (JSON)
