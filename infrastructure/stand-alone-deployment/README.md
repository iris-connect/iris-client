# IRIS Client Stand-alone Konfiguration

Beispiel einer Installaton vom IRIS CLient ohne Docker. 

# Konfiguration

[Link zur config]

# Starten der Anwendung

```
# EPS Server Starten
sh scripts/start-eps.sh

# Proxy Server EPS Starten
sh scripts/start-private-proxy-eps.sh

# Proxy Server Starten
sh scripts/start-private-proxy.sh

# IRIS Client Backend for Frontend starten
sh scripts/start-iris-client-bff.sh 

# NGINX Starten (kann in ihrem Fall auch ein anderer Webserver sein. Dienst hier nur als Beispiel)
nginx -p $(pwd) -c conf/nginx/nginx.conf
```

