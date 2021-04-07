FROM caddy/caddy:2.3.0-alpine

# TODO: update to node 14 as soon as caddy uses newer alpine version
# install node + npm
RUN apk add --no-cache nodejs=12.21.0-r0 npm=12.21.0-r0

# make the 'app' folder the current working directory
WORKDIR /app

# copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./

# install project dependencies
RUN npm ci

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

# build app for production with minification
RUN npm run build

# copy build artifacts to webserver root
RUN rm -rf /usr/share/caddy
RUN mv dist /usr/share/caddy

# copy webserver configuration
COPY ./Caddyfile /etc/caddy/Caddyfile

# note: exposed port needs to match port in Caddyfile
EXPOSE 28080