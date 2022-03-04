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
VERSION_SUFFIX="-POST-RELEASE"
MAJOR=`echo $VERSION | cut -d'.' -f1`
MINOR=$MAJOR.`echo $VERSION | cut -d'.' -f2`
MAJOR_LATEST=$MAJOR-latest
MINOR_LATEST=$MAJOR.`echo $VERSION | cut -d'.' -f2`-latest

echo "version = $VERSION"

# expect commit sha as second parameter
COMMIT=$2

# expect channel as the third parameter
# if the channel is undefined, the default channel == main is used for this release
CHANNEL=$3
RELEASE=$([[ -z $CHANNEL ]] && echo 1 || echo 0)

echo "channel = $CHANNEL; is release = $RELEASE"

printf "\n  Build NGINX image  \n\n"
NGINX_IMAGE_NAME="$NAMESPACE/iris-client-nginx"
docker build -t $NGINX_IMAGE_NAME ./infrastructure/docker/nginx/

docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$VERSION
docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MAJOR_LATEST
docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MAJOR
	docker tag $NGINX_IMAGE_NAME $NGINX_IMAGE_NAME:$MINOR
fi

printf "\n  Build IRIS Client EPS image  \n\n"
IRIS_CLIENT_EPS_IMAGE_NAME="$NAMESPACE/iris-client-eps"
docker build -t $IRIS_CLIENT_EPS_IMAGE_NAME ./infrastructure/docker/iris-client-eps/

docker tag $IRIS_CLIENT_EPS_IMAGE_NAME $IRIS_CLIENT_EPS_IMAGE_NAME:$VERSION
docker tag $IRIS_CLIENT_EPS_IMAGE_NAME $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR_LATEST
docker tag $IRIS_CLIENT_EPS_IMAGE_NAME $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $IRIS_CLIENT_EPS_IMAGE_NAME $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR
	docker tag $IRIS_CLIENT_EPS_IMAGE_NAME $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR
fi

printf "\n  Build IRIS Client PROXY image  \n\n"
IRIS_CLIENT_PROXY_IMAGE_NAME="$NAMESPACE/iris-client-proxy"
docker build -t $IRIS_CLIENT_PROXY_IMAGE_NAME ./infrastructure/docker/iris-client-proxy/

docker tag $IRIS_CLIENT_PROXY_IMAGE_NAME $IRIS_CLIENT_PROXY_IMAGE_NAME:$VERSION
docker tag $IRIS_CLIENT_PROXY_IMAGE_NAME $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR_LATEST
docker tag $IRIS_CLIENT_PROXY_IMAGE_NAME $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $IRIS_CLIENT_PROXY_IMAGE_NAME $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR
	docker tag $IRIS_CLIENT_PROXY_IMAGE_NAME $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR
fi

printf "\n  Build App EPS image  \n\n"
APP_EPS_IMAGE_NAME="$NAMESPACE/app-eps"
docker build -t $APP_EPS_IMAGE_NAME ./infrastructure/docker/app-eps/

docker tag $APP_EPS_IMAGE_NAME $APP_EPS_IMAGE_NAME:$VERSION
docker tag $APP_EPS_IMAGE_NAME $APP_EPS_IMAGE_NAME:$MAJOR_LATEST
docker tag $APP_EPS_IMAGE_NAME $APP_EPS_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $APP_EPS_IMAGE_NAME $APP_EPS_IMAGE_NAME:$MAJOR
	docker tag $APP_EPS_IMAGE_NAME $APP_EPS_IMAGE_NAME:$MINOR
fi

printf "\n Set version to package.json and build FE image  \n\n"

cd ./iris-client-fe

# Set new version in package.json
npm version $VERSION

FE_IMAGE_NAME="$NAMESPACE/iris-client-frontend"
docker build --build-arg VUE_APP_VERSION_ID=$VERSION --build-arg VUE_APP_BUILD_ID=$COMMIT -t $FE_IMAGE_NAME .

docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$VERSION
docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MAJOR_LATEST
docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MAJOR
	docker tag $FE_IMAGE_NAME $FE_IMAGE_NAME:$MINOR
fi

# Set new version with suffix in pom.xml to avoid accidentally overwriting the release images in Docker.io after the merge back into develop.
# These changed package.json will be commited by @semantic-release/git
npm version $VERSION$VERSION_SUFFIX

cd ..

printf "\n  Set version to POMs and build BFF image and JAR  \n\n"
# Set new version in pom.xml using mvn versions:set command
mvn versions:set -DnewVersion=$VERSION -DprocessAllModules=true

BFF_IMAGE_NAME="$NAMESPACE/iris-client-bff"

