# CommunityControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll3**](CommunityControllerApi.md#getAll3) | **GET** /communities/all/{since} | 
[**getAllUuids7**](CommunityControllerApi.md#getAllUuids7) | **GET** /communities/uuids | 
[**getByUuids9**](CommunityControllerApi.md#getByUuids9) | **POST** /communities/query | 

<a name="getAll3"></a>
# **getAll3**
> List&lt;CommunityDto&gt; getAll3(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CommunityControllerApi apiInstance = new CommunityControllerApi();
Long since = 789L; // Long | 
try {
    List<CommunityDto> result = apiInstance.getAll3(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAll3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids7"></a>
# **getAllUuids7**
> List&lt;String&gt; getAllUuids7()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CommunityControllerApi apiInstance = new CommunityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids7();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getAllUuids7");
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

<a name="getByUuids9"></a>
# **getByUuids9**
> List&lt;CommunityDto&gt; getByUuids9(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CommunityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CommunityControllerApi apiInstance = new CommunityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CommunityDto> result = apiInstance.getByUuids9(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunityControllerApi#getByUuids9");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CommunityDto&gt;**](CommunityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

