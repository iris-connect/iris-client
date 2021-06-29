# Bestellung von Zertifikaten - Staging Umgebung

Folgen Sie dieser Anleitung für die Austellung von Zertifikaten für die Staging Umgebung.

## Bestimmung vom offiziellen RKI Namen

Bestimmen Sie den offiziellen RKI Namen Ihres Gesundheitsamtes. Gehen Sie dafür auf https://tools.rki.de/PLZTool und suchen Sie nach Ihrem GA. Der offizielle Name ist die obere Zeit im Adressfeld ([Beispiel Bonn](./Beispiel-Bonn.png)). 

### Austellung: TLS-Zertifikat - Private Proxy

Für die Staging Umgebung wird die Test-Domain `staging.test-gesundheitsamt.de` vom IRIS Team bereit gestellt. Sie können sich dafür ein selbst signiertes Zertifikat ausstellen.

```
→ openssl req -x509 -sha256 -nodes -newkey rsa:2048 -days 365 -keyout staging.test-gesundheitsamt.de.key -out staging.test-gesundheitsamt.de.crt
Generating a RSA private key
writing new private key to 'staging.test-gesundheitsamt.de.key'
-----
Country Name (2 letter code) [AU]: <leer>
State or Province Name (full name) [Some-State]: <leer>
Locality Name (eg, city) []: <leer>
Organization Name (eg, company) [Internet Widgits Pty Ltd]: <leer>
Organizational Unit Name (eg, section) []: <leer>
Common Name (e.g. server FQDN or YOUR name) []: staging.test-gesundheitsamt.de.crt**
Email Address []: <leer>
```


### Austellung: mTLS-Zertifikat - EPS ( IRIS Client BFF )

1. Bestimmen Sie den CN Namen (Common Name) für Ihre Client Installation. 
   ```
   Der allgemeine Aufbau vom CN Feld ist: ga-${sanitized_name(Offizieller RKI Name)}

   sanitized_name bedeutet folgendes: Ersetzung aller Umlaute (z.B. ö -> oe). Sonderzeichen und Leerzeichen (auch aufeinanderfolgend) werden durch ein '-' ersetzt. Alles Lower Case.

   # Beispiel Bonn
   Offizieller RKI Name:   Stadt Bonn
   sanitized_name:         stadt-bonn
   CN Feld:                ga-stadt-bonn
   ```

2. Erstellen Sie eine CSR Request

   Zunächst müssen Sie einen sog. CSR (Certificate Signing Request) erstellen. Dafür kann folgendes Skript verwendet werden.

   ```
   ./scripts/create-csr-for-eps.sh
   ```

   Das Skript muss mit 3 Parameters aufgerufen werden. Passen sie dann die Felder ensprechend an indem sie die Variablen (${}) entsprechend ersetzen. 

   ```
   sh create-csr-for-eps.sh "${Strasse und Hausnummer Ihres GAs}" "${PLZ und Ort Ihres GAs}" "${CN Ihres GAs (s.o.)}"
   ```

   Wir haben Aufrufbeispiele Beispiele für [Bonn](./examples/Bonn-example.txt) und [Köln](./examples/Koeln-example.txt) hinterlegt.

3. Führen Sie das Script aus

   ```
   sh create-csr-for-eps.sh $1 $2 $3 # Mit Ihren Parametern
   
   # Als Ergebnis bekommen Sie 3 Dateien
   -rw-------   1 johnnypark  staff      3243 May 26 12:42 ${CN}.key
   -rw-r--r--   1 johnnypark  staff       800 May 26 12:42 ${CN}.pub
   -rw-r--r--   1 johnnypark  staff      1947 May 26 12:42 ${CN}.csr
   ```
   Legen Sie die .key Datei an einem Sicheren Ort ab. Diese wird für die weitere Konfiguration im IRIS Client verwendet

4. Senden Sie den CSR an das IRIS Rollout Team

   Senden Sie die .csr Datei an [IRIS-Rollout-Team](mailto:rollout@iris-gateway.de) und erhalten Sie Ihre .crt Datei von uns zurück. Legen Sie die .crt Datei sicher mit dem dazugehörigen .key ab.

### Ausstellung: mTLS-Zertifikat - EPS ( IRIS Private Proxy )

Der Prozess für den Proxy enspricht 1 zu 1 dem o.g. Prozess. Mit 2 Unterschieden, dem CN Namen und das zu verwendene Skript. 

1. Bestimmen Sie den CN Namen (Common Name) für Ihre Client Installation. 
   ```
   Der allgemeine Aufbau vom CN Feld ist: ga-${sanitized_name(Offizieller RKI Name)}-proxy

   sanitized_name bedeutet folgendes: Ersetzung aller Umlaute (z.B. ö -> oe). Sonderzeichen und Leerzeichen (auch aufeinanderfolgend) werden durch ein '-' ersetzt. Alles Lower Case.

   # Beispiel Bonn
   Offizieller RKI Name:   Stadt Bonn
   sanitized_name:         stadt-bonn
   CN Feld:                ga-stadt-bonn-proxy
   ```
2. Erstellen Sie eine CSR Request

   Zunächst müssen Sie einen sog. CSR (Certificate Signing Request) erstellen. Dafür kann folgendes Skript verwendet werden.

   ```
   ./scripts/create-csr-for-proxy.sh
   ```

   Das Skript muss mit 3 Parametern aufgerufen werden. Passen sie dann die Felder ensprechend an indem sie die Variablen (${}) entsprechend ersetzen. 

   ```
   sh create-csr-for-proxy.sh "${Strasse und Hausnummer Ihres GAs}" "${PLZ und Ort Ihres GAs}" "${CN Ihres GAs (s.o.)}"
   ```

   Auch hierfür haben wir Aufrufbeispiele Beispiele für [Bonn](./examples/Bonn-example-proxy.txt) und [Köln](./examples/Koeln-example-proxy.txt) hinterlegt.

3. Führen Sie das Script aus

   ```
   sh create-csr-for-proxy.sh $1 $2 $3 # Mit Ihren Parametern
   
   # Als Ergebnis bekommen Sie 3 Dateien
   -rw-------   1 johnnypark  staff      3243 May 26 12:42 ${CN}.key
   -rw-r--r--   1 johnnypark  staff       800 May 26 12:42 ${CN}.pub
   -rw-r--r--   1 johnnypark  staff      1947 May 26 12:42 ${CN}.csr
   ```
   Legen Sie die .key Datei an einem Sicheren Ort ab. Diese wird für die weitere Konfiguration im IRIS Client verwendet

4. Senden Sie den CSR an das IRIS Rollout Team

   Senden Sie die .csr Datei an [IRIS-Rollout-Team](mailto:rollout@iris-gateway.de) und erhalten Sie Ihre .crt Datei von uns zurück. Legen Sie die .crt Datei sicher mit dem dazugehörigen .key ab.

