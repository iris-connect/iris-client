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

### Compiles and minifies for production

```bash
npm run build
```

### Run your unit tests

```bash
npm run test:unit
```

### Lints and fixes files

```bash
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

### API Client

The api client was generated using a swagger code generator.

The api spec and instructions for code generation can be found here:  
<https://github.com/iris-gateway/IRIS>

### Start production server locally

We are using caddy as webserver for production.  
[https://caddyserver.com](https://caddyserver.com/)

It provides http basic auth and rewrites URLs to enable client side routing.

You can start it locally using the Caddyfile found in this directoy.

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

4. Start caddy

   ```bash
   caddy run
   ```
