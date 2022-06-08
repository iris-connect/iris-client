FROM node:17-alpine as builder

# make the 'app' folder the current working directory
WORKDIR /app

# copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./

# install project dependencies
RUN npm ci --legacy-peer-deps

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

ARG VUE_APP_VERSION_ID=production
ARG VUE_APP_BUILD_ID=local

# build app for production with minification
RUN npm run build --legacy-peer-deps

FROM caddy:2.5.1-alpine

# copy build artifacts to webserver root
RUN rm -rf /usr/share/caddy
COPY --from=builder /app/dist /usr/share/caddy

# copy webserver configuration
COPY ./Caddyfile /etc/caddy/Caddyfile

# Create a group and user
RUN addgroup --gid 9999 iris && adduser --disabled-password --gecos '' --uid 9999 -G iris -s /bin/ash iris
COPY --chown=iris:iris ./autosave.json /config/caddy/autosave.json
# Change to non-root privilege
USER iris:iris

# note: exposed port needs to match port in Caddyfile
EXPOSE 28080
