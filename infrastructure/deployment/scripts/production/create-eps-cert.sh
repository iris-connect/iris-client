# Usage: create-eps-cert.sh [your signature cert private key] [your signature cert] [common name]

key=$1
sign_cert=$2
cn=$3

extFile=extFile.txt

openssl req -new -sha256 -key "${key}" -subj "/CN=${cn}" -addext "keyUsage=digitalSignature" -addext "subjectAltName = URI:iris-name://${cn}"  -out "${cn}-sign.csr";

echo "[SANKey]\nsubjectAltName = URI:iris-name://${cn},URI:iris-group://health-departments,DNS:${cn},DNS:*.${cn}.local\nkeyUsage = digitalSignature" > $extFile

openssl x509 -req -in "${cn}-sign.csr" -CA $sign_cert -CAkey $key -CAcreateserial -out "${cn}.crt"  -days 500 -sha256 -extensions SANKey -extfile $extFile;

FINGERPRINT=$(openssl x509 -noout -fingerprint -sha256 -inform pem -in ${cn}.crt | sed -e 's/://g' | sed -r 's/.*=(.*)$/\1/g' | awk '{print tolower($0)}')

echo "# Information needed for IRIS Team"
echo "CN:                      $cn"
echo "Certificate Fingerprint: $FINGERPRINT"

rm $extFile

