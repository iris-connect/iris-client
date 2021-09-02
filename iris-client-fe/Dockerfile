FROM caddy:2.4.3-alpine

# install node + npm
RUN apk add --update nodejs npm

# make the 'app' folder the current working directory
WORKDIR /app

# copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./

# install project dependencies
RUN npm ci

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

ARG VUE_APP_VERSION_ID=production
ARG VUE_APP_BUILD_ID=local

# build app for production with minification
RUN npm run build

# copy build artifacts to webserver root
RUN rm -rf /usr/share/caddy
RUN mv dist /usr/share/caddy

# copy webserver configuration
COPY ./Caddyfile /etc/caddy/Caddyfile

# note: exposed port needs to match port in Caddyfile
EXPOSE 28080