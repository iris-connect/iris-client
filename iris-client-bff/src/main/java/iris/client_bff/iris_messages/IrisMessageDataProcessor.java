package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;

import java.util.UUID;

/**
 * For each type of MessageData a dedicated MessageDataProcessor has to be
 * implemented, inheriting this Interface.
 * 
 * Please note: The MessageData type is neither known nor processed directly
 * within the "iris_messages" package. When adding a new MessageData type there
 * should never be the need to change anything within the "iris_messages"
 * package.
 */
public interface IrisMessageDataProcessor {

	/**
	 * The MessageDataProcessor for a specific type of MessageData is defined by its
	 * unique discriminator. This discriminator has to be identical in the frontend
	 * and backend parts of the application.
	 * 
	 * E.g.: "event-tracking" for Events, "vaccination-report" for VaccinationInfo
	 * 
	 * @return The unique discriminator for the specific MessageData type
	 */
	String getDiscriminator();

	/**
	 * When a user creates / sends a Message with DataAttachments
	 * (IrisMessageInsertDto -> DataAttachment), it has to be validated.
	 * 
	 * The Message is validated by the IrisMessageController's Validator, except
	 * it's specific type(s) of exportSelection (payload of DataAttachment).
	 * 
	 * The validation of the exportSelection is handled by the MessageDataProcessor.
	 * 
	 * Validation should be done using JSR-380 Bean Validation and Validation
	 * Annotations. For parsing and validating the JSON, the IrisMessageDataUtils
	 * class can be used.
	 * 
	 * @param exportSelection JSON String representation of an ExportSelectionDto
	 *                        with Validation Annotations, containing the ID of a
	 *                        single Object and - if required - additional
	 *                        information (like the IDs of a subselection of
	 *                        elements), e.g.:
	 * 
	 *                        - "event-tracking": Object: ID of the event,
	 *                        subselection: IDs of the selected guests
	 * 
	 *                        - "vaccination-report": Object: ID of the report,
	 *                        subselection: IDs of the selected employees
	 * 
	 * @throws IrisMessageDataException If the validation fails, an
	 *                                  IrisMessageDataException has to be thrown.
	 *                                  The containing error message is forwarded
	 *                                  within a ResponseStatusException to the
	 *                                  frontend.
	 */
	void validateExportSelection(String exportSelection) throws IrisMessageDataException;

	/**
	 * When a user wants to update an existing Entity (target Object) with a
	 * MessageDataPayload (source Object), (s)he has to select which parts of the
	 * source Object should be used to update the target Object. This selection has
	 * to be validated.
	 * 
	 * The importTarget (ID of the target Object) is always an optional UUID and not
	 * part of the importSelection.
	 * 
	 * The validation of the importSelection is handled by the MessageDataProcessor.
	 * 
	 * Validation should be done using JSR-380 Bean Validation and Validation
	 * Annotations. For parsing and validating the JSON, the IrisMessageDataUtils
	 * class can be used.
	 * 
	 * @param importSelection JSON String representation of an ImportSelectionDto
	 *                        with Validation Annotations, containing the parts of
	 *                        the source Object, a user wants to import, e.g.:
	 * 
	 *                        - "event-tracking": IDs of the selected guests
	 * 
	 *                        - "vaccination-report": IDs of the selected employees
	 * 
	 * @throws IrisMessageDataException If the validation fails, an
	 *                                  IrisMessageDataException has to be thrown.
	 *                                  The containing error message is forwarded
	 *                                  within a ResponseStatusException to the
	 *                                  frontend.
	 */
	void validateImportSelection(String importSelection) throws IrisMessageDataException;

