# Changelog

## [1.5.1](https://github.com/iris-connect/iris-client/compare/v1.5.0...v1.5.1) (2022-03-29)


### Bug Fixes

* **Messages:** Improves the performance of the . The list of possible recipients for messages is now created cyclically in a background job and is immediately available for the frontend. ([73cda44](https://github.com/iris-connect/iris-client/commit/73cda44780a74c24638e6841eb1ba4d3e245348a)), closes [#678](https://github.com/iris-connect/iris-client/issues/678) [#680](https://github.com/iris-connect/iris-client/issues/680)
* Removes long deprecated environment variables from `.env.sample`. This variables have had no effect for some time. ([9bf0a55](https://github.com/iris-connect/iris-client/commit/9bf0a559c9e9227f2d6325cb2a5ce63a06d8c2ff)), closes [#679](https://github.com/iris-connect/iris-client/issues/679)
* RPC methods can now be extended with additional parameters while still remaining compatible with legacy RPC clients if default values are used. ([4320b23](https://github.com/iris-connect/iris-client/commit/4320b23e77b4b53a0f2de0ea0fa59df29ca935d3)), closes [iris-connect/iris-backlog#278](https://github.com/iris-connect/iris-backlog/issues/278)
* Updates EPS to version v0.2.6 ([2cd3a4a](https://github.com/iris-connect/iris-client/commit/2cd3a4a482255971e326414a203cd16fe1b2192a))

## [1.5.1-rc.2](https://github.com/iris-connect/iris-client/compare/v1.5.1-rc.1...v1.5.1-rc.2) (2022-03-25)


### Bug Fixes

* Updates EPS to version v0.2.6 ([2cd3a4a](https://github.com/iris-connect/iris-client/commit/2cd3a4a482255971e326414a203cd16fe1b2192a))

## [1.5.1-rc.1](https://github.com/iris-connect/iris-client/compare/v1.5.0...v1.5.1-rc.1) (2022-03-25)


### Bug Fixes

* **Messages:** Improves the performance of the . The list of possible recipients for messages is now created cyclically in a background job and is immediately available for the frontend. ([73cda44](https://github.com/iris-connect/iris-client/commit/73cda44780a74c24638e6841eb1ba4d3e245348a)), closes [#678](https://github.com/iris-connect/iris-client/issues/678) [#680](https://github.com/iris-connect/iris-client/issues/680)
* Removes long deprecated environment variables from `.env.sample`. This variables have had no effect for some time. ([9bf0a55](https://github.com/iris-connect/iris-client/commit/9bf0a559c9e9227f2d6325cb2a5ce63a06d8c2ff)), closes [#679](https://github.com/iris-connect/iris-client/issues/679)
* RPC methods can now be extended with additional parameters while still remaining compatible with legacy RPC clients if default values are used. ([4320b23](https://github.com/iris-connect/iris-client/commit/4320b23e77b4b53a0f2de0ea0fa59df29ca935d3)), closes [iris-connect/iris-backlog#278](https://github.com/iris-connect/iris-backlog/issues/278)

# [1.5.0](https://github.com/iris-connect/iris-client/compare/v1.4.1...v1.5.0) (2022-03-16)


### Bug Fixes

* Adds grpc server to client EPS. Still not reachable from outside, but necessary for communication between health departments. ([679c08e](https://github.com/iris-connect/iris-client/commit/679c08e13ed2e40fe2bcbf8be486f50641e31af5)), closes [#637](https://github.com/iris-connect/iris-client/issues/637)
* Fixes an error when changing user data (IllegalStateException: Cannot convert value of type 'java.lang.String' […] no matching editors or conversion strategy found). ([58b3d42](https://github.com/iris-connect/iris-client/commit/58b3d425f71098f53ab54e52fea51cad2ccc9e10))


### Features

* Adds a hint to search for hd-contacts by postal code or city into the message input frontend. ([032a648](https://github.com/iris-connect/iris-client/commit/032a6482d9e43b426b34b3ff3dff25ca53c77fc9)), closes [#632](https://github.com/iris-connect/iris-client/issues/632) [#636](https://github.com/iris-connect/iris-client/issues/636)
* Displays meta-data (who and when created/last modified an entity) on event-tracking, index-tracking and user detail pages. ([da1b7a6](https://github.com/iris-connect/iris-client/commit/da1b7a66097d864e6ab0f93b296d1476675b19c8)), closes [iris-connect/iris-backlog#234](https://github.com/iris-connect/iris-backlog/issues/234) [#638](https://github.com/iris-connect/iris-client/issues/638)
* **Docker Compose:** Adds scope labels to the services in the Docker Compose file to avoid conflicts with possibly existing other instances of Watchtower on the same Docker host. ([499267f](https://github.com/iris-connect/iris-client/commit/499267f05d61e74a0100649be80863a0bb508563)), closes [#666](https://github.com/iris-connect/iris-client/issues/666)
* **Messages:** Uses `_ping` and the EPS version check when building the recipient list to determine if a health department is able to receive messages. This avoids additional configuration and enables faster propagation of the feature. ([446da17](https://github.com/iris-connect/iris-client/commit/446da176fa26cc3dd81151048b6133a30e964b54)), closes [#668](https://github.com/iris-connect/iris-client/issues/668)
* **vaccination report:** Apps connected to EPS can announce the submission of a vaccination report via the JSON-RPC method `announceVaccinationInfoList`. A transmission channel is then opened briefly for the respective user of the app. ([c38078a](https://github.com/iris-connect/iris-client/commit/c38078a4aadc98d14fb6e4995aab34a312cc3279)), closes [iris-connect/iris-backlog#273](https://github.com/iris-connect/iris-backlog/issues/273) [#635](https://github.com/iris-connect/iris-client/issues/635)
* **vaccination report:** Apps connected to EPS can submit a vaccination report via the JSON-RPC method `submitVaccinationInfoList`. The information is saved and made available to health department staff via the front end. ([72b1f74](https://github.com/iris-connect/iris-client/commit/72b1f7452e17957392db5f430da0b9909744859d)), closes [iris-connect/iris-backlog#274](https://github.com/iris-connect/iris-backlog/issues/274) [#651](https://github.com/iris-connect/iris-client/issues/651)
* **vaccination report:** There is a new view in the front end with an overview of submitted vaccination reports. In the details view of a vaccination report, the submitted persons are displayed with their vaccination status. ([00e93e8](https://github.com/iris-connect/iris-client/commit/00e93e88dcfea4c4eb71577127bf80d5d892b70a)), closes [iris-connect/iris-backlog#275](https://github.com/iris-connect/iris-backlog/issues/275) [#629](https://github.com/iris-connect/iris-client/issues/629)

# [1.5.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.5.0-rc.2...v1.5.0-rc.3) (2022-03-15)


### Features

* **Messages:** Uses `_ping` and the EPS version check when building the recipient list to determine if a health department is able to receive messages. This avoids additional configuration and enables faster propagation of the feature. ([446da17](https://github.com/iris-connect/iris-client/commit/446da176fa26cc3dd81151048b6133a30e964b54)), closes [#668](https://github.com/iris-connect/iris-client/issues/668)

# [1.5.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.5.0-rc.1...v1.5.0-rc.2) (2022-03-14)


### Features

* **Docker Compose:** Adds scope labels to the services in the Docker Compose file to avoid conflicts with possibly existing other instances of Watchtower on the same Docker host. ([499267f](https://github.com/iris-connect/iris-client/commit/499267f05d61e74a0100649be80863a0bb508563)), closes [#666](https://github.com/iris-connect/iris-client/issues/666)

# [1.5.0-rc.1](https://github.com/iris-connect/iris-client/compare/v1.4.1...v1.5.0-rc.1) (2022-03-09)


### Bug Fixes

* Adds grpc server to client EPS. Still not reachable from outside, but necessary for communication between health departments. ([679c08e](https://github.com/iris-connect/iris-client/commit/679c08e13ed2e40fe2bcbf8be486f50641e31af5)), closes [#637](https://github.com/iris-connect/iris-client/issues/637)
* Fixes an error when changing user data (IllegalStateException: Cannot convert value of type 'java.lang.String' […] no matching editors or conversion strategy found). ([58b3d42](https://github.com/iris-connect/iris-client/commit/58b3d425f71098f53ab54e52fea51cad2ccc9e10))


### Features

* Adds a hint to search for hd-contacts by postal code or city into the message input frontend. ([032a648](https://github.com/iris-connect/iris-client/commit/032a6482d9e43b426b34b3ff3dff25ca53c77fc9)), closes [#632](https://github.com/iris-connect/iris-client/issues/632) [#636](https://github.com/iris-connect/iris-client/issues/636)
* Displays meta-data (who and when created/last modified an entity) on event-tracking, index-tracking and user detail pages. ([da1b7a6](https://github.com/iris-connect/iris-client/commit/da1b7a66097d864e6ab0f93b296d1476675b19c8)), closes [iris-connect/iris-backlog#234](https://github.com/iris-connect/iris-backlog/issues/234) [#638](https://github.com/iris-connect/iris-client/issues/638)
* **vaccination report:** Apps connected to EPS can announce the submission of a vaccination report via the JSON-RPC method `announceVaccinationInfoList`. A transmission channel is then opened briefly for the respective user of the app. ([c38078a](https://github.com/iris-connect/iris-client/commit/c38078a4aadc98d14fb6e4995aab34a312cc3279)), closes [iris-connect/iris-backlog#273](https://github.com/iris-connect/iris-backlog/issues/273) [#635](https://github.com/iris-connect/iris-client/issues/635)
* **vaccination report:** Apps connected to EPS can submit a vaccination report via the JSON-RPC method `submitVaccinationInfoList`. The information is saved and made available to health department staff via the front end. ([72b1f74](https://github.com/iris-connect/iris-client/commit/72b1f7452e17957392db5f430da0b9909744859d)), closes [iris-connect/iris-backlog#274](https://github.com/iris-connect/iris-backlog/issues/274) [#651](https://github.com/iris-connect/iris-client/issues/651)
* **vaccination report:** There is a new view in the front end with an overview of submitted vaccination reports. In the details view of a vaccination report, the submitted persons are displayed with their vaccination status. ([00e93e8](https://github.com/iris-connect/iris-client/commit/00e93e88dcfea4c4eb71577127bf80d5d892b70a)), closes [iris-connect/iris-backlog#275](https://github.com/iris-connect/iris-backlog/issues/275) [#629](https://github.com/iris-connect/iris-client/issues/629)

## [1.4.1](https://github.com/iris-connect/iris-client/compare/v1.4.0...v1.4.1) (2022-03-01)


### Bug Fixes

* Adds grpc server to client EPS. Still not reachable from outside, but necessary for communication between health departments. ([5ac3206](https://github.com/iris-connect/iris-client/commit/5ac3206960b99ff4060b8ff50425be25db831aeb)), closes [#637](https://github.com/iris-connect/iris-client/issues/637)
* Fixes an error when changing user data (IllegalStateException: Cannot convert value of type 'java.lang.String' […] no matching editors or conversion strategy found). ([2b41d0a](https://github.com/iris-connect/iris-client/commit/2b41d0a596d0794fb89593d60e4025d52661d5cc))

# [1.4.0](https://github.com/iris-connect/iris-client/compare/v1.3.1...v1.4.0) (2022-02-25)


### Bug Fixes

* **deps:** Updates Postgresql JDBC driver (now 42.3.2) to fix a vulnerability in this dependency. ([7dd2641](https://github.com/iris-connect/iris-client/commit/7dd2641b44ced77146461622c4b84f8fa2eb48ac))
* Removes license reference for code from SORMAS that isn't used anymore. ([00db4a4](https://github.com/iris-connect/iris-client/commit/00db4a410fad6edc69ae7437c73030309a74ca5a))
* Updates data-export code to handle breaking changes of sheetJS module export. ([f4f38c8](https://github.com/iris-connect/iris-client/commit/f4f38c81e7c3626d52bbedd837a128b569c522fb)), closes [#628](https://github.com/iris-connect/iris-client/issues/628)


### Features

* Extends the audit metadata in BFF to save who create and last modify an entity. ([246eaa5](https://github.com/iris-connect/iris-client/commit/246eaa5833cbe85d765093420df9f729221875dd)), closes [iris-connect/iris-backlog#234](https://github.com/iris-connect/iris-backlog/issues/234) [#597](https://github.com/iris-connect/iris-client/issues/597)
* Health departments with IRIS can send messages to each other, encrypted and secured via the IRIS-Connect secure network. There is a central output and input for messages. Currently, only text messages are possible. In the near future, data sets as attachments will follow. ([6922f2c](https://github.com/iris-connect/iris-client/commit/6922f2c8dd3f7bbf0eed4b576625c45762450379)), closes [iris-connect/iris-backlog#256](https://github.com/iris-connect/iris-backlog/issues/256) [iris-connect/iris-backlog#259](https://github.com/iris-connect/iris-backlog/issues/259) [#541](https://github.com/iris-connect/iris-client/issues/541)
* Images are signed before uploading to docker to insure their integrity. ([20f4381](https://github.com/iris-connect/iris-client/commit/20f4381115bb24d5f0ecc550c9ba66bb1c3f5b86)), closes [#569](https://github.com/iris-connect/iris-client/issues/569)

# [1.4.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.4.0-rc.2...v1.4.0-rc.3) (2022-02-21)


### Bug Fixes

* Removes license reference for code from SORMAS that isn't used anymore. ([00db4a4](https://github.com/iris-connect/iris-client/commit/00db4a410fad6edc69ae7437c73030309a74ca5a))

# [1.4.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.4.0-rc.1...v1.4.0-rc.2) (2022-02-21)


### Bug Fixes

* **deps:** Updates Postgresql JDBC driver (now 42.3.2) to fix a vulnerability in this dependency. ([7dd2641](https://github.com/iris-connect/iris-client/commit/7dd2641b44ced77146461622c4b84f8fa2eb48ac))
* Updates data-export code to handle breaking changes of sheetJS module export. ([f4f38c8](https://github.com/iris-connect/iris-client/commit/f4f38c81e7c3626d52bbedd837a128b569c522fb)), closes [#628](https://github.com/iris-connect/iris-client/issues/628)


### Features

* Extends the audit metadata in BFF to save who create and last modify an entity. ([246eaa5](https://github.com/iris-connect/iris-client/commit/246eaa5833cbe85d765093420df9f729221875dd)), closes [iris-connect/iris-backlog#234](https://github.com/iris-connect/iris-backlog/issues/234) [#597](https://github.com/iris-connect/iris-client/issues/597)
* Health departments with IRIS can send messages to each other, encrypted and secured via the IRIS-Connect secure network. There is a central output and input for messages. Currently, only text messages are possible. In the near future, data sets as attachments will follow. ([6922f2c](https://github.com/iris-connect/iris-client/commit/6922f2c8dd3f7bbf0eed4b576625c45762450379)), closes [iris-connect/iris-backlog#256](https://github.com/iris-connect/iris-backlog/issues/256) [iris-connect/iris-backlog#259](https://github.com/iris-connect/iris-backlog/issues/259) [#541](https://github.com/iris-connect/iris-client/issues/541)

# [1.4.0-rc.1](https://github.com/iris-connect/iris-client/compare/v1.3.1...v1.4.0-rc.1) (2022-02-04)


### Features

* Images are signed before uploading to docker to insure their integrity. ([20f4381](https://github.com/iris-connect/iris-client/commit/20f4381115bb24d5f0ecc550c9ba66bb1c3f5b86)), closes [#569](https://github.com/iris-connect/iris-client/issues/569)

## [1.3.1](https://github.com/iris-connect/iris-client/compare/v1.3.0...v1.3.1) (2022-02-01)


### Bug Fixes

* D-Trust root certificates were missing and are now included in docker-images ([7ea894a](https://github.com/iris-connect/iris-client/commit/7ea894a579979861b36d1c5651cacf60a2a39ea2))

# [1.3.0](https://github.com/iris-connect/iris-client/compare/v1.2.1...v1.3.0) (2022-02-01)


### Bug Fixes

* Avoids irrelevant but logged ClassNotFoundException. It is correct that the class iris.backend_service.configurations.CentralConfigurationException is missing. ([89c2da8](https://github.com/iris-connect/iris-client/commit/89c2da81bc1b009e10026a1d9802386ad3f6a2de)), closes [#527](https://github.com/iris-connect/iris-client/issues/527)
* Changes the certificate paths to the new schema introduced with new EPS. ([6772bdc](https://github.com/iris-connect/iris-client/commit/6772bdc8c98939a0684e890946232ffa399d929c))
* Changes the certificate paths to the new schema introduced with new EPS. ([fe1a0b3](https://github.com/iris-connect/iris-client/commit/fe1a0b3c89abb306f178e88f963881c684b36540))
* Cleans the `.env(.sample)` from values that are prescribed for the environments. Admins in the HDs do not have to do anything with it now, instead the correct values are set internally based on the `IRIS_ENV`. ([0211219](https://github.com/iris-connect/iris-client/commit/0211219f794160f294664f27e77452dbe4bc0a53)), closes [#566](https://github.com/iris-connect/iris-client/issues/566)
* **Deps:** Sets new log4j version to fix current vulnerability in versions ≤2.17.0 ([c57d32f](https://github.com/iris-connect/iris-client/commit/c57d32ff1907124e6466b32f5fba5331fa74596f))
* from version 0.22.0 Trivy scans POMs and disables JAR detection ([ce5f37b](https://github.com/iris-connect/iris-client/commit/ce5f37b4be3183bfdf87ba23b5348029d9a7b1ed))
* ignores vulnerability with unclear reason ([5fdc9db](https://github.com/iris-connect/iris-client/commit/5fdc9dbf95bbf36fab44d9485f9a820554a635d9))
* Removes a few default values for configurations that do not work in general, but hide the lack of an individual configuration. If the `.env(.sample)` has been filled in according to the notes it contains, then all mandatory values are already set correctly and no action is required. ([c61dc00](https://github.com/iris-connect/iris-client/commit/c61dc00cd179361b158ea625d7a749c386bb1399)), closes [#561](https://github.com/iris-connect/iris-client/issues/561)
* Removes the long deprecated environment variable TRUSTED_CA_CRT from `.env.sample`. This has not had any effect for some time. ([1e19b7e](https://github.com/iris-connect/iris-client/commit/1e19b7ea62f48f87999b78cf7d73037e6f22a6ee)), closes [#554](https://github.com/iris-connect/iris-client/issues/554) [#555](https://github.com/iris-connect/iris-client/issues/555)
* Stack traces are not included in error responses now to avoid giving too many internal details to the outside world for error conditions. ([07c6476](https://github.com/iris-connect/iris-client/commit/07c647615acb9581f2a71f8442554f5bdf05a2db)), closes [#560](https://github.com/iris-connect/iris-client/issues/560)
* **Standalone installation:** The announcement database file is now relative to the execution path by default and can be additionally configured with ANNOUNCEMENT_DB_FOLDER. ([f9e5bf9](https://github.com/iris-connect/iris-client/commit/f9e5bf91b60a10a8d533e61d2dc26502ff05b196)), closes [#222](https://github.com/iris-connect/iris-client/issues/222) [#528](https://github.com/iris-connect/iris-client/issues/528)
* The property `SECURITY_JWT_SHARED_SECRET` is now deprecated and is no longer considered. Instead, the secret is set randomly. This relieves the administrators of the IRIS client from this technically necessary detail. ([1bfe801](https://github.com/iris-connect/iris-client/commit/1bfe801dcc4637aa5a8fc8d9f14c9e3362813b8a)), closes [#553](https://github.com/iris-connect/iris-client/issues/553) [#556](https://github.com/iris-connect/iris-client/issues/556)
* Updates EPS from version from v0.1.69 to v0.2.1 ([fc4e767](https://github.com/iris-connect/iris-client/commit/fc4e7671e531159d40df86233687969bc0ad7287)), closes [#526](https://github.com/iris-connect/iris-client/issues/526)


### Features

* Adds alerts about configuration mismatch of proxy target subdomain. ([7327bf8](https://github.com/iris-connect/iris-client/commit/7327bf8f18876ec5532e93608c304daa628f6e12)), closes [#529](https://github.com/iris-connect/iris-client/issues/529)
* **Codescan:** adds more queries to the CodeQL scan to be more helpful ([8fb828c](https://github.com/iris-connect/iris-client/commit/8fb828c4e5b7ab1adf275195c39fbeb7ffaeef1f))
* **Database:** Updates the integrated PostgreSQL database (Docker-Compose installation) to the version `13.5`. ([60ed899](https://github.com/iris-connect/iris-client/commit/60ed8995745cf4a51a92ce9961b134f7c1fe3dff)), closes [#540](https://github.com/iris-connect/iris-client/issues/540)
* set content specific cell formats to excel export for octoware ([5d2854d](https://github.com/iris-connect/iris-client/commit/5d2854d286c5340aabd0f015df5837e3f7633fe2)), closes [iris-connect/iris-backlog#264](https://github.com/iris-connect/iris-backlog/issues/264)
* **Standalone installation:** Provides a script to check and import the now required root certificates to the key store of the used Java installation. This script runs together with 'start-iris-client-bff.sh'. ([ed691f8](https://github.com/iris-connect/iris-client/commit/ed691f8ee801e59601cfbb13fbfaca68012b78a8)), closes [iris-connect/iris-backlog#226](https://github.com/iris-connect/iris-backlog/issues/226) [#586](https://github.com/iris-connect/iris-client/issues/586)
* The certificate of the local EPS is validated by the IRIS Client now. This will further improve security within the health department installation. ([870ae47](https://github.com/iris-connect/iris-client/commit/870ae4747980039c19284c8b6142562b2f7840a3)), closes [iris-connect/iris-backlog#226](https://github.com/iris-connect/iris-backlog/issues/226) [#568](https://github.com/iris-connect/iris-client/issues/568)
* To protect against flooding with very large amounts of data, submissions is now limited. This concerns both the pure amount of data and the number of elements per data submission. ([7733fe8](https://github.com/iris-connect/iris-client/commit/7733fe84d91480512d3e1a1a479d6cdadfb04a4a)), closes [iris-backlog/issues#180](https://github.com/iris-backlog/issues/issues/180) [#573](https://github.com/iris-connect/iris-client/issues/573)


### Reverts

* Revert "chore: test of templates for PRs" ([15b92dd](https://github.com/iris-connect/iris-client/commit/15b92dd5b6e573e1ebed38329227f37f8008461a))
* Revert "chore: test of templates for issuess" ([b96fbeb](https://github.com/iris-connect/iris-client/commit/b96fbeb4cf6078ae226db169a2f2c2cefec742b1))

# [1.3.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.3.0-rc.2...v1.3.0-rc.3) (2022-02-01)


### Bug Fixes

* Changes the certificate paths to the new schema introduced with new EPS. ([6772bdc](https://github.com/iris-connect/iris-client/commit/6772bdc8c98939a0684e890946232ffa399d929c))


### Features

* **Standalone installation:** Provides a script to check and import the now required root certificates to the key store of the used Java installation. This script runs together with 'start-iris-client-bff.sh'. ([ed691f8](https://github.com/iris-connect/iris-client/commit/ed691f8ee801e59601cfbb13fbfaca68012b78a8)), closes [iris-connect/iris-backlog#226](https://github.com/iris-connect/iris-backlog/issues/226) [#586](https://github.com/iris-connect/iris-client/issues/586)

# [1.3.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.3.0-rc.1...v1.3.0-rc.2) (2022-01-26)


### Bug Fixes

* Changes the certificate paths to the new schema introduced with new EPS. ([fe1a0b3](https://github.com/iris-connect/iris-client/commit/fe1a0b3c89abb306f178e88f963881c684b36540))

# [1.3.0-rc.1](https://github.com/iris-connect/iris-client/compare/v1.2.1...v1.3.0-rc.1) (2022-01-26)


### Bug Fixes

* Avoids irrelevant but logged ClassNotFoundException. It is correct that the class iris.backend_service.configurations.CentralConfigurationException is missing. ([89c2da8](https://github.com/iris-connect/iris-client/commit/89c2da81bc1b009e10026a1d9802386ad3f6a2de)), closes [#527](https://github.com/iris-connect/iris-client/issues/527)
* Cleans the `.env(.sample)` from values that are prescribed for the environments. Admins in the HDs do not have to do anything with it now, instead the correct values are set internally based on the `IRIS_ENV`. ([0211219](https://github.com/iris-connect/iris-client/commit/0211219f794160f294664f27e77452dbe4bc0a53)), closes [#566](https://github.com/iris-connect/iris-client/issues/566)
* **Deps:** Sets new log4j version to fix current vulnerability in versions ≤2.17.0 ([c57d32f](https://github.com/iris-connect/iris-client/commit/c57d32ff1907124e6466b32f5fba5331fa74596f))
* from version 0.22.0 Trivy scans POMs and disables JAR detection ([ce5f37b](https://github.com/iris-connect/iris-client/commit/ce5f37b4be3183bfdf87ba23b5348029d9a7b1ed))
* ignores vulnerability with unclear reason ([5fdc9db](https://github.com/iris-connect/iris-client/commit/5fdc9dbf95bbf36fab44d9485f9a820554a635d9))
* Removes a few default values for configurations that do not work in general, but hide the lack of an individual configuration. If the `.env(.sample)` has been filled in according to the notes it contains, then all mandatory values are already set correctly and no action is required. ([c61dc00](https://github.com/iris-connect/iris-client/commit/c61dc00cd179361b158ea625d7a749c386bb1399)), closes [#561](https://github.com/iris-connect/iris-client/issues/561)
* Removes the long deprecated environment variable TRUSTED_CA_CRT from `.env.sample`. This has not had any effect for some time. ([1e19b7e](https://github.com/iris-connect/iris-client/commit/1e19b7ea62f48f87999b78cf7d73037e6f22a6ee)), closes [#554](https://github.com/iris-connect/iris-client/issues/554) [#555](https://github.com/iris-connect/iris-client/issues/555)
* Stack traces are not included in error responses now to avoid giving too many internal details to the outside world for error conditions. ([07c6476](https://github.com/iris-connect/iris-client/commit/07c647615acb9581f2a71f8442554f5bdf05a2db)), closes [#560](https://github.com/iris-connect/iris-client/issues/560)
* **Standalone installation:** The announcement database file is now relative to the execution path by default and can be additionally configured with ANNOUNCEMENT_DB_FOLDER. ([f9e5bf9](https://github.com/iris-connect/iris-client/commit/f9e5bf91b60a10a8d533e61d2dc26502ff05b196)), closes [#222](https://github.com/iris-connect/iris-client/issues/222) [#528](https://github.com/iris-connect/iris-client/issues/528)
* The property `SECURITY_JWT_SHARED_SECRET` is now deprecated and is no longer considered. Instead, the secret is set randomly. This relieves the administrators of the IRIS client from this technically necessary detail. ([1bfe801](https://github.com/iris-connect/iris-client/commit/1bfe801dcc4637aa5a8fc8d9f14c9e3362813b8a)), closes [#553](https://github.com/iris-connect/iris-client/issues/553) [#556](https://github.com/iris-connect/iris-client/issues/556)
* Updates EPS from version from v0.1.69 to v0.2.1 ([fc4e767](https://github.com/iris-connect/iris-client/commit/fc4e7671e531159d40df86233687969bc0ad7287)), closes [#526](https://github.com/iris-connect/iris-client/issues/526)


### Features

* Adds alerts about configuration mismatch of proxy target subdomain. ([7327bf8](https://github.com/iris-connect/iris-client/commit/7327bf8f18876ec5532e93608c304daa628f6e12)), closes [#529](https://github.com/iris-connect/iris-client/issues/529)
* **Codescan:** adds more queries to the CodeQL scan to be more helpful ([8fb828c](https://github.com/iris-connect/iris-client/commit/8fb828c4e5b7ab1adf275195c39fbeb7ffaeef1f))
* **Database:** Updates the integrated PostgreSQL database (Docker-Compose installation) to the version `13.5`. ([60ed899](https://github.com/iris-connect/iris-client/commit/60ed8995745cf4a51a92ce9961b134f7c1fe3dff)), closes [#540](https://github.com/iris-connect/iris-client/issues/540)
* set content specific cell formats to excel export for octoware ([5d2854d](https://github.com/iris-connect/iris-client/commit/5d2854d286c5340aabd0f015df5837e3f7633fe2)), closes [iris-connect/iris-backlog#264](https://github.com/iris-connect/iris-backlog/issues/264)
* The certificate of the local EPS is validated by the IRIS Client now. This will further improve security within the health department installation. ([870ae47](https://github.com/iris-connect/iris-client/commit/870ae4747980039c19284c8b6142562b2f7840a3)), closes [iris-connect/iris-backlog#226](https://github.com/iris-connect/iris-backlog/issues/226) [#568](https://github.com/iris-connect/iris-client/issues/568)
* To protect against flooding with very large amounts of data, submissions is now limited. This concerns both the pure amount of data and the number of elements per data submission. ([7733fe8](https://github.com/iris-connect/iris-client/commit/7733fe84d91480512d3e1a1a479d6cdadfb04a4a)), closes [iris-backlog/issues#180](https://github.com/iris-backlog/issues/issues/180) [#573](https://github.com/iris-connect/iris-client/issues/573)


### Reverts

* Revert "chore: test of templates for PRs" ([15b92dd](https://github.com/iris-connect/iris-client/commit/15b92dd5b6e573e1ebed38329227f37f8008461a))
* Revert "chore: test of templates for issuess" ([b96fbeb](https://github.com/iris-connect/iris-client/commit/b96fbeb4cf6078ae226db169a2f2c2cefec742b1))

## [1.2.1](https://github.com/iris-connect/iris-client/compare/v1.2.0...v1.2.1) (2021-12-14)


### Bug Fixes

* Fixes incorrect use of random generators when selecting words for readable tokens and replaces it with a secure random generator. ([219ab0e](https://github.com/iris-connect/iris-client/commit/219ab0e289165d2412068049c47fc42ed420919d)), closes [#525](https://github.com/iris-connect/iris-client/issues/525)

# [1.2.0](https://github.com/iris-connect/iris-client/compare/v1.1.1...v1.2.0) (2021-12-14)


### Bug Fixes

* `+` at the beginning of phone numbers are now preserved during CSV export. So an international phone number now remains complete. ([3464165](https://github.com/iris-connect/iris-client/commit/346416561859d8bb102f0e7126ef75eb852c24bc)), closes [#443](https://github.com/iris-connect/iris-client/issues/443) [#445](https://github.com/iris-connect/iris-client/issues/445)
* An admin can now change a user's data again. An error occurred ([0fda2b6](https://github.com/iris-connect/iris-client/commit/0fda2b6728f9441e30d4e9d7e7954e05990cf112))
* apply removal of the index-tracking-url to e2e tests and remove the csv data export test for index-tracking as there is no data to export ([0a2a029](https://github.com/iris-connect/iris-client/commit/0a2a0299e9b312e7d653ad4ca2cc450494d6e669))
* Avoids readable but not writable announcement.db files for EPS by an update from EPS version v0.1.68 to v0.1.69. ([0ab8c9e](https://github.com/iris-connect/iris-client/commit/0ab8c9e310d4aec259cdacc314c968262863fe9c))
* Cleans the Docker image of the frontend from artifacts that are only needed for the build. This makes it more secure and greatly reduced in size. ([63051b1](https://github.com/iris-connect/iris-client/commit/63051b10ed5c0d6c6ba4128cf8d6f5df9a173584)), closes [#442](https://github.com/iris-connect/iris-client/issues/442)
* Clearing case_data_request before adding new not-null fields. Not critical, because feature is not in use yet. ([9389239](https://github.com/iris-connect/iris-client/commit/9389239d094676b0643746d91d1c4c4543261241)), closes [#490](https://github.com/iris-connect/iris-client/issues/490)
* **data export:** Allow more number variants in the phone number sanitization. ([a181c34](https://github.com/iris-connect/iris-client/commit/a181c34ff824d401b4f351996ea490564bfc30fe)), closes [#478](https://github.com/iris-connect/iris-client/issues/478) [#479](https://github.com/iris-connect/iris-client/issues/479)
* extend data export sanitization whitelist to allow a wider range of Latin Letters ([4082e5f](https://github.com/iris-connect/iris-client/commit/4082e5fb80860ccc678e0f96d1b7d91809cc13a8))
* Improves timeouts in Client and extends the checks for the status (EPS version) to better determine the status. ([af73f61](https://github.com/iris-connect/iris-client/commit/af73f61e5c2e6fd0376a33a153f6786843d6dedd)), closes [#509](https://github.com/iris-connect/iris-client/issues/509)
* Increases burst and delay to allow a large number of status requests for the apps. ([9945be5](https://github.com/iris-connect/iris-client/commit/9945be5a3194de0214859535e161aac3e18de8a3)), closes [#500](https://github.com/iris-connect/iris-client/issues/500)
* new pre release ([e81866c](https://github.com/iris-connect/iris-client/commit/e81866cfae3fb6aac2529b58db5b28af8a465c58))
* new pre release ([3028f64](https://github.com/iris-connect/iris-client/commit/3028f64c7347ee3e83db9562955e35a924d4563a))
* Restricts the content of …deployment….zip to the folders and files relevant for deployment. The previously included parent folder is now removed. ([580e551](https://github.com/iris-connect/iris-client/commit/580e551ea4181f16d7808e8fdad6139a2dbf8aed)), closes [#432](https://github.com/iris-connect/iris-client/issues/432) [#433](https://github.com/iris-connect/iris-client/issues/433)
* update cypress to fix vulnerable dependencies and update the other npm packages to the latest versions ([38dc6c7](https://github.com/iris-connect/iris-client/commit/38dc6c7145bdb232e2d0c176890c70dc7c4d3031)), closes [#498](https://github.com/iris-connect/iris-client/issues/498)


### Features

* A readable code is now used for the data request to an index case. This consists of four random words and an abbreviation for the department. ([e913f8a](https://github.com/iris-connect/iris-client/commit/e913f8a3fd60fcfd70b422866048a38e26f64344)), closes [iris-connect/iris-backlog#245](https://github.com/iris-connect/iris-backlog/issues/245) [#460](https://github.com/iris-connect/iris-client/issues/460)
* Add event tracking xlsx data export for octoware, refactor and unify the data export functionality ([b442014](https://github.com/iris-connect/iris-client/commit/b4420144c6569f9f2c1d53a46c63652be528325e))
* **Admin:** Docker is now told the health status of the client backend (BFF) container. Administrators can build monitoring functions on this. ([8d262ac](https://github.com/iris-connect/iris-client/commit/8d262ace1ebaf34ac4460a9932cde46d23ed0e44)), closes [#451](https://github.com/iris-connect/iris-client/issues/451)
* Display checkin app status information in the frontend to inform the user if there are problems with a connected data provider ([c0c21f1](https://github.com/iris-connect/iris-client/commit/c0c21f1a5617f0ea1e8047523374ca9a25b5c9f4)), closes [iris-connect/iris-backlog#221](https://github.com/iris-connect/iris-backlog/issues/221) [#444](https://github.com/iris-connect/iris-client/issues/444)
* Provides excel export for event tracking standard format and improves table selection usability. ([5132b0a](https://github.com/iris-connect/iris-client/commit/5132b0a8edde6986b958e10f07d81b1316974d93)), closes [#266](https://github.com/iris-connect/iris-client/issues/266) [#476](https://github.com/iris-connect/iris-client/issues/476)
* Updates EPS from version v0.1.66 to v0.1.68. ([c7afbf5](https://github.com/iris-connect/iris-client/commit/c7afbf5efe92cddfa18a136704385fd74c2f727b))
* Uses and requires Java 17 (new long term support version) as baseline. ([558171b](https://github.com/iris-connect/iris-client/commit/558171ba2698c8cb8f4ea8df8e3c31548ed3602f)), closes [#393](https://github.com/iris-connect/iris-client/issues/393)
* When a user changes his password, the old password is now also expected and checked to ensure it is correct. ([72af5dc](https://github.com/iris-connect/iris-client/commit/72af5dce9d8d2ac57f22e97d368586deba345f27)), closes [iris-connect/iris-backlog#250](https://github.com/iris-connect/iris-backlog/issues/250) [#431](https://github.com/iris-connect/iris-client/issues/431)

# [1.2.0-rc.6](https://github.com/iris-connect/iris-client/compare/v1.2.0-rc.5...v1.2.0-rc.6) (2021-12-10)


### Bug Fixes

* apply removal of the index-tracking-url to e2e tests and remove the csv data export test for index-tracking as there is no data to export ([0a2a029](https://github.com/iris-connect/iris-client/commit/0a2a0299e9b312e7d653ad4ca2cc450494d6e669))
* Avoids readable but not writable announcement.db files for EPS by an update from EPS version v0.1.68 to v0.1.69. ([0ab8c9e](https://github.com/iris-connect/iris-client/commit/0ab8c9e310d4aec259cdacc314c968262863fe9c))
* extend data export sanitization whitelist to allow a wider range of Latin Letters ([4082e5f](https://github.com/iris-connect/iris-client/commit/4082e5fb80860ccc678e0f96d1b7d91809cc13a8))
* Improves timeouts in Client and extends the checks for the status (EPS version) to better determine the status. ([af73f61](https://github.com/iris-connect/iris-client/commit/af73f61e5c2e6fd0376a33a153f6786843d6dedd)), closes [#509](https://github.com/iris-connect/iris-client/issues/509)
* update cypress to fix vulnerable dependencies and update the other npm packages to the latest versions ([38dc6c7](https://github.com/iris-connect/iris-client/commit/38dc6c7145bdb232e2d0c176890c70dc7c4d3031)), closes [#498](https://github.com/iris-connect/iris-client/issues/498)

# [1.2.0-rc.5](https://github.com/iris-connect/iris-client/compare/v1.2.0-rc.4...v1.2.0-rc.5) (2021-12-07)


### Bug Fixes

* Increases burst and delay to allow a large number of status requests for the apps. ([9945be5](https://github.com/iris-connect/iris-client/commit/9945be5a3194de0214859535e161aac3e18de8a3)), closes [#500](https://github.com/iris-connect/iris-client/issues/500)

# [1.2.0-rc.4](https://github.com/iris-connect/iris-client/compare/v1.2.0-rc.3...v1.2.0-rc.4) (2021-12-02)


### Bug Fixes

* new pre release ([e81866c](https://github.com/iris-connect/iris-client/commit/e81866cfae3fb6aac2529b58db5b28af8a465c58))

# [1.2.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.2.0-rc.2...v1.2.0-rc.3) (2021-11-29)


### Bug Fixes

* new pre release ([3028f64](https://github.com/iris-connect/iris-client/commit/3028f64c7347ee3e83db9562955e35a924d4563a))

# [1.2.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.2.0-rc.1...v1.2.0-rc.2) (2021-11-29)


### Bug Fixes

* Clearing case_data_request before adding new not-null fields. Not critical, because feature is not in use yet. ([9389239](https://github.com/iris-connect/iris-client/commit/9389239d094676b0643746d91d1c4c4543261241)), closes [#490](https://github.com/iris-connect/iris-client/issues/490)

# [1.2.0-rc.1](https://github.com/iris-connect/iris-client/compare/v1.1.1...v1.2.0-rc.1) (2021-11-29)


### Bug Fixes

* `+` at the beginning of phone numbers are now preserved during CSV export. So an international phone number now remains complete. ([3464165](https://github.com/iris-connect/iris-client/commit/346416561859d8bb102f0e7126ef75eb852c24bc)), closes [#443](https://github.com/iris-connect/iris-client/issues/443) [#445](https://github.com/iris-connect/iris-client/issues/445)
* An admin can now change a user's data again. An error occurred ([0fda2b6](https://github.com/iris-connect/iris-client/commit/0fda2b6728f9441e30d4e9d7e7954e05990cf112))
* Cleans the Docker image of the frontend from artifacts that are only needed for the build. This makes it more secure and greatly reduced in size. ([63051b1](https://github.com/iris-connect/iris-client/commit/63051b10ed5c0d6c6ba4128cf8d6f5df9a173584)), closes [#442](https://github.com/iris-connect/iris-client/issues/442)
* **data export:** Allow more number variants in the phone number sanitization. ([a181c34](https://github.com/iris-connect/iris-client/commit/a181c34ff824d401b4f351996ea490564bfc30fe)), closes [#478](https://github.com/iris-connect/iris-client/issues/478) [#479](https://github.com/iris-connect/iris-client/issues/479)
* Restricts the content of …deployment….zip to the folders and files relevant for deployment. The previously included parent folder is now removed. ([580e551](https://github.com/iris-connect/iris-client/commit/580e551ea4181f16d7808e8fdad6139a2dbf8aed)), closes [#432](https://github.com/iris-connect/iris-client/issues/432) [#433](https://github.com/iris-connect/iris-client/issues/433)


### Features

* A readable code is now used for the data request to an index case. This consists of four random words and an abbreviation for the department. ([e913f8a](https://github.com/iris-connect/iris-client/commit/e913f8a3fd60fcfd70b422866048a38e26f64344)), closes [iris-connect/iris-backlog#245](https://github.com/iris-connect/iris-backlog/issues/245) [#460](https://github.com/iris-connect/iris-client/issues/460)
* Add event tracking xlsx data export for octoware, refactor and unify the data export functionality ([b442014](https://github.com/iris-connect/iris-client/commit/b4420144c6569f9f2c1d53a46c63652be528325e))
* **Admin:** Docker is now told the health status of the client backend (BFF) container. Administrators can build monitoring functions on this. ([8d262ac](https://github.com/iris-connect/iris-client/commit/8d262ace1ebaf34ac4460a9932cde46d23ed0e44)), closes [#451](https://github.com/iris-connect/iris-client/issues/451)
* Display checkin app status information in the frontend to inform the user if there are problems with a connected data provider ([c0c21f1](https://github.com/iris-connect/iris-client/commit/c0c21f1a5617f0ea1e8047523374ca9a25b5c9f4)), closes [iris-connect/iris-backlog#221](https://github.com/iris-connect/iris-backlog/issues/221) [#444](https://github.com/iris-connect/iris-client/issues/444)
* Provides excel export for event tracking standard format and improves table selection usability. ([5132b0a](https://github.com/iris-connect/iris-client/commit/5132b0a8edde6986b958e10f07d81b1316974d93)), closes [#266](https://github.com/iris-connect/iris-client/issues/266) [#476](https://github.com/iris-connect/iris-client/issues/476)
* Updates EPS from version v0.1.66 to v0.1.68. ([c7afbf5](https://github.com/iris-connect/iris-client/commit/c7afbf5efe92cddfa18a136704385fd74c2f727b))
* Uses and requires Java 17 (new long term support version) as baseline. ([558171b](https://github.com/iris-connect/iris-client/commit/558171ba2698c8cb8f4ea8df8e3c31548ed3602f)), closes [#393](https://github.com/iris-connect/iris-client/issues/393)
* When a user changes his password, the old password is now also expected and checked to ensure it is correct. ([72af5dc](https://github.com/iris-connect/iris-client/commit/72af5dce9d8d2ac57f22e97d368586deba345f27)), closes [iris-connect/iris-backlog#250](https://github.com/iris-connect/iris-backlog/issues/250) [#431](https://github.com/iris-connect/iris-client/issues/431)

## [1.1.1](https://github.com/iris-connect/iris-client/compare/v1.1.0...v1.1.1) (2021-11-17)


### Bug Fixes

* The configuration for the private proxy now contains the necessary variable declarations again. Thanks to [@jl-sitnrw](https://github.com/jl-sitnrw) ([71ea823](https://github.com/iris-connect/iris-client/commit/71ea8239b07729729c9b4916e0b8ba28eef1222d)), closes [#467](https://github.com/iris-connect/iris-client/issues/467)
* The endpoint of the JSONRPC client is set to the right variable now. ([9d598bc](https://github.com/iris-connect/iris-client/commit/9d598bc3267f69ceeaa4e84d34d8abfe4f0ffd33))
* With the stand alone installation, there is no longer an error at startup because a property is set with the wrong name. Thanks to [@jl-sitnrw](https://github.com/jl-sitnrw) ([8aa669d](https://github.com/iris-connect/iris-client/commit/8aa669d392db703828b3e588ab708ef5e74fb073)), closes [#466](https://github.com/iris-connect/iris-client/issues/466)

# [1.1.0](https://github.com/iris-connect/iris-client/compare/v1.0.1...v1.1.0) (2021-11-03)


### Bug Fixes

* After an update, the frontend is automatically reloaded if it would ([cb98b9c](https://github.com/iris-connect/iris-client/commit/cb98b9cc4795dd9f792d70d4ec95d11ede6d4e6e)), closes [iris-connect/iris-backlog#229](https://github.com/iris-connect/iris-backlog/issues/229) [#358](https://github.com/iris-connect/iris-client/issues/358)
* An admin can now change a user's data again. An error occurred ([402baba](https://github.com/iris-connect/iris-client/commit/402babad6e1232bfff0eae8ed6fa5d2f5f2964e7))
* **build:** The image tags in the composite files are now correct with ([9146a1f](https://github.com/iris-connect/iris-client/commit/9146a1f11602506bcf2a16c0c94be84afa58ff89))
* Cleans the Docker image of the frontend from artifacts that are only needed for the build. This makes it more secure and greatly reduced in size. ([d62ca2a](https://github.com/iris-connect/iris-client/commit/d62ca2a15a59d680e9a913e2da71a6d7462917d0)), closes [#442](https://github.com/iris-connect/iris-client/issues/442)
* Fixed an occasional blank home screen when contact address information is missing. ([8a7fe34](https://github.com/iris-connect/iris-client/commit/8a7fe347e27d486db574145a1a605567246a8630)), closes [#350](https://github.com/iris-connect/iris-client/issues/350)
* Front end no longer runs as root in the container. ([6da6662](https://github.com/iris-connect/iris-client/commit/6da6662c205ffef5248239abf710b5eded4d32d0)), closes [iris-connect/iris-backlog#224](https://github.com/iris-connect/iris-backlog/issues/224) [#329](https://github.com/iris-connect/iris-client/issues/329)
* Front end no longer runs as root in the container. ([e8abc17](https://github.com/iris-connect/iris-client/commit/e8abc170228d17b24742f315b7e4f3a8105d1870)), closes [iris-connect/iris-backlog#224](https://github.com/iris-connect/iris-backlog/issues/224) [#326](https://github.com/iris-connect/iris-client/issues/326)
* If an error occurs in the app, a data request is now no longer ([9e0c176](https://github.com/iris-connect/iris-client/commit/9e0c1767e59bcfab40600f89693984cd3cf5b346)), closes [iris-connect/iris-backlog#124](https://github.com/iris-connect/iris-backlog/issues/124)
* International phone numbers will be accepted now. ([6b08b0a](https://github.com/iris-connect/iris-client/commit/6b08b0ac5a85e367d9b26e59eab0441d15b98a4e)), closes [iris-connect/iris-backlog#230](https://github.com/iris-connect/iris-backlog/issues/230)
* Meaningful error message if username is already used. ([e244049](https://github.com/iris-connect/iris-client/commit/e244049aab136b74f884e29034cbebec3b42092b)), closes [iris-connect/iris-backlog#192](https://github.com/iris-connect/iris-backlog/issues/192)
* Removes license reference for code generated by Swagger that no ([85c07a5](https://github.com/iris-connect/iris-client/commit/85c07a5a899a8c3dc69293c0c942fd28ad624cad))
* Restricts the content of …deployment….zip to the folders and files ([d265238](https://github.com/iris-connect/iris-client/commit/d2652387828dc2f9ff238082e19d6352b4952623)), closes [#432](https://github.com/iris-connect/iris-client/issues/432) [#433](https://github.com/iris-connect/iris-client/issues/433)
* Use event request guest's mobile number as phone number if mobile number is valid and phone number is not. ([99fd390](https://github.com/iris-connect/iris-client/commit/99fd3903357936cf9890beecbe345a67f45e3010)), closes [iris-connect/iris-backlog#249](https://github.com/iris-connect/iris-backlog/issues/249) [#419](https://github.com/iris-connect/iris-client/issues/419)


### Features

* Add search hint to location search dialog explaining on how to use the new search algorithm. ([3022d28](https://github.com/iris-connect/iris-client/commit/3022d28cd2ea3e6304c97bd3733c66e04520aa82)), closes [iris-connect/iris-backlog#110](https://github.com/iris-connect/iris-backlog/issues/110) [#370](https://github.com/iris-connect/iris-client/issues/370)
* Adds automated end to end tests to check many frontend core functionalities. ([3dd737c](https://github.com/iris-connect/iris-client/commit/3dd737c55f5fe4823e64a3791dcbe4692fbf053a)), closes [#420](https://github.com/iris-connect/iris-client/issues/420)
* Change atomic address from columns to comma separated text in standard csv export. ([a9a9fb5](https://github.com/iris-connect/iris-client/commit/a9a9fb51766414b98caa97aef27c45b3a91aac93)), closes [iris-connect/iris-backlog#246](https://github.com/iris-connect/iris-backlog/issues/246) [#403](https://github.com/iris-connect/iris-client/issues/403)
* Enables MySQL, MariaDB and MSSQL to be used alongside PostgreSQL as DBMS. ([416172f](https://github.com/iris-connect/iris-client/commit/416172fa970e5bda7877de77c005b714b2ff7d04)), closes [#214](https://github.com/iris-connect/iris-client/issues/214) [#386](https://github.com/iris-connect/iris-client/issues/386)
* Extends the search of events and cases to a fuzzy and wildcard search over the visible text columns of the event and the case overview. ([7457f11](https://github.com/iris-connect/iris-client/commit/7457f11089866d408587a3b1e90fd0ab32b82e0a)), closes [iris-connect/iris-backlog#110](https://github.com/iris-connect/iris-backlog/issues/110) [#385](https://github.com/iris-connect/iris-client/issues/385)
* Old cases and events are deleted after a configurable time (default is after 6 months) with all associated data. ([3da22e4](https://github.com/iris-connect/iris-client/commit/3da22e419a14b346bc9a54a6f6968246e18a3742)), closes [iris-connect/iris-backlog#244](https://github.com/iris-connect/iris-backlog/issues/244) [#384](https://github.com/iris-connect/iris-client/issues/384)


### Reverts

* Revert "fix: Front end no longer runs as root in the container." (#328) ([98a4b30](https://github.com/iris-connect/iris-client/commit/98a4b30af3c63e4f01aecdfc1e6b388f878d7db9)), closes [#328](https://github.com/iris-connect/iris-client/issues/328)

# [1.1.0-rc.3](https://github.com/iris-connect/iris-client/compare/v1.1.0-rc.2...v1.1.0-rc.3) (2021-11-03)


### Bug Fixes

* An admin can now change a user's data again. An error occurred ([402baba](https://github.com/iris-connect/iris-client/commit/402babad6e1232bfff0eae8ed6fa5d2f5f2964e7))
* Cleans the Docker image of the frontend from artifacts that are only needed for the build. This makes it more secure and greatly reduced in size. ([d62ca2a](https://github.com/iris-connect/iris-client/commit/d62ca2a15a59d680e9a913e2da71a6d7462917d0)), closes [#442](https://github.com/iris-connect/iris-client/issues/442)
* Restricts the content of …deployment….zip to the folders and files ([d265238](https://github.com/iris-connect/iris-client/commit/d2652387828dc2f9ff238082e19d6352b4952623)), closes [#432](https://github.com/iris-connect/iris-client/issues/432) [#433](https://github.com/iris-connect/iris-client/issues/433)

# [1.1.0-rc.2](https://github.com/iris-connect/iris-client/compare/v1.1.0-rc.1...v1.1.0-rc.2) (2021-10-30)


### Bug Fixes

* **build:** The image tags in the composite files are now correct with ([9146a1f](https://github.com/iris-connect/iris-client/commit/9146a1f11602506bcf2a16c0c94be84afa58ff89))

# [1.1.0-rc.1](https://github.com/iris-connect/iris-client/compare/v1.0.1...v1.1.0-rc.1) (2021-10-26)


### Bug Fixes

* After an update, the frontend is automatically reloaded if it would ([cb98b9c](https://github.com/iris-connect/iris-client/commit/cb98b9cc4795dd9f792d70d4ec95d11ede6d4e6e)), closes [iris-connect/iris-backlog#229](https://github.com/iris-connect/iris-backlog/issues/229) [#358](https://github.com/iris-connect/iris-client/issues/358)
* Fixed an occasional blank home screen when contact address information is missing. ([8a7fe34](https://github.com/iris-connect/iris-client/commit/8a7fe347e27d486db574145a1a605567246a8630)), closes [#350](https://github.com/iris-connect/iris-client/issues/350)
* Front end no longer runs as root in the container. ([6da6662](https://github.com/iris-connect/iris-client/commit/6da6662c205ffef5248239abf710b5eded4d32d0)), closes [iris-connect/iris-backlog#224](https://github.com/iris-connect/iris-backlog/issues/224) [#329](https://github.com/iris-connect/iris-client/issues/329)
* Front end no longer runs as root in the container. ([e8abc17](https://github.com/iris-connect/iris-client/commit/e8abc170228d17b24742f315b7e4f3a8105d1870)), closes [iris-connect/iris-backlog#224](https://github.com/iris-connect/iris-backlog/issues/224) [#326](https://github.com/iris-connect/iris-client/issues/326)
* If an error occurs in the app, a data request is now no longer ([9e0c176](https://github.com/iris-connect/iris-client/commit/9e0c1767e59bcfab40600f89693984cd3cf5b346)), closes [iris-connect/iris-backlog#124](https://github.com/iris-connect/iris-backlog/issues/124)
* International phone numbers will be accepted now. ([6b08b0a](https://github.com/iris-connect/iris-client/commit/6b08b0ac5a85e367d9b26e59eab0441d15b98a4e)), closes [iris-connect/iris-backlog#230](https://github.com/iris-connect/iris-backlog/issues/230)
* Meaningful error message if username is already used. ([e244049](https://github.com/iris-connect/iris-client/commit/e244049aab136b74f884e29034cbebec3b42092b)), closes [iris-connect/iris-backlog#192](https://github.com/iris-connect/iris-backlog/issues/192)
* Removes license reference for code generated by Swagger that no ([85c07a5](https://github.com/iris-connect/iris-client/commit/85c07a5a899a8c3dc69293c0c942fd28ad624cad))
* Use event request guest's mobile number as phone number if mobile number is valid and phone number is not. ([99fd390](https://github.com/iris-connect/iris-client/commit/99fd3903357936cf9890beecbe345a67f45e3010)), closes [iris-connect/iris-backlog#249](https://github.com/iris-connect/iris-backlog/issues/249) [#419](https://github.com/iris-connect/iris-client/issues/419)


### Features

* Add search hint to location search dialog explaining on how to use the new search algorithm. ([3022d28](https://github.com/iris-connect/iris-client/commit/3022d28cd2ea3e6304c97bd3733c66e04520aa82)), closes [iris-connect/iris-backlog#110](https://github.com/iris-connect/iris-backlog/issues/110) [#370](https://github.com/iris-connect/iris-client/issues/370)
* Adds automated end to end tests to check many frontend core functionalities. ([3dd737c](https://github.com/iris-connect/iris-client/commit/3dd737c55f5fe4823e64a3791dcbe4692fbf053a)), closes [#420](https://github.com/iris-connect/iris-client/issues/420)
* Change atomic address from columns to comma separated text in standard csv export. ([a9a9fb5](https://github.com/iris-connect/iris-client/commit/a9a9fb51766414b98caa97aef27c45b3a91aac93)), closes [iris-connect/iris-backlog#246](https://github.com/iris-connect/iris-backlog/issues/246) [#403](https://github.com/iris-connect/iris-client/issues/403)
* Enables MySQL, MariaDB and MSSQL to be used alongside PostgreSQL as DBMS. ([416172f](https://github.com/iris-connect/iris-client/commit/416172fa970e5bda7877de77c005b714b2ff7d04)), closes [#214](https://github.com/iris-connect/iris-client/issues/214) [#386](https://github.com/iris-connect/iris-client/issues/386)
* Extends the search of events and cases to a fuzzy and wildcard search over the visible text columns of the event and the case overview. ([7457f11](https://github.com/iris-connect/iris-client/commit/7457f11089866d408587a3b1e90fd0ab32b82e0a)), closes [iris-connect/iris-backlog#110](https://github.com/iris-connect/iris-backlog/issues/110) [#385](https://github.com/iris-connect/iris-client/issues/385)
* Old cases and events are deleted after a configurable time (default is after 6 months) with all associated data. ([3da22e4](https://github.com/iris-connect/iris-client/commit/3da22e419a14b346bc9a54a6f6968246e18a3742)), closes [iris-connect/iris-backlog#244](https://github.com/iris-connect/iris-backlog/issues/244) [#384](https://github.com/iris-connect/iris-client/issues/384)


### Reverts

* Revert "fix: Front end no longer runs as root in the container." (#328) ([98a4b30](https://github.com/iris-connect/iris-client/commit/98a4b30af3c63e4f01aecdfc1e6b388f878d7db9)), closes [#328](https://github.com/iris-connect/iris-client/issues/328)

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
