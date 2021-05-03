FROM nginx
COPY config/nginx.conf /etc/nginx/
COPY config/sites-enabled/ /etc/nginx/sites-enabled/
COPY config/sites-enabled-sormas/ /etc/nginx/sites-enabled/
