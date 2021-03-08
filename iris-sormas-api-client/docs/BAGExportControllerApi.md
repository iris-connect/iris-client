# BAGExportControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**exportCases**](BAGExportControllerApi.md#exportCases) | **GET** /bagexport/cases | 
[**exportContacts**](BAGExportControllerApi.md#exportContacts) | **GET** /bagexport/contacts | 

<a name="exportCases"></a>
# **exportCases**
> exportCases(body)



### Example
```java
// Import classes:
//import de.healthIMIS.sormas.client.invoker.ApiClient;
//import de.healthIMIS.sormas.client.invoker.ApiException;
//import de.healthIMIS.sormas.client.invoker.Configuration;
//import de.healthIMIS.sormas.client.invoker.auth.*;
//import de.healthIMIS.sormas.client.api.BAGExportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

BAGExportControllerApi apiInstance = new BAGExportControllerApi();
String body = "body_example"; // String | 
try {
    apiInstance.exportCases(body);
} catch (ApiException e) {
    System.err.println("Exception when calling BAGExportControllerApi#exportCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/octet-stream

<a name="exportContacts"></a>
# **exportContacts**
> exportContacts(body)



### Example
```java
// Import classes:
//import de.healthIMIS.sormas.client.invoker.ApiClient;
//import de.healthIMIS.sormas.client.invoker.ApiException;
//import de.healthIMIS.sormas.client.invoker.Configuration;
//import de.healthIMIS.sormas.client.invoker.auth.*;
//import de.healthIMIS.sormas.client.api.BAGExportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

BAGExportControllerApi apiInstance = new BAGExportControllerApi();
String body = "body_example"; // String | 
try {
    apiInstance.exportContacts(body);
} catch (ApiException e) {
    System.err.println("Exception when calling BAGExportControllerApi#exportContacts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/octet-stream

