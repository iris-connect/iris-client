# IRIS Client - Upgrade Guides

## IRIS-Client - Docker Compose Installation

### Release Candidate RC-11 und RC-12 auf 1.0.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.0.0) finden Sie Informationen zum Release 1.0.0.

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.0.0-rc.13/deployment-1.0.0-rc.13.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 
2. Wechseln Sie in das aktuelle `Installationsverzeichnis`.
3. Ersetzen Sie ihre aktuelle `docker-compose.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose.yml .
    ```
4. Ersetzen Sie ihre aktuelle `docker-compose-ext-postgres.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose-ext-postgres.yml .
    ```    
5. Sollten Sie individuelle Änderungen vorgenommen haben ( z.B. eine eigene Network Konfiguration), ziehen Sie diese bitte nach. 
6. Fahren Sie die aktuelle Version herunter. 
    ```
    $ docker-compose down
    ```    
    > Bitte unbedingt ohne die Option (-v) ausführen. 
7. Installieren Sie die aktuellen Images.
    ```
    $ docker-compose pull
    ```
8. Fahren Sie die neue Version hoch.
    ```
    $ docker-compose up -d
    ```
9. Loggen Sie sich mit Ihrem Benutzer ein und gehen Sie auf `Über IRIS`. Unter Version sollten Sie die neue Version `1.0.0` vorfinden. 

## IRIS-Client - Stand-Alone Installation 

### Release Candidate RC-11 und RC-12 auf 1.0.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.0.0) finden Sie Informationen zum Release 1.0.0.

Dieser Upgrade-Guide geht davon aus, dass Sie den IRIS Client in einem `Installationsverzeichnis` installiert haben und dabei die Stand-Alone Ordner Struktur benutzen.

#### EPS

1. Laden Sie sich [aktuelle EPS Version 0.1.61](https://github.com/iris-connect/eps/releases/tag/v0.1.61) herunter. Sie benötigen die Binaries `eps` und `proxy` für Ihre Ziel-Plattform. 

2. Stellen Sie sich, dass `eps` und `proxy` in Ihrem Pfad liegen. 

#### IRIS Client BFF

1. Laden Sie sich [aktuelle EPS Version vom IRIS CLient BFF](https://github.com/iris-connect/iris-client/releases/download/v1.0.0-rc.13/iris-client-bff-1.0.0-rc.13.jar) herunter. Ihre Ziel-Plattform. 

2. Kopieren sie die ausführbare Datei in den `bin` Ordner vom `Installationsverzeichnis`.  

    ```
    cp <Jar Datei> <Installationsverzeichnis>/bin
    ```

#### IRIS CLient Frontend

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.0.0-rc.13/iris-client-fe-1.0.0-rc.13.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren Sie den gesamten Inhalt vom `Zielverzeichnis` in den `public` Ordner vom `Installationsverzeichnis`.
    > Sollten Sie einen eigenen Webserver betreiben, müssen Sie den Pfad entsprechend anpassen. 
    ```
    cp -R <Zielverzeichnis>/* <Installationsverzeichnis>/public
    ```

#### Konfigurations-Dateien

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.0.0-rc.13/stand-alone-deployment-1.0.0-rc.13.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren und ersetzen Sie den gesamten Inhalt der EPS Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/eps/roles <Installationsverzeichnis>/conf/eps    
    ```

3. Kopieren und ersetzen Sie den gesamten Inhalt der PROXY Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/proxy/roles <Installationsverzeichnis>/conf/proxy    
    ```

3. Kopieren und ersetzen Sie den gesamten Inhalt der PROXY Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
