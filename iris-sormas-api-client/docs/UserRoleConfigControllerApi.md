# UserRoleConfigControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll12**](UserRoleConfigControllerApi.md#getAll12) | **GET** /userroles/all/{since} | 
[**getAllUuids20**](UserRoleConfigControllerApi.md#getAllUuids20) | **GET** /userroles/uuids | 
[**getDeletedUuids1**](UserRoleConfigControllerApi.md#getDeletedUuids1) | **GET** /userroles/deleted/{since} | 

<a name="getAll12"></a>
# **getAll12**
> List&lt;UserRoleConfigDto&gt; getAll12(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
Long since = 789L; // Long | 
try {
    List<UserRoleConfigDto> result = apiInstance.getAll12(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAll12");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;UserRoleConfigDto&gt;**](UserRoleConfigDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids20"></a>
# **getAllUuids20**
> List&lt;String&gt; getAllUuids20()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
try {
    List<String> result = apiInstance.getAllUuids20();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getAllUuids20");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuids1"></a>
# **getDeletedUuids1**
> List&lt;String&gt; getDeletedUuids1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.UserRoleConfigControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


UserRoleConfigControllerApi apiInstance = new UserRoleConfigControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuids1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserRoleConfigControllerApi#getDeletedUuids1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

