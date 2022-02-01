# IRIS Client - Upgrade Guides


## IRIS-Client - Docker Compose Installation

### 1.2.x -> 1.3.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.3.0) finden Sie Informationen zum Release 1.3.0.

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.3.0/deployment-1.3.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 
2. Wechseln Sie in das aktuelle `Installationsverzeichnis`.
3. Ersetzen Sie ihre aktuelle `docker-compose.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose.yml .
    ```
4. Ersetzen Sie ihre aktuelle `docker-compose-ext-postgres.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose-ext-postgres.yml .
    ```    
5. Sollten Sie individuelle Änderungen vorgenommen haben ( z.B. eine eigene Netzwerkkonfiguration), ziehen Sie diese bitte nach. 
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
9. Loggen Sie sich mit Ihrem Benutzer ein und gehen Sie auf `Über IRIS`. Unter Version sollten Sie die neue Version `1.3.0` vorfinden.

### 1.1.x -> 1.2.1

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.2.0) finden Sie Informationen zum Release 1.2.0.  
[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.2.1) finden Sie Informationen zum Release 1.2.1.

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.2.1/deployment-1.2.1.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 
2. Wechseln Sie in das aktuelle `Installationsverzeichnis`.
3. Ersetzen Sie ihre aktuelle `docker-compose.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose.yml .
    ```
4. Ersetzen Sie ihre aktuelle `docker-compose-ext-postgres.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose-ext-postgres.yml .
    ```    
5. Sollten Sie individuelle Änderungen vorgenommen haben ( z.B. eine eigene Netzwerkkonfiguration), ziehen Sie diese bitte nach. 
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
9. Loggen Sie sich mit Ihrem Benutzer ein und gehen Sie auf `Über IRIS`. Unter Version sollten Sie die neue Version `1.2.1` vorfinden.

### 1.0.1 -> 1.1.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.1.0) finden Sie Informationen zum Release 1.1.0.

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.1.0/deployment-1.1.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 
2. Wechseln Sie in das aktuelle `Installationsverzeichnis`.
3. Ersetzen Sie ihre aktuelle `docker-compose.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose.yml .
    ```
4. Ersetzen Sie ihre aktuelle `docker-compose-ext-postgres.yml` mit der Version aus dem neuen Release. 
    ```
    $ cp <Zielverzeichnis>/docker-compose-ext-postgres.yml .
    ```    
5. Sollten Sie individuelle Änderungen vorgenommen haben ( z.B. eine eigene Netzwerkkonfiguration), ziehen Sie diese bitte nach. 
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
9. Loggen Sie sich mit Ihrem Benutzer ein und gehen Sie auf `Über IRIS`. Unter Version sollten Sie die neue Version `1.1.0` vorfinden.

### 1.0.0 -> 1.0.1

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.0.1) finden Sie Informationen zum Release 1.0.1.

1. Fahren Sie die aktuelle Version herunter. 
    ```
    $ docker-compose down
    ```    
    > Bitte unbedingt ohne die Option (-v) ausführen. 

1. Laden Sie sich die Images vom letzten Release herunter.
    ```
    $ docker-compose pull
    ```    
1. Starten Sie die neue Version. 
    ```
    $ docker-compose up -d
    ```    
1. Loggen Sie sich mit Ihrem Benutzer ein und gehen Sie auf `Über IRIS`. Unter Version sollten Sie die neue Version `1.0.1` vorfinden. 

## IRIS-Client - Stand-Alone Installation 

### 1.2.x -> 1.3.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.3.0) finden Sie Informationen zum Release 1.3.0.

Dieser Upgrade-Guide geht davon aus, dass Sie den IRIS Client in einem `Installationsverzeichnis` installiert haben und dabei die Stand-Alone Ordner Struktur benutzen.

#### Java Laufzeitumgebung

