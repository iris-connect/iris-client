# VisitControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids10**](VisitControllerApi.md#getAllActiveUuids10) | **GET** /visits/uuids | 
[**getAllVisits1**](VisitControllerApi.md#getAllVisits1) | **GET** /visits/all/{since} | 
[**getByUuids30**](VisitControllerApi.md#getByUuids30) | **POST** /visits/query | 
[**postVisits1**](VisitControllerApi.md#postVisits1) | **POST** /visits/push | 

<a name="getAllActiveUuids10"></a>
# **getAllActiveUuids10**
> List&lt;String&gt; getAllActiveUuids10()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.VisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


VisitControllerApi apiInstance = new VisitControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids10();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VisitControllerApi#getAllActiveUuids10");
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

<a name="getAllVisits1"></a>
# **getAllVisits1**
> List&lt;VisitDto&gt; getAllVisits1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.VisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


VisitControllerApi apiInstance = new VisitControllerApi();
Long since = 789L; // Long | 
try {
    List<VisitDto> result = apiInstance.getAllVisits1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VisitControllerApi#getAllVisits1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;VisitDto&gt;**](VisitDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids30"></a>
# **getByUuids30**
> List&lt;VisitDto&gt; getByUuids30(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.VisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


VisitControllerApi apiInstance = new VisitControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<VisitDto> result = apiInstance.getByUuids30(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VisitControllerApi#getByUuids30");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;VisitDto&gt;**](VisitDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postVisits1"></a>
# **postVisits1**
> List&lt;PushResult&gt; postVisits1(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.VisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


VisitControllerApi apiInstance = new VisitControllerApi();
List<VisitDto> body = Arrays.asList(new VisitDto()); // List<VisitDto> | 
try {
    List<PushResult> result = apiInstance.postVisits1(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VisitControllerApi#postVisits1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;VisitDto&gt;**](VisitDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

