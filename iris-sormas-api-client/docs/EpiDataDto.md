# EpiDataDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**exposureDetailsKnown** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**activityAsCaseDetailsKnown** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**contactWithSourceCaseKnown** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**highTransmissionRiskArea** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**largeOutbreaksArea** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**areaInfectedAnimals** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**exposures** | [**List&lt;ExposureDto&gt;**](ExposureDto.md) |  |  [optional]
**activitiesAsCase** | [**List&lt;ActivityAsCaseDto&gt;**](ActivityAsCaseDto.md) |  |  [optional]
