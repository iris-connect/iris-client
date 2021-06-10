# PreviousHospitalizationDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**pseudonymized** | **Boolean** |  |  [optional]
**admissionDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**dischargeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**region** | [**RegionReferenceDto**](RegionReferenceDto.md) |  |  [optional]
**district** | [**DistrictReferenceDto**](DistrictReferenceDto.md) |  |  [optional]
**community** | [**CommunityReferenceDto**](CommunityReferenceDto.md) |  |  [optional]
**healthFacility** | [**FacilityReferenceDto**](FacilityReferenceDto.md) |  |  [optional]
**healthFacilityDetails** | **String** |  |  [optional]
**isolated** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**description** | **String** |  |  [optional]
**hospitalizationReason** | [**HospitalizationReasonType**](HospitalizationReasonType.md) |  |  [optional]
**otherHospitalizationReason** | **String** |  |  [optional]
**intensiveCareUnit** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**intensiveCareUnitStart** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**intensiveCareUnitEnd** | [**Instant**](OffsetDateTime.md) |  |  [optional]
