#!/bin/bash

# exit when any command fails
set -e

DCT_PATH=~/.docker/trust/private/

DCT_KEY_FILE="$DCT_PATH$DCT_PRIVATE_KEY_IDENTIFIER.key"

mkdir -p "$DCT_PATH"

echo "$DCT_PRIVATE_KEY > $DCT_KEY_FILE"

chmod 600 "$DCT_KEY_FILE"

DOCKER_CONTENT_TRUST_REPOSITORY_PASSPHRASE="$DCT_PRIVATE_KEY_PASSPHRASE" DOCKER_CONTENT_TRUST=1 docker trust key load "$DCT_KEY_FILE"

