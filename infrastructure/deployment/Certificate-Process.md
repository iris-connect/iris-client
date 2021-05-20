# Prozess zur Zertifikatsausstellung für Gesundheitsämter

## Staging Umgebung

Auf der Staging Umgebung werden Zertifikate vom IRIS Team bereit gestellt. Dazu erzeugen Sie bitte zunächste Ihre Zertifikatsignierungsanforderung.

Die Struktur des CN Feldes ist wie folgt:

```
# RKI: https://tools.rki.de/PLZTool
ga-${sanitized_name(Offizieller RKI Name)}


# Beispiel Köln
# RKI Name: Stadt Köln
# -> CN: ga-stadt-koeln

# sanitized_name = Ersetzung aller Umlaute. Sonderzeichen werden durch '-' ersetzt. Alles Lower Case.
```

Beispiel CSR Request für GA Köln:

```
O="Gesundheitsamt"
ST="Neumarkt 15-21"
L="50667 Köln"
C="DE"
OU="IT"
CN="ga-stadt-koeln"
LEN="4096"

openssl genrsa -out "${CN}.key" ${LEN};
openssl rsa -in "${CN}.key" -pubout -out "${CN}.pub";

openssl req -new -sha256 -key "${CN}.key" -subj "/C=${C}/ST=${ST}/L=${L}/O=${O}/OU=${OU}/CN=${CN}" -addext "keyUsage=digitalSignature" -addext "subjectAltName = URI:iris-name://${CN},URI:iris-group://health-departments,DNS:${CN};DNS:*.${CN}.local"  -out "${CN}.csr";
```

Senden Sie die .csr und Ihre Domain an [IRIS-Rollout-Team](mailto:rollout@iris-gateway.de) und erhalten Sie Ihre .crt-Datei von uns zurück.
