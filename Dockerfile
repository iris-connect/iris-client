FROM node:lts-alpine

# install simple http server for serving static content
RUN npm install -g http-server

# make the 'app' folder the current working directory
WORKDIR /app

# copy both 'package.json' and 'package-lock.json' (if available)
COPY package*.json ./

# install project dependencies
RUN npm install

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

# build app for production with minification
RUN npm run build

# 404.html will be served if a file is not found. 
# This can be used for Single-Page App (SPA) hosting to serve the entry page.
# https://www.npmjs.com/package/http-server
RUN mv dist/index.html dist/404.html

EXPOSE 28080
CMD [ "http-server", "dist", "--port", "28080" ]