	/**
	 * When sending / creating a Message with DataAttachments, a MessageDataPayload
	 * Object has to be built based on the exportSelection.
	 * 
	 * The exportSelection is validated in the IrisMessageController by using the
	 * validateExportSelection Method of the MessageDataProcessor.
	 * 
	 * The MessageDataPayload Object is created on the author's side and has to
	 * contain ALL information that are required to create or update an Entity and
	 * its dependencies on the recipients side.
	 * 
	 * IMPORTANT: When implementing the MessageDataPayload Object:
	 * 
	 * - As the same MessageDataPayload Object is used for export and import,
	 * add @DefuseJsonString(maxLength = …) Annotations to String fields that need
	 * to be defused.
	 * 
	 * - Avoid passing internal / autogenerated information (like UUIDs).
	 * 
	 * - If there are collections within the MessageDataPayload which will be
	 * selectable during the import process on the recipients side, it is mandatory
	 * to add "messageDataSelectId"s to the collection items (e.g. by using random
	 * UUIDs). Otherwise, the importSelection component in the frontend won't work.
	 * 
	 * For implementation, follow these steps:
	 * 
	 * 1. Parse the exportSelection String to get the ExportSelectionDto
	 * 
	 * 2. Retrieve the Entity with the ID of the selected Object from the database
	 * 
	 * 3. Map the Entity to the MessageDataPayload Object and filter any
	 * subselection of elements if present
	 * 
	 * 4. return the JSON String representation of the MessageDataPayload Object
	 * 
	 * The class IrisMessageDataUtils can be used to parse from or serialize to
	 * JSON.
	 * 
	 * @param exportSelection JSON String representation of an ExportSelectionDto,
	 *                        containing the ID of the selected Object and - if
	 *                        required - additional information
	 * @return JSON String representation of the MessageDataPayload that is sent to
	 *         the Message recipient
	 * @throws IrisMessageDataException If the build fails, an
	 *                                  IrisMessageDataException has to be thrown.
	 *                                  The containing error message is forwarded
	 *                                  within a ResponseStatusException to the
	 *                                  frontend.
	 */
	String buildPayload(String exportSelection) throws IrisMessageDataException;

	/**
	 * This method is used to create a new entry in the database when the user
	 * imports a MessageDataPayload Object without specifying an import target.
	 * 
	 * IMPORTANT: When importing the MessageDataPayload Object:
	 *
	 * Use @DefuseJsonString(maxLength = …) in the MessageDataPayload Object to
	 * annotate String fields that need to be defused.
	 * 
	 * For implementation, follow these steps:
	 * 
	 * 1. Parse the payload String to get the defused MessageDataPayload
	 * 
	 * 2. Use the MessageDataPayload to create a new Entity (fill in missing
	 * properties with substitution values if necessary)
	 * 
	 * 3. Save the Entity to the database
	 * 
	 * The class IrisMessageDataUtils can be used to parse from or serialize to
	 * JSON.
	 * 
	 * @param payload JSON String representation of the MessageDataPayload that is
	 *                used to create a new entry in the database on the recipients
	 *                side
	 * @throws IrisMessageDataException If the import fails, an
	 *                                  IrisMessageDataException has to be thrown.
	 *                                  The containing error message is forwarded
	 *                                  within a ResponseStatusException to the
	 *                                  frontend.
	 */
	void importPayload(String payload) throws IrisMessageDataException;

