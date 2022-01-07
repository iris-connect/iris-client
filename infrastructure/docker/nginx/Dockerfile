FROM nginx:1.21.5-alpine
RUN echo 'server_tokens off;' > /etc/nginx/conf.d/server_tokens.conf
COPY config/templates/ /etc/nginx/templates/
