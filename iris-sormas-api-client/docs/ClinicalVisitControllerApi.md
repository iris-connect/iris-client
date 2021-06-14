# ClinicalVisitControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids1**](ClinicalVisitControllerApi.md#getAllActiveUuids1) | **GET** /clinicalvisits/uuids | 
[**getAllVisits**](ClinicalVisitControllerApi.md#getAllVisits) | **GET** /clinicalvisits/all/{since} | 
[**getByUuids8**](ClinicalVisitControllerApi.md#getByUuids8) | **POST** /clinicalvisits/query | 
[**postVisits**](ClinicalVisitControllerApi.md#postVisits) | **POST** /clinicalvisits/push | 

<a name="getAllActiveUuids1"></a>
# **getAllActiveUuids1**
> List&lt;String&gt; getAllActiveUuids1()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ClinicalVisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ClinicalVisitControllerApi apiInstance = new ClinicalVisitControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClinicalVisitControllerApi#getAllActiveUuids1");
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

<a name="getAllVisits"></a>
# **getAllVisits**
> List&lt;ClinicalVisitDto&gt; getAllVisits(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ClinicalVisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ClinicalVisitControllerApi apiInstance = new ClinicalVisitControllerApi();
Long since = 789L; // Long | 
try {
    List<ClinicalVisitDto> result = apiInstance.getAllVisits(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClinicalVisitControllerApi#getAllVisits");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;ClinicalVisitDto&gt;**](ClinicalVisitDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids8"></a>
# **getByUuids8**
> List&lt;ClinicalVisitDto&gt; getByUuids8(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ClinicalVisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ClinicalVisitControllerApi apiInstance = new ClinicalVisitControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<ClinicalVisitDto> result = apiInstance.getByUuids8(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClinicalVisitControllerApi#getByUuids8");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;ClinicalVisitDto&gt;**](ClinicalVisitDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postVisits"></a>
# **postVisits**
> List&lt;PushResult&gt; postVisits(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ClinicalVisitControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ClinicalVisitControllerApi apiInstance = new ClinicalVisitControllerApi();
List<ClinicalVisitDto> body = Arrays.asList(new ClinicalVisitDto()); // List<ClinicalVisitDto> | 
try {
    List<PushResult> result = apiInstance.postVisits(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClinicalVisitControllerApi#postVisits");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ClinicalVisitDto&gt;**](ClinicalVisitDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

