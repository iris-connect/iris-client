# EventParticipantControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids3**](EventParticipantControllerApi.md#getAllActiveUuids3) | **GET** /eventparticipants/uuids | 
[**getAllEventParticipantsAfter**](EventParticipantControllerApi.md#getAllEventParticipantsAfter) | **GET** /eventparticipants/all/{since} | 
[**getByEventUuids**](EventParticipantControllerApi.md#getByEventUuids) | **POST** /eventparticipants/query/events | 
[**getByPersonUuids2**](EventParticipantControllerApi.md#getByPersonUuids2) | **POST** /eventparticipants/query/persons | 
[**getByUuids16**](EventParticipantControllerApi.md#getByUuids16) | **POST** /eventparticipants/query | 
[**getDeletedUuidsSince2**](EventParticipantControllerApi.md#getDeletedUuidsSince2) | **GET** /eventparticipants/deleted/{since} | 
[**postEventParticipants**](EventParticipantControllerApi.md#postEventParticipants) | **POST** /eventparticipants/push | 

<a name="getAllActiveUuids3"></a>
# **getAllActiveUuids3**
> List&lt;String&gt; getAllActiveUuids3()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids3();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getAllActiveUuids3");
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

<a name="getAllEventParticipantsAfter"></a>
# **getAllEventParticipantsAfter**
> List&lt;EventParticipantDto&gt; getAllEventParticipantsAfter(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
Long since = 789L; // Long | 
try {
    List<EventParticipantDto> result = apiInstance.getAllEventParticipantsAfter(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getAllEventParticipantsAfter");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByEventUuids"></a>
# **getByEventUuids**
> List&lt;EventParticipantDto&gt; getByEventUuids(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<EventParticipantDto> result = apiInstance.getByEventUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getByEventUuids");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByPersonUuids2"></a>
# **getByPersonUuids2**
> List&lt;EventParticipantDto&gt; getByPersonUuids2(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<EventParticipantDto> result = apiInstance.getByPersonUuids2(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getByPersonUuids2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids16"></a>
# **getByUuids16**
> List&lt;EventParticipantDto&gt; getByUuids16(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<EventParticipantDto> result = apiInstance.getByUuids16(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getByUuids16");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince2"></a>
# **getDeletedUuidsSince2**
> List&lt;String&gt; getDeletedUuidsSince2(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#getDeletedUuidsSince2");
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

<a name="postEventParticipants"></a>
# **postEventParticipants**
> List&lt;PushResult&gt; postEventParticipants(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.EventParticipantControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


EventParticipantControllerApi apiInstance = new EventParticipantControllerApi();
List<EventParticipantDto> body = Arrays.asList(new EventParticipantDto()); // List<EventParticipantDto> | 
try {
    List<PushResult> result = apiInstance.postEventParticipants(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventParticipantControllerApi#postEventParticipants");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;EventParticipantDto&gt;**](EventParticipantDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

