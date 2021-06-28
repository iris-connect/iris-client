# # Skript zur Erstellung eines Signature Schluesselpaares zur Beantragung des Signatur Zertifikates bei D-TRUST
NAME=$1
PRIV_KEY=$NAME-priv-key
PUB_KEY=$NAME-pub-key

openssl ecparam -name secp256r1 -genkey -noout -out "${PRIV_KEY}.pem" 
openssl ec -in "${PRIV_KEY}.pem" -pubout > "${PUB_KEY}.pem"

