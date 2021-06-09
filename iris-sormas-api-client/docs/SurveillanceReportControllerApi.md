# SurveillanceReportControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getByCaseUuids1**](SurveillanceReportControllerApi.md#getByCaseUuids1) | **POST** /surveillancereports/query/cases | 
[**postCaseReports**](SurveillanceReportControllerApi.md#postCaseReports) | **POST** /surveillancereports/push | 

<a name="getByCaseUuids1"></a>
# **getByCaseUuids1**
> List&lt;SurveillanceReportDto&gt; getByCaseUuids1(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SurveillanceReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SurveillanceReportControllerApi apiInstance = new SurveillanceReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<SurveillanceReportDto> result = apiInstance.getByCaseUuids1(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SurveillanceReportControllerApi#getByCaseUuids1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;SurveillanceReportDto&gt;**](SurveillanceReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postCaseReports"></a>
# **postCaseReports**
> List&lt;PushResult&gt; postCaseReports(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SurveillanceReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SurveillanceReportControllerApi apiInstance = new SurveillanceReportControllerApi();
List<SurveillanceReportDto> body = Arrays.asList(new SurveillanceReportDto()); // List<SurveillanceReportDto> | 
try {
    List<PushResult> result = apiInstance.postCaseReports(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SurveillanceReportControllerApi#postCaseReports");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;SurveillanceReportDto&gt;**](SurveillanceReportDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

