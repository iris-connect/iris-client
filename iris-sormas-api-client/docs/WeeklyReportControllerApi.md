# WeeklyReportControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllUuids21**](WeeklyReportControllerApi.md#getAllUuids21) | **GET** /weeklyreports/uuids | 
[**getAllWeeklyReports**](WeeklyReportControllerApi.md#getAllWeeklyReports) | **GET** /weeklyreports/all/{since} | 
[**getByUuids31**](WeeklyReportControllerApi.md#getByUuids31) | **POST** /weeklyreports/query | 
[**postWeeklyReports**](WeeklyReportControllerApi.md#postWeeklyReports) | **POST** /weeklyreports/push | 

<a name="getAllUuids21"></a>
# **getAllUuids21**
> List&lt;String&gt; getAllUuids21()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
try {
    List<String> result = apiInstance.getAllUuids21();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getAllUuids21");
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

<a name="getAllWeeklyReports"></a>
# **getAllWeeklyReports**
> List&lt;WeeklyReportDto&gt; getAllWeeklyReports(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
Long since = 789L; // Long | 
try {
    List<WeeklyReportDto> result = apiInstance.getAllWeeklyReports(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getAllWeeklyReports");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids31"></a>
# **getByUuids31**
> List&lt;WeeklyReportDto&gt; getByUuids31(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<WeeklyReportDto> result = apiInstance.getByUuids31(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#getByUuids31");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postWeeklyReports"></a>
# **postWeeklyReports**
> List&lt;PushResult&gt; postWeeklyReports(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.WeeklyReportControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


WeeklyReportControllerApi apiInstance = new WeeklyReportControllerApi();
List<WeeklyReportDto> body = Arrays.asList(new WeeklyReportDto()); // List<WeeklyReportDto> | 
try {
    List<PushResult> result = apiInstance.postWeeklyReports(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling WeeklyReportControllerApi#postWeeklyReports");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;WeeklyReportDto&gt;**](WeeklyReportDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

