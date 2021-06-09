# FeatureConfigurationControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllFeatureConfigurations**](FeatureConfigurationControllerApi.md#getAllFeatureConfigurations) | **GET** /featureconfigurations/all/{since} | 
[**getAllUuids14**](FeatureConfigurationControllerApi.md#getAllUuids14) | **GET** /featureconfigurations/uuids | 
[**getByUuids19**](FeatureConfigurationControllerApi.md#getByUuids19) | **POST** /featureconfigurations/query | 
[**getDeletedUuids**](FeatureConfigurationControllerApi.md#getDeletedUuids) | **GET** /featureconfigurations/deleted/{since} | 

<a name="getAllFeatureConfigurations"></a>
# **getAllFeatureConfigurations**
> List&lt;FeatureConfigurationDto&gt; getAllFeatureConfigurations(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<FeatureConfigurationDto> result = apiInstance.getAllFeatureConfigurations(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllFeatureConfigurations");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getAllUuids14"></a>
# **getAllUuids14**
> List&lt;String&gt; getAllUuids14()



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
try {
    List<String> result = apiInstance.getAllUuids14();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getAllUuids14");
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

<a name="getByUuids19"></a>
# **getByUuids19**
> List&lt;FeatureConfigurationDto&gt; getByUuids19(body)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> | 
try {
    List<FeatureConfigurationDto> result = apiInstance.getByUuids19(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getByUuids19");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**List&lt;FeatureConfigurationDto&gt;**](FeatureConfigurationDto.md)

### Authorization

[basicAuth](../README.md#basicAuth)[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="getDeletedUuids"></a>
# **getDeletedUuids**
> List&lt;String&gt; getDeletedUuids(since)



### Example
```java
// Import classes:
//import iris.sormas.client.invoker.ApiClient;
//import iris.sormas.client.invoker.ApiException;
//import iris.sormas.client.invoker.Configuration;
//import iris.sormas.client.invoker.auth.*;
//import iris.sormas.client.api.FeatureConfigurationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");


FeatureConfigurationControllerApi apiInstance = new FeatureConfigurationControllerApi();
Long since = 789L; // Long | 
try {
    List<String> result = apiInstance.getDeletedUuids(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FeatureConfigurationControllerApi#getDeletedUuids");
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

