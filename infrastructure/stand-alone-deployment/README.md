# IRIS-Client - Stand-Alone Installation

> Die Anleitung wurde auf Linux und macOS getestet.

Bei dieser Installationsvariante werden die einzelnen Pakete des IRIS-Clients heruntergeladen und über Skripte gestartet. Dafür gibt es folgende vordefinierte Ordnerstruktur.

```
.
├── bin # Hier wird das JAR vom IRIS-Client BFF abgelegt
├── ca # Root CA Dateien für staging und Live-Umgebung.
├── conf # Konfigurationsdateien für die Komponenten
│   ├── eps
│   │   ├── certs # Hier werden die Zertifikate und Schlüssel von mTLS-Zertifikat - EPS (IRIS-Client BFF)	 und mTLS-Zertifikat - EPS (IRIS Private-Proxy) abgelegt 
│   │   └── roles
│   │       ├── live
│   │       │   ├── hd
│   │       │   └── private-proxy-eps
│   │       └── staging
│   │           ├── hd
│   │           └── private-proxy-eps
│   ├── nginx
│   └── proxy
│       ├── certs # Hier wird das Zertifikat und der Schlüssel vom TLS-Zertifikat - Private-Proxy abgelegt.
│       └── roles
│           ├── live
│           │   └── private-proxy
│           └── staging
│               └── private-proxy
├── db # Hier speichert der private-proxy seine Announcements.
├── public  # Hier müssen die Dateien vom IRIS-Client Frontend abgelegt werden.
└── scripts # Start Skripte der Komponenten
```

Die Ordnerstruktur kann wie sie ist von Github übernommen werden und wird im weiteren Verlauf als Installationspfad referenziert.

## Ausstellung von Zertifikaten

