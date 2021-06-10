# EventControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids4**](EventControllerApi.md#getAllActiveUuids4) | **GET** /events/uuids | 
[**getAllEvents**](EventControllerApi.md#getAllEvents) | **GET** /events/all/{since} | 
[**getArchivedUuidsSince1**](EventControllerApi.md#getArchivedUuidsSince1) | **GET** /events/archived/{since} | 
[**getByUuids17**](EventControllerApi.md#getByUuids17) | **POST** /events/query | 
[**getDeletedUuidsSince3**](EventControllerApi.md#getDeletedUuidsSince3) | **GET** /events/deleted/{since} | 
[**getIndexList2**](EventControllerApi.md#getIndexList2) | **POST** /events/indexList | 
[**postEvents**](EventControllerApi.md#postEvents) | **POST** /events/push | 

<a name="getAllActiveUuids4"></a>
# **getAllActiveUuids4**
> List&lt;String&gt; getAllActiveUuids4()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids4();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getAllActiveUuids4");
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

<a name="getAllEvents"></a>
# **getAllEvents**
> List&lt;EventDto&gt; getAllEvents(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long | 
try {
    List<EventDto> result = apiInstance.getAllEvents(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getAllEvents");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;EventDto&gt;**](EventDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getArchivedUuidsSince1"></a>
# **getArchivedUuidsSince1**
> List&lt;String&gt; getArchivedUuidsSince1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getArchivedUuidsSince1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getArchivedUuidsSince1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids17"></a>
# **getByUuids17**
> List&lt;EventDto&gt; getByUuids17(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<EventDto> result = apiInstance.getByUuids17(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getByUuids17");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;EventDto&gt;**](EventDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince3"></a>
# **getDeletedUuidsSince3**
> List&lt;String&gt; getDeletedUuidsSince3(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince3(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getDeletedUuidsSince3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getIndexList2"></a>
# **getIndexList2**
> PageEventIndexDto getIndexList2(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PageEventIndexDto result = apiInstance.getIndexList2(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#getIndexList2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PageEventIndexDto**](PageEventIndexDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postEvents"></a>
# **postEvents**
> List&lt;PushResult&gt; postEvents(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventControllerApi apiInstance = new EventControllerApi();
List<EventDto> body = Arrays.asList(new EventDto()); // List<EventDto> | 
try {
    List<PushResult> result = apiInstance.postEvents(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventControllerApi#postEvents");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;EventDto&gt;**](EventDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

