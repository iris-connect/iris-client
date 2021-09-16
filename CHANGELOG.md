# Changelog

## [1.0.1](https://github.com/iris-connect/iris-client/compare/v1.0.0...v1.0.1) (2021-09-02)


### Bug Fixes

* Fixes bug that iris location service ENV parameter had no effect.  ([517d436](https://github.com/iris-connect/iris-client/commit/517d436b65193da4f5d51cf8475f1e90353e725b))

# [1.0.0](https://github.com/iris-connect/iris-client/compare/v0.1.0...v1.0.0) (2021-08-31)


### Bug Fixes

* Add package-lock to fe project ([40b1a78](https://github.com/iris-connect/iris-client/commit/40b1a78e0a353a3ea57ccb8c15c6e2d6cb51072c))
* Adds restart policy to all docker compose services. ([#217](https://github.com/iris-connect/iris-client/issues/217)) ([3a7534b](https://github.com/iris-connect/iris-client/commit/3a7534b3bce69ad58075ba522ad120831a2982b1))
* adjust unit tests for new location search api ([#108](https://github.com/iris-connect/iris-client/issues/108)) ([bc5343a](https://github.com/iris-connect/iris-client/commit/bc5343ae9ea205e714d52238b0ff26c3f32baf46)), closes [iris-connect/iris-backlog#96](https://github.com/iris-connect/iris-backlog/issues/96)
* Admin user settings do not apply ([#115](https://github.com/iris-connect/iris-client/issues/115)) ([2f93a48](https://github.com/iris-connect/iris-client/commit/2f93a4838d3eb475c85a946f3bdb9805d2eb0618))
* date-picker visual bug when resizing page ([#152](https://github.com/iris-connect/iris-client/issues/152)) ([06fbb45](https://github.com/iris-connect/iris-client/commit/06fbb45058e52fba85434ab568432b6133abc477))
* enable cross origin POST for data submission endpoint ([389a08d](https://github.com/iris-connect/iris-client/commit/389a08d034db361568e1fa34732593eac1ab3536))
* FE image build error ([#229](https://github.com/iris-connect/iris-client/issues/229)) ([6905694](https://github.com/iris-connect/iris-client/commit/690569495df166d7ec360128214ff1e7c1ef6c6e))
* Fixes bug in migration schema. see https://www.postgresql.org/docs/9.1/sql-altertable.html. ([61146cd](https://github.com/iris-connect/iris-client/commit/61146cd321a2f7f3069e081cbb2076cff7ed5d10))
* fixes build path for nginx ([3d2ffeb](https://github.com/iris-connect/iris-client/commit/3d2ffebf2e302cdea794b300bb5820e9375e2fcc)), closes [#65](https://github.com/iris-connect/iris-client/issues/65)
* fixes build path for nginx ([dc859cb](https://github.com/iris-connect/iris-client/commit/dc859cb0a190a1ef7001d86bce156806dafe8038)), closes [#65](https://github.com/iris-connect/iris-client/issues/65)
* fixes ClassNotFoundException with jsonrpc4j ([#315](https://github.com/iris-connect/iris-client/issues/315)) ([a0e1cdc](https://github.com/iris-connect/iris-client/commit/a0e1cdc77fcbd36957437e02722fc49f26244372))
* Fixes errors in release process and updates documentation based on user feedback. ([f18b5a7](https://github.com/iris-connect/iris-client/commit/f18b5a78731924f1dddd2f98e268ec3a99518002)), closes [#107](https://github.com/iris-connect/iris-client/issues/107)
* Fixes issue that calls to SD are not using HTTP_PROXY ([#131](https://github.com/iris-connect/iris-client/issues/131)) ([767af86](https://github.com/iris-connect/iris-client/commit/767af866ad0851168f443b46bd6d016047afe0dd))
* fixes pipeline build problem ([#168](https://github.com/iris-connect/iris-client/issues/168)) ([9a5bd58](https://github.com/iris-connect/iris-client/commit/9a5bd586ac596bf5acaf49451ccab290c87757ed))
* Fixes problem stand alone release did not contain the ca folder. ([#201](https://github.com/iris-connect/iris-client/issues/201)) ([6c5fba5](https://github.com/iris-connect/iris-client/commit/6c5fba54287127c0b7d88b2b4ef410f7018cc3e6))
* Fixes problem that incorrect version and build were shown. ([fe917ec](https://github.com/iris-connect/iris-client/commit/fe917ec0504427e994f9b3e95394a269f76b534e))
* Fixes problem that PROXY_URL was still not applied. ([dffcb33](https://github.com/iris-connect/iris-client/commit/dffcb3351770a5d423220a880b0876d77666b0c2))
* fixes type in Frontend on home/Home.vue ([#234](https://github.com/iris-connect/iris-client/issues/234)) ([db1e73c](https://github.com/iris-connect/iris-client/commit/db1e73cb09092b1cfc10b342814a6ec394971d98))
* fixes wrong expected response status ([596d99d](https://github.com/iris-connect/iris-client/commit/596d99da3493addc7c8529bf1b6dd65c9ff5f094))
* Fixes wrong URL endpoint. ([8e4774a](https://github.com/iris-connect/iris-client/commit/8e4774a46fab2bf2033f14a8ae2715ee2efb3fc9))
* handle missing address information & error messages ([#279](https://github.com/iris-connect/iris-client/issues/279)) ([f13b0e2](https://github.com/iris-connect/iris-client/commit/f13b0e2c1c7d50135ebc499622a94372378f4c37))
* Handle missing contact property in location object, avoid null Error. ([#149](https://github.com/iris-connect/iris-client/issues/149)) ([ce2a686](https://github.com/iris-connect/iris-client/commit/ce2a6862ee9cb7baede0fffcc83db1589e01026a)), closes [iris-gateway/iris-backlog#119](https://github.com/iris-gateway/iris-backlog/issues/119)
* Limits length of user names to 50 to ensure clean display. ([93a195b](https://github.com/iris-connect/iris-client/commit/93a195b7ffbea4fa24c70f8b1131e52abdc64989)), closes [#270](https://github.com/iris-connect/iris-client/issues/270)
* moves dev documentation for Java to iris-client repo ([7167607](https://github.com/iris-connect/iris-client/commit/7167607fac434bec4325771f12e3111c6538f79f))
* Removes default admin credentials for production environment and ([#258](https://github.com/iris-connect/iris-client/issues/258)) ([ba9340e](https://github.com/iris-connect/iris-client/commit/ba9340e2d842b9e74af919c3605c2e8957e7468e))
* removes parent relationship in Sormas API client ([#64](https://github.com/iris-connect/iris-client/issues/64)) ([544be54](https://github.com/iris-connect/iris-client/commit/544be547508a527ed02a749c5929af026012325c)), closes [#61](https://github.com/iris-connect/iris-client/issues/61)
* removes the picker-input-field css class to make the field changeable via keyboard after selection via click ([#125](https://github.com/iris-connect/iris-client/issues/125)) ([272e6f9](https://github.com/iris-connect/iris-client/commit/272e6f9b77c1e5f4d993ee94358a62845b550ec7)), closes [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83)
* Removes unused keystore ([759b1ba](https://github.com/iris-connect/iris-client/commit/759b1ba70b4ca4258632c37be332a264757ca3be))
* renames packages ([72d0430](https://github.com/iris-connect/iris-client/commit/72d043020c84b7968d4aa05f3d409fd94f9f25e8)), closes [iris-gateway/iris-backlog#68](https://github.com/iris-gateway/iris-backlog/issues/68)
* repair mock api for location search ([#161](https://github.com/iris-connect/iris-client/issues/161)) ([3fe5cb6](https://github.com/iris-connect/iris-client/commit/3fe5cb656be01ce7018d7e81b18aaa31c8318aa0))
* restore the header mapping function ([#146](https://github.com/iris-connect/iris-client/issues/146)) ([27e79a0](https://github.com/iris-connect/iris-client/commit/27e79a04fa8e0f7798bee56b1e5d3137cff3b10c)), closes [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74)
* reverts package-lock.json to version 1 ([9aaa213](https://github.com/iris-connect/iris-client/commit/9aaa21373ca80791395c0f7b32e35ea9d8b7fa73))
* too many connections from EPS. ([#165](https://github.com/iris-connect/iris-client/issues/165)) ([8710ba1](https://github.com/iris-connect/iris-client/commit/8710ba1a9d0c98cc81a0dfc9ab7accf22ecdaa12))
* Triggers new release 1.0.0-rc.9 ([8b690e3](https://github.com/iris-connect/iris-client/commit/8b690e35e60799711d7dc76605d60d8712ce27e1))
* typo ([75769c7](https://github.com/iris-connect/iris-client/commit/75769c7c19e4d543fbade414ba79d9f4a5e7377e))
* UI improvements for commenting section of event tracking details ([4a5ee20](https://github.com/iris-connect/iris-client/commit/4a5ee20ffa42810c69108a3c1be3fff78694782e)), closes [iris-connect/iris-backlog#60](https://github.com/iris-connect/iris-backlog/issues/60)
* update failing unit tests for new rpc client config ([596c7b8](https://github.com/iris-connect/iris-client/commit/596c7b8053d914cbd21684094586124891f1bad3))
* update fe api client to use correct path for events ([3f78075](https://github.com/iris-connect/iris-client/commit/3f78075d2bb1fe1f3dd233a63d4ce8e8d2d7fc5e))
* Updates Spring Boot. Fixes https://github.com/iris-connect/iris-client/issues/155 ([342e220](https://github.com/iris-connect/iris-client/commit/342e2206577f4bd6fc43351863d7ec9897047bcc)), closes [#155](https://github.com/iris-connect/iris-client/issues/155)
* Validation of incoming guest list with cutting strings at maxlength. ([db6d7b2](https://github.com/iris-connect/iris-client/commit/db6d7b2ce44456765f4d30487d745f3069473a7a)), closes [iris-connect/iris-backlog#233](https://github.com/iris-connect/iris-backlog/issues/233) [#254](https://github.com/iris-connect/iris-client/issues/254)
* **readme:** adds link to git flow description ([aba7621](https://github.com/iris-connect/iris-client/commit/aba7621c47ee4f4b1ae8536d7fecc004fad6e542))
* **readme:** adds the definitions of commit messages and branching strategy ([3c0adfb](https://github.com/iris-connect/iris-client/commit/3c0adfb11d2677cbae1735c856a6aa86c9d6999c))
* **readme:** fixes wrong displayed list ([15f4098](https://github.com/iris-connect/iris-client/commit/15f40986aafde362a7f3774346062bbe2f24a918))


### Features

* [#77](https://github.com/iris-connect/iris-client/issues/77) Adds both location name and officalName to the display of the location. ([db90ea3](https://github.com/iris-connect/iris-client/commit/db90ea35b027885cfda5a33c1715fef130ac7f01))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Adjust the display formatting ([d7002ef](https://github.com/iris-connect/iris-client/commit/d7002efe1808c2905bd662c4c72099186a7e2406))
* [#77](https://github.com/iris-connect/iris-client/issues/77) changes var to let ([c5a67e6](https://github.com/iris-connect/iris-client/commit/c5a67e6fb94ec97898a82490bb3dbba29be40897))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Implemented checks to prevent undefined for the offical name and replace inline style with vuetify classes ([e65dec6](https://github.com/iris-connect/iris-client/commit/e65dec665f932a0168d563985d84896cac688ebb))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Improves the display of location names to prevent different variable usages  ([294d08c](https://github.com/iris-connect/iris-client/commit/294d08c3d1a9256ef48e5142023ffc3017e034ac))
* [#77](https://github.com/iris-connect/iris-client/issues/77) optimise code to remove else command ([bd3cfb7](https://github.com/iris-connect/iris-client/commit/bd3cfb738506f4dd014e8337b0327f6e0b9e63d8))
* 192 Feature flag for index cases ([#194](https://github.com/iris-connect/iris-client/issues/194)) ([22e0a4b](https://github.com/iris-connect/iris-client/commit/22e0a4b9e571e4a10551d944587423cac0ca3a76)), closes [#192](https://github.com/iris-connect/iris-client/issues/192)
* aborted requests handling ([#129](https://github.com/iris-connect/iris-client/issues/129)) ([e449e36](https://github.com/iris-connect/iris-client/commit/e449e368194fc5a0546e7890878835c0c92fd9c4)), closes [iris-connect/iris-backlog#91](https://github.com/iris-connect/iris-backlog/issues/91)
* Activates blocking time after failed login attempts to avoid brute-force attack. ([0339ce7](https://github.com/iris-connect/iris-client/commit/0339ce7f511997c2794a1620a1a2f6510e4ff982)), closes [iris-connect/iris-backlog#225](https://github.com/iris-connect/iris-backlog/issues/225) [#255](https://github.com/iris-connect/iris-client/issues/255)
* add links to about page ([387b1d9](https://github.com/iris-connect/iris-client/commit/387b1d9619b7943c11b86d03ca87e3e0decf6df1))
* Adds guide for installing standalone Java Application. ([63d2dd5](https://github.com/iris-connect/iris-client/commit/63d2dd5609a7bc17e3bfc0c8db78d7f954b1b1a9))
* adds password policy to frontend validation ([#262](https://github.com/iris-connect/iris-client/issues/262)) ([63449d6](https://github.com/iris-connect/iris-client/commit/63449d64415d5cab3e9e0583ae766dea5ea4bb74))
* Adds possibility to override standard SSL port ([#167](https://github.com/iris-connect/iris-client/issues/167)) ([d923b6e](https://github.com/iris-connect/iris-client/commit/d923b6e6b311f42912135481858822f734244732))
* After the guest list has been submitted, the proxy announcement should be closed ([#145](https://github.com/iris-connect/iris-client/issues/145)) ([4f553ea](https://github.com/iris-connect/iris-client/commit/4f553eac26d8eb1ca7606e2d1f790027af20e1d8)), closes [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#129](https://github.com/iris-gateway/iris-backlog/issues/129) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218)
* Allows querying system information and the health status of the client via the .../actuator endpoint. ([dd38d1f](https://github.com/iris-connect/iris-client/commit/dd38d1f9e40b0a690d50879525a03e8778fa5906)), closes [iris-connect/iris-backlog#105](https://github.com/iris-connect/iris-backlog/issues/105) [#136](https://github.com/iris-connect/iris-client/issues/136)
* announce connection to private-proxy without service-directory ([967ae33](https://github.com/iris-connect/iris-client/commit/967ae331810913a1a633693a05537c2050e6d570))
* As a health department employee I want that e-mail sending is asynchronous and repeated in case of errors so that I will get my notification when new data is received ([#207](https://github.com/iris-connect/iris-client/issues/207)) ([f950eb1](https://github.com/iris-connect/iris-client/commit/f950eb15681abad3559b3cfe2c42b5c18efc3d4a)), closes [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-connect/iris-backlog#126](https://github.com/iris-connect/iris-backlog/issues/126) [iris-connect/iris-backlog#126](https://github.com/iris-connect/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [#230](https://github.com/iris-connect/iris-client/issues/230) [Feature/backlog#160](https://github.com/Feature/backlog/issues/160) [#225](https://github.com/iris-connect/iris-client/issues/225) [backlog#160](https://github.com/backlog/issues/160) [#228](https://github.com/iris-connect/iris-client/issues/228) [#226](https://github.com/iris-connect/iris-client/issues/226) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#200](https://github.com/iris-connect/iris-backlog/issues/200)
* Blocks delete requests in the client backend if a user would ([#256](https://github.com/iris-connect/iris-client/issues/256)) ([60d5e91](https://github.com/iris-connect/iris-client/commit/60d5e915c0a56a1fed10fa155769eb5371e41451))
* csv export atomic address ([#235](https://github.com/iris-connect/iris-client/issues/235)) ([368cf66](https://github.com/iris-connect/iris-client/commit/368cf66efa93cf1c57a13172346f2ceaf0614e4d))
* disable access to index-case feature in FE ([#193](https://github.com/iris-connect/iris-client/issues/193)) ([5b77fbd](https://github.com/iris-connect/iris-client/commit/5b77fbdb085d87b3f88bba63b9d8ce04676a7212))
* disable index cases views ([0939dac](https://github.com/iris-connect/iris-client/commit/0939dacb4070e9a77c173a9fd11fa9ae8ce7acea)), closes [iris-connect/iris-backlog#102](https://github.com/iris-connect/iris-backlog/issues/102)
* disables the user delete button for the current user ([e8394f3](https://github.com/iris-connect/iris-client/commit/e8394f3e46892f16565f9cf022c211cd75181d0f)), closes [#250](https://github.com/iris-connect/iris-client/issues/250) [#259](https://github.com/iris-connect/iris-client/issues/259)
* display about page with iris-client information ([1362b26](https://github.com/iris-connect/iris-client/commit/1362b26af502eb57eb6602f694012688f69636ca)), closes [iris-connect/iris-backlog#95](https://github.com/iris-connect/iris-backlog/issues/95)
* e-mail deliviery ([#135](https://github.com/iris-connect/iris-client/issues/135)) ([31c6140](https://github.com/iris-connect/iris-client/commit/31c6140ef43da38b685352137f0fa3ccea06588b)), closes [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85)
* edit event tracking details + abort/complete event trackings ([ca40b95](https://github.com/iris-connect/iris-client/commit/ca40b95bd9c5e547bfded36592fa20deab12ce26)), closes [iris-gateway/iris-backlog/#59](https://github.com/iris-gateway/iris-backlog//issues/59) [iris-gateway/iris-backlog/#60](https://github.com/iris-gateway/iris-backlog//issues/60) [iris-gateway/iris-backlog/#61](https://github.com/iris-gateway/iris-backlog//issues/61)
* enable rate limiting on nginx Refs iris-connect/iris-backlog[#168](https://github.com/iris-connect/iris-client/issues/168)^C ([#212](https://github.com/iris-connect/iris-client/issues/212)) ([cbaa17d](https://github.com/iris-connect/iris-client/commit/cbaa17d940cfc2ccb2ff5d28e8af50a1e71f4791))
* enhances frontend security & performance ([#233](https://github.com/iris-connect/iris-client/issues/233)) ([a3798c5](https://github.com/iris-connect/iris-client/commit/a3798c599827011e3942db93356f045054cf23f6))
* hides nginx version for security reasons ([#272](https://github.com/iris-connect/iris-client/issues/272)) ([8194a13](https://github.com/iris-connect/iris-client/commit/8194a1321ada380181f0d3b0377bbd334bc43508))
* improved dates configuration for new events ([ec8cbbc](https://github.com/iris-connect/iris-client/commit/ec8cbbc045046a8e3034eba5f4751bc7ff8a899b)), closes [iris-connect/iris-backlog#117](https://github.com/iris-connect/iris-backlog/issues/117) [iris-connect/iris-backlog#118](https://github.com/iris-connect/iris-backlog/issues/118)
* index case data ([611d4f0](https://github.com/iris-connect/iris-client/commit/611d4f03f893245f90cca593f03ffbff54e032d9)), closes [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129)
* Integrates an automatic update of the IRIS client. ([1109c34](https://github.com/iris-connect/iris-client/commit/1109c34ba012879b70509b48fcb9b99b0657244e)), closes [iris-connect/iris-backlog#104](https://github.com/iris-connect/iris-backlog/issues/104) [#126](https://github.com/iris-connect/iris-client/issues/126)
* introduce faceting + pagination in location search ([33c0616](https://github.com/iris-connect/iris-client/commit/33c0616ba95e352050b96539c1e3e3103cd3e5b1)), closes [iris-connect/iris-backlog#96](https://github.com/iris-connect/iris-backlog/issues/96)
* introduce proxy service client and announcements ([cb78b1d](https://github.com/iris-connect/iris-client/commit/cb78b1deb251081f8614785558ac02319a5f6b8c))
* Log outgoing data requests with common format ([b1e72fc](https://github.com/iris-connect/iris-client/commit/b1e72fcfc21e720ce4ccd000e4f69e1a569b451f)), closes [iris-connect/iris-backlog#202](https://github.com/iris-connect/iris-backlog/issues/202) [iris-connect/iris-backlog#169](https://github.com/iris-connect/iris-backlog/issues/169) [#224](https://github.com/iris-connect/iris-client/issues/224)
* logo change and logos in 'about' page ([7bd42e3](https://github.com/iris-connect/iris-client/commit/7bd42e31e3963203487b4c2fbdf60f355a404214)), closes [iris-connect/iris-backlog#115](https://github.com/iris-connect/iris-backlog/issues/115) [iris-connect/iris-backlog#112](https://github.com/iris-connect/iris-backlog/issues/112)
* Makes own profile editable for regular users. ([6a94785](https://github.com/iris-connect/iris-client/commit/6a94785338b5ae206a426ed0cad89506861ae957)), closes [#265](https://github.com/iris-connect/iris-client/issues/265)
* Merge pull request [#54](https://github.com/iris-connect/iris-client/issues/54) from iris-gateway/feature/indexviews ([3e385a8](https://github.com/iris-connect/iris-client/commit/3e385a89d9c4dfa51dbae9c48c7056e4417c4751)), closes [iris-gateway/iris-backlog#1](https://github.com/iris-gateway/iris-backlog/issues/1) [iris-gateway/iris-backlog#4](https://github.com/iris-gateway/iris-backlog/issues/4) [iris-gateway/iris-backlog#5](https://github.com/iris-gateway/iris-backlog/issues/5)
* Move eps config to containers ([#175](https://github.com/iris-connect/iris-client/issues/175)) ([9484983](https://github.com/iris-connect/iris-client/commit/9484983f31c911ab9ef97774e79262873e80e542))
* pagination and faceting API for data request ([f7644df](https://github.com/iris-connect/iris-client/commit/f7644dfafcb6760e814f4a320a22a4802bfef501)), closes [iris-connect/iris-backlog#88](https://github.com/iris-connect/iris-backlog/issues/88)
* renders primary texts in a darker color to improve readability ([#263](https://github.com/iris-connect/iris-client/issues/263)) ([8ec0ac3](https://github.com/iris-connect/iris-client/commit/8ec0ac3a4ad1193db1827ce6ffdf2abdfc78e9c9))
* revoke jwt tokens of modified, deleted or logged out users ([cd1ffb9](https://github.com/iris-connect/iris-client/commit/cd1ffb9ee1a41127042b62cddfee71a60074da1f)), closes [iris-connect/iris-backlog#90](https://github.com/iris-connect/iris-backlog/issues/90)
* S.InputValidation in frontend and bff ([#223](https://github.com/iris-connect/iris-client/issues/223)) ([e0f86ac](https://github.com/iris-connect/iris-client/commit/e0f86ac5dddd4941b9a8689d16490903f5a9a0e2)), closes [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145)
* setting page title globally ([#151](https://github.com/iris-connect/iris-client/issues/151)) ([5785820](https://github.com/iris-connect/iris-client/commit/57858202d84c18734d2822396d52c0245ddff27b))
* Shows login rate limit in frontend ([#261](https://github.com/iris-connect/iris-client/issues/261)) ([074aed3](https://github.com/iris-connect/iris-client/commit/074aed32e26474e0f01962297f50143cfc288a8f))
* SORMAS-compatible CSV-Export of guest lists ([#114](https://github.com/iris-connect/iris-client/issues/114)) ([da3e369](https://github.com/iris-connect/iris-client/commit/da3e36980ade9363334517b4d6b470da76d648f1)), closes [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74)
* staging env configuration for new proxy service architecture ([8ec8626](https://github.com/iris-connect/iris-client/commit/8ec86262cc12e79b57ba3308e4b0d148ec6b761e))
* Stand Alone Documentation for Health Departments without Docker environment. ([2af2c1f](https://github.com/iris-connect/iris-client/commit/2af2c1f48f624655b5770c6ced357132d580cf45)), closes [#182](https://github.com/iris-connect/iris-client/issues/182)
* Submit and receive data requests/submission via EPS ([#83](https://github.com/iris-connect/iris-client/issues/83)) ([79538e9](https://github.com/iris-connect/iris-client/commit/79538e914f44fa95bd2bc4b441615777c0cdd66a))
* submitted event data transmitted data are checked for completeness ([a513a69](https://github.com/iris-connect/iris-client/commit/a513a69ae8d61711e254affee9a14061a57890f3)), closes [#268](https://github.com/iris-connect/iris-client/issues/268)
* The password policy is checked in the backend and has been extended. ([d6bca44](https://github.com/iris-connect/iris-client/commit/d6bca446aa8428bf57a09465406fee6aaeb35d76)), closes [iris-connect/iris-backlog#222](https://github.com/iris-connect/iris-backlog/issues/222) [#248](https://github.com/iris-connect/iris-client/issues/248)
* ui improvements to the event tracking creation ([#81](https://github.com/iris-connect/iris-client/issues/81)) ([7ced631](https://github.com/iris-connect/iris-client/commit/7ced63142517aba0c97d57165dbd8e31a3e388f2)), closes [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#50](https://github.com/iris-gateway/iris-backlog/issues/50) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83)
* update eps to 0.1.61 and adjust config for new eps version ([#273](https://github.com/iris-connect/iris-client/issues/273)) ([788de3f](https://github.com/iris-connect/iris-client/commit/788de3f7b56f226ac3e8f27730cdc41dcfccb4db))
* updated favicon to match new logo ([8f5b5ed](https://github.com/iris-connect/iris-client/commit/8f5b5ed5d11f27a152f582347117d55adfe28960))
* use env vars for hostnames in eps roles ([#199](https://github.com/iris-connect/iris-client/issues/199)) ([34a3df4](https://github.com/iris-connect/iris-client/commit/34a3df458f5f59e1fdb7f994bd29c9a33560f62c))
* use gender equitable user roles ([#267](https://github.com/iris-connect/iris-client/issues/267)) ([2479b8a](https://github.com/iris-connect/iris-client/commit/2479b8a4bec3db74eda58040cb832dafe0038654))
* use local fonts for gdpr compliance ([#220](https://github.com/iris-connect/iris-client/issues/220)) ([6492e3a](https://github.com/iris-connect/iris-client/commit/6492e3a690537ee92b137d2f28c2c40eb1f26623))
* weekly data api implementation. Refs iris-gateway/iris-backlog#{86} ([19de6ba](https://github.com/iris-connect/iris-client/commit/19de6ba2e8c95b20fa7ccd265643073cc52e7771))
* weekly data visualization ([#79](https://github.com/iris-connect/iris-client/issues/79)) ([e42822c](https://github.com/iris-connect/iris-client/commit/e42822ca3c6553535bc26bb005e89d9f603c24ef))


### Reverts

* Revert "feat: updated favicon to match new logo" (#159) ([36a3298](https://github.com/iris-connect/iris-client/commit/36a3298cba899ad9ed268a6ae067bdd83dcb08ed)), closes [#159](https://github.com/iris-connect/iris-client/issues/159)
* Revert ""officialName" und "Name" einer Location korrekt anzeigen (#71)" ([7b844a7](https://github.com/iris-connect/iris-client/commit/7b844a7457f17e5cfe2fa62eb7c811d9c148271c)), closes [#71](https://github.com/iris-connect/iris-client/issues/71)


### BREAKING CHANGES

* this requires an iris-gateway deployment with public-proxy-eps

Co-authored-by: Tim <62595633+lucky-lusa@users.noreply.github.com>

# [1.0.0-rc.14](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.13...v1.0.0-rc.14) (2021-08-31)


### Bug Fixes

* fixes ClassNotFoundException with jsonrpc4j ([#315](https://github.com/iris-connect/iris-client/issues/315)) ([a0e1cdc](https://github.com/iris-connect/iris-client/commit/a0e1cdc77fcbd36957437e02722fc49f26244372))

# [1.0.0-rc.13](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.12...v1.0.0-rc.13) (2021-08-30)


### Bug Fixes

* fixes type in Frontend on home/Home.vue ([#234](https://github.com/iris-connect/iris-client/issues/234)) ([db1e73c](https://github.com/iris-connect/iris-client/commit/db1e73cb09092b1cfc10b342814a6ec394971d98))
* handle missing address information & error messages ([#279](https://github.com/iris-connect/iris-client/issues/279)) ([f13b0e2](https://github.com/iris-connect/iris-client/commit/f13b0e2c1c7d50135ebc499622a94372378f4c37))
* Limits length of user names to 50 to ensure clean display. ([93a195b](https://github.com/iris-connect/iris-client/commit/93a195b7ffbea4fa24c70f8b1131e52abdc64989)), closes [#270](https://github.com/iris-connect/iris-client/issues/270)
* Removes default admin credentials for production environment and ([#258](https://github.com/iris-connect/iris-client/issues/258)) ([ba9340e](https://github.com/iris-connect/iris-client/commit/ba9340e2d842b9e74af919c3605c2e8957e7468e))
* Validation of incoming guest list with cutting strings at maxlength. ([db6d7b2](https://github.com/iris-connect/iris-client/commit/db6d7b2ce44456765f4d30487d745f3069473a7a)), closes [iris-connect/iris-backlog#233](https://github.com/iris-connect/iris-backlog/issues/233) [#254](https://github.com/iris-connect/iris-client/issues/254)


### Features

* Activates blocking time after failed login attempts to avoid brute-force attack. ([0339ce7](https://github.com/iris-connect/iris-client/commit/0339ce7f511997c2794a1620a1a2f6510e4ff982)), closes [iris-connect/iris-backlog#225](https://github.com/iris-connect/iris-backlog/issues/225) [#255](https://github.com/iris-connect/iris-client/issues/255)
* adds password policy to frontend validation ([#262](https://github.com/iris-connect/iris-client/issues/262)) ([63449d6](https://github.com/iris-connect/iris-client/commit/63449d64415d5cab3e9e0583ae766dea5ea4bb74))
* After the guest list has been submitted, the proxy announcement should be closed ([#145](https://github.com/iris-connect/iris-client/issues/145)) ([4f553ea](https://github.com/iris-connect/iris-client/commit/4f553eac26d8eb1ca7606e2d1f790027af20e1d8)), closes [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#129](https://github.com/iris-gateway/iris-backlog/issues/129) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-gateway/iris-backlog#120](https://github.com/iris-gateway/iris-backlog/issues/120) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218) [iris-connect/iris-backlog#218](https://github.com/iris-connect/iris-backlog/issues/218)
* As a health department employee I want that e-mail sending is asynchronous and repeated in case of errors so that I will get my notification when new data is received ([#207](https://github.com/iris-connect/iris-client/issues/207)) ([f950eb1](https://github.com/iris-connect/iris-client/commit/f950eb15681abad3559b3cfe2c42b5c18efc3d4a)), closes [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-connect/iris-backlog#126](https://github.com/iris-connect/iris-backlog/issues/126) [iris-connect/iris-backlog#126](https://github.com/iris-connect/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [iris-gateway/iris-backlog#126](https://github.com/iris-gateway/iris-backlog/issues/126) [#230](https://github.com/iris-connect/iris-client/issues/230) [Feature/backlog#160](https://github.com/Feature/backlog/issues/160) [#225](https://github.com/iris-connect/iris-client/issues/225) [backlog#160](https://github.com/backlog/issues/160) [#228](https://github.com/iris-connect/iris-client/issues/228) [#226](https://github.com/iris-connect/iris-client/issues/226) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#199](https://github.com/iris-connect/iris-backlog/issues/199) [iris-connect/iris-backlog#200](https://github.com/iris-connect/iris-backlog/issues/200)
* Blocks delete requests in the client backend if a user would ([#256](https://github.com/iris-connect/iris-client/issues/256)) ([60d5e91](https://github.com/iris-connect/iris-client/commit/60d5e915c0a56a1fed10fa155769eb5371e41451))
* csv export atomic address ([#235](https://github.com/iris-connect/iris-client/issues/235)) ([368cf66](https://github.com/iris-connect/iris-client/commit/368cf66efa93cf1c57a13172346f2ceaf0614e4d))
* disables the user delete button for the current user ([e8394f3](https://github.com/iris-connect/iris-client/commit/e8394f3e46892f16565f9cf022c211cd75181d0f)), closes [#250](https://github.com/iris-connect/iris-client/issues/250) [#259](https://github.com/iris-connect/iris-client/issues/259)
* enable rate limiting on nginx Refs iris-connect/iris-backlog[#168](https://github.com/iris-connect/iris-client/issues/168)^C ([#212](https://github.com/iris-connect/iris-client/issues/212)) ([cbaa17d](https://github.com/iris-connect/iris-client/commit/cbaa17d940cfc2ccb2ff5d28e8af50a1e71f4791))
* enhances frontend security & performance ([#233](https://github.com/iris-connect/iris-client/issues/233)) ([a3798c5](https://github.com/iris-connect/iris-client/commit/a3798c599827011e3942db93356f045054cf23f6))
* hides nginx version for security reasons ([#272](https://github.com/iris-connect/iris-client/issues/272)) ([8194a13](https://github.com/iris-connect/iris-client/commit/8194a1321ada380181f0d3b0377bbd334bc43508))
* Makes own profile editable for regular users. ([6a94785](https://github.com/iris-connect/iris-client/commit/6a94785338b5ae206a426ed0cad89506861ae957)), closes [#265](https://github.com/iris-connect/iris-client/issues/265)
* renders primary texts in a darker color to improve readability ([#263](https://github.com/iris-connect/iris-client/issues/263)) ([8ec0ac3](https://github.com/iris-connect/iris-client/commit/8ec0ac3a4ad1193db1827ce6ffdf2abdfc78e9c9))
* S.InputValidation in frontend and bff ([#223](https://github.com/iris-connect/iris-client/issues/223)) ([e0f86ac](https://github.com/iris-connect/iris-client/commit/e0f86ac5dddd4941b9a8689d16490903f5a9a0e2)), closes [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-connect/iris-backlog#145](https://github.com/iris-connect/iris-backlog/issues/145) [iris-gateway/iris-backlog#145](https://github.com/iris-gateway/iris-backlog/issues/145)
* Shows login rate limit in frontend ([#261](https://github.com/iris-connect/iris-client/issues/261)) ([074aed3](https://github.com/iris-connect/iris-client/commit/074aed32e26474e0f01962297f50143cfc288a8f))
* submitted event data transmitted data are checked for completeness ([a513a69](https://github.com/iris-connect/iris-client/commit/a513a69ae8d61711e254affee9a14061a57890f3)), closes [#268](https://github.com/iris-connect/iris-client/issues/268)
* The password policy is checked in the backend and has been extended. ([d6bca44](https://github.com/iris-connect/iris-client/commit/d6bca446aa8428bf57a09465406fee6aaeb35d76)), closes [iris-connect/iris-backlog#222](https://github.com/iris-connect/iris-backlog/issues/222) [#248](https://github.com/iris-connect/iris-client/issues/248)
* update eps to 0.1.61 and adjust config for new eps version ([#273](https://github.com/iris-connect/iris-client/issues/273)) ([788de3f](https://github.com/iris-connect/iris-client/commit/788de3f7b56f226ac3e8f27730cdc41dcfccb4db))
* use gender equitable user roles ([#267](https://github.com/iris-connect/iris-client/issues/267)) ([2479b8a](https://github.com/iris-connect/iris-client/commit/2479b8a4bec3db74eda58040cb832dafe0038654))

# [1.0.0-rc.12](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.11...v1.0.0-rc.12) (2021-07-27)


### Bug Fixes

* Adds restart policy to all docker compose services. ([#217](https://github.com/iris-connect/iris-client/issues/217)) ([3a7534b](https://github.com/iris-connect/iris-client/commit/3a7534b3bce69ad58075ba522ad120831a2982b1))
* FE image build error ([#229](https://github.com/iris-connect/iris-client/issues/229)) ([6905694](https://github.com/iris-connect/iris-client/commit/690569495df166d7ec360128214ff1e7c1ef6c6e))
* repair mock api for location search ([#161](https://github.com/iris-connect/iris-client/issues/161)) ([3fe5cb6](https://github.com/iris-connect/iris-client/commit/3fe5cb656be01ce7018d7e81b18aaa31c8318aa0))
* typo ([75769c7](https://github.com/iris-connect/iris-client/commit/75769c7c19e4d543fbade414ba79d9f4a5e7377e))


### Features

* 192 Feature flag for index cases ([#194](https://github.com/iris-connect/iris-client/issues/194)) ([22e0a4b](https://github.com/iris-connect/iris-client/commit/22e0a4b9e571e4a10551d944587423cac0ca3a76)), closes [#192](https://github.com/iris-connect/iris-client/issues/192)
* Log outgoing data requests with common format ([b1e72fc](https://github.com/iris-connect/iris-client/commit/b1e72fcfc21e720ce4ccd000e4f69e1a569b451f)), closes [iris-connect/iris-backlog#202](https://github.com/iris-connect/iris-backlog/issues/202) [iris-connect/iris-backlog#169](https://github.com/iris-connect/iris-backlog/issues/169) [#224](https://github.com/iris-connect/iris-client/issues/224)
* use local fonts for gdpr compliance ([#220](https://github.com/iris-connect/iris-client/issues/220)) ([6492e3a](https://github.com/iris-connect/iris-client/commit/6492e3a690537ee92b137d2f28c2c40eb1f26623))

# [1.0.0-rc.11](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.10...v1.0.0-rc.11) (2021-07-02)


### Bug Fixes

* enable cross origin POST for data submission endpoint ([389a08d](https://github.com/iris-connect/iris-client/commit/389a08d034db361568e1fa34732593eac1ab3536))
* Fixes problem stand alone release did not contain the ca folder. ([#201](https://github.com/iris-connect/iris-client/issues/201)) ([6c5fba5](https://github.com/iris-connect/iris-client/commit/6c5fba54287127c0b7d88b2b4ef410f7018cc3e6))

# [1.0.0-rc.10](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.9...v1.0.0-rc.10) (2021-07-02)


### Bug Fixes

* Fixes problem that incorrect version and build were shown. ([fe917ec](https://github.com/iris-connect/iris-client/commit/fe917ec0504427e994f9b3e95394a269f76b534e))


### Features

* disable access to index-case feature in FE ([#193](https://github.com/iris-connect/iris-client/issues/193)) ([5b77fbd](https://github.com/iris-connect/iris-client/commit/5b77fbdb085d87b3f88bba63b9d8ce04676a7212))
* index case data ([611d4f0](https://github.com/iris-connect/iris-client/commit/611d4f03f893245f90cca593f03ffbff54e032d9)), closes [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129) [iris-connect/iris-backlog/issues#129](https://github.com/iris-connect/iris-backlog/issues/issues/129)
* Stand Alone Documentation for Health Departments without Docker environment. ([2af2c1f](https://github.com/iris-connect/iris-client/commit/2af2c1f48f624655b5770c6ced357132d580cf45)), closes [#182](https://github.com/iris-connect/iris-client/issues/182)
* use env vars for hostnames in eps roles ([#199](https://github.com/iris-connect/iris-client/issues/199)) ([34a3df4](https://github.com/iris-connect/iris-client/commit/34a3df458f5f59e1fdb7f994bd29c9a33560f62c))

# [1.0.0-rc.9](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.8...v1.0.0-rc.9) (2021-06-30)


### Bug Fixes

* Triggers new release 1.0.0-rc.9 ([8b690e3](https://github.com/iris-connect/iris-client/commit/8b690e35e60799711d7dc76605d60d8712ce27e1))

# [1.0.0-rc.8](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.7...v1.0.0-rc.8) (2021-06-30)


### Bug Fixes

* date-picker visual bug when resizing page ([#152](https://github.com/iris-connect/iris-client/issues/152)) ([06fbb45](https://github.com/iris-connect/iris-client/commit/06fbb45058e52fba85434ab568432b6133abc477))
* fixes pipeline build problem ([#168](https://github.com/iris-connect/iris-client/issues/168)) ([9a5bd58](https://github.com/iris-connect/iris-client/commit/9a5bd586ac596bf5acaf49451ccab290c87757ed))
* Handle missing contact property in location object, avoid null Error. ([#149](https://github.com/iris-connect/iris-client/issues/149)) ([ce2a686](https://github.com/iris-connect/iris-client/commit/ce2a6862ee9cb7baede0fffcc83db1589e01026a)), closes [iris-gateway/iris-backlog#119](https://github.com/iris-gateway/iris-backlog/issues/119)
* restore the header mapping function ([#146](https://github.com/iris-connect/iris-client/issues/146)) ([27e79a0](https://github.com/iris-connect/iris-client/commit/27e79a04fa8e0f7798bee56b1e5d3137cff3b10c)), closes [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74)
* too many connections from EPS. ([#165](https://github.com/iris-connect/iris-client/issues/165)) ([8710ba1](https://github.com/iris-connect/iris-client/commit/8710ba1a9d0c98cc81a0dfc9ab7accf22ecdaa12))
* Updates Spring Boot. Fixes https://github.com/iris-connect/iris-client/issues/155 ([342e220](https://github.com/iris-connect/iris-client/commit/342e2206577f4bd6fc43351863d7ec9897047bcc)), closes [#155](https://github.com/iris-connect/iris-client/issues/155)


### Features

* add links to about page ([387b1d9](https://github.com/iris-connect/iris-client/commit/387b1d9619b7943c11b86d03ca87e3e0decf6df1))
* Adds possibility to override standard SSL port ([#167](https://github.com/iris-connect/iris-client/issues/167)) ([d923b6e](https://github.com/iris-connect/iris-client/commit/d923b6e6b311f42912135481858822f734244732))
* e-mail deliviery ([#135](https://github.com/iris-connect/iris-client/issues/135)) ([31c6140](https://github.com/iris-connect/iris-client/commit/31c6140ef43da38b685352137f0fa3ccea06588b)), closes [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85)
* improved dates configuration for new events ([ec8cbbc](https://github.com/iris-connect/iris-client/commit/ec8cbbc045046a8e3034eba5f4751bc7ff8a899b)), closes [iris-connect/iris-backlog#117](https://github.com/iris-connect/iris-backlog/issues/117) [iris-connect/iris-backlog#118](https://github.com/iris-connect/iris-backlog/issues/118)
* Move eps config to containers ([#175](https://github.com/iris-connect/iris-client/issues/175)) ([9484983](https://github.com/iris-connect/iris-client/commit/9484983f31c911ab9ef97774e79262873e80e542))
* setting page title globally ([#151](https://github.com/iris-connect/iris-client/issues/151)) ([5785820](https://github.com/iris-connect/iris-client/commit/57858202d84c18734d2822396d52c0245ddff27b))
* updated favicon to match new logo ([8f5b5ed](https://github.com/iris-connect/iris-client/commit/8f5b5ed5d11f27a152f582347117d55adfe28960))


### Reverts

* Revert "feat: updated favicon to match new logo" (#159) ([36a3298](https://github.com/iris-connect/iris-client/commit/36a3298cba899ad9ed268a6ae067bdd83dcb08ed)), closes [#159](https://github.com/iris-connect/iris-client/issues/159)

# [1.0.0-rc.8](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.7...v1.0.0-rc.8) (2021-06-24)


### Bug Fixes

* date-picker visual bug when resizing page ([#152](https://github.com/iris-connect/iris-client/issues/152)) ([06fbb45](https://github.com/iris-connect/iris-client/commit/06fbb45058e52fba85434ab568432b6133abc477))
* fixes pipeline build problem ([#168](https://github.com/iris-connect/iris-client/issues/168)) ([9a5bd58](https://github.com/iris-connect/iris-client/commit/9a5bd586ac596bf5acaf49451ccab290c87757ed))
* Handle missing contact property in location object, avoid null Error. ([#149](https://github.com/iris-connect/iris-client/issues/149)) ([ce2a686](https://github.com/iris-connect/iris-client/commit/ce2a6862ee9cb7baede0fffcc83db1589e01026a)), closes [iris-gateway/iris-backlog#119](https://github.com/iris-gateway/iris-backlog/issues/119)
* restore the header mapping function ([#146](https://github.com/iris-connect/iris-client/issues/146)) ([27e79a0](https://github.com/iris-connect/iris-client/commit/27e79a04fa8e0f7798bee56b1e5d3137cff3b10c)), closes [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74)
* too many connections from EPS. ([#165](https://github.com/iris-connect/iris-client/issues/165)) ([8710ba1](https://github.com/iris-connect/iris-client/commit/8710ba1a9d0c98cc81a0dfc9ab7accf22ecdaa12))
* Updates Spring Boot. Fixes https://github.com/iris-connect/iris-client/issues/155 ([342e220](https://github.com/iris-connect/iris-client/commit/342e2206577f4bd6fc43351863d7ec9897047bcc)), closes [#155](https://github.com/iris-connect/iris-client/issues/155)


### Features

* add links to about page ([387b1d9](https://github.com/iris-connect/iris-client/commit/387b1d9619b7943c11b86d03ca87e3e0decf6df1))
* Adds possibility to override standard SSL port ([#167](https://github.com/iris-connect/iris-client/issues/167)) ([d923b6e](https://github.com/iris-connect/iris-client/commit/d923b6e6b311f42912135481858822f734244732))
* e-mail deliviery ([#135](https://github.com/iris-connect/iris-client/issues/135)) ([31c6140](https://github.com/iris-connect/iris-client/commit/31c6140ef43da38b685352137f0fa3ccea06588b)), closes [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-connect/iris-backlog#85](https://github.com/iris-connect/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85) [iris-gateway/iris-backlog#85](https://github.com/iris-gateway/iris-backlog/issues/85)
* improved dates configuration for new events ([ec8cbbc](https://github.com/iris-connect/iris-client/commit/ec8cbbc045046a8e3034eba5f4751bc7ff8a899b)), closes [iris-connect/iris-backlog#117](https://github.com/iris-connect/iris-backlog/issues/117) [iris-connect/iris-backlog#118](https://github.com/iris-connect/iris-backlog/issues/118)
* setting page title globally ([#151](https://github.com/iris-connect/iris-client/issues/151)) ([5785820](https://github.com/iris-connect/iris-client/commit/57858202d84c18734d2822396d52c0245ddff27b))
* updated favicon to match new logo ([8f5b5ed](https://github.com/iris-connect/iris-client/commit/8f5b5ed5d11f27a152f582347117d55adfe28960))


### Reverts

* Revert "feat: updated favicon to match new logo" (#159) ([36a3298](https://github.com/iris-connect/iris-client/commit/36a3298cba899ad9ed268a6ae067bdd83dcb08ed)), closes [#159](https://github.com/iris-connect/iris-client/issues/159)

# [1.0.0-rc.7](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.6...v1.0.0-rc.7) (2021-06-10)


### Bug Fixes

* Fixes problem that PROXY_URL was still not applied. ([dffcb33](https://github.com/iris-connect/iris-client/commit/dffcb3351770a5d423220a880b0876d77666b0c2))

# [1.0.0-rc.6](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.5...v1.0.0-rc.6) (2021-06-10)


### Features

* Integrates an automatic update of the IRIS client. ([1109c34](https://github.com/iris-connect/iris-client/commit/1109c34ba012879b70509b48fcb9b99b0657244e)), closes [iris-connect/iris-backlog#104](https://github.com/iris-connect/iris-backlog/issues/104) [#126](https://github.com/iris-connect/iris-client/issues/126)

# [1.0.0-rc.5](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.4...v1.0.0-rc.5) (2021-06-10)


### Bug Fixes

* Fixes issue that calls to SD are not using HTTP_PROXY ([#131](https://github.com/iris-connect/iris-client/issues/131)) ([767af86](https://github.com/iris-connect/iris-client/commit/767af866ad0851168f443b46bd6d016047afe0dd))
* removes the picker-input-field css class to make the field changeable via keyboard after selection via click ([#125](https://github.com/iris-connect/iris-client/issues/125)) ([272e6f9](https://github.com/iris-connect/iris-client/commit/272e6f9b77c1e5f4d993ee94358a62845b550ec7)), closes [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83)


### Features

* aborted requests handling ([#129](https://github.com/iris-connect/iris-client/issues/129)) ([e449e36](https://github.com/iris-connect/iris-client/commit/e449e368194fc5a0546e7890878835c0c92fd9c4)), closes [iris-connect/iris-backlog#91](https://github.com/iris-connect/iris-backlog/issues/91)
* Allows querying system information and the health status of the client via the .../actuator endpoint. ([dd38d1f](https://github.com/iris-connect/iris-client/commit/dd38d1f9e40b0a690d50879525a03e8778fa5906)), closes [iris-connect/iris-backlog#105](https://github.com/iris-connect/iris-backlog/issues/105) [#136](https://github.com/iris-connect/iris-client/issues/136)
* disable index cases views ([0939dac](https://github.com/iris-connect/iris-client/commit/0939dacb4070e9a77c173a9fd11fa9ae8ce7acea)), closes [iris-connect/iris-backlog#102](https://github.com/iris-connect/iris-backlog/issues/102)
* logo change and logos in 'about' page ([7bd42e3](https://github.com/iris-connect/iris-client/commit/7bd42e31e3963203487b4c2fbdf60f355a404214)), closes [iris-connect/iris-backlog#115](https://github.com/iris-connect/iris-backlog/issues/115) [iris-connect/iris-backlog#112](https://github.com/iris-connect/iris-backlog/issues/112)
* SORMAS-compatible CSV-Export of guest lists ([#114](https://github.com/iris-connect/iris-client/issues/114)) ([da3e369](https://github.com/iris-connect/iris-client/commit/da3e36980ade9363334517b4d6b470da76d648f1)), closes [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74) [iris-gateway/iris-backlog#74](https://github.com/iris-gateway/iris-backlog/issues/74)

# [1.0.0-rc.4](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.3...v1.0.0-rc.4) (2021-05-29)


### Bug Fixes

* update failing unit tests for new rpc client config ([596c7b8](https://github.com/iris-connect/iris-client/commit/596c7b8053d914cbd21684094586124891f1bad3))


### Features

* announce connection to private-proxy without service-directory ([967ae33](https://github.com/iris-connect/iris-client/commit/967ae331810913a1a633693a05537c2050e6d570))
* display about page with iris-client information ([1362b26](https://github.com/iris-connect/iris-client/commit/1362b26af502eb57eb6602f694012688f69636ca)), closes [iris-connect/iris-backlog#95](https://github.com/iris-connect/iris-backlog/issues/95)
* revoke jwt tokens of modified, deleted or logged out users ([cd1ffb9](https://github.com/iris-connect/iris-client/commit/cd1ffb9ee1a41127042b62cddfee71a60074da1f)), closes [iris-connect/iris-backlog#90](https://github.com/iris-connect/iris-backlog/issues/90)

# [1.0.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.2...v1.0.0-rc.3) (2021-05-28)


### Bug Fixes

* Admin user settings do not apply ([#115](https://github.com/iris-connect/iris-client/issues/115)) ([2f93a48](https://github.com/iris-connect/iris-client/commit/2f93a4838d3eb475c85a946f3bdb9805d2eb0618))

# [1.0.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.0.0-rc.1...v1.0.0-rc.2) (2021-05-27)


### Bug Fixes

* adjust unit tests for new location search api ([#108](https://github.com/iris-connect/iris-client/issues/108)) ([bc5343a](https://github.com/iris-connect/iris-client/commit/bc5343ae9ea205e714d52238b0ff26c3f32baf46)), closes [iris-connect/iris-backlog#96](https://github.com/iris-connect/iris-backlog/issues/96)
* Fixes errors in release process and updates documentation based on user feedback. ([f18b5a7](https://github.com/iris-connect/iris-client/commit/f18b5a78731924f1dddd2f98e268ec3a99518002)), closes [#107](https://github.com/iris-connect/iris-client/issues/107)


### Features

* introduce faceting + pagination in location search ([33c0616](https://github.com/iris-connect/iris-client/commit/33c0616ba95e352050b96539c1e3e3103cd3e5b1)), closes [iris-connect/iris-backlog#96](https://github.com/iris-connect/iris-backlog/issues/96)

# [1.0.0-rc.1](https://github.com/iris-connect/iris-client/compare/v0.1.0...v1.0.0-rc.1) (2021-05-26)


### Bug Fixes

* Add package-lock to fe project ([40b1a78](https://github.com/iris-connect/iris-client/commit/40b1a78e0a353a3ea57ccb8c15c6e2d6cb51072c))
* Fixes bug in migration schema. see https://www.postgresql.org/docs/9.1/sql-altertable.html. ([61146cd](https://github.com/iris-connect/iris-client/commit/61146cd321a2f7f3069e081cbb2076cff7ed5d10))
* fixes build path for nginx ([3d2ffeb](https://github.com/iris-connect/iris-client/commit/3d2ffebf2e302cdea794b300bb5820e9375e2fcc)), closes [#65](https://github.com/iris-connect/iris-client/issues/65)
* fixes build path for nginx ([dc859cb](https://github.com/iris-connect/iris-client/commit/dc859cb0a190a1ef7001d86bce156806dafe8038)), closes [#65](https://github.com/iris-connect/iris-client/issues/65)
* fixes wrong expected response status ([596d99d](https://github.com/iris-connect/iris-client/commit/596d99da3493addc7c8529bf1b6dd65c9ff5f094))
* Fixes wrong URL endpoint. ([8e4774a](https://github.com/iris-connect/iris-client/commit/8e4774a46fab2bf2033f14a8ae2715ee2efb3fc9))
* moves dev documentation for Java to iris-client repo ([7167607](https://github.com/iris-connect/iris-client/commit/7167607fac434bec4325771f12e3111c6538f79f))
* removes parent relationship in Sormas API client ([#64](https://github.com/iris-connect/iris-client/issues/64)) ([544be54](https://github.com/iris-connect/iris-client/commit/544be547508a527ed02a749c5929af026012325c)), closes [#61](https://github.com/iris-connect/iris-client/issues/61)
* Removes unused keystore ([759b1ba](https://github.com/iris-connect/iris-client/commit/759b1ba70b4ca4258632c37be332a264757ca3be))
* reverts package-lock.json to version 1 ([9aaa213](https://github.com/iris-connect/iris-client/commit/9aaa21373ca80791395c0f7b32e35ea9d8b7fa73))
* UI improvements for commenting section of event tracking details ([4a5ee20](https://github.com/iris-connect/iris-client/commit/4a5ee20ffa42810c69108a3c1be3fff78694782e)), closes [iris-connect/iris-backlog#60](https://github.com/iris-connect/iris-backlog/issues/60)
* update fe api client to use correct path for events ([3f78075](https://github.com/iris-connect/iris-client/commit/3f78075d2bb1fe1f3dd233a63d4ce8e8d2d7fc5e))
* **readme:** adds link to git flow description ([aba7621](https://github.com/iris-connect/iris-client/commit/aba7621c47ee4f4b1ae8536d7fecc004fad6e542))
* **readme:** adds the definitions of commit messages and branching strategy ([3c0adfb](https://github.com/iris-connect/iris-client/commit/3c0adfb11d2677cbae1735c856a6aa86c9d6999c))
* **readme:** fixes wrong displayed list ([15f4098](https://github.com/iris-connect/iris-client/commit/15f40986aafde362a7f3774346062bbe2f24a918))
* renames packages ([72d0430](https://github.com/iris-connect/iris-client/commit/72d043020c84b7968d4aa05f3d409fd94f9f25e8)), closes [iris-gateway/iris-backlog#68](https://github.com/iris-gateway/iris-backlog/issues/68)


### Features

* [#77](https://github.com/iris-connect/iris-client/issues/77) Adds both location name and officalName to the display of the location. ([db90ea3](https://github.com/iris-connect/iris-client/commit/db90ea35b027885cfda5a33c1715fef130ac7f01))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Adjust the display formatting ([d7002ef](https://github.com/iris-connect/iris-client/commit/d7002efe1808c2905bd662c4c72099186a7e2406))
* [#77](https://github.com/iris-connect/iris-client/issues/77) changes var to let ([c5a67e6](https://github.com/iris-connect/iris-client/commit/c5a67e6fb94ec97898a82490bb3dbba29be40897))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Implemented checks to prevent undefined for the offical name and replace inline style with vuetify classes ([e65dec6](https://github.com/iris-connect/iris-client/commit/e65dec665f932a0168d563985d84896cac688ebb))
* [#77](https://github.com/iris-connect/iris-client/issues/77) Improves the display of location names to prevent different variable usages  ([294d08c](https://github.com/iris-connect/iris-client/commit/294d08c3d1a9256ef48e5142023ffc3017e034ac))
* [#77](https://github.com/iris-connect/iris-client/issues/77) optimise code to remove else command ([bd3cfb7](https://github.com/iris-connect/iris-client/commit/bd3cfb738506f4dd014e8337b0327f6e0b9e63d8))
* Adds guide for installing standalone Java Application. ([63d2dd5](https://github.com/iris-connect/iris-client/commit/63d2dd5609a7bc17e3bfc0c8db78d7f954b1b1a9))
* edit event tracking details + abort/complete event trackings ([ca40b95](https://github.com/iris-connect/iris-client/commit/ca40b95bd9c5e547bfded36592fa20deab12ce26)), closes [iris-gateway/iris-backlog/#59](https://github.com/iris-gateway/iris-backlog//issues/59) [iris-gateway/iris-backlog/#60](https://github.com/iris-gateway/iris-backlog//issues/60) [iris-gateway/iris-backlog/#61](https://github.com/iris-gateway/iris-backlog//issues/61)
* introduce proxy service client and announcements ([cb78b1d](https://github.com/iris-connect/iris-client/commit/cb78b1deb251081f8614785558ac02319a5f6b8c))
* Merge pull request [#54](https://github.com/iris-connect/iris-client/issues/54) from iris-gateway/feature/indexviews ([3e385a8](https://github.com/iris-connect/iris-client/commit/3e385a89d9c4dfa51dbae9c48c7056e4417c4751)), closes [iris-gateway/iris-backlog#1](https://github.com/iris-gateway/iris-backlog/issues/1) [iris-gateway/iris-backlog#4](https://github.com/iris-gateway/iris-backlog/issues/4) [iris-gateway/iris-backlog#5](https://github.com/iris-gateway/iris-backlog/issues/5)
* pagination and faceting API for data request ([f7644df](https://github.com/iris-connect/iris-client/commit/f7644dfafcb6760e814f4a320a22a4802bfef501)), closes [iris-connect/iris-backlog#88](https://github.com/iris-connect/iris-backlog/issues/88)
* staging env configuration for new proxy service architecture ([8ec8626](https://github.com/iris-connect/iris-client/commit/8ec86262cc12e79b57ba3308e4b0d148ec6b761e))
* Submit and receive data requests/submission via EPS ([#83](https://github.com/iris-connect/iris-client/issues/83)) ([79538e9](https://github.com/iris-connect/iris-client/commit/79538e914f44fa95bd2bc4b441615777c0cdd66a))
* ui improvements to the event tracking creation ([#81](https://github.com/iris-connect/iris-client/issues/81)) ([7ced631](https://github.com/iris-connect/iris-client/commit/7ced63142517aba0c97d57165dbd8e31a3e388f2)), closes [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [#83](https://github.com/iris-connect/iris-client/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83) [iris-gateway/iris-backlog#50](https://github.com/iris-gateway/iris-backlog/issues/50) [iris-gateway/iris-backlog#83](https://github.com/iris-gateway/iris-backlog/issues/83)
* weekly data api implementation. Refs iris-gateway/iris-backlog#{86} ([19de6ba](https://github.com/iris-connect/iris-client/commit/19de6ba2e8c95b20fa7ccd265643073cc52e7771))
* weekly data visualization ([#79](https://github.com/iris-connect/iris-client/issues/79)) ([e42822c](https://github.com/iris-connect/iris-client/commit/e42822ca3c6553535bc26bb005e89d9f603c24ef))


### BREAKING CHANGES

* this requires an iris-gateway deployment with public-proxy-eps

Co-authored-by: Tim <62595633+lucky-lusa@users.noreply.github.com>
