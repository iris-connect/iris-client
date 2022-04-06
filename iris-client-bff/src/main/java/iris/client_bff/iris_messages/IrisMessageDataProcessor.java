package iris.client_bff.iris_messages;

import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;

import java.util.UUID;

public interface IrisMessageDataProcessor {
	String getDiscriminator();

	void validateExportSelection(String exportSelection) throws IrisMessageDataException;

	void validateImportSelection(String importSelection) throws IrisMessageDataException;

	String buildPayload(String exportSelection) throws IrisMessageDataException;

	void importPayload(String payload) throws IrisMessageDataException;

	void importPayload(String payload, UUID importTargetId, String importSelection) throws IrisMessageDataException;

	Object getViewPayload(String payload) throws IrisMessageDataException;

	Object getImportSelectionViewPayload(String payload, UUID importTargetId) throws IrisMessageDataException;
}
