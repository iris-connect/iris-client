# IRIS Client SORMAS Sidecar

"Backend for frontend" service for [IRIS client frontend](https://github.com/iris-gateway/iris-client-frontend).

## Authentication

At the moment you can configure DB authentication. 

### DB Authentication

In this setting the local database is used to store authentication information. You need to set the following property in Spring configuration. 

```
security.auth=db # Activates the DB auth
security.auth.db.admin-user-name=admin # Creates an initial admin user if it does not exist.
security.auth.db.admin-user-password=admin # Set the initial admin user password.
security.jwt.jwt-shared-secret=thissecretshouldbeusedfortestingpurposesonly
```

Using this setting, the authentication is activated for all calls towards the client need to be authorized. 

