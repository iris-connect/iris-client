# TaskControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAll10**](TaskControllerApi.md#getAll10) | **GET** /tasks/all/{since} | 
[**getAllActiveUuids8**](TaskControllerApi.md#getAllActiveUuids8) | **GET** /tasks/uuids | 
[**getArchivedUuidsSince2**](TaskControllerApi.md#getArchivedUuidsSince2) | **GET** /tasks/archived/{since} | 
[**getByUuid2**](TaskControllerApi.md#getByUuid2) | **GET** /tasks/{uuid} | 
[**getByUuids27**](TaskControllerApi.md#getByUuids27) | **POST** /tasks/query | 
[**getIndexList5**](TaskControllerApi.md#getIndexList5) | **POST** /tasks/indexList | 
[**postTasks**](TaskControllerApi.md#postTasks) | **POST** /tasks/push | 

<a name="getAll10"></a>
# **getAll10**
> List&lt;TaskDto&gt; getAll10(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
Long since = 789L; // Long | 
try {
    List<TaskDto> result = apiInstance.getAll10(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getAll10");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;TaskDto&gt;**](TaskDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllActiveUuids8"></a>
# **getAllActiveUuids8**
> List&lt;String&gt; getAllActiveUuids8()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids8();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getAllActiveUuids8");
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

<a name="getArchivedUuidsSince2"></a>
# **getArchivedUuidsSince2**
> List&lt;String&gt; getArchivedUuidsSince2(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getArchivedUuidsSince2(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getArchivedUuidsSince2");
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

<a name="getByUuid2"></a>
# **getByUuid2**
> TaskDto getByUuid2(uuid)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
String uuid = "uuid_example"; // String | 
try {
    TaskDto result = apiInstance.getByUuid2(uuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getByUuid2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | **String**|  |

### Return type

[**TaskDto**](TaskDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids27"></a>
# **getByUuids27**
> List&lt;TaskDto&gt; getByUuids27(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<TaskDto> result = apiInstance.getByUuids27(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getByUuids27");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;TaskDto&gt;**](TaskDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getIndexList5"></a>
# **getIndexList5**
> PageTaskIndexDto getIndexList5(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PageTaskIndexDto result = apiInstance.getIndexList5(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#getIndexList5");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PageTaskIndexDto**](PageTaskIndexDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postTasks"></a>
# **postTasks**
> List&lt;PushResult&gt; postTasks(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.TaskControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


TaskControllerApi apiInstance = new TaskControllerApi();
List<TaskDto> body = Arrays.asList(new TaskDto()); // List<TaskDto> | 
try {
    List<PushResult> result = apiInstance.postTasks(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TaskControllerApi#postTasks");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;TaskDto&gt;**](TaskDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

