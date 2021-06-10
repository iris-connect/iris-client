# TaskIndexDto

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**pseudonymized** | **Boolean** |  |  [optional]
**uuid** | **String** |  |  [optional]
**taskContext** | [**TaskContext**](TaskContext.md) |  |  [optional]
**caze** | [**CaseReferenceDto**](CaseReferenceDto.md) |  |  [optional]
**event** | [**EventReferenceDto**](EventReferenceDto.md) |  |  [optional]
**contact** | [**ContactReferenceDto**](ContactReferenceDto.md) |  |  [optional]
**region** | **String** |  |  [optional]
**district** | **String** |  |  [optional]
**community** | **String** |  |  [optional]
**taskType** | [**TaskType**](TaskType.md) |  |  [optional]
**priority** | [**TaskPriority**](TaskPriority.md) |  |  [optional]
**dueDate** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**suggestedStart** | [**Instant**](OffsetDateTime.md) |  |  [optional]
**taskStatus** | [**TaskStatus**](TaskStatus.md) |  |  [optional]
**creatorUser** | [**UserReferenceDto**](UserReferenceDto.md) |  |  [optional]
**creatorComment** | **String** |  |  [optional]
**assigneeUser** | [**UserReferenceDto**](UserReferenceDto.md) |  |  [optional]
**assigneeReply** | **String** |  |  [optional]
**jurisdiction** | [**TaskJurisdictionDto**](TaskJurisdictionDto.md) |  |  [optional]
**contextReference** | [**ReferenceDto**](ReferenceDto.md) |  |  [optional]