Wie [hier](../deployment/docs/Installation.md#folgende-zertifikate-werden-f%C3%BCr-die-externe-kommunikation-ben%C3%B6tigt) beschrieben werden für den Betrieb des IRIS-Clients verschiedene Zertifikate benötigt. Der Prozess der Austellung der Zertifikate unterscheidet sich je nach Umgebung (Staging oder Live).

### Zertifikate für Staging-Umgebung

Bitte folgen Sie [dieser Anleitung für die Staging-Umgebung](../deployment/docs/Certificate-Process-Staging.md). 

### Zertifikate für Produktions-Umgebung

Bitte folgen Sie [dieser Anleitung für die Produktions-Umgebung](../deployment/docs/Certificate-Process_Prod_technical.md). 



## Vorbereitung

1. eps und Proxy installieren

Auf dem System _muss_ die letzte Version von **eps** und **Proxy** installiert sein. Diese kann aus dem [öffentlichen Repository](https://github.com/iris-connect/eps/releases) heruntergeladen werden.
    ```
    # Test ob die Komponenten installiert sind

    → eps
    ERRO[0000] invalid input data: name(expected a string), directory(not a map), channels(not a list)

    → proxy
    NAME:
    Proxy Server - Run all proxy server commands

    USAGE:
    proxy [global options] command [command options] [arguments...]

    COMMANDS:
    run      Run the public or private proxy
    help, h  Shows a list of commands or help for one command

    GLOBAL OPTIONS:
    --level value  The desired log level (default: "info")
    --help, -h     show help
    ```
1. Einrichtung IRIS-Client BFF
    
    Das IRIS-Client BFF kann aus dem [öffentlichen Repository](https://github.com/iris-connect/iris-client/releases) heruntergeladen werden. Danach muss das Jahr in den Ordner `./bin` kopiert werden.

    ```
    # Beispiel am Release 1.0.0-rc.8 
    cp iris-client-bff-1.0.0-rc.8.jar <Installationspfad>/bin
    ```
1. Einrichtung IRIS-Client Frontend	

    > Diese Anleitung funktioniert wenn Sie NGINX benutzen

    Beim Frontend handelt es sich um statisches HTML. Es kann aus dem [öffentlichen Repository](https://github.com/iris-connect/iris-client/releases) heruntergeladen werden. Danach muss der Inhalt des Archives in das `public` Verzeichnis kopiert werden.

    ```
    # Beispiel: 1.0.0-rc.8
    unzip -d <Installationspfad>/public iris-client-fe-1.0.0-rc.8.zip
    ```
    Aufgrund eines aktuellen Bugs müssen Sie die index.html bearbeiten und folgende Änderung vornehmen.
    ```
    vi <Installationspfad>/public/index.html
    # IST
    localContactPerson: {
        name:
        "{{env "LOCAL_CONTACT_PERSON_NAME"}}",
        phone:
        "{{env "LOCAL_CONTACT_PERSON_PHONE"}}",
        mail:
        "{{env "LOCAL_CONTACT_PERSON_MAIL"}}",
    }

    # SOLL
    localContactPerson: {
        name:
        "",
        phone:
        "",
        mail:
        "",
    }
    ```
    Dieses Problem ist bekannt und wird unter diesem [Ticket](https://github.com/iris-connect/iris-client/issues/182) bearbeitet.
    
1. Zertifikate an der richtigen Stelle hinterlegen.

    a) Kopieren Sie das Zertifikat und den Schlüssel vom `TLS-Zertifikat - Private Proxy` in den Ordner `conf/proxy/certs`.

    ```
    cp <Ihr Pfad zum TLS-Zertifikat - Private Proxy> conf/proxy/certs
    cp <Ihr Pfad zum Schlüssel vom TLS-Zertifikat - Private Proxy> conf/proxy/certs
    ```

    b) Kopieren Sie das Zertifikat und den Schlüssel vom `mTLS-Zertifikat - EPS ( IRIS Client BFF )` in den Ordner `conf/eps/certs`.

    ```
    cp <Ihr Pfad zum mTLS-Zertifikat - EPS ( IRIS Client BFF )> conf/eps/certs
    cp <Ihr Pfad zum Schlüssel vom mTLS-Zertifikat - EPS ( IRIS Client BFF )> conf/eps/certs
    ```

    c) Kopieren Sie das Zertifikat und den Schlüssel vom `mTLS-Zertifikat - EPS ( IRIS Private Proxy )` in den Ordner `conf/eps/certs`.

    > Für die Produktions-Umgebung wird nur ein Zertifikat und Schlüssel für `IRIS Private Proxy` und `IRIS Client BFF` benutzt. 

    ```
    cp <Ihr Pfad zum mTLS-Zertifikat - EPS ( IRIS Private Proxy )> conf/eps/certs
    cp <Ihr Pfad zum Schlüssel vom mTLS-Zertifikat - EPS ( IRIS Private Proxy )> conf/eps/certs
    ```



# Konfiguration

## Anlegen der Konfiguration

Die Anwendung wird über eine [`.env`-Datei](https://docs.docker.com/compose/environment-variables/#the-env-file) konfiguriert und auf das Gesundheitsamt abgestimmt. Eine Beispielkonfiguration inklusive einer Erklärung der einzelnen Variablen findet man in der beigelegten [`.env.sample`-Datei](../.env.sample).

Erstellen Sie Kopie von `.env.sample`

```
cp .env.sample .env
```

Passen Sie die Konfiguration auf ihr GA an.

```
.env öffnen und bearbeiten (siehe nachfolgende Schritte)
```

## Datenbankverbindung einrichten

Hier können Sie die Verbindung zu Ihrer Postgres DB einrichten.
```
POSTGRES_USER 
POSTGRES_PASSWORD
POSTGRES_HOST
POSTGRES_DB
```
Die DB muss vorher von Ihnen angelegt werden. Der User benötigt die Berechtigung Tabellen zu erstellen und zu ändern. Das Datenbankschema wird von der Applikation verwaltet und eventuelle Änderungen werden für Sie transparent migriert.

## Einrichtung der Benutzer-Authentifizierung

Der IRIS-Client stellt die Benutzer [JSON Webtokens](https://de.wikipedia.org/wiki/JSON_Web_Token) aus. Dafür wird ein HMAC512 Algorithmus benutzt. Dieser Algorithmus verlangt ein starkes *Shared Secret*. Das kann wie folgt konfiguriert werden:

```
SECURITY_JWT_JWT_SHARED_SECRET
```

Wir empfehlen, einen Passwortgenerator zu benutzen (z.B. https://passwordsgenerator.net). 

## Einrichtung Ihrer lokalen Domain

> Diese Anleitung geht davon aus, dass Sie NGINX benutzen

Der IRIS-Client ist eine HTTPS-basierte Webanwendung die unter einer Domain betrieben wird. Diese Domain ist für Sie frei wählbar. Sie müssen für Ihre Domain ein valides Zertifikat und den entsprechenden Schlüssel konfigurieren. 

Das Zertifikat und der Schlüssel müssen im Order `./conf/nginx` abgelegt werden. 

```
# Beispiel
.
├── conf
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

## Einrichtung Startpasswort Admin-Zugang

Wie oben beschrieben erstellt der IRIS-Client initial einen Admin-Account. Folgende Parameter sind dafür vorgesehen.

```
SECURITY_AUTH_DB_ADMIN_USER_NAME
SECURITY_AUTH_DB_ADMIN_USER_PASSWORD
```

Sobald der Account beim ersten Starten angelegt wurde, werden die Parameter ignoriert. 

## Einrichtung HTTP Connect Proxy

In den meisten GAs werden Verbindungen ins Internet über einen Proxy geleitet. Dafür ist folgender Parameter vorgesehen. 

```
PROXY_URL
```

Der Proxy-Server muss Tunneling über HTTP_CONNECT unterstützen. Weitere Einstellungen bezüglich der Ports und Domains welche freigegeben werden müssen finden Sie [hier](../deployment/docs/Installation.md#des-weiteren-gibt-es-folgende-kommunikationsverbindungen). 

## Einrichtung IRIS Umgebung

IRIS bietet zwei Umgebungen an (Staging und Produktion). Mit diesem Parameter können Sie konfigurieren, zu welcher der beiden Umgebungen sich der IRIS-Client verbinden soll. Es gibt zwei vordefinierte Werte für diesen Parameter. 

```
# Staging: IRIS_ENV=staging
# Live: IRIS_ENV=live
IRIS_ENV=
```

Von dieser Einstellung hänger unter anderem ab, zu welchem Service Directory sich Ihr IRIS-Client verbindet und welchen ROOT-Zertifikaten vertraut wird. 

## Einrichtung des Service-Directory Endpunkts

Wie in der [Architektur](../deployment/docs/Architektur.md) beschrieben, benötigt der IRIS-Client Zugriff auf das IRIS Service-Directory, damit es weiß welche Services unter welcher Adresse zu erreichen sind. Die richtige Service-Directory-URL hängt von der Umgebung (Staging oder Live ab). 

```
# Staging: 
EPS_SD_ENDPOINT=https://test.iris-gateway.de:32324/jsonrpc

# Live: 
EPS_SD_ENDPOINT=https://prod.iris-gateway.de:32324/jsonrpc
```
## Einrichtung vom IRIS Locations Service

Wie in der [Architektur](../deployment/docs/Architektur.md) beschrieben, benötigt der IRIS-Client Zugriff auf den IRIS Locations Service, damit die Mitarbeiter im GA u.a. nach Betrieben und Gastronomien suchen können. Es gibt zwei vordefinierte Werte für diesen Parameter. 

```
# staging:  ls-1
# live:     locations-production-1
EPS_LS_NAME=
```

## Einrichtung des IRIS Public-Proxy

Wie in der [Architektur](../deployment/docs/Architektur.md) beschrieben, benötigt der IRIS-Client Zugriff auf den IRIS Public-Proxy-Service, damit eingehende Daten wie z.B. Kontakttagebücher empfangen werden können. Es gibt zwei vordefinierte Werte für diesen Parameter. 

```
# staging:  public-proxy-1
# live:     public-proxy-production-1
EPS_PP_NAME=
```


## Einrichtung: TLS-Zertifikat (Private-Proxy)

Wie in der [Architektur](../deployment/docs/Architektur.md) beschrieben, kann der IRIS-Client über das Proxy-Netzwerk Kontakttagebücher entgegennehmen. Dafür muss ein TLS Zertifikat und dessen privater Schlüssel konfiguriert werden. 

Die Domain unterscheidet sich je nach Umgebung. 

Für Staging gibt es eine vom IRIS-Team zur Verfügung gestellte Domain namens `proxy.test-gesundheitsamt.de`. 

Für die Produktions-Umgebung wurde Ihnen die Domain vom Land zentral bereit gestellt. Sie sollten dazu Informationen erhalten haben.

```
PROXY_SUBDOMAIN=<Ihre zugewiesene Domain>
```

Wie oben beschrieben sollte das Zertifikat und der private Schlüssel im Verzeichnis `./conf/proxy/certs` abgelegt sein. 

```
.
├── conf
    └── proxy
       └── certs 
           ├── <TLS-Zertifikat - Private Proxy>
           └── <Der dazugehörige private Schlüssel>

```

Tragen Sie dann die beiden Dateien in die Konfiguration ein.

```
PROXY_TLS_CERT=<TLS-Zertifikat - Private Proxy>
PROXY_TLS_CERT_KEY=<Der dazugehörige private Schlüssel>
```



## Einrichtung: mTLS-Zertifikat - EPS ( IRIS Client BFF )

Damit der IRIS-Client sich im IRIS-Netzwerk anmelden und mit anderen Teilnehmern kommunizieren und Daten austauschen kann, wird ein IRIS-Client-Zertifikat benötigt. Der Prozess dafür ist weiter oben in der Anleitung dokumentiert.

Nach dem man das Zertifikat erhalten hat, muss man es zusammen mit dem Schlüssel im Ordner `./conf/eps/certs` ablegen.

```
# Beispiel
├── conf
│   ├── eps
│   │   ├── certs
│   │   │   ├── <mTLS-Zertifikat - EPS ( IRIS Client BFF )>
│   │   │   └── <Der dazugehörige private Schlüssel>
```

Danach müssen die Namen des Zertifikates und dessen Schlüssel in der Konfiguration eintragen werden. Zudem muss der Namen konfiguriert werden, unter dem sich der IRIS-Client im Netzwerk anmeldet. Der Name ist der selbe wie der CN-Name aus dem Zertifikats-Prozess.

```
EPS_OP=${CN aus dem Zertifikat}
EPS_CLIENT_CERT=<mTLS-Zertifikat - EPS ( IRIS Client BFF )> # Beispiel s.o.
EPS_CLIENT_CERT_KEY=<Der dazugehörige private Schlüssel> # Beispiel s.o.
```

## Einrichtung: mTLS-Zertifikat - EPS ( IRIS Private Proxy ) 

Damit der IRIS Client wie in der [Architektur](../deployment/docs/Architektur.md) beschrieben eingehende Verbindungen über das IRIS Proxy Netzwerk erlauben kann, benötigt man ein Proxy Client Zertifikat. Der Prozess dafür ist weiter oben in der Anleitung dokumentiert.

Das Zertifikat muss es zusammen mit dem Schlüssel im Ordner `./conf/eps/certs` abgelegt werden.

> Für die Prpduktions-Umgebung wird nur ein Zertifikat und Schlüssel für `IRIS Private Proxy` und `IRIS Client BFF` benutzt. 
> 
> Es unterscheidet sich der Parameter `PROXY_OP`. Dieser Wert muss auf den 2. SAN Eintrag gesetzt werden (eps-proxy.\<Ihre zugewiesene Domain\>).
 
```
# Beispiel
├── conf
│   ├── eps
│   │   ├── certs
│   │   │   ├── <mTLS-Zertifikat - EPS ( IRIS Private Proxy ) >
│   │   │   └── <Der dazugehörige private Schlüssel>
```
Danach müssen die Namen des Zertifikates und dessen Schlüssel in der Konfiguration eingetragen werden. Zudem muss der Name konfiguriert werden, unter dem sich der IRIS-Client im Netzwerk anmeldet. Der Name ist der selbe wie der CN-Name aus dem Zertifikats-Prozess.

```
PROXY_OP=${CN oder ein SAN Eintrag}
PROXY_CLIENT_CERT=<mTLS-Zertifikat - EPS ( IRIS Private Proxy ) >
PROXY_CLIENT_CERT_KEY=<Der dazugehörige private Schlüssel>
```


# Starten der Anwendung

```
# EPS Server starten
sh scripts/start-eps.sh

# Proxy-Server eps starten
sh scripts/start-private-proxy-eps.sh

# Proxy-Server starten
sh scripts/start-private-proxy.sh

# IRIS-Client Backend for Frontend starten
sh scripts/start-iris-client-bff.sh 

# NGINX starten (kann in ihrem Fall auch ein anderer Webserver sein. Dient hier nur als Beispiel)
nginx -p $(pwd) -c conf/nginx/nginx.conf
```

