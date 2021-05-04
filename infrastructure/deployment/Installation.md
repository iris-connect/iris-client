# IRIS Client

Der IRIS Client ist der Teil vom IRIS Ecosystem, der auf einem Server im Gesundheitsamt (bzw. beim IT Dienstleiser) installiert wird und die Kernfunktionalitäten für Endbenutzer zur Verfügung stellt. 

## Systemvoraussetzungen

Für den Betrieb des IRIS Clients empfehlen wir folgende Dimension:

```
VM mit 2 vCores, 16 GB Ram und 20 GB Storage
```

## IRIS Client Komponenten

Folgende Komponenten werden vom IRIS Team bereit gestellt. 

| Komponente | Beschreibung | Technologie |
|-|-|-|
| IRIS Client Frontend | Statische Web-Anwendung (Single Page Application) | [Typescript](https://www.typescriptlang.org/), [Vue.js](https://vuejs.org/), [Vuetify](https://vuetifyjs.com/en/) |
| IRIS Client Backend | Backend Komponente für die Webanwendung. Kümmert sich um Authentifizierung und Authorisierung, stellte die gesicherte Verbindung zu den zentralen und de-zentralen IRIS Servicen bereit. | Java, [Spring Boot](https://spring.io/projects/spring-boot) |

Folgende Laufzeit-Abhängigkeiten werden vom IRIS Client vorausgesetzt. 

| Laufzeit-Abhängigkeit | Beschreibung  |
|-|-|
| Postgres DB | Das IRIS Client Backend benutzt eine Postgres Datenbank für die a) Verwaltung der Benutzer und b) für die Speicherung der offenen Index Fall Anfragen und Ereignis Anfragen |
| Webserver | Für die Bereistellung des IRIS Client Frontend über eine sichere HTTPS Verbindung wird ein Webserver benötigt. Dieser muss in der Lage sein a) Die statische Webanwendung auszuliefern und b) Anfragen an die API an das Backend weiterzuleiten |
| Proxy Server | In vielen GAs werden ausgehende Verbindungen über einen Proxy Server geroutet. Der IRIS Client stellt eine Konfigurationsmöglichkeit dafür zur Verfügung. |

> Die Standard-Installationsvariante mit Docker Compose bringt bereits ein vorkonfiguriertes Setup inklusive der Postgres DB und dem Webserver (nginx) mit. 

Desweiteren werden folgende Konfigurations-Abhängikeiten benötigt. 

| Konfigurations-Abhängigkeit | Beschreibung  |
|-|-|
| Domain | Die vom GA anhängige Domain unter der der IRIS Client für die Benutzer erreichbar ist (z.B. iris.bonn.local) |
| Domain Zertifikat und Schlüssel | Für die o.g. Domain muss ein valides Zertifikat inklusive privatem Schlüssel bereitgestellt werden. |
| GA Client Zertifikat | Der IRIS Client benutzt ein für das GA ausgestelltes Client Zertifikat um mit den zentralen IRIS Servicen zu kommunizieren. Darüber hinaus werden alle Anfragen die vom IRIS Client ausgehen mit dem Zertifikat signiert. |


## Authentifizierung und Authorisierung

Aktuell bietet der IRIS client eine eigene Benutzerverwaltung an, die von einem IT Administrator betreut werden muss. 



## IRIS Client - Installation mit Docker Compose

Für diese Installationsart wird der IRIS Client in einer [Docker](https://docker.io) Umgebung gestartet. Die notwendigen Images werden vom [INÖG Vereichnis im Dockerhub](https://hub.docker.com/u/inoeg) heruntergeladen. 

### Installation Docker und Docker compose

Bevor man mit der Installation des IRIS Clients beginnen kann, muss man die Docker Umgebung Installieren. Die Art der Installation ist abhängig vom jeweiligen Betriebssystem des Servers.

#### Linux Ubuntu

1) Installation der Docker Engine ist [hier beschrieben](https://docs.docker.com/engine/install/ubuntu/).

2) Installation von Docker Compose ist [hier beschrieben](https://docs.docker.com/compose/install/#install-compose-on-linux-systems).

#### Microsoft Windows 10

1) Installation eines »Windows Subsystem für Linux« (WSL), da die Docker Engine ein unix-artiges Betriebssystem verlangt. [hier beschrieben](https://docs.microsoft.com/en-us/windows/wsl/install-win10). Gegenwärtig wurde die Linux-Distribution »Ubuntu 20.04 LTS« erprobt. 

2) Installation »Docker Desktop für Windows«, Desktop-Applikation, die alle Werkzeuge (Docker Engine, Docker Compose) mitbringt, um Docker auf Windows zu betreiben. [hier beschrieben](https://docs.docker.com/docker-for-windows/install/)

### Installation IRIS Client



1) Auspacken des Installations Archives

    ```
    unzip iris-client.zip
    ```

1) Eine Kopie von .env.sample erstellen

    ```
    cp .env.sample .env
    ```

1) Konfigurations Parameter anpassen

    ```
    .env öffnen und bearbeiten (siehe Konfiguration IRIS Client)
    ```

1) IRIS Client mit Docker Compose und interner Postgres starten.

    ```
    docker-compose up -d
    ```

1) IRIS Client mit Docker Compose und externer Postgres starten.

    ```
    docker-compose -f docker-compose-ext-postgres.yml up -d
    ```


3) Überprüfen ob alle services laufen

    ```
    # embedded Postgres
    docker-compose ps

    # external Postgres
    docker-compose -f docker-compose-ext-postgres.yml ps
    ```

4) Die Logfiles der Services einsehen

    ```
    # embedded Postgres
    docker-compose logs -f

    # external Postgres
    docker-compose -f docker-compose-ext-postgres.yml logs -f 
    ```

5) Überprüfen ob der Webserver eine gültige Antwort liefert

    ```
    curl -k -v https://{IRIS_CLIENT_DOMAIN}
    ```

### Konfiguration IRIS Client

siehe [.env.sample](.env.sample)
