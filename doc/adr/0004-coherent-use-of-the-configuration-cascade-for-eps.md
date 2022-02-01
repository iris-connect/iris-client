# 4. Coherent use of the configuration cascade for EPS applications

Date: 2022-01-10

## Status

Accepted

## Context

In our architecture with EPS, Docker and Docker-Compose, configurations can be made with environment variables in different places. It is important to take a coherent approach here to avoid confusion and errors.

## Decision

According to the following scheme the configurations should be made with values:

1. For basic differences between the deployed installations respectively EPS networks (prod (live), test (staging), ...) we use setting files that bundle the configurations for such a network. These files are located under a folder for the stage (prod/live or test/staging). The folder to be used during execution is defined by the environment variable `IRIS_ENV`.  
Ex: The name of the service directory and root ca certificate files.  
Folder Ex: `roles/live/hd` vs. `roles/staging/hd`

2. In the Docker Compose file, values should be set for environment variables that are uniform for all deployments with this file.  
Ex: Hosts or ports that refer to the other containers in the compose file.

3. In the `.env` (or a script) finally the values should be set, which are individual for the respective installation.  
Ex: ULRs and file names …

## Consequences

If we use this cascade correctly, then it is clear which settings/values belong where. Conversely, it is also clear in which context settings have an effect.
