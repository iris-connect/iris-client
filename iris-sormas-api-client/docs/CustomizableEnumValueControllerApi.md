# CustomizableEnumValueControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCustomizableEnumValues**](CustomizableEnumValueControllerApi.md#getAllCustomizableEnumValues) | **GET** /customizableenumvalues/all/{since} | 
[**getAllUuids10**](CustomizableEnumValueControllerApi.md#getAllUuids10) | **GET** /customizableenumvalues/uuids | 
[**getByUuids13**](CustomizableEnumValueControllerApi.md#getByUuids13) | **POST** /customizableenumvalues/query | 

<a name="getAllCustomizableEnumValues"></a>
# **getAllCustomizableEnumValues**
> List&lt;CustomizableEnumValueDto&gt; getAllCustomizableEnumValues(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CustomizableEnumValueControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CustomizableEnumValueControllerApi apiInstance = new CustomizableEnumValueControllerApi();
Long since = 789L; // Long | 
try {
    List<CustomizableEnumValueDto> result = apiInstance.getAllCustomizableEnumValues(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CustomizableEnumValueControllerApi#getAllCustomizableEnumValues");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CustomizableEnumValueDto&gt;**](CustomizableEnumValueDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids10"></a>
# **getAllUuids10**
> List&lt;String&gt; getAllUuids10()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CustomizableEnumValueControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CustomizableEnumValueControllerApi apiInstance = new CustomizableEnumValueControllerApi();
try {
    List<String> result = apiInstance.getAllUuids10();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CustomizableEnumValueControllerApi#getAllUuids10");
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

<a name="getByUuids13"></a>
# **getByUuids13**
> List&lt;CustomizableEnumValueDto&gt; getByUuids13(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CustomizableEnumValueControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CustomizableEnumValueControllerApi apiInstance = new CustomizableEnumValueControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CustomizableEnumValueDto> result = apiInstance.getByUuids13(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CustomizableEnumValueControllerApi#getByUuids13");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CustomizableEnumValueDto&gt;**](CustomizableEnumValueDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

