# SormasToSormasControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptSharedCaseRequest**](SormasToSormasControllerApi.md#acceptSharedCaseRequest) | **POST** /sormasToSormas/cases/request/accept | 
[**acceptSharedContactRequest**](SormasToSormasControllerApi.md#acceptSharedContactRequest) | **POST** /sormasToSormas/contacts/request/accept | 
[**acceptSharedEventRequest**](SormasToSormasControllerApi.md#acceptSharedEventRequest) | **POST** /sormasToSormas/events/request/accept | 
[**rejectSharedCaseRequest**](SormasToSormasControllerApi.md#rejectSharedCaseRequest) | **POST** /sormasToSormas/cases/request/reject | 
[**rejectSharedContactRequest**](SormasToSormasControllerApi.md#rejectSharedContactRequest) | **POST** /sormasToSormas/contacts/request/reject | 
[**rejectSharedEventRequest**](SormasToSormasControllerApi.md#rejectSharedEventRequest) | **POST** /sormasToSormas/events/request/reject | 
[**revokeShareRequests**](SormasToSormasControllerApi.md#revokeShareRequests) | **POST** /sormasToSormas/revoke-requests | 
[**saveReturnedCase**](SormasToSormasControllerApi.md#saveReturnedCase) | **PUT** /sormasToSormas/cases | 
[**saveReturnedContact**](SormasToSormasControllerApi.md#saveReturnedContact) | **PUT** /sormasToSormas/contacts | 
[**saveReturnedEvent**](SormasToSormasControllerApi.md#saveReturnedEvent) | **PUT** /sormasToSormas/events | 
[**saveSharedCase**](SormasToSormasControllerApi.md#saveSharedCase) | **POST** /sormasToSormas/cases | 
[**saveSharedCaseRequest**](SormasToSormasControllerApi.md#saveSharedCaseRequest) | **POST** /sormasToSormas/cases/request | 
[**saveSharedContact**](SormasToSormasControllerApi.md#saveSharedContact) | **POST** /sormasToSormas/contacts | 
[**saveSharedContactRequest**](SormasToSormasControllerApi.md#saveSharedContactRequest) | **POST** /sormasToSormas/contacts/request | 
[**saveSharedEventRequest**](SormasToSormasControllerApi.md#saveSharedEventRequest) | **POST** /sormasToSormas/events/request | 
[**saveSharedEvents**](SormasToSormasControllerApi.md#saveSharedEvents) | **POST** /sormasToSormas/events | 
[**syncSharedCases**](SormasToSormasControllerApi.md#syncSharedCases) | **POST** /sormasToSormas/cases/sync | 
[**syncSharedContacts**](SormasToSormasControllerApi.md#syncSharedContacts) | **POST** /sormasToSormas/contacts/sync | 
[**syncSharedEvents**](SormasToSormasControllerApi.md#syncSharedEvents) | **POST** /sormasToSormas/events/sync | 
[**syncSharedLAbMessages**](SormasToSormasControllerApi.md#syncSharedLAbMessages) | **POST** /sormasToSormas/labmessages | 

<a name="acceptSharedCaseRequest"></a>
# **acceptSharedCaseRequest**
> acceptSharedCaseRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.acceptSharedCaseRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#acceptSharedCaseRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="acceptSharedContactRequest"></a>
# **acceptSharedContactRequest**
> acceptSharedContactRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.acceptSharedContactRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#acceptSharedContactRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="acceptSharedEventRequest"></a>
# **acceptSharedEventRequest**
> acceptSharedEventRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.acceptSharedEventRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#acceptSharedEventRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="rejectSharedCaseRequest"></a>
# **rejectSharedCaseRequest**
> rejectSharedCaseRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.rejectSharedCaseRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#rejectSharedCaseRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="rejectSharedContactRequest"></a>
# **rejectSharedContactRequest**
> rejectSharedContactRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.rejectSharedContactRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#rejectSharedContactRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="rejectSharedEventRequest"></a>
# **rejectSharedEventRequest**
> rejectSharedEventRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.rejectSharedEventRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#rejectSharedEventRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="revokeShareRequests"></a>
# **revokeShareRequests**
> revokeShareRequests(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    apiInstance.revokeShareRequests(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#revokeShareRequests");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveReturnedCase"></a>
# **saveReturnedCase**
> saveReturnedCase(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveReturnedCase(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveReturnedCase");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveReturnedContact"></a>
# **saveReturnedContact**
> saveReturnedContact(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveReturnedContact(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveReturnedContact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveReturnedEvent"></a>
# **saveReturnedEvent**
> saveReturnedEvent(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveReturnedEvent(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveReturnedEvent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedCase"></a>
# **saveSharedCase**
> saveSharedCase(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedCase(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedCase");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedCaseRequest"></a>
# **saveSharedCaseRequest**
> saveSharedCaseRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedCaseRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedCaseRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedContact"></a>
# **saveSharedContact**
> saveSharedContact(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedContact(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedContact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedContactRequest"></a>
# **saveSharedContactRequest**
> saveSharedContactRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedContactRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedContactRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedEventRequest"></a>
# **saveSharedEventRequest**
> saveSharedEventRequest(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedEventRequest(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedEventRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="saveSharedEvents"></a>
# **saveSharedEvents**
> saveSharedEvents(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.saveSharedEvents(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#saveSharedEvents");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="syncSharedCases"></a>
# **syncSharedCases**
> syncSharedCases(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.syncSharedCases(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#syncSharedCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="syncSharedContacts"></a>
# **syncSharedContacts**
> syncSharedContacts(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.syncSharedContacts(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#syncSharedContacts");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="syncSharedEvents"></a>
# **syncSharedEvents**
> syncSharedEvents(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.syncSharedEvents(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#syncSharedEvents");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="syncSharedLAbMessages"></a>
# **syncSharedLAbMessages**
> syncSharedLAbMessages(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.SormasToSormasControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


SormasToSormasControllerApi apiInstance = new SormasToSormasControllerApi();
SormasToSormasEncryptedDataDto body = new SormasToSormasEncryptedDataDto(); // SormasToSormasEncryptedDataDto | 
try {
    apiInstance.syncSharedLAbMessages(body);
} catch (ApiException e) {
    System.err.println("Exception when calling SormasToSormasControllerApi#syncSharedLAbMessages");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SormasToSormasEncryptedDataDto**](SormasToSormasEncryptedDataDto.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

