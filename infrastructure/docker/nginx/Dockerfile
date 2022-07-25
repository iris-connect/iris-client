FROM nginx:1.23.1-alpine
RUN echo 'server_tokens off;' > /etc/nginx/conf.d/server_tokens.conf
COPY config/templates/ /etc/nginx/templates/
