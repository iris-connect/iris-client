ST=$1
L=$2
CN=$3

O="Gesundheitsamt"
ST=$1
L=$2
C="DE"
OU="IT"
CN=$3
LEN="4096"

openssl genrsa -out "${CN}.key" ${LEN};
openssl rsa -in "${CN}.key" -pubout -out "${CN}.pub";

openssl req -new -sha256 -key "${CN}.key" -subj "/C=${C}/ST=${ST}/L=${L}/O=${O}/OU=${OU}/CN=${CN}" -addext "keyUsage=digitalSignature" -addext "subjectAltName = URI:iris-name://${CN},URI:iris-group://health-departments,DNS:${CN},DNS:*.${CN}.local"  -out "${CN}.csr";