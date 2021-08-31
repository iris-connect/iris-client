# CampaignFormMetaControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCampaignFormMeta**](CampaignFormMetaControllerApi.md#getAllCampaignFormMeta) | **GET** /campaignFormMeta/all/{since} | 
[**getAllUuids4**](CampaignFormMetaControllerApi.md#getAllUuids4) | **GET** /campaignFormMeta/uuids | 
[**getByUuids5**](CampaignFormMetaControllerApi.md#getByUuids5) | **POST** /campaignFormMeta/query | 

<a name="getAllCampaignFormMeta"></a>
# **getAllCampaignFormMeta**
> List&lt;CampaignFormMetaDto&gt; getAllCampaignFormMeta(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
Long since = 789L; // Long | 
try {
    List<CampaignFormMetaDto> result = apiInstance.getAllCampaignFormMeta(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getAllCampaignFormMeta");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CampaignFormMetaDto&gt;**](CampaignFormMetaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids4"></a>
# **getAllUuids4**
> List&lt;String&gt; getAllUuids4()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
try {
    List<String> result = apiInstance.getAllUuids4();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getAllUuids4");
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

<a name="getByUuids5"></a>
# **getByUuids5**
> List&lt;CampaignFormMetaDto&gt; getByUuids5(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CampaignFormMetaControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CampaignFormMetaControllerApi apiInstance = new CampaignFormMetaControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CampaignFormMetaDto> result = apiInstance.getByUuids5(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CampaignFormMetaControllerApi#getByUuids5");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CampaignFormMetaDto&gt;**](CampaignFormMetaDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

