# HospitalizationDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**admittedToHealthFacility** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**admissionDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**dischargeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**isolated** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**isolationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**leftAgainstAdvice** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**hospitalizedPreviously** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**previousHospitalizations** | [**List&lt;PreviousHospitalizationDto&gt;**](PreviousHospitalizationDto.md) |  |  [optional]
**intensiveCareUnit** | [**YesNoUnknown**](YesNoUnknown.md) |  |  [optional]
**intensiveCareUnitStart** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**intensiveCareUnitEnd** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**hospitalizationReason** | [**HospitalizationReasonType**](HospitalizationReasonType.md) |  |  [optional]
**otherHospitalizationReason** | **String** |  |  [optional]
