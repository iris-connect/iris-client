# VisitDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**person** | [**PersonReferenceDto**](PersonReferenceDto.md) |  | 
**disease** | [**Disease**](Disease.md) |  |  [optional]
**visitDateTime** | [**Instant**](OffsetDateTime.md) |  | 
**visitUser** | [**UserReferenceDto**](UserReferenceDto.md) |  | 
**visitStatus** | [**VisitStatus**](VisitStatus.md) |  | 
**visitRemarks** | **String** |  |  [optional]
**symptoms** | [**SymptomsDto**](SymptomsDto.md) |  |  [optional]
**reportLat** | **Double** |  |  [optional]
**reportLon** | **Double** |  |  [optional]
**reportLatLonAccuracy** | **Float** |  |  [optional]
**origin** | [**VisitOrigin**](VisitOrigin.md) |  |  [optional]
