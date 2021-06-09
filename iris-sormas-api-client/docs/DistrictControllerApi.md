# DistrictControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll6**](DistrictControllerApi.md#getAll6) | **GET** /districts/all/{since} | 
[**getAllUuids12**](DistrictControllerApi.md#getAllUuids12) | **GET** /districts/uuids | 
[**getByUuids15**](DistrictControllerApi.md#getByUuids15) | **POST** /districts/query | 

<a name="getAll6"></a>
# **getAll6**
> List&lt;DistrictDto&gt; getAll6(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DistrictControllerApi apiInstance = new DistrictControllerApi();
Long since = 789L; // Long | 
try {
    List<DistrictDto> result = apiInstance.getAll6(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAll6");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids12"></a>
# **getAllUuids12**
> List&lt;String&gt; getAllUuids12()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DistrictControllerApi apiInstance = new DistrictControllerApi();
try {
    List<String> result = apiInstance.getAllUuids12();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getAllUuids12");
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

<a name="getByUuids15"></a>
# **getByUuids15**
> List&lt;DistrictDto&gt; getByUuids15(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DistrictControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DistrictControllerApi apiInstance = new DistrictControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<DistrictDto> result = apiInstance.getByUuids15(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DistrictControllerApi#getByUuids15");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;DistrictDto&gt;**](DistrictDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

