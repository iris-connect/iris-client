# iris-client-frontend

This repository implements a frontend / web-application for the IRIS-client.

It is based on Vue.js + Vuetify

## Project setup

```bash
npm install
```

### Compiles and hot-reloads for development

```bash
npm run serve
```

### Run integration mode with real backend

```bash
npm run serve:int
```

The api base url which points to the sormas sidecar can be configured in:
`.env.integration`

### Compiles and minifies for production

```bash
npm run build
```

### Run your unit tests

```bash
npm run test:unit
```

### Run e2e tests (locally)

```bash
npm run test:e2e
```

Before running tests locally you have to create 2 files:
- `.env.e2e_test_local` (based on `.env.e2e_test_local.dist`)
- `cypress.env.json` (based on `cypress.env.json.dist`)

You can use the script `test:e2e:prepare` to generate the files.
If you are using Windows, please use the script `test:e2e:prepare:win`

**Do not commit / push the generated files!**

If you run the tests locally, the mock server is used for handling api requests.

To test with a "real" api server you have to:
1. Change the server address by editing the `VUE_APP_API_BASE_URL` parameter in the file `.env.e2e_test`.
2. Disable the mockup server by setting `VUE_APP_ENABLE_MOCK_SERVER` to `false` in the file `.env.e2e_test`.
3. Change the auth credentials for the app login in the file `cypress.env.json`.

**Do not commit / push your changes / the generated files!**

### Lints and fixes files

```bash
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

## App configuration

### Runtime config

The application is configured at runtime via environment variables.

This is achieved by first injecting the config using server side rendering into the global window scope.
See `public/index.html`.

Later on these values are picked up at runtime in the Vue app.
See `src/config.ts`.

### Enabling / Disabling index tracking features

To enable index tracking features:
`[[URL]]?indexTracking=enabled`

To disable index tracking features:
`[[URL]]?indexTracking=disabled`

Please note:
- The default setting is "disabled"
- Enabling / disabling the feature reloads the page and removes the query-string automatically
- The feature can be triggered on every page (even on index tracking pages)
- The enabled / disabled state is stored in localStorage

### Enabling / Disabling normalizer logging feature

To enable normalizer log:
`[[URL]]?normalizeLog=enabled`

To disable normalizer log:
`[[URL]]?normalizeLog=disabled`

Please note:
- The default setting is "disabled"
- Enabling / disabling the logging feature reloads the page and removes the query-string automatically
- The normalizer is always active / enabled - independent of the log settings
- The feature can be triggered on every page
- The enabled / disabled state is stored in localStorage

### Routes/views and params

In this section you find all deeplinks for our SPA.

We are currently not supporting any URL query parameters to e.g. prefill text fields for event creation.

#### Dashboard

```text
/
```

#### Event creation

```text
/events/new
```

### Event details

```text
/events/details/:id
```

#### Event list

```text
/events/list
```

#### Case creation

```text
/cases/new
```

### Case details

```text
/cases/details/:id
```

#### Case list

```text
/cases/list
```

### API Client

The api client was generated using a swagger code generator.

The api spec and instructions for code generation can be found here:  
<https://github.com/iris-gateway/IRIS>

### Start production server locally

We are using caddy as webserver for production.  
[https://caddyserver.com](https://caddyserver.com/)

It provides http basic auth and rewrites URLs to enable client side routing.

The webserver is configured using the Caddyfile which can be found in this directoy.

#### Run locally using docker

1. Build image

   ```bash
   docker build . -t fe:1.0
   ```

2. Run container

   ```bash
   docker container run --name web -p 8080:28080 -e SORMAS_SIDECAR_BASE_URL="https://api.staging.iris-gateway.de" fe:1.0
   ```

3. Open [http://localhost:8080](http://localhost:8080)

#### Run locally using caddy

Steps:

1. Build application

   ```bash
   npm run build
   ```

2. Open and edit Caddyfile to use local `dist` directory as web root

   ```bash
   # define web root
   root * dist
   ```

3. Install caddy  
   [https://caddyserver.com/docs/install](https://caddyserver.com/docs/install)

4. Configure runtime

   ```bash
   export SORMAS_SIDECAR_BASE_URL="https://api.staging.iris-gateway.de"
   export LOCAL_CONTACT_PERSON_NAME="Max Musterkontakt"
   export LOCAL_CONTACT_PERSON_PHONE="069 123 4567"
   export LOCAL_CONTACT_PERSON_MAIL="max.musterkontakt@ga.de"
   export CSV_EXPORT_STANDARD_ATOMIC_ADDRESS="false"
   ```

5. Start caddy

   ```bash
   caddy run
   ```

6. Open [http://localhost:28080](http://localhost:28080)
