# AreaControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll1**](AreaControllerApi.md#getAll1) | **GET** /areas/all/{since} | 
[**getAllUuids2**](AreaControllerApi.md#getAllUuids2) | **GET** /areas/uuids | 
[**getByUuids3**](AreaControllerApi.md#getByUuids3) | **POST** /areas/query | 

<a name="getAll1"></a>
# **getAll1**
> List&lt;AreaDto&gt; getAll1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.AreaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


AreaControllerApi apiInstance = new AreaControllerApi();
Long since = 789L; // Long | 
try {
    List<AreaDto> result = apiInstance.getAll1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AreaControllerApi#getAll1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;AreaDto&gt;**](AreaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids2"></a>
# **getAllUuids2**
> List&lt;String&gt; getAllUuids2()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.AreaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


AreaControllerApi apiInstance = new AreaControllerApi();
try {
    List<String> result = apiInstance.getAllUuids2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AreaControllerApi#getAllUuids2");
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

<a name="getByUuids3"></a>
# **getByUuids3**
> List&lt;AreaDto&gt; getByUuids3(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.AreaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


AreaControllerApi apiInstance = new AreaControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<AreaDto> result = apiInstance.getByUuids3(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AreaControllerApi#getByUuids3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;AreaDto&gt;**](AreaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

