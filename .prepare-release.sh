#!/bin/bash

# exit when any command fails
set -e

# keep track of the last executed command
trap 'last_command=$current_command; current_command=$BASH_COMMAND' DEBUG
# echo an error message before exiting
trap 'echo "\"${last_command}\" command failed with exit code $?."' ERR

NAMESPACE="inoeg"

printf "\n  Build components and prepare release  \n\n"

# Get version number from version tag if this given
VERSION=`echo $1 | cut -d'v' -f2`
MAJOR=`echo $VERSION | cut -d'.' -f1`
MINOR=`echo $VERSION | cut -d'.' -f2`

echo "version = $VERSION"

# expect commit sha as second parameter
COMMIT=$2

printf "\n  Build NGINX image  \n\n"
NGINX_IMAGE_NAME="$NAMESPACE/iris-client-nginx"
docker build -t $NGINX_IMAGE_NAME ./infrastructure/docker/nginx/

docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$VERSION
docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MAJOR
docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MAJOR.$MINOR

printf "\n  Build FE image  \n\n"
FE_IMAGE_NAME="$NAMESPACE/iris-client-frontend"
docker build -t $FE_IMAGE_NAME ./iris-client-fe/

docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$VERSION
docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MAJOR
docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MAJOR.$MINOR

printf "\n  Set version to POMs and build BFF image and JAR  \n\n"
# Set new version in pom.xml using mvn versions:set command
mvn versions:set -DnewVersion=$VERSION -DprocessAllModules=true

# Package the new version and copy it to release folder
# These files will be upload to github by @semantic-release/github
mvn -B clean package spring-boot:repackage spring-boot:build-image -Dspring-boot.build-image.publish=false
mkdir release && cp ./iris-client-bff/target/*.jar release

BFF_IMAGE_NAME="$NAMESPACE/iris-client-bff"
docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:latest
docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MAJOR
docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MAJOR.$MINOR

# Generate third-party dependencies for BFF and move them to root
# File will be uploaded to github by @semantic-release/github
mvn -B license:aggregate-add-third-party
mv ./target/generated-sources/license/THIRD-PARTY.txt ./BFF-THIRD-PARTY-LICENSES.md

# Set new version with prefix in pom.xml to avoid accidentally overwriting the release images in Docker.io after the merge back into develop.
# These new pom.xml and changelog.md generated by @semantic-release/changelog
# will be commit it by @semantic-release/git
mvn versions:set -DnewVersion=$VERSION-POST_RELEASE -DoldVersion=$VERSION -DprocessAllModules=true

printf "\n  Build FE ZIP  \n\n"
cd ./iris-client-fe
npm ci

# Generate third-party dependencies for FE and move them to root
# File will be uploaded to github by @semantic-release/github
npm run generate-licenses
npm run convert-licenses
mv ./licenses-prod.md ../FE-THIRD-PARTY-LICENSES-PROD.md
mv ./licenses-dev.md ../FE-THIRD-PARTY-LICENSES-DEV.md

export VUE_APP_BUILD_ID=$COMMIT
export VUE_APP_VERSION_ID=$VERSION
export VUE_APP_API_BASE_URL="/api"
npm run build
cd dist && zip -qq -r ../../release/iris-client-fe-$VERSION.zip *

printf "\n  Create ZIP of deployment scripts and instructions  \n\n"
cd ../../infrastructure/deployment && zip -qq -r ../../release/deployment-$VERSION.zip * .*

cd ../../

printf "\n  Push images and tags to docker registry  \n\n"

docker push $BFF_IMAGE_NAME:$VERSION
docker push $BFF_IMAGE_NAME:latest
docker push $BFF_IMAGE_NAME:$MAJOR
docker push $BFF_IMAGE_NAME:$MAJOR.$MINOR

docker push $FE_IMAGE_NAME:$VERSION
docker push $FE_IMAGE_NAME:latest
docker push $FE_IMAGE_NAME:$MAJOR
docker push $FE_IMAGE_NAME:$MAJOR.$MINOR

docker push $NGINX_IMAGE_NAME:$VERSION
docker push $NGINX_IMAGE_NAME:latest
docker push $NGINX_IMAGE_NAME:$MAJOR
docker push $NGINX_IMAGE_NAME:$MAJOR.$MINOR

printf "\n  COMPLETED: Build components and prepare release  \n\n"

#printenv 
