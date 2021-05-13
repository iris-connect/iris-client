#IRIS EPS

To communicate with the distributed services, IRIS uses its own EPS (endpoint server). The following describes how to install and configure the EPS.

## Local development setup

### Install EPS

The following steps are necessary:

- Clone eps from [https://github.com/iris-gateway/eps](https://github.com/iris-gateway/eps)
- Make eps binary and certificates according to [https://github.com/iris-gateway/eps#getting-started](https://github.com/iris-gateway/eps#getting-started) Go Compiler is needed.
- Copy certificates from cloned directory /settings/dev/certs to this directory /settings/dev/certs
- Switch to iris-client-eps directory
- Start server with `EPS_SETTINGS=settings/dev/roles/hd-1 eps --level debug server run`
- `eps-client.clientUrl=https://localhost:5556/jsonrpc` should be set in iris-client-bff
- Make sure at least iris-gateway/iris-location-service is started and running its EPS

## Staging

Todo:

- Create binary for linux / eps docker image
- Integrate staging config and certificates to docker image
- Deployment to staging
