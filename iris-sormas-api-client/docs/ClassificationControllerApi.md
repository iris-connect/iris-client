# ClassificationControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll2**](ClassificationControllerApi.md#getAll2) | **GET** /classification/all/{since} | 

<a name="getAll2"></a>
# **getAll2**
> List&lt;DiseaseClassificationCriteriaDto&gt; getAll2(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ClassificationControllerApi apiInstance = new ClassificationControllerApi();
Long since = 789L; // Long | 
try {
    List<DiseaseClassificationCriteriaDto> result = apiInstance.getAll2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#getAll2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;DiseaseClassificationCriteriaDto&gt;**](DiseaseClassificationCriteriaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

