# Usage: create-fingerprint-from-cert.sh < Zertifikate #3 - mTLS-Zertifikat - EPS >

cert=$1

export CN=`openssl x509 -noout -subject -in  $cert | sed -n '/^subject/s/^.*CN = //p'|tr -d '\n'`
export FINGERPRINT=$(openssl x509 -noout -fingerprint -sha256 -inform pem -in $cert | sed -e 's/://g' | sed -r 's/.*=(.*)$/\1/g' | awk '{print tolower($0)}')

echo "# Information needed for IRIS Team"
echo "CN:                      $CN"
echo "Certificate Fingerprint: $FINGERPRINT"