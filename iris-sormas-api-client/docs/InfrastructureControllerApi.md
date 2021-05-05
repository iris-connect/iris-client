# InfrastructureControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getInfrastructureSyncData**](InfrastructureControllerApi.md#getInfrastructureSyncData) | **POST** /infrastructure/sync | 

<a name="getInfrastructureSyncData"></a>
# **getInfrastructureSyncData**
> InfrastructureSyncDto getInfrastructureSyncData(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.InfrastructureControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

InfrastructureControllerApi apiInstance = new InfrastructureControllerApi();
InfrastructureChangeDatesDto body = new InfrastructureChangeDatesDto(); // InfrastructureChangeDatesDto | 
try {
    InfrastructureSyncDto result = apiInstance.getInfrastructureSyncData(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling InfrastructureControllerApi#getInfrastructureSyncData");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InfrastructureChangeDatesDto**](InfrastructureChangeDatesDto.md)|  | [optional]

### Return type

[**InfrastructureSyncDto**](InfrastructureSyncDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: application/json; charset=UTF-8

