# IRIS-Client - Stand-Alone Installation

> Die Anleitung wurde auf Linux und Mac Os x getestet.

Bei dieser Installationsvariante werden die einzelnen Paket des IRIS Clients heruntergeladen und über Skripte gestartet. Dafür gibt es folgende vordefinierte Ordnerstruktur.

```
.
├── bin # Hier wird das JAR vom IRIS Client BFF abgelegt
├── ca # Root CA Dateien für staging und live Umgebung.
├── conf # Konfigurationsdateien für die Komponenten
│   ├── eps
│   │   ├── certs # Hier werden die Zertifikate und Schlüssel von mTLS-Zertifikat - EPS ( IRIS Client BFF )	 und mTLS-Zertifikat - EPS ( IRIS Private Proxy ) abgelegt 
│   │   └── roles
│   │       ├── live
│   │       │   ├── hd
│   │       │   └── private-proxy-eps
│   │       └── staging
│   │           ├── hd
│   │           └── private-proxy-eps
│   ├── nginx
│   └── proxy
│       ├── certs # Hier wird das Zertifikat und der Schlüssel vom TLS-Zertifikat - Private Proxy abgelegt.
│       └── roles
│           ├── live
│           │   └── private-proxy
│           └── staging
│               └── private-proxy
├── db # Hier speichert der private-proxy seine Announcements.
├── public  # Hier müssen die Dateien vom IRIS Client Frontend abgelegt werden.
└── scripts # Start Skripte der Komponenten
```

Die Ordnerstruktur kann wie sie ist von Github übernommen werden und wird im weiteren Verlauf als Installationspfad referenziert.

## Vorbereitung

1. Auf dem System muss die letzte Version vom **eps** und **proxy** installiert sein. Diese kann über das öffentliche Repository heruntergeladen werden.
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
1. Einrichtung IRIS CLient BFF
    
    Das IRIS Client BFF kann über das [öffentliche Repository](https://github.com/iris-connect/iris-client/releases) heruntergeladen werden. Danach muss das Jahr in den Ordner `./bin` kopiert werden.

    ```
    # Beispiel am Release 1.0.0-rc.8 
    cp iris-client-bff-1.0.0-rc.8.jar <Installationspfad>/bin
    ```
1. Einrichtung IRIS Client Frontend	

    > Diese Anleitung funktioniert wenn sie nginx benutzen

    Beim Frontend handelt es sich um statisches HTML. Es kann über das [öffentliche Repository](https://github.com/iris-connect/iris-client/releases) heruntergeladen werden. Danach muss der Inhalt des Archives in das `public` Verzeichnis kopiert werden.

    ```
    
    ```

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

