# CampaignControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCampaignFormData1**](CampaignControllerApi.md#getAllCampaignFormData1) | **GET** /campaigns/all/{since} | 
[**getAllUuids5**](CampaignControllerApi.md#getAllUuids5) | **GET** /campaigns/uuids | 
[**getByUuids6**](CampaignControllerApi.md#getByUuids6) | **POST** /campaigns/query | 

<a name="getAllCampaignFormData1"></a>
# **getAllCampaignFormData1**
> List&lt;CampaignDto&gt; getAllCampaignFormData1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignControllerApi apiInstance = new CampaignControllerApi();
Long since = 789L; // Long | 
try {
    List<CampaignDto> result = apiInstance.getAllCampaignFormData1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getAllCampaignFormData1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CampaignDto&gt;**](CampaignDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids5"></a>
# **getAllUuids5**
> List&lt;String&gt; getAllUuids5()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignControllerApi apiInstance = new CampaignControllerApi();
try {
    List<String> result = apiInstance.getAllUuids5();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getAllUuids5");
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

<a name="getByUuids6"></a>
# **getByUuids6**
> List&lt;CampaignDto&gt; getByUuids6(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignControllerApi apiInstance = new CampaignControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CampaignDto> result = apiInstance.getByUuids6(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignControllerApi#getByUuids6");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CampaignDto&gt;**](CampaignDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

