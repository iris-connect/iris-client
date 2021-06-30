# EPS Docker image for contact tracing apps

## Usage

Run the image with the correct environment set. The certs MUST match the envioronment e.g. you can only use test certs with IRIS test env. 

## Container Configuration

| ENV param | Values |
|-|-|
| EPS_SETTINGS | Can be one of the following depending on the environment.  <br /><br />test: `settings/roles/test/app`<br />prod: `settings/roles/prod/app`
| APP_CN | The Common Name of your App from your certificate |
| APP_BACKEND_ENDPOINT | The HTTP(s) endpoint where EPS can reach your app. |


## Certificates

1. Create a folder with your certificate and key

2. Both need to match the schema `${APP_CN}.crt` and `${APP_CN}.key`.

```
# Example: $APP_CN: Recover
└── certs
    ├── Recover.crt
    └── Recover.key
```
## Run the image

```
# Example
# $APP_CN:      Recover
# Environment:  test

docker run -v $(pwd)/certs:/app/settings/certs -e EPS_SETTINGS="settings/roles/test/app" -e APP_CN="Recover" -e APP_BACKEND_ENDPOINT="http://localhost" inoeg/app-eps:latest
```