**IRIS nutzt ab Version 1.2 Java 17 und setzt dieses voraus! Sorgen Sie dafür, dass eine aktuelle Version von Java 17 installiert ist!** Damit verwenden wir die neuste Version von Java mit Langzeitsupport.

#### EPS

1. Laden Sie sich die [aktuelle EPS Version 0.2.1](https://github.com/iris-connect/eps/releases/tag/v0.2.1) herunter. Sie benötigen die Binaries `eps` und `proxy` für Ihre Ziel-Plattform. 

2. Stellen Sie sicher, dass `eps` und `proxy` in Ihrem Pfad liegen. 

#### IRIS Client BFF

1. Laden Sie sich die [aktuelle Version vom IRIS CLient BFF](https://github.com/iris-connect/iris-client/releases/download/v1.3.0/iris-client-bff-1.3.0.jar) herunter. 

2. Kopieren sie die ausführbare Datei in den `bin` Ordner vom `Installationsverzeichnis`.  

    ```
    cp <Jar Datei> <Installationsverzeichnis>/bin
    ```

#### IRIS CLient Frontend

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.3.0/iris-client-fe-1.3.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren Sie den gesamten Inhalt vom `Zielverzeichnis` in den `public` Ordner vom `Installationsverzeichnis`.
    > Sollten Sie einen eigenen Webserver betreiben, müssen Sie den Pfad entsprechend anpassen. 
    ```
    cp -R <Zielverzeichnis>/* <Installationsverzeichnis>/public
    ```

#### Konfigurations-Dateien

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.3.0/stand-alone-deployment-1.3.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren und ersetzen Sie den gesamten Inhalt der EPS Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/eps/roles <Installationsverzeichnis>/conf/eps    
    ```

3. Kopieren und ersetzen Sie den gesamten Inhalt der PROXY Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/proxy/roles <Installationsverzeichnis>/conf/proxy    
    ```

#### Hinweis zu neuem Verhalten beim Start-Skript

Im Ordner 'scipts' der 'stand-alone-deployment-1.3.0.zip' ist eine neues Skript 'import-root-cert.sh' enthalten. Mit diesem können die nun benötigten Root-Zertifikate in den Key-Store der verwendetet Java-Installation importiert werden. Dies erfolgt für die in der '.env' gesetzten Umgebung ('IRIS_ENV'). Das Skript wird mit dem 'start-iris-client-bff.sh' zusammen ausgeführt, so dass kein zusätzliche Aktion nötig ist. **Beim ersten Start muss allerdings für den Import ein Passwort bei 'sudo' eingegeben werden!**
    
### 1.1.x -> 1.2.1

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.2.0) finden Sie Informationen zum Release 1.2.0.  
[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.2.1) finden Sie Informationen zum Release 1.2.1.

Dieser Upgrade-Guide geht davon aus, dass Sie den IRIS Client in einem `Installationsverzeichnis` installiert haben und dabei die Stand-Alone Ordner Struktur benutzen.

#### Java Laufzeitumgebung

**IRIS nutzt ab Version 1.2 Java 17 und setzt dieses voraus! Sorgen Sie dafür, dass eine aktuelle Version von Java 17 installiert ist!** Damit verwenden wir die neuste Version von Java mit Langzeitsupport.

#### EPS

1. Laden Sie sich die [aktuelle EPS Version 0.1.69](https://github.com/iris-connect/eps/releases/tag/v0.1.69) herunter. Sie benötigen die Binaries `eps` und `proxy` für Ihre Ziel-Plattform. 

2. Stellen Sie sicher, dass `eps` und `proxy` in Ihrem Pfad liegen. 

#### IRIS Client BFF

1. Laden Sie sich die [aktuelle Version vom IRIS CLient BFF](https://github.com/iris-connect/iris-client/releases/download/v1.2.1/iris-client-bff-1.2.1.jar) herunter. 

2. Kopieren sie die ausführbare Datei in den `bin` Ordner vom `Installationsverzeichnis`.  

    ```
    cp <Jar Datei> <Installationsverzeichnis>/bin
    ```

#### IRIS CLient Frontend

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.2.1/iris-client-fe-1.2.1.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren Sie den gesamten Inhalt vom `Zielverzeichnis` in den `public` Ordner vom `Installationsverzeichnis`.
    > Sollten Sie einen eigenen Webserver betreiben, müssen Sie den Pfad entsprechend anpassen. 
    ```
    cp -R <Zielverzeichnis>/* <Installationsverzeichnis>/public
    ```

#### Konfigurations-Dateien

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.2.1/stand-alone-deployment-1.2.1.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren und ersetzen Sie den gesamten Inhalt der EPS Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/eps/roles <Installationsverzeichnis>/conf/eps    
    ```

3. Kopieren und ersetzen Sie den gesamten Inhalt der PROXY Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/proxy/roles <Installationsverzeichnis>/conf/proxy    
    ```

### 1.0.1 -> 1.1.0

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.1.0) finden Sie Informationen zum Release 1.1.0.

Dieser Upgrade-Guide geht davon aus, dass Sie den IRIS Client in einem `Installationsverzeichnis` installiert haben und dabei die Stand-Alone Ordner Struktur benutzen.

#### EPS

1. Laden Sie sich die [aktuelle EPS Version 0.1.67](https://github.com/iris-connect/eps/releases/tag/v0.1.67) herunter. Sie benötigen die Binaries `eps` und `proxy` für Ihre Ziel-Plattform. 

2. Stellen Sie sicher, dass `eps` und `proxy` in Ihrem Pfad liegen. 

#### IRIS Client BFF

1. Laden Sie sich die [aktuelle Version vom IRIS CLient BFF](https://github.com/iris-connect/iris-client/releases/download/v1.1.0/iris-client-bff-1.1.0.jar) herunter. 

2. Kopieren sie die ausführbare Datei in den `bin` Ordner vom `Installationsverzeichnis`.  

    ```
    cp <Jar Datei> <Installationsverzeichnis>/bin
    ```

#### IRIS CLient Frontend

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.1.0/iris-client-fe-1.1.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren Sie den gesamten Inhalt vom `Zielverzeichnis` in den `public` Ordner vom `Installationsverzeichnis`.
    > Sollten Sie einen eigenen Webserver betreiben, müssen Sie den Pfad entsprechend anpassen. 
    ```
    cp -R <Zielverzeichnis>/* <Installationsverzeichnis>/public
    ```

#### Konfigurations-Dateien

1. Laden Sie sich [das neue Release](https://github.com/iris-connect/iris-client/releases/download/v1.1.0/stand-alone-deployment-1.1.0.zip) herunter und entpacken Sie es in ein `Zielverzeichnis`. 

2. Kopieren und ersetzen Sie den gesamten Inhalt der EPS Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/eps/roles <Installationsverzeichnis>/conf/eps    
    ```

3. Kopieren und ersetzen Sie den gesamten Inhalt der PROXY Konfiguration in den entsprechenden Ordner vom `Installationsverzeichnis`.
    ```
    cp -R <Zielverzeichnis>/conf/proxy/roles <Installationsverzeichnis>/conf/proxy    
    ```

### 1.0.0 -> 1.0.1

[Hier](https://github.com/iris-connect/iris-client/releases/tag/v1.0.1) finden Sie Informationen zum Release 1.0.1.

#### IRIS Client BFF

1. Laden Sie sich [aktuelle EPS Version vom IRIS CLient BFF](https://github.com/iris-connect/iris-client/releases/download/v1.0.1/iris-client-bff-1.0.1.jar) herunter.

2. Kopieren sie die ausführbare Datei in den `bin` Ordner vom `Installationsverzeichnis`.  

    ```
    cp <Jar Datei> <Installationsverzeichnis>/bin
    ```
