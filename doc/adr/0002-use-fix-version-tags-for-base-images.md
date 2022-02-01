# 2. Use fix version tags for base images

Date: 2022-01-07

## Status

Accepted

## Context

In Docker files and other places where base images are specified, they can often be very general without tags to very precise tags.

Ex: `1.21.5, mainline, 1, 1.21, latest`

A sensible approach is needed here.

## Decision

If it is not just a pure operating system as a base image (i.e. Alpine), but language frameworks (e.g. JDK), other frameworks (e.g. Spring, Node) or applications (e.g. PostgreSQL, NGINX), then we tag them with concrete version containing tags.

Ex: `nginx:1.21.5-alpine` instead of `nginx:alpine`

## Consequences

Without fixed versions in tags, it is difficult to determine the current version of an application/framework in the base image. A commit in Git also does not lead back to the same artifact later. This prevents clear dependency management.

With fixed versions in tags, updates become explicit and thus traceable (there is a commit) and also more verifiable with a PR.

With a suitable commit message, the update can and should also be made visible to users in the release notes, increasing transparency in the project.

With fixed tags, a different way must be used to ensure that updates are made. For this, we have integrated various scanners (Trivy, dependabot, CodeQL) into the build and in addition to it in the project, which report severe errors and available updates. Through these, necessary updates are initiated. 