# Package the new version and copy it to release folder
# These files will be upload to github by @semantic-release/github
mvn -B clean verify spring-boot:repackage spring-boot:build-image -Dspring-boot.build-image.publish=false -Dimage.tag=$BFF_IMAGE_NAME:$VERSION
mkdir release && cp ./iris-client-bff/target/*.jar release

docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:latest
docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MAJOR_LATEST
docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MAJOR
	docker tag $BFF_IMAGE_NAME:$VERSION $BFF_IMAGE_NAME:$MINOR
fi

# Generate third-party dependencies for BFF and move them to root
# File will be uploaded to github by @semantic-release/github
mvn -B license:aggregate-add-third-party
mv ./target/generated-sources/license/THIRD-PARTY.txt ./BFF-THIRD-PARTY-LICENSES.md

# Set new version with suffix in pom.xml to avoid accidentally overwriting the release images in Docker.io after the merge back into develop.
# These changed pom.xml will be commited by @semantic-release/git
mvn versions:set -DnewVersion=$VERSION$VERSION_SUFFIX -DoldVersion=$VERSION -DprocessAllModules=true

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
export VUE_APP_LOCAL_CONTACT_PERSON_NAME=""
export VUE_APP_LOCAL_CONTACT_PERSON_MAIL=""
export VUE_APP_LOCAL_CONTACT_PERSON_PHONE=""
export VUE_APP_CSV_EXPORT_STANDARD_ATOMIC_ADDRESS=""
npm run build
cd dist && zip -qq -r ../../release/iris-client-fe-$VERSION.zip *


cd ../../infrastructure/deployment

if (( ! $RELEASE )); then
	printf "\n  Adds -latest to the image tags in Compose files because the build is a pre release \n\n"
	sed -i 's/\(inoeg\/iris-client.*\)/\1-latest/' docker-compose.yml docker-compose-ext-postgres.yml
fi

printf "\n  Create ZIP of deployment scripts and instructions  \n\n"
zip -qr ../../release/deployment-$VERSION.zip * .[a-zA-Z0-9_-]*

printf "\n  Create ZIP of stand-alone-deployment  \n\n"
cd ../../infrastructure/stand-alone-deployment && zip -qr ../../release/stand-alone-deployment-$VERSION.zip * .[a-zA-Z0-9_-]*


cd ../../

printf "\n  Signing images and tags with DCT  \n\n"

# print identifier of used dct signing key
echo "signing-key identifier = $DCT_PRIVATE_KEY_IDENTIFIER"
export DOCKER_CONTENT_TRUST_REPOSITORY_PASSPHRASE="$DCT_PRIVATE_KEY_PASSPHRASE"
export DOCKER_CONTENT_TRUST=1

docker trust sign --local $NGINX_IMAGE_NAME:$VERSION
docker trust sign --local $NGINX_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $NGINX_IMAGE_NAME:$MINOR_LATEST
docker trust sign --local $IRIS_CLIENT_EPS_IMAGE_NAME:$VERSION
docker trust sign --local $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR_LATEST
docker trust sign --local $IRIS_CLIENT_PROXY_IMAGE_NAME:$VERSION
docker trust sign --local $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR_LATEST
docker trust sign --local $APP_EPS_IMAGE_NAME:$VERSION
docker trust sign --local $APP_EPS_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $APP_EPS_IMAGE_NAME:$MINOR_LATEST
docker trust sign --local $FE_IMAGE_NAME:$VERSION
docker trust sign --local $FE_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $FE_IMAGE_NAME:$MINOR_LATEST
docker trust sign --local $BFF_IMAGE_NAME:latest
docker trust sign --local $BFF_IMAGE_NAME:$MAJOR_LATEST
docker trust sign --local $BFF_IMAGE_NAME:$MINOR_LATEST

if (( $RELEASE )); then
  	docker trust sign --local $NGINX_IMAGE_NAME:$MAJOR
  	docker trust sign --local $NGINX_IMAGE_NAME:$MINOR
  	docker trust sign --local $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR
  	docker trust sign --local $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR
  	docker trust sign --local $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR
  	docker trust sign --local $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR
  	docker trust sign --local $APP_EPS_IMAGE_NAME:$MAJOR
  	docker trust sign --local $APP_EPS_IMAGE_NAME:$MINOR
  	docker trust sign --local $FE_IMAGE_NAME:$MAJOR
  	docker trust sign --local $FE_IMAGE_NAME:$MINOR
  	docker trust sign --local $BFF_IMAGE_NAME:$MAJOR
  	docker trust sign --local $BFF_IMAGE_NAME:$MINOR
fi

export DOCKER_CONTENT_TRUST=0

printf "\n  Push images and tags to docker registry  \n\n"

docker push $BFF_IMAGE_NAME:$VERSION
docker push $BFF_IMAGE_NAME:latest
docker push $BFF_IMAGE_NAME:$MAJOR_LATEST
docker push $BFF_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $BFF_IMAGE_NAME:$MAJOR
	docker push $BFF_IMAGE_NAME:$MINOR
fi

docker push $FE_IMAGE_NAME:$VERSION
docker push $FE_IMAGE_NAME:latest
docker push $FE_IMAGE_NAME:$MAJOR_LATEST
docker push $FE_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $FE_IMAGE_NAME:$MAJOR
	docker push $FE_IMAGE_NAME:$MINOR
fi

docker push $NGINX_IMAGE_NAME:$VERSION
docker push $NGINX_IMAGE_NAME:latest
docker push $NGINX_IMAGE_NAME:$MAJOR_LATEST
docker push $NGINX_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $NGINX_IMAGE_NAME:$MAJOR
	docker push $NGINX_IMAGE_NAME:$MINOR
fi

docker push $IRIS_CLIENT_EPS_IMAGE_NAME:$VERSION
docker push $IRIS_CLIENT_EPS_IMAGE_NAME:latest
docker push $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR_LATEST
docker push $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $IRIS_CLIENT_EPS_IMAGE_NAME:$MAJOR
	docker push $IRIS_CLIENT_EPS_IMAGE_NAME:$MINOR
fi

docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:$VERSION
docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:latest
docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR_LATEST
docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:$MAJOR
	docker push $IRIS_CLIENT_PROXY_IMAGE_NAME:$MINOR
fi

docker push $APP_EPS_IMAGE_NAME:$VERSION
docker push $APP_EPS_IMAGE_NAME:latest
docker push $APP_EPS_IMAGE_NAME:$MAJOR_LATEST
docker push $APP_EPS_IMAGE_NAME:$MINOR_LATEST
if (( $RELEASE )); then
	docker push $APP_EPS_IMAGE_NAME:$MAJOR
	docker push $APP_EPS_IMAGE_NAME:$MINOR
fi

printf "\n  COMPLETED: Build components and prepare release  \n\n"

#printenv
