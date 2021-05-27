# Changelog

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
