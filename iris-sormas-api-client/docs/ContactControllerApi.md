# ContactControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllActiveUuids2**](ContactControllerApi.md#getAllActiveUuids2) | **GET** /contacts/uuids | 
[**getAllContacts**](ContactControllerApi.md#getAllContacts) | **GET** /contacts/all/{since} | 
[**getByPersonUuids1**](ContactControllerApi.md#getByPersonUuids1) | **POST** /contacts/query/persons | 
[**getByUuids10**](ContactControllerApi.md#getByUuids10) | **POST** /contacts/query | 
[**getDeletedUuidsSince1**](ContactControllerApi.md#getDeletedUuidsSince1) | **GET** /contacts/deleted/{since} | 
[**getIndexList1**](ContactControllerApi.md#getIndexList1) | **POST** /contacts/indexList | 
[**postContacts**](ContactControllerApi.md#postContacts) | **POST** /contacts/push | 

<a name="getAllActiveUuids2"></a>
# **getAllActiveUuids2**
> List&lt;String&gt; getAllActiveUuids2()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
try {
    List<String> result = apiInstance.getAllActiveUuids2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getAllActiveUuids2");
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

<a name="getAllContacts"></a>
# **getAllContacts**
> List&lt;ContactDto&gt; getAllContacts(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
Long since = 789L; // Long | 
try {
    List<ContactDto> result = apiInstance.getAllContacts(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getAllContacts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;ContactDto&gt;**](ContactDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByPersonUuids1"></a>
# **getByPersonUuids1**
> List&lt;ContactDto&gt; getByPersonUuids1(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<ContactDto> result = apiInstance.getByPersonUuids1(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getByPersonUuids1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;ContactDto&gt;**](ContactDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids10"></a>
# **getByUuids10**
> List&lt;ContactDto&gt; getByUuids10(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<ContactDto> result = apiInstance.getByUuids10(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getByUuids10");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;ContactDto&gt;**](ContactDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuidsSince1"></a>
# **getDeletedUuidsSince1**
> List&lt;String&gt; getDeletedUuidsSince1(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuidsSince1(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getDeletedUuidsSince1");
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

<a name="getIndexList1"></a>
# **getIndexList1**
> PageContactIndexDto getIndexList1(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PageContactIndexDto result = apiInstance.getIndexList1(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#getIndexList1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PageContactIndexDto**](PageContactIndexDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postContacts"></a>
# **postContacts**
> List&lt;PushResult&gt; postContacts(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.ContactControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


ContactControllerApi apiInstance = new ContactControllerApi();
List<ContactDto> body = Arrays.asList(new ContactDto()); // List<ContactDto> | 
try {
    List<PushResult> result = apiInstance.postContacts(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ContactControllerApi#postContacts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ContactDto&gt;**](ContactDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

