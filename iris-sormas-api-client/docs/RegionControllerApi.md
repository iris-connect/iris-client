# RegionControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll8**](RegionControllerApi.md#getAll8) | **GET** /regions/all/{since} | 
[**getAllUuids17**](RegionControllerApi.md#getAllUuids17) | **GET** /regions/uuids | 
[**getByUuids24**](RegionControllerApi.md#getByUuids24) | **POST** /regions/query | 

<a name="getAll8"></a>
# **getAll8**
> List&lt;RegionDto&gt; getAll8(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


RegionControllerApi apiInstance = new RegionControllerApi();
Long since = 789L; // Long | 
try {
    List<RegionDto> result = apiInstance.getAll8(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAll8");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids17"></a>
# **getAllUuids17**
> List&lt;String&gt; getAllUuids17()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


RegionControllerApi apiInstance = new RegionControllerApi();
try {
    List<String> result = apiInstance.getAllUuids17();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getAllUuids17");
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

<a name="getByUuids24"></a>
# **getByUuids24**
> List&lt;RegionDto&gt; getByUuids24(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.RegionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


RegionControllerApi apiInstance = new RegionControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<RegionDto> result = apiInstance.getByUuids24(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegionControllerApi#getByUuids24");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;RegionDto&gt;**](RegionDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