	/**
	 * This method is used to update an existing entry in the database when the user
	 * imports a MessageDataPayload Object, specifying an import target.
	 * 
	 * The importSelection is validated in the IrisMessageController by using the
	 * validateImportSelection Method of the MessageDataProcessor.
	 * 
	 * IMPORTANT: When importing the MessageDataPayload Object:
	 * 
	 * Use @DefuseJsonString(maxLength = …) in the MessageDataPayload Object to
	 * annotate String fields that need to be defused.
	 * 
	 * For implementation, follow these steps:
	 * 
	 * 1. Parse the payload String to get the defused MessageDataPayload
	 * 
	 * 2. Parse the importSelection String to get the ImportSelectionDto
	 * 
	 * 3. Retrieve the import target Entity with the ID matching the importTargetId
	 * from the database
	 * 
	 * 4. Apply the user-selected parts of the MessageDataPayload to the Entity,
	 * handling any subselection of elements based on the ImportSelectionDto
	 * 
	 * 5. Save the updated Entity to the database
	 * 
	 * The class IrisMessageDataUtils can be used to parse from or serialize to
	 * JSON.
	 * 
	 * @param payload         JSON String representation of the MessageDataPayload
	 *                        that is used to update an existing entry in the
	 *                        database on the recipients side
	 * @param importTargetId  The UUID of the importTarget, the user wants to update
	 * @param importSelection JSON String representation of an ImportSelectionDto,
	 *                        containing the parts of the MessageDataPayload, a user
	 *                        wants to import
	 * @throws IrisMessageDataException If the import fails, an
	 *                                  IrisMessageDataException has to be thrown.
	 *                                  The containing error message is forwarded
	 *                                  within a ResponseStatusException to the
	 *                                  frontend.
	 */
	void importPayload(String payload, UUID importTargetId, String importSelection) throws IrisMessageDataException;

	/**
	 * This method is used to provide a Dto for displaying a preview of the
	 * MessageDataPayload in the frontend.
	 * 
	 * For implementation: Using existing code for the details view (DetailsDto,
	 * frontend components, etc.) might be the best approach as it saves a lot of
	 * time.
	 * 
	 * Use @DefuseJsonString(maxLength = …) in the MessageDataPayload Object to
	 * annotate String fields that need to be defused. The class
	 * IrisMessageDataUtils can be used to parse from or serialize to JSON.
	 * 
	 * @param payload JSON String representation of the MessageDataPayload
	 * @return A Web Dto that is sufficient to display a detailed view of the
	 *         received data in the frontend
	 * @throws IrisMessageDataException If the processing of the MessageDataPayload
	 *                                  fails, an IrisMessageDataException has to be
	 *                                  thrown. The containing error message is
	 *                                  forwarded within a ResponseStatusException
	 *                                  to the frontend.
	 */
	Object getViewPayload(String payload) throws IrisMessageDataException;

	/**
	 * When updating an existing entry in the database there might be elements in
	 * the MessageDataPayload that are identical to the target Object.
	 * 
	 * This method is used to provide a selection view for the user, showing
	 * duplicate / conflicting elements.
	 * 
	 * Please note: Make sure to exclude any form of IDs from the comparison as they
	 * were stripped or randomly generated in the build process of the
	 * MessageDataPayload!
	 * 
	 * For implementation, follow these steps:
	 * 
	 * 1. Parse the payload String to get the defused MessageDataPayload
	 * 
	 * 2. Retrieve the import target Entity with the ID matching the importTargetId
	 * from the database
	 * 
	 * 3. Retrieve all selectable collections of the MessageDataPayload
	 * 
	 * 4. Find duplicates within the selectable collections of the
	 * MessageDataPayload by comparing relevant properties of corresponding
	 * collection items of the Entity
	 * 
	 * 5. return an ImportSelectionViewPayloadDto containing the selectables and
	 * duplicates
	 * 
	 * Use @DefuseJsonString(maxLength = …) in the MessageDataPayload Object to
	 * annotate String fields that need to be defused. The class
	 * IrisMessageDataUtils can be used to parse from or serialize to JSON.
	 * 
	 * @param payload        JSON String representation of the MessageDataPayload
	 * @param importTargetId The UUID of the importTarget, the user wants to update
	 * @return A ImportSelectionViewPayloadDto containing selectables and duplicates
	 * @throws IrisMessageDataException If the processing of the MessageDataPayload
	 *                                  or the retrieval of the importTarget fails,
	 *                                  an IrisMessageDataException has to be
	 *                                  thrown. The containing error message is
	 *                                  forwarded within a ResponseStatusException
	 *                                  to the frontend.
	 */
	Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException;
}
