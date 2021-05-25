# Installation Docker und Docker compose

Bevor man mit der Installation des IRIS Clients beginnen kann, muss man die Docker Umgebung Installieren. Die Art der Installation ist abhängig vom jeweiligen Betriebssystem des Servers.

## Linux Ubuntu

1. Installation der Docker Engine ist [hier beschrieben](https://docs.docker.com/engine/install/ubuntu/).

2. Installation von Docker Compose ist [hier beschrieben](https://docs.docker.com/compose/install/#install-compose-on-linux-systems).

## Microsoft Windows 10

1. Installation eines »Windows Subsystem für Linux« (WSL), da die Docker Engine ein unix-artiges Betriebssystem verlangt. [hier beschrieben](https://docs.microsoft.com/en-us/windows/wsl/install-win10). Gegenwärtig wurde die Linux-Distribution »Ubuntu 20.04 LTS« erprobt.

2. »Windows Subsystem für Linux« starten. Es gibt dafür eine Desktop-App. Erweitere Rechte (Starten als Administrator) sind nicht erforderlich.

3. Installation »Docker Desktop für Windows«, Desktop-Applikation, die alle Werkzeuge (Docker Engine, Docker Compose) mitbringt, um Docker auf Windows zu betreiben. [hier beschrieben](https://docs.docker.com/docker-for-windows/install/)

4. »Docker Desktop für Windows« starten - auch OHNE Administrator-Rolle möglich.

## Installation IRIS Client

### Download der Releases

Die Releases werden [über Github bereitgestellt](https://github.com/iris-connect/iris-client/releases). Dort finden Sie zudem alle Informationen bezüglich neuen Features und sonstigen Änderungen, die das Release mit sich bringt. 

Unterhalb vom Release befinden sich verschiedene Installations Artefakte (Assets). Für Docker Compose laden sie sich bitte das Asset `deployment-${version}.zip` herunter. 

### Strutur des Release Archives

Packen Sie zunächst das Release Archive aus

```
unzip `deployment-${version}.zip`
```

Die Struktur des Archives ist wie folgt.

```
.
├── conf 
│   ├── eps
│   │   ├── certs
│   │   │   ├── # hier müssen die GA Client Zertifikate für EPS abgelegt werden.
│   │   └── roles
│   │       └── hd
│   │           └── 001_default.yml # Die Konfiguration für den EPS service. Diese wird mit Umgebungsvariablen parameterisiert. 
│   └── nginx
│       ├── # hier müssen die Server Zertifikate für die Interne Domain abgelegt werden. 
│   └── proxy
│       ├── certs
│       │   ├── # hier müssen die Proxy Client Zertifikate abgelegt werden.
│       └── roles
│           ├── private-proxy
│           │   └── 001_default.yml # Die Konfiguration für den Proxy service. Diese wird mit Umgebungsvariablen parameterisiert. 
│           └── private-proxy-eps
│               └── 001_default.yml # Die Konfiguration für den Proxy EPS service. Diese wird mit Umgebungsvariablen parameterisiert. 
├── docker-compose-ext-postgres.yml # Die Docker Compose Konfiguration mit einer externen Postgres DB.
├── docker-compose.yml # Die Docker Compose Konfiguration mit einer embedded Postgres DB.
└── docs
    ├── # Die Dokumentation
```

### Anlegen der Konfiguration

Die Anwendung wird über eine `.env` Datei konfiguriert und auf das GA abgestimmt. Eine Beispiel Konfiguration inklusive einer Erklärung der einzelnen Variablen findet man [hier](../.env.sample).

Erstellen Sie Kopie von .env.sample

```
cp .env.sample .env
```

Passen Sie die Konfiguration auf ihr GA an.

```
.env öffnen und bearbeiten (siehe Konfiguration IRIS Client)
```

### Datenbankverbindung einrichten

Für die Einrichtung der Datenbank gibt es 2 Möglichkeiten. Man kann entweder a) die mitgelieferte Postgres DB benutzen (`docker-compose.yml`) oder b) eine eigene Postgres DB betrieben (`docker-compose-ext-postgres.yml`).

Für die *Variante a)* müssen folgende Parameter gesetzt werden. 

```
POSTGRES_USER 
POSTGRES_PASSWORD
```
Bitte wählen Sie ein Password, welches Ihrem internen Sicherheitsstandard entspricht. 

Für die *Variante b)* müssen folgende Parameter gesetzt werden. 

```
POSTGRES_USER 
POSTGRES_PASSWORD
POSTGRES_HOST
POSTGRES_DB
```
Die DB muss vorher von Ihnen angelegt werden. Der User benötigt die Berechtigung Tabellen zu erstellen und zu ändern. Das Datenbank Schema wird von der Applikations gemanaged und eventuelle Änderungen werden für Sie transparent migriert.

### Einrichtung der Authentifizierung

Der IRIS Client stellt die Benutzer [JSON Webtokens](https://de.wikipedia.org/wiki/JSON_Web_Token) aus. Dafür wird ein HMAC512 Algorithmus benutzt. Dieser Algorithmus verlangt ein starkes *Shared Secret*. Das kann wie folgt konfiguriert werden.

```
SECURITY_JWT_JWT_SHARED_SECRET
```

Wir empfehlen, einen Passwort Generator zu benutzen (z.B. https://passwordsgenerator.net). 

### Einrichtung Ihrer lokalen Domain

Der IRIS Client ist eine HTTPS basierte Webanwendung die unter einer Domain betrieben wird. Diese Domain ist für sie frei wählbar. Sie müssen für Ihre Domain ein validates Zertifikat unt den entsprechenden Schlüssel konfigurieren. 

Das Zertifikat und der Key müssen im Order `./conf/nginx` abgelegt werden. 

```
# Beispiel
.
├── conf 
│   └── nginx
│       ├── iris-ga.crt
│       ├── iris-ga.key
```

Danach müssen folgende Parameter in der Konfiguration gesetzt werden.

```
IRIS_CLIENT_DOMAIN=<Ihre Domain>
IRIS_CLIENT_DOMAIN_CERT=iris-ga.crt # Beispiel s.o.
IRIS_CLIENT_DOMAIN_CERT_KEY=iris-ga.key # Beispiel s.o.
```
### Einrichtung Startpasswort Admin Zugang

Wie oben beschrieben erstellt der IRIS Client initial einen Admin account. Folgende Parameter sind dafür vorgesehen.

```
SECURITY_AUTH_DB_ADMIN_USER_NAME
SECURITY_AUTH_DB_ADMIN_USER_PASSWORD
```

Sobald der Account beim ersten Starten angelegt wurde. Werden die Parameter ignoriert. 

### Einrichtung HTTPS Proxy

In den meisten GAs werden Verbindungen ins Internet über einen Proxy geleitet. Dafür ist folgender Parameter vorgesehen. 

```
HTTPS_PROXY
```

Der Proxy Server muss Tunneling über HTTP_CONNECT unterstützen. Weiter Einstellungen bezüglich der Ports und Domains sind weiter oben dokumentiert. 

### Einrichtung Service Directory

Wie in der [Architektur](./Architektur.md) beschrieben benötigt der IRIS Client Zugriff auf das IRIS Service Directory, damit es weiß welche Services unter welcher Adresse zu erreichen sind. Die richtige Service Directory URL hängt von der Umgebung (Staging oder Live ab). 

```
# Staging: 
EPS_SD_ENDPOINT=https://iris.staging.iris-gateway.de:3322/jsonrpc

# Live: 
EPS_SD_ENDPOINT=TBD
```

### Einrichtung Root Zertifikat

Wie in der [Architektur](./Architektur.md) beschrieben besteht das IRIS Netzwerk aus viele Akteuren die sich untereinander vertrauen. Dafür muss je nach Umgebung ein Zertifikat konfiguriert werden, welchem der IRIS Client vertraut. Die Zertifikate liegen dem IRIS Client bei und müssen je mach Umgebung konfiguriert werden.

Dafür muss folgender Parameter gesetzt werden.

```
# Staging: 
TRUSTED_CA_CRT=root-staging.crt

# Live: 
TRUSTED_CA_CRT=root-live.crt
```
### Einrichtung GA Client Zertifikat

Damit der IRIS Client sich im IRIS Netzwerk anmelden und mit anderen Teilnehmern kommunizieren und Daten austauschen kann, benötigt man ein IRIS GA Client Zertifikat. Der Prozess dafür ist [hier](Certificate-Process-Staging.md) dokumentiert. 

Nach dem man das Zertifikat erhalten hat, muss man es zusammen mit dem Schlüssel im Ordner `./conf/eps/certs` ablegen.

```
# Beispiel
├── conf
│   ├── eps
│   │   ├── certs
│   │   │   ├── ga-client.crt
│   │   │   ├── ga-client.key
```

Danach muss man die Zertifikate in der Konfiguration hinterlegen. Zudem muss man den Namen konfigurieren, unter dem sich der IRIS Client im Netzwerk anmeldet. Der Name ist der selbe wie der CN Name aus dem Zertifikats Prozess.

```
EPS_OP=${CN aus dem Zertifikat}
EPS_CLIENT_CERT=ga-client.crt # Beispiel s.o.
EPS_CLIENT_CERT_KEY=ga-client.key # Beispiel s.o.
```

### Einrichtung Proxy Client Zertifikat

Damit der IRIS Client wie in der [Architektur](./Architektur.md) beschrieben eingehende Verbindungen über das IRIS Proxy Netzwerk erlauben kann, benötigt man ein Proxy Client Zertifikat. Der Prozess dafür ist [hier](Certificate-Process-Staging.md) dokumentiert. 

Nach dem man das Zertifikat erhalten hat, muss man es zusammen mit dem Schlüssel im Ordner `./conf/proxy/certs` ablegen.

```
# Beispiel
.
├── conf
│   └── proxy
│       ├── certs
│       │   ├── ga-client-proxy.crt
│       │   ├── ga-client-proxy.key
```
Danach muss man die Zertifikate in der Konfiguration hinterlegen. Zudem muss man den Namen konfigurieren, unter dem sich der IRIS Client im Netzwerk anmeldet. Der Name ist der selbe wie der CN Name aus dem Zertifikats Prozess.

```
PROXY_OP=${CN aus dem Zertifikat}
PROXY_CLIENT_CERT=ga-client-proxy.crt
PROXY_CLIENT_CERT_KEY=ga-client-proxy.key
```

### IRIS Client starten und testen

1. IRIS Client mit Docker Compose und interner Postgres starten.

   ```
   docker-compose up -d
   ```

1. IRIS Client mit Docker Compose und externer Postgres starten.

   ```
   docker-compose -f docker-compose-ext-postgres.yml up -d
   ```

1. Überprüfen ob alle services laufen

   ```
   # embedded Postgres
   docker-compose ps

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml ps
   ```

1. Die Logfiles der aller Services einsehen

   ```
   # embedded Postgres
   docker-compose logs -f

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml logs -f
   ```

1. Die Logfiles eines bestimmten Services einsehen

   ```
   # embedded Postgres
   docker-compose logs -f nginx

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml logs -f nginx
   ```


1. Überprüfen ob der Webserver eine gültige Antwort liefert

   ```
   curl -k -v https://{IRIS_CLIENT_DOMAIN}
   ```

### Beispiel Konfiguration

Weitere Erläuterungen zu den einzelnen Parametern finden man in der [.env.sample](.env.sample).

