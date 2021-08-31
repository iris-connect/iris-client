# DiseaseConfigurationControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllDiseaseConfigurations**](DiseaseConfigurationControllerApi.md#getAllDiseaseConfigurations) | **GET** /diseaseconfigurations/all/{since} | 
[**getAllUuids11**](DiseaseConfigurationControllerApi.md#getAllUuids11) | **GET** /diseaseconfigurations/uuids | 
[**getByUuids14**](DiseaseConfigurationControllerApi.md#getByUuids14) | **POST** /diseaseconfigurations/query | 

<a name="getAllDiseaseConfigurations"></a>
# **getAllDiseaseConfigurations**
> List&lt;DiseaseConfigurationDto&gt; getAllDiseaseConfigurations(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<DiseaseConfigurationDto> result = apiInstance.getAllDiseaseConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllDiseaseConfigurations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids11"></a>
# **getAllUuids11**
> List&lt;String&gt; getAllUuids11()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids11();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getAllUuids11");
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

<a name="getByUuids14"></a>
# **getByUuids14**
> List&lt;DiseaseConfigurationDto&gt; getByUuids14(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.DiseaseConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


DiseaseConfigurationControllerApi apiInstance = new DiseaseConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<DiseaseConfigurationDto> result = apiInstance.getByUuids14(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DiseaseConfigurationControllerApi#getByUuids14");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;DiseaseConfigurationDto&gt;**](DiseaseConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

