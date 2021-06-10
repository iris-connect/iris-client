# PointOfEntryControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll7**](PointOfEntryControllerApi.md#getAll7) | **GET** /pointsofentry/all/{since} | 
[**getAllUuids16**](PointOfEntryControllerApi.md#getAllUuids16) | **GET** /pointsofentry/uuids | 
[**getByUuids22**](PointOfEntryControllerApi.md#getByUuids22) | **POST** /pointsofentry/query | 

<a name="getAll7"></a>
# **getAll7**
> List&lt;PointOfEntryDto&gt; getAll7(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
Long since = 789L; // Long | 
try {
    List<PointOfEntryDto> result = apiInstance.getAll7(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAll7");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids16"></a>
# **getAllUuids16**
> List&lt;String&gt; getAllUuids16()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
try {
    List<String> result = apiInstance.getAllUuids16();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getAllUuids16");
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

<a name="getByUuids22"></a>
# **getByUuids22**
> List&lt;PointOfEntryDto&gt; getByUuids22(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PointOfEntryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PointOfEntryControllerApi apiInstance = new PointOfEntryControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PointOfEntryDto> result = apiInstance.getByUuids22(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointOfEntryControllerApi#getByUuids22");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PointOfEntryDto&gt;**](PointOfEntryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

