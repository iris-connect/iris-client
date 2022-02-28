---
name: Release
about: Issue for the preparation of a new release.
title: Release
labels: ''
assignees: ''

---

Pre-publication checklist:
- [ ] Are the EPS images from the client up to date with EPS?
- [ ] Is the EPS version in the gateway up to date? (`epsRepoTag` in `/iris-gateway/infrastructure/iris-gateway/values.yaml`)
- [ ] Is a current compatible version installed on the gateway?
- [ ] Has the release been sufficiently tested on the test environment?
- [ ] Have all necessary configurations been made in the SD?
- [ ] Have new methods for communication been enabled in the SD?

Work after the construction of the release:
- [ ] Update guide for new release added and adjusted.
- [ ] Changelog checked and adjusted if necessary.
- [ ] Added link to update guide in changelog.
- [ ] AKDB and others informed about release in Slack.
