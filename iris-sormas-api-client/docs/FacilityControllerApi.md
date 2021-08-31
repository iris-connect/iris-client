# FacilityControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllByRegion**](FacilityControllerApi.md#getAllByRegion) | **GET** /facilities/region/{regionUuid}/{since} | 
[**getAllUuids13**](FacilityControllerApi.md#getAllUuids13) | **GET** /facilities/uuids | 
[**getAllWithoutRegion**](FacilityControllerApi.md#getAllWithoutRegion) | **GET** /facilities/general/{since} | 
[**getByUuids18**](FacilityControllerApi.md#getByUuids18) | **POST** /facilities/query | 

<a name="getAllByRegion"></a>
# **getAllByRegion**
> List&lt;FacilityDto&gt; getAllByRegion(regionUuid, since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FacilityControllerApi apiInstance = new FacilityControllerApi();
String regionUuid = "regionUuid_example"; // String | 
Long since = 789L; // Long | 
try {
    List<FacilityDto> result = apiInstance.getAllByRegion(regionUuid, since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllByRegion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **regionUuid** | **String**|  |
 **since** | **Long**|  |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids13"></a>
# **getAllUuids13**
> List&lt;String&gt; getAllUuids13()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FacilityControllerApi apiInstance = new FacilityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids13();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllUuids13");
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

<a name="getAllWithoutRegion"></a>
# **getAllWithoutRegion**
> List&lt;FacilityDto&gt; getAllWithoutRegion(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FacilityControllerApi apiInstance = new FacilityControllerApi();
Long since = 789L; // Long | 
try {
    List<FacilityDto> result = apiInstance.getAllWithoutRegion(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllWithoutRegion");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids18"></a>
# **getByUuids18**
> List&lt;FacilityDto&gt; getByUuids18(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FacilityControllerApi apiInstance = new FacilityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<FacilityDto> result = apiInstance.getByUuids18(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getByUuids18");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

