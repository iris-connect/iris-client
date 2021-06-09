# PersonControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllPersons**](PersonControllerApi.md#getAllPersons) | **GET** /persons/all/{since} | 
[**getAllUuids15**](PersonControllerApi.md#getAllUuids15) | **GET** /persons/uuids | 
[**getByExternalIds**](PersonControllerApi.md#getByExternalIds) | **POST** /persons/query/byExternalIds | 
[**getByUuid1**](PersonControllerApi.md#getByUuid1) | **GET** /persons/{uuid} | 
[**getByUuids21**](PersonControllerApi.md#getByUuids21) | **POST** /persons/query | 
[**getIndexList3**](PersonControllerApi.md#getIndexList3) | **POST** /persons/indexList | 
[**postPersons**](PersonControllerApi.md#postPersons) | **POST** /persons/push | 

<a name="getAllPersons"></a>
# **getAllPersons**
> List&lt;PersonDto&gt; getAllPersons(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
Long since = 789L; // Long | 
try {
    List<PersonDto> result = apiInstance.getAllPersons(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllPersons");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids15"></a>
# **getAllUuids15**
> List&lt;String&gt; getAllUuids15()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
try {
    List<String> result = apiInstance.getAllUuids15();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllUuids15");
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

<a name="getByExternalIds"></a>
# **getByExternalIds**
> List&lt;PersonDto&gt; getByExternalIds(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PersonDto> result = apiInstance.getByExternalIds(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getByExternalIds");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuid1"></a>
# **getByUuid1**
> PersonDto getByUuid1(uuid)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
String uuid = "uuid_example"; // String | 
try {
    PersonDto result = apiInstance.getByUuid1(uuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getByUuid1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | **String**|  |

### Return type

[**PersonDto**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getByUuids21"></a>
# **getByUuids21**
> List&lt;PersonDto&gt; getByUuids21(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<PersonDto> result = apiInstance.getByUuids21(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getByUuids21");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getIndexList3"></a>
# **getIndexList3**
> PagePersonIndexDto getIndexList3(offset, size)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
Integer offset = 56; // Integer | 
Integer size = 56; // Integer | 
try {
    PagePersonIndexDto result = apiInstance.getIndexList3(offset, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getIndexList3");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **Integer**|  | [optional]
 **size** | **Integer**|  | [optional]

### Return type

[**PagePersonIndexDto**](PagePersonIndexDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postPersons"></a>
# **postPersons**
> List&lt;PushResult&gt; postPersons(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


PersonControllerApi apiInstance = new PersonControllerApi();
List<PersonDto> body = Arrays.asList(new PersonDto()); // List<PersonDto> | 
try {
    List<PushResult> result = apiInstance.postPersons(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#postPersons");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;PersonDto&gt;**](PersonDto.md)|  | [optional]

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

