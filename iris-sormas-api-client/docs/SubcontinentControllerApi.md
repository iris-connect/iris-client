# SubcontinentControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll9**](SubcontinentControllerApi.md#getAll9) | **GET** /subcontinents/all/{since} | 
[**getAllUuids18**](SubcontinentControllerApi.md#getAllUuids18) | **GET** /subcontinents/uuids | 
[**getByUuids26**](SubcontinentControllerApi.md#getByUuids26) | **POST** /subcontinents/query | 

<a name="getAll9"></a>
# **getAll9**
> List&lt;SubcontinentDto&gt; getAll9(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SubcontinentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SubcontinentControllerApi apiInstance = new SubcontinentControllerApi();
Long since = 789L; // Long | 
try {
    List<SubcontinentDto> result = apiInstance.getAll9(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SubcontinentControllerApi#getAll9");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;SubcontinentDto&gt;**](SubcontinentDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids18"></a>
# **getAllUuids18**
> List&lt;String&gt; getAllUuids18()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SubcontinentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SubcontinentControllerApi apiInstance = new SubcontinentControllerApi();
try {
    List<String> result = apiInstance.getAllUuids18();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SubcontinentControllerApi#getAllUuids18");
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

<a name="getByUuids26"></a>
# **getByUuids26**
> List&lt;SubcontinentDto&gt; getByUuids26(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SubcontinentControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SubcontinentControllerApi apiInstance = new SubcontinentControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<SubcontinentDto> result = apiInstance.getByUuids26(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SubcontinentControllerApi#getByUuids26");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;SubcontinentDto&gt;**](SubcontinentDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

