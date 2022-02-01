# 3. Coherent use of the configuration cascade for Spring applications (properties and environment variables)

Date: 2022-01-10

## Status

Accepted

## Context

In our architecture with Spring, Docker and Docker-Compose, configurations can be made with properties and environment variables in different places. It is important to take a coherent approach here to avoid confusion and errors.

## Decision

According to the following scheme the configurations should be made with values:

1. In the `application.properties` only values for properties may be set that are sensible and usable standards for all environments and deployment scenarios. An adjustment may not be a duty, but serve the individual fine adjustment.  
Ex: A scheduler for a delete job.

2. If properties cannot be set in a general and meaningful way in `application.properties`, then they should be implemented in the code as mandatory properties, so that it is quickly obvious at startup if they are missing.  
Ex: With Bean Validation in properties classes. 

3. For deployments we generally use the Spring profile `prod` with its subprofiles `prod...`. Configurations for the deployment environments are to be made here.  
Ex: PostgreSQL instead of a H2 DB

4. For basic differences between the deployed installations respectively EPS networks (prod (live), test (staging), ...) we use additional profiles starting with `stage`, like `stage_live` and `stage_staging`.  
Ex: The name of the backend service and the public proxy within EPS network. 

5. In the Docker Compose file, values should be set for environment variables that are uniform for all deployments with this file.  
Ex: URLs, hosts or ports that refer to the other containers in the compose file.

6. in the `.env` (or a script) finally the values should be set, which are individual for the respective installation.  
Ex: Passwords and ULRs accessible from the outside …

Addendum:

- Default values for development and automatic integration tests should be stored in `dev...` profiles.
- The `application.propierties` is a good overview of the default values of properties and easy to use. It should be preferred over implementing default values in code.

## Consequences

Points 1 and 2 ensure that necessary configurations are not overlooked during deployment and that incorrect default values are not used by mistake. Missing configurations are also quickly noticed when the application is started.

If we use this cascade correctly, then it is clear which settings/values belong where. Conversely, it is also clear in which context settings have an effect.
