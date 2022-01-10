# IRIS Client - Docker und Docker Compose Installation

Bevor man mit der Installation des IRIS connect Clients beginnen kann, muss die Docker Umgebung installiert werden. Die Art der Installation ist abhängig vom jeweiligen Betriebssystem des Servers.

## Linux Ubuntu

1. Installation der Docker Engine ist [hier beschrieben](https://docs.docker.com/engine/install/ubuntu/).

2. Installation von Docker Compose ist [hier beschrieben](https://docs.docker.com/compose/install/#install-compose-on-linux-systems).

## Microsoft Windows 10

1. Installation eines »Windows Subsystem für Linux« (WSL), da die Docker Engine ein unix-artiges Betriebssystem verlangt. [Hier beschrieben](https://docs.microsoft.com/en-us/windows/wsl/install-win10). Gegenwärtig wurde die Linux-Distribution »Ubuntu 20.04 LTS« erprobt.

2. »Windows Subsystem für Linux« starten. Es gibt dafür eine Desktop-App. Erweitere Rechte (Starten als Admin) sind nicht erforderlich.

3. Installation »Docker Desktop für Windows«, Desktop-Applikation, die alle Werkzeuge (Docker Engine, Docker Compose) mitbringt, um Docker auf Windows zu betreiben. [Hier beschrieben](https://docs.docker.com/docker-for-windows/install/)

4. »Docker Desktop für Windows« starten - auch OHNE Admin-Rolle möglich.

# IRIS Client Installation

## Ausstellung von Zertifikaten

Wie [hier](./Installation.md) beschrieben, werden für den Betrieb des IRIS Clients verschiedene Zertifikate benötigt. Der Prozess der Ausstellung der Zertifikate unterscheidet sich je nach Umgebung (Staging oder Live).

### Zertifikate für Staging Umgebung

Bitte folgen Sie [dieser Anleitung](./Certificate-Process-Staging.md). 

### Zertifikate für Live Umgebung

Bitte folgen Sie [dieser Anleitung](./Certificate-Process_Prod_technical.md). 

## Download der Releases

Die Releases werden über [Github bereitgestellt](https://github.com/iris-connect/iris-client/releases). Dort finden Sie zudem alle Informationen bezüglich neuer Features und sonstigen Änderungen, die das Release mit sich bringt. 

Unterhalb vom Release befinden sich verschiedene Installations-Artefakte (Assets). Für Docker Compose laden Sie sich bitte das Asset `deployment-${version}.zip` herunter. 

## Struktur des Release Archives

Packen Sie zunächst das Release Archive aus

```
unzip `deployment-${version}.zip`
```

Die Struktur des Archives ist wie nachfolgend beschrieben.

```
.
├── certs
│   ├── eps      # Zertifikate für EPS ( IRIS Client BFF ), Private Proxy und EPS ( Private Proxy ).
│   └── nginx    # Zertifikat und Schlüssel der internen Web Domain. 
├── docs         # Die Dokumentation
├── scripts      # Hilfs-Skripte für die Zertifikatsbestellung und -verwaltung.
├── docker-compose-ext-postgres.yml    # Die Docker Compose Konfiguration mit einer externen Postgres DB.
├── docker-compose.yml                 # Die Docker Compose Konfiguration mit einer embedded Postgres DB.
└─ .env.sample                         # Konfigurations-Template
```

## Anlegen der Konfiguration

Die Anwendung wird über eine [`.env`](https://docs.docker.com/compose/environment-variables/#the-env-file)  Datei konfiguriert und auf das GA abgestimmt. Eine Beispiel Konfiguration inklusive einer Erklärung der einzelnen Variablen findet man in der beigelegten [`.env.sample`](../.env.sample)  Datei.

Erstellen Sie eine Kopie von .env.sample

```
cp .env.sample .env
```

Passen Sie die Konfiguration auf ihr GA an.

```
.env öffnen und bearbeiten (siehe nachfolgende Schritte)
```

## Datenbankverbindung einrichten

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
Die DB muss vorher von Ihnen angelegt werden. Der User benötigt die Berechtigung, Tabellen zu erstellen und zu ändern. Das Datenbank-Schema wird von der Applikation verwaltet und eventuelle Änderungen werden für Sie transparent migriert.

## Einrichtung Ihrer lokalen Domain

Der IRIS Client ist eine HTTPS basierte Webanwendung, die unter einer Domain betrieben wird. Diese Domain ist frei wählbar. Sie müssen für Ihre Domain ein validiertes Zertifikat und den entsprechenden Schlüssel konfigurieren. 

Das Zertifikat und der Key müssen im Order `./certs/nginx` abgelegt werden. 

```
# Beispiel
.
├── certs
    └── nginx
        ├── iris-ga.crt
        └── iris-ga.key
```

Danach müssen folgende Parameter in der Konfiguration gesetzt werden.

```
IRIS_CLIENT_DOMAIN=<Ihre Domain>
IRIS_CLIENT_DOMAIN_CERT=iris-ga.crt # Beispiel s.o.
IRIS_CLIENT_DOMAIN_CERT_KEY=iris-ga.key # Beispiel s.o.
```

## Einrichtung des Ports ( Optional )

Standardmäßig wird das Frontend des IRIS Clients über den HTTPS Standard Port 443 ausgeliefert. Dies kann mit dem folgenden optionalen Parameter angepasst werden. 

```
IRIS_CLIENT_PORT
```


## Einrichtung Startpasswort Admin Zugang

Wie oben beschrieben erstellt der IRIS Client initial einen Admin Account. Folgende Parameter sind dafür vorgesehen.

```
SECURITY_AUTH_DB_ADMIN_USER_NAME
SECURITY_AUTH_DB_ADMIN_USER_PASSWORD
```

Sobald der Account beim ersten Starten angelegt wurde, werden die Parameter ignoriert. 

## Lokale Kontaktinformationen

Sie können für Ihre lokalen Benutzer:innen Informationen zum Support bereitstellen. Diese Informationen werden dann im Web-Interface angezeigt (Beispiel [hier](./images/support-information.png)). 

Folgende Parameter müssen dafür gesetzt sein.
```
LOCAL_CONTACT_PERSON_NAME
LOCAL_CONTACT_PERSON_MAIL
LOCAL_CONTACT_PERSON_PHONE
```

## Adressformat im CSV Export ( ab Version 1.0.0 )

Standardmäßig wird die Adresse für einen Eintrag in einer Gästeliste beim CSV Export in ein Feld geschrieben. Dieses Verhalten kann konfiguriert werden, so dass der IRIS Client die Adressdaten in 4 Felder aufteilt (Straße, PLZ, Hausnummer, Ort). 

Dafür muss das folgende Flag gesetzt werden.
```
CSV_EXPORT_STANDARD_ATOMIC_ADDRESS="true"
```

## Einrichtung HTTP Connect Proxy

In den meisten GAs werden Verbindungen ins Internet über einen Proxy geleitet. Dafür ist folgender Parameter vorgesehen. 

```
PROXY_URL
```

Der Proxy Server muss Tunneling über HTTP_CONNECT unterstützen. Weitere Einstellungen bezüglich der Ports und Domains, welche freigegeben werden müssen, finden Sie [hier](Installation.md). 

## Logdateien

Die Logausgaben aller Services werden vom IRIS Client in Dateien persistiert. Standardmäßig wird dafür im aktuellen Verzeichnis ein Order `logs` erstellt. Der Pfad dieses Ordners kann über den folgenden optionalen Parameter angepasst werden. 

```
LOG_FOLDER
```

## Einrichtung Root Zertifikat ( veraltet )

Bitte nicht mehr konfigurieren. Verwenden Sie stattdessen [Einrichtung IRIS Umgebung](#einrichtung-iris-umgebung).

## Einrichtung IRIS Umgebung

IRIS bietet 2 Umgebungen an (staging und live). Mit diesem Parameter können Sie konfigurieren, zu welcher der beiden Umgebungen sich der IRIS Client verbinden soll. Es gibt 2 vordefinierte Werte für diesen Parameter. 

```
# Staging: IRIS_ENV=staging
# Live: IRIS_ENV=live
IRIS_ENV=
```

Von dieser Einstellung hängt unter anderen ab, zu welchem Service Directory sich Ihr IRIS Client verbindet und welchem ROOT Zertifikat vertraut wird. 

## Einrichtung: TLS-Zertifikat ( Private Proxy )

Wie in der [Architektur](./Architektur.md) beschrieben, kann der IRIS Client über das Proxy Netzwerk Kontakttagebücher entgegennehmen. Dafür muss ein TLS Zertifikat und dessen privater Schlüssel konfiguriert werden. 

Die Domain unterscheidet sich je nach Umgebung. 

Für Staging gibt es eine vom IRIS Team zur Verfügung gestellte Domain namens `proxy.test-gesundheitsamt.de`. 

Für die Live-Umgebung wurde Ihnen die Domain vom Land zentral bereitgestellt. Sie sollten dazu Informationen erhalten haben.

```
PROXY_SUBDOMAIN=<Ihre zugewiesene Domain>
```

Kopieren Sie dann das Zertifikat und den privaten Schlüssel in das Verzeichnis `./certs/eps`. 

```
.
├── certs
    └── eps
        ├── <Ihr Zertifikat>
        └── <Der dazugehörige private Schlüssel>

```

Tragen Sie dann die beiden Dateien in die Konfiguration ein.

```
PROXY_TLS_CERT=<Ihr Zertifikat>
PROXY_TLS_CERT_KEY=<Der dazugehörige private Schlüssel>
```



## Einrichtung: mTLS-Zertifikat - EPS ( IRIS Client BFF )

Damit der IRIS Client sich im IRIS Netzwerk anmelden und mit anderen Teilnehmern kommunizieren sowie Daten austauschen kann, benötigt man ein IRIS GA Client Zertifikat. Der Prozess dafür ist weiter oben in der Anleitung dokumentiert.

Nach dem man das Zertifikat erhalten hat, muss man es zusammen mit dem Schlüssel im Ordner `./certs/eps` ablegen.

```
# Beispiel
.
├── certs
│   ├── eps
│   │   ├── ga-client.crt
│   │   └── ga-client.key
```

Danach muss man die Namen des Zertifikates und dessen Schlüssel in der Konfiguration eintragen. Zudem muss man den Namen konfigurieren, unter dem sich der IRIS Client im Netzwerk anmeldet. Der Name ist derselbe wie der CN Name aus dem Zertifikats-Prozess.

```
EPS_OP=${CN aus dem Zertifikat}
EPS_CLIENT_CERT=ga-client.crt # Beispiel s.o.
EPS_CLIENT_CERT_KEY=ga-client.key # Beispiel s.o.
```

## Einrichtung: mTLS-Zertifikat - EPS ( IRIS Private Proxy ) 

Damit der IRIS Client wie in der [Architektur](./Architektur.md) beschrieben eingehende Verbindungen über das IRIS Proxy Netzwerk erlauben kann, benötigt man ein Proxy Client Zertifikat. Der Prozess dafür ist weiter oben in der Anleitung dokumentiert.

Nach dem man das Zertifikat erhalten hat, muss man es zusammen mit dem Schlüssel im Ordner `./certs/eps` ablegen.

> Für die Live Umgebung sind das Zertifikat und der Schlüssel für den `IRIS Private Proxy` die gleichen wie für den `IRIS Client BFF`. 
> 
> Es unterscheidet sich der Parameter `PROXY_OP`. Dieser Wert muss auf den 2. SAN Eintrag gesetzt werden (eps-proxy.\<Ihr zugewiesene Domain\>).
 
```
# Beispiel
.
├── certs
│   └── eps
│       ├── ga-client-proxy.crt
│       └── ga-client-proxy.key
```
Danach muss man die Namen des Zertifikates und dessen Schlüssel in der Konfiguration eintragen. Zudem muss man den Namen konfigurieren, unter dem sich der IRIS Client im Netzwerk anmeldet. Der Name ist derselbe wie der CN Name aus dem Zertifikats-Prozess.

```
PROXY_OP=${CN oder ein SAN Eintrag}
PROXY_CLIENT_CERT=ga-client-proxy.crt
PROXY_CLIENT_CERT_KEY=ga-client-proxy.key
```

## IRIS Client starten und testen

1. IRIS Client mit Docker Compose und interner Postgres starten.

   ```
   docker-compose up -d
   ```

2. IRIS Client mit Docker Compose und externer Postgres starten.

   ```
   docker-compose -f docker-compose-ext-postgres.yml up -d
   ```

3. Überprüfen, ob alle Services laufen

   ```
   # embedded Postgres
   docker-compose ps

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml ps
   ```

4. Die Logfiles aller Services einsehen

   ```
   # embedded Postgres
   docker-compose logs -f

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml logs -f
   ```

5. Die Logfiles eines bestimmten Services einsehen

   ```
   # embedded Postgres
   docker-compose logs -f nginx

   # external Postgres
   docker-compose -f docker-compose-ext-postgres.yml logs -f nginx
   ```


6. Überprüfen, ob der Webserver eine gültige Antwort liefert

   ```
   curl -k -v https://{IRIS_CLIENT_DOMAIN}
   ```

## Beispiel Konfiguration

Weitere Erläuterungen zu den einzelnen Parametern finden Sie in der [`.env.sample`](./.env.sample) Datei.

