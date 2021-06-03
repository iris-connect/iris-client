# IRIS Client Installation

Der IRIS Client ist der Teil vom IRIS Ecosystem, der auf einem Server im Gesundheitsamt (bzw. beim IT Dienstleiser) installiert wird und die Kernfunktionalitäten für Endbenutzer zur Verfügung stellt.

## Architektur

[Hier geht es weiter](./Architektur.md)


## Systemvoraussetzungen

Für den Betrieb aller Komponenten des IRIS Clients empfehlen wir folgende Server Dimension:

```
VM mit 2 vCores, 16 GB Ram und 20 GB Storage
```

Aus technischer Perspektive können alle IRIS Komponenten auf dem selben Server betrieben werden.

## Umgebungen

IRIS stellt zwei Umgebungen bereit. Staging zum Testen und Live für den Produktionsbetrieb mit echten Daten. 


## IRIS Client Komponenten und Abhängigkeiten

### Folgende Komponenten werden vom IRIS Team bereit gestellt.


| Komponente           | Beschreibung                                                                                                                                                                             | Technologie                                                                                                       | Download |
| -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | -|
| IRIS Client Frontend | Statische Web-Anwendung (Single Page Application)                                                                                                                                        | [Typescript](https://www.typescriptlang.org/), [Vue.js](https://vuejs.org/), [Vuetify](https://vuetifyjs.com/en/) | [Github](https://github.com/iris-connect/iris-client/releases) |
| IRIS Client Backend  | Backend Komponente für die Webanwendung. Kümmert sich um Authentifizierung und Authorisierung, stellte die gesicherte Verbindung zu den zentralen und de-zentralen IRIS Servicen bereit. | Java, [Spring Boot](https://spring.io/projects/spring-boot) | [Github](https://github.com/iris-connect/iris-client/releases) |                                                       |
| EPS                  | Endpunktserver für den Punkt-zu-Punkt Datenübertragung der Gästelisten und Kontakttagebüchern                                                       | [Go](https://golang.org/)                                                                                         |  [Github](https://github.com/iris-connect/eps/releases) |
| Proxy                  | Managed eingehende Verbindungen über Public Proxy.                                                       | [Go](https://golang.org/)                                                                                         |  [Github](https://github.com/iris-connect/eps/releases) |
| EPS (Proxy)                  | Endpunktserver für Proxy.                                                       | [Go](https://golang.org/)                                                                                         |  [Github](https://github.com/iris-connect/eps/releases) |

### Folgende Laufzeit-Abhängigkeiten werden vom IRIS Client vorausgesetzt.

| Laufzeit-Abhängigkeit | Beschreibung                                                                                                                                                                                                                                         |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Postgres DB           | Das IRIS Client Backend benutzt eine Postgres Datenbank für die a) Verwaltung der Benutzer und b) für die Speicherung der offenen Index Fall Anfragen und Ereignis Anfragen                                                                          |
| Webserver             | Für die Bereistellung vom IRIS Client Frontend über eine sichere HTTPS Verbindung wird ein Webserver benötigt. Dieser muss in der Lage sein a) Die statische Webanwendung auszuliefern und b) API Anfragen an das IRIS Client Backend weiterzuleiten |

> Die Standard-Installationsvariante mit Docker Compose bringt bereits ein vorkonfiguriertes Setup inklusive der Postgres DB, dem Webserver (nginx) und EPS mit.

### Des Weiteren werden folgende Konfigurations-Abhängigkeiten benötigt.

| Konfigurations-Abhängigkeit     | Beschreibung                                                                                                                                                                                                                                                                                                    |
| ------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Domain                          | Die vom GA bestimmte Domain unter der der IRIS Client für die Benutzer erreichbar ist (z.B. iris.bonn.local)                                                                                                                                                                                                    |
| Domain Zertifikat und Schlüssel | Für die o.g. Domain muss ein valides Zertifikat inklusive privatem Schlüssel bereitgestellt werden.                                                                                                                                                                                                             |
| GA Client Zertifikat            | Der IRIS Client benutzt ein für das GA ausgestelltes Client Zertifikat um mit den zentralen IRIS Servicen zu kommunizieren. Darüber hinaus werden alle Anfragen die vom IRIS Client ausgehen mit dem Zertifikat signiert. Für die Staging Umgebung ist der Prozess [hier](Certificate-Process-Staging.md) dokumentiert. |
| Proxy Client Zertifikat         | Der proxy benutzt ein für das GA ausgestelltes Client Zertifikat um mit den public proxy zu kommunizieren. Das Proxy Client Zertifikat ist nicht das selbe wie das GA Client Zertifikat. Für die Staging Umgebung ist der Prozess [hier](Certificate-Process-Staging.md) dokumentiert. |

### Des Weiteren gibt es folgende infrastrukturelle Anhängigkeiten

| Infrastruktur-Abhängigkeit                            | Beschreibung                                                                                                                         |
| ----------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| HTTPS Proxy                                           | Proxy server für EPS. Unterstützung für [HTTP_CONNECT](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/CONNECT) notwendig. |
| Proxy Freischaltung - IRIS Central Services - Staging | Zugriff auf iris.staging.iris-gateway.de (ConnectPorts: 3322, 4445, 5559, 9999)                                                            |
| Proxy Freischaltung - IRIS App Provider - Staging     | Zugriff auf \*.apps.staging.iris-gateway.de (ConnectPorts: 4444)                                                                     |
| Proxy Freischaltung - IRIS Central Services - Live    | - (TBD)                                                                                                                              |
| Proxy Freischaltung - IRIS App Provider - Live        | - (TBD)                                                                                                                              |

## Authentifizierung und Authorisierung

Aktuell bietet der IRIS client eine eigene Benutzerverwaltung an, die von einem IT Administrator betreut werden muss. Das Startpasswort für den Admin Account kann beim erstmaligen Starten gesetzt werden (siehe [.env.sample](../.env.sample)).

## IRIS Client - Stand-Alone Installation

[Hier geht es weiter. ](./Installation-Standalone.md)


## IRIS Client - Docker Compose Installation

[Hier geht es weiter. ](./Installation-Docker-Compose.md)

