# IRIS-Client Installation

Der IRIS-Client ist der Teil vom IRIS Ecosystem, der auf einem Server im Gesundheitsamt (bzw. beim IT-Dienstleister) installiert wird und die Kernfunktionalitäten für Endbenutzer zur Verfügung stellt.

## Architektur

[Architekturbeschreibung](./Architektur.md)

## Systemvoraussetzungen

Für den Betrieb aller Komponenten des IRIS-Clients empfehlen wir folgende Serverdimensionierung:

```
VM mit 2 vCores, 16 GB Ram und 20 GB Storage
```

Aus technischer Perspektive können alle IRIS-Komponenten auf demselben Server betrieben werden.

## Umgebungen

IRIS stellt zwei Umgebungen bereit. Staging zum Testen und Live für den Produktionsbetrieb mit echten Daten.

## IRIS-Client Komponenten und Abhängigkeiten

### Folgende Komponenten werden vom IRIS Team bereitgestellt.

| Komponente | Beschreibung | Technologie | Download |
| - | - | - | - | 
| IRIS Client Frontend | Statische Web-Anwendung (Single Page Application)                                                                                                                                   | [Typescript](https://www.typescriptlang.org/), [Vue.js](https://vuejs.org/), [Vuetify](https://vuetifyjs.com/en/) | [Github](https://github.com/iris-connect/iris-client/releases) |
| IRIS Client BFF  | Backend for Frontend Komponente für die Webanwendung. Kümmert sich um Authentifizierung und Authorisierung, stellt die gesicherte Verbindung zu den zentralen und dezentralen IRIS Teilnehmern bereit. | Java, [Spring Boot](https://spring.io/projects/spring-boot)                                                       | [Github](https://github.com/iris-connect/iris-client/releases) |     |
| EPS ( IRIS Client BFF )                  | Endpunktserver für den Punkt-zu-Punkt Datenübertragung der Gästelisten und Kontakttagebüchern                                                                                          | [Go](https://golang.org/)                                                                                         | [Github](https://github.com/iris-connect/eps/releases)         |
| Private Proxy                | Verwaltet eingehende Verbindungen über Public Proxy.                                                                                                                                   | [Go](https://golang.org/)                                                                                         | [Github](https://github.com/iris-connect/eps/releases)         |
| EPS ( Private Proxy )          | Endpunktserver vom Private Proxy. Wird benutzt, um eigehende Verbindung z.B. für die Übermittlung von Kontakttagebüchern anzukündigen.                                                            | [Go](https://golang.org/)                                                                                         | [Github](https://github.com/iris-connect/eps/releases)         |

### Folgende Laufzeit-Abhängigkeiten werden vom IRIS-Client vorausgesetzt.

| Laufzeit-Abhängigkeit | Beschreibung                                                                                                                                                                                                                                         |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Postgres DB           | Das IRIS-Client Backend benutzt eine Postgres Datenbank für die a) Verwaltung der Konten und b) für die Speicherung der offenen Indexfall-Anfragen und Ereignis-Anfragen                                                                             |
| Webserver             | Für die Bereitstellung vom IRIS-Client Frontend über eine sichere HTTPS Verbindung wird ein Webserver benötigt. Dieser muss in der Lage sein a) Die statische Webanwendung auszuliefern und b) API Anfragen an das IRIS-Client Backend weiterzuleiten |
| HTTPS Proxy ( Optional ) | Proxy Server für EPS Kommunikation. Unterstützung für [HTTP_CONNECT](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/CONNECT) notwendig. Dies ist nicht notwendig, sollte Ihr Server direkte Firewall Freischaltungen ins Internet besitzen. |

> Die Standard-Installationsvariante mit Docker Compose bringt bereits ein vorkonfiguriertes Setup inklusive der Postgres DB, dem Webserver (nginx) und EPS mit.

### Folgende Zertifikate werden für die externe Kommunikation benötigt:

| Konfigurations-Abhängigkeit     | Beschreibung                                                                                                                                                                                                                                                                                                            |
| ------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Zertifikat der internen Web Domain | Der IRIS Client wird in der Regel unter einer GA internen Domain bereit gestellt. Für diese Domain muss ein valides Zertifikat inklusive privatem Schlüssel konfiguriert werden. |
| TLS-Zertifikat - Private Proxy | Ein TLS-Zertifikat für den IRIS Private Proxy des GA. Identität des GA im Internet mit dem Ziel, Kontakttagebücher und Gästelisten direkt ins GA zu übermitteln (TLS/HTTPS). Der Ausstellungs-Prozess für die **Staging Umgebung** ist [hier](Certificate-Process-Staging.md) dokumentiert. Der Ausstellungs-Prozess für die **Live Umgebung** ist [hier](Certificate-Process_Prod_technical.md) dokumentiert.|
| mTLS-Zertifikat - EPS ( IRIS Client BFF )         | Der IRIS-Client benutzt ein für das GA ausgestelltes mTLS Zertifikat, um mit den zentralen IRIS Services zu kommunizieren. Darüber hinaus werden alle Anfragen, die vom IRIS-Client ausgehen, mit dem Zertifikat signiert. Der Ausstellungs-Prozess für die **Staging Umgebung** ist [hier](Certificate-Process-Staging.md) dokumentiert. Der Ausstellungs-Prozess für die **Live Umgebung** ist [hier](Certificate-Process_Prod_technical.md) dokumentiert. |
| mTLS-Zertifikat - EPS ( IRIS Private Proxy ) | Der Proxy benutzt ein für das GA ausgestelltes mTLS Zertifikat, um mit dem public-Proxy zu kommunizieren und eingehende Verbindungen, z.B. zum Übertragen von Kontakttagebüchern, zuzulassen. Der Ausstellungs-Prozess für die **Staging Umgebung** ist [hier](Certificate-Process-Staging.md) dokumentiert. Der Ausstellungs-Prozess für die **Live Umgebung** ist [hier](Certificate-Process_Prod_technical.md) dokumentiert.                                   |

> In der Live Umgebung handelt es sich bei den beiden EPS Zertifikaten um das gleiche Zertifikat.


### Des Weiteren gibt es folgende Kommunikationsverbindungen

> Diese Verbindungen müssen sowohl bei direkter als auch bei indirekter Kommunkation ( via Proxy ) erlaubt werden. 

|     Service                |     Umgebung |     Ziel-Host                   |     Ziel-Ports          |
|----------------------------|--------------|---------------------------------|------------------------|
|     IRIS Service Directory |     staging  |     test.iris-gateway.de        |     32324              |
|     IRIS Locations Service |     staging  |     test.iris-gateway.de        |     32323              |
|     IRIS Public Proxy      |     staging  |     test.iris-gateway.de        |     32326, 32327       |
|     IRIS Connected Apps    |     staging  |     *.apps.test.iris-gateway.de |     4443, 4444, 443    |
|     IRIS Service Directory |     live     |     prod.iris-gateway.de        |     32324              |
|     IRIS Locations Service |     live     |     prod.iris-gateway.de        |     32323              |
|     IRIS Public Proxy      |     live     |     prod.iris-gateway.de        |     32326, 32327       |
|     IRIS Connected Apps    |     live     |     *.apps.prod.iris-gateway.de |     4444, 443          |
## Authentifizierung und Authorisierung

Aktuell bietet der IRIS-Client eine eigene Kontoverwaltung an, die von der IT-Administration betreut werden muss. Das Startpasswort für den Admin Account kann beim erstmaligen Starten gesetzt werden (siehe [.env.sample](../.env.sample)).

## IRIS-Client - Docker Compose Installation

Bei dieser Installationsvariante werden alle Komponenten vom IRIS Client in virtuellen Containern gestartet. Diese Container sind vom IRIS Team vorkonfiguriert und müssen um GA individuelle Parameter ergänzt werden. 

> Diese Installationsart ist empfohlen.

[Docker Compose Installation](./Installation-Docker-Compose.md)


## IRIS-Client - Stand-Alone Installation

Bei dieser Installationsvariante werden alle Komponenten vom IRIS Client von einem lokalen Administrator installiert und um individuelle Parameter ergänzt. Die Komponenten werden vom IRIS Team über Github zum Download bereitgestellt.  

> Diese Installationsart ist aufgrund der komplexen Konfiguration NICHT empfohlen.

[Stand-Alone Installation](../../stand-alone-deployment/README.md)

## Testen der Installation

Unabhängig von der Installations Variante können Sie testen, ob die Installation erfolgreich war.

[Test](Smoketest.md)

## Upgrade Guides

Unabhängig von der Installations Variante finden Sie hier Informationen über Upgrades auf eine neue Version. 

[Upgrade Guides](Upgrade-Guides.md)

## Backups

Die Verantwortung, Backups der Installation durchzuführen, liegt bei den Administrierenden.

Wir empfehlen die verwendete Datenbank, die Inhalte des Volumes `proxy_db_iris`, die eigenen Zertifikate und eventuell angepasste `.env` und Konfigurationsdateien regelmäßig zu sichern.

