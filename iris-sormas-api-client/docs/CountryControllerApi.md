# CountryControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll5**](CountryControllerApi.md#getAll5) | **GET** /countries/all/{since} | 
[**getAllUuids9**](CountryControllerApi.md#getAllUuids9) | **GET** /countries/uuids | 
[**getByUuids12**](CountryControllerApi.md#getByUuids12) | **POST** /countries/query | 

<a name="getAll5"></a>
# **getAll5**
> List&lt;CountryDto&gt; getAll5(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CountryControllerApi apiInstance = new CountryControllerApi();
Long since = 789L; // Long | 
try {
    List<CountryDto> result = apiInstance.getAll5(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getAll5");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CountryDto&gt;**](CountryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids9"></a>
# **getAllUuids9**
> List&lt;String&gt; getAllUuids9()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CountryControllerApi apiInstance = new CountryControllerApi();
try {
    List<String> result = apiInstance.getAllUuids9();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getAllUuids9");
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

<a name="getByUuids12"></a>
# **getByUuids12**
> List&lt;CountryDto&gt; getByUuids12(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CountryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CountryControllerApi apiInstance = new CountryControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CountryDto> result = apiInstance.getByUuids12(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CountryControllerApi#getByUuids12");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CountryDto&gt;**](CountryDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

