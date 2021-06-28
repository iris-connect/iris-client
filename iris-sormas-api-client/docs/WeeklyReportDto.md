# WeeklyReportDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**creationDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**changeDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**uuid** | **String** |  |  [optional]
**reportingUser** | [**UserReferenceDto**](UserReferenceDto.md) |  |  [optional]
**reportDateTime** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**district** | [**DistrictReferenceDto**](DistrictReferenceDto.md) |  |  [optional]
**community** | [**CommunityReferenceDto**](CommunityReferenceDto.md) |  |  [optional]
**healthFacility** | [**FacilityReferenceDto**](FacilityReferenceDto.md) |  |  [optional]
**assignedOfficer** | [**UserReferenceDto**](UserReferenceDto.md) |  |  [optional]
**totalNumberOfCases** | **Integer** |  |  [optional]
**year** | **Integer** |  |  [optional]
**epiWeek** | **Integer** |  |  [optional]
**reportEntries** | [**List&lt;WeeklyReportEntryDto&gt;**](WeeklyReportEntryDto.md) |  |  [optional]
