# PathogenTestDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**sample** | [**SampleReferenceDto**](SampleReferenceDto.md) |  | 
**testedDisease** | [**Disease**](Disease.md) |  | 
**testedDiseaseVariant** | [**DiseaseVariant**](DiseaseVariant.md) |  |  [optional]
**testedDiseaseDetails** | **String** |  |  [optional]
**typingId** | **String** |  |  [optional]
**testType** | [**PathogenTestType**](PathogenTestType.md) |  | 
**pcrTestSpecification** | [**PCRTestSpecification**](PCRTestSpecification.md) |  |  [optional]
**testTypeText** | **String** |  |  [optional]
**testDateTime** | [**Instant**](OffsetDateTime.md) |  | 
**lab** | [**FacilityReferenceDto**](FacilityReferenceDto.md) |  | 
**labDetails** | **String** |  |  [optional]
**labUser** | [**UserReferenceDto**](UserReferenceDto.md) |  | 
**testResult** | [**PathogenTestResultType**](PathogenTestResultType.md) |  | 
**testResultText** | **String** |  | 
**testResultVerified** | **Boolean** |  | 
**fourFoldIncreaseAntibodyTiter** | **Boolean** |  |  [optional]
**serotype** | **String** |  |  [optional]
**cqValue** | **Float** |  |  [optional]
**reportDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**viaLims** | **Boolean** |  |  [optional]
