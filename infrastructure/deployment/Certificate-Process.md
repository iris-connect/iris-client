# Prozess zur Zertifikatsausstellung für Gesundheitsämter

## Staging Umgebung

Auf der Staging Umgebung werden Zertifikate vom IRIS Team bereit gestellt. Dazu erzeugen Sie bitte zunächste Ihre Zertifikatsignierungsanforderung.

Die Struktur des CN Feldes ist wie folgt:

```
# RKI: https://tools.rki.de/PLZTool
ga-${sanitized_name(Offizieller RKI Name)}

# sanitized_name = Ersetzung aller Umlaute (z.B. ö -> oe). Sonderzeichen und Leerzeichen (auch aufeinanderfolgend) werden durch ein '-' ersetzt. Alles Lower Case.

# Beispiele
#
# RKI Name: Stadt Köln
# Link: https://tools.rki.de/PLZTool/?q=K%C3%B6ln#1.05.3.15.
# -> CN: ga-stadt-koeln
#
# RKI Name: Landkreis Südliche Weinstraße / Landau
# Link: https://tools.rki.de/PLZTool/?q=Landau#1.07.0.69.
# -> CN: ga-landkreis-suedliche-weinstrasse-landau
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

openssl req -new -sha256 -key "${CN}.key" -subj "/C=${C}/ST=${ST}/L=${L}/O=${O}/OU=${OU}/CN=${CN}" -addext "keyUsage=digitalSignature" -addext "subjectAltName = URI:iris-name://${CN},URI:iris-group://health-departments,DNS:${CN},DNS:*.${CN}.local"  -out "${CN}.csr";
```

Senden Sie die .csr und Ihre Domain an [IRIS-Rollout-Team](mailto:rollout@iris-gateway.de) und erhalten Sie Ihre .crt-Datei von uns zurück.
