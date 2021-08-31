# CaseControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllCases**](CaseControllerApi.md#getAllCases) | **GET** /cases/all/{since} | 
[**getAllUuids6**](CaseControllerApi.md#getAllUuids6) | **GET** /cases/uuids | 
[**getArchivedUuidsSince**](CaseControllerApi.md#getArchivedUuidsSince) | **GET** /cases/archived/{since} | 
[**getByPersonUuids**](CaseControllerApi.md#getByPersonUuids) | **POST** /cases/query/persons | 
[**getByUuid**](CaseControllerApi.md#getByUuid) | **GET** /cases/{uuid} | 
[**getByUuids7**](CaseControllerApi.md#getByUuids7) | **POST** /cases/query | 
[**getDeletedUuidsSince**](CaseControllerApi.md#getDeletedUuidsSince) | **GET** /cases/deleted/{since} | 
[**getDuplicates**](CaseControllerApi.md#getDuplicates) | **POST** /cases/getduplicates | 
[**getDuplicates1**](CaseControllerApi.md#getDuplicates1) | **POST** /cases/getduplicates/{reportDateThreshold} | 
[**getIndexDetailedList**](CaseControllerApi.md#getIndexDetailedList) | **POST** /cases/detailedIndexList | 
[**getIndexList**](CaseControllerApi.md#getIndexList) | **POST** /cases/indexList | 
[**postCases**](CaseControllerApi.md#postCases) | **POST** /cases/push | 

<a name="getAllCases"></a>
# **getAllCases**
> List&lt;CaseDataDto&gt; getAllCases(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<CaseDataDto> result = apiInstance.getAllCases(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids6"></a>
# **getAllUuids6**
> List&lt;String&gt; getAllUuids6()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
try {
    List<String> result = apiInstance.getAllUuids6();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getAllUuids6");
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

<a name="getArchivedUuidsSince"></a>
# **getArchivedUuidsSince**
> List&lt;String&gt; getArchivedUuidsSince(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getArchivedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getArchivedUuidsSince");
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

<a name="getByPersonUuids"></a>
# **getByPersonUuids**
> List&lt;CaseDataDto&gt; getByPersonUuids(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CaseDataDto> result = apiInstance.getByPersonUuids(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getByPersonUuids");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuid"></a>
# **getByUuid**
> CaseDataDto getByUuid(uuid)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
String uuid = "uuid_example"; // String | 
try {
    CaseDataDto result = apiInstance.getByUuid(uuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getByUuid");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | **String**|  |

### Return type

[**CaseDataDto**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids7"></a>
# **getByUuids7**
> List&lt;CaseDataDto&gt; getByUuids7(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<CaseDataDto> result = apiInstance.getByUuids7(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getByUuids7");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;CaseDataDto&gt;**](CaseDataDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince"></a>
# **getDeletedUuidsSince**
> List&lt;String&gt; getDeletedUuidsSince(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getDeletedUuidsSince");
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

<a name="getDuplicates"></a>
# **getDuplicates**
> List&lt;CasePersonDto&gt; getDuplicates(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
CasePersonDto body = new CasePersonDto(); // CasePersonDto | 
try {
    List<CasePersonDto> result = apiInstance.getDuplicates(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getDuplicates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CasePersonDto**](CasePersonDto.md)|  | [optional]

### Return type

[**List&lt;CasePersonDto&gt;**](CasePersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDuplicates1"></a>
# **getDuplicates1**
> List&lt;CasePersonDto&gt; getDuplicates1(reportDateThreshold, body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Integer reportDateThreshold = 56; // Integer | 
CasePersonDto body = new CasePersonDto(); // CasePersonDto | 
try {
    List<CasePersonDto> result = apiInstance.getDuplicates1(reportDateThreshold, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getDuplicates1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **reportDateThreshold** | **Integer**|  |
 **body** | [**CasePersonDto**](CasePersonDto.md)|  | [optional]

### Return type

[**List&lt;CasePersonDto&gt;**](CasePersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getIndexDetailedList"></a>
# **getIndexDetailedList**
> PageCaseIndexDetailedDto getIndexDetailedList(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PageCaseIndexDetailedDto result = apiInstance.getIndexDetailedList(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getIndexDetailedList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PageCaseIndexDetailedDto**](PageCaseIndexDetailedDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getIndexList"></a>
# **getIndexList**
> PageCaseIndexDto getIndexList(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PageCaseIndexDto result = apiInstance.getIndexList(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#getIndexList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PageCaseIndexDto**](PageCaseIndexDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postCases"></a>
# **postCases**
> List&lt;PushResult&gt; postCases(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.CaseControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


CaseControllerApi apiInstance = new CaseControllerApi();
List<CaseDataDto> body = Arrays.asList(new CaseDataDto()); // List<CaseDataDto> | 
try {
    List<PushResult> result = apiInstance.postCases(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CaseControllerApi#postCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;CaseDataDto&gt;**](CaseDataDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

