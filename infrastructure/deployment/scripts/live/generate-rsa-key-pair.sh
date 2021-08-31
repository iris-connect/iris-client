# Skript zur Erstellung eines RSA Schluesselpaares zur Beantragung des TLS Zertifikates bei D-TRUST
# Benutzung: generate-rsa-key-pair.sh <Name des Zertifikates z.b. mTLS-Zertifikat-EPS>

NAME=$1 
LEN="4096"

openssl genrsa -out "${NAME}.key" ${LEN};
openssl rsa -in "${NAME}.key" -pubout -out "${NAME}.pub";
