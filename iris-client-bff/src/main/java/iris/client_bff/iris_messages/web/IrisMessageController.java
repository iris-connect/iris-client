package iris.client_bff.iris_messages.web;

import iris.client_bff.core.messages.ErrorMessages;
import iris.client_bff.core.validation.NoSignOfAttack;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import iris.client_bff.iris_messages.IrisMessageDataProcessors;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.iris_messages.exceptions.IrisMessageDataException;
import iris.client_bff.iris_messages.exceptions.IrisMessageException;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/iris-messages")
@Validated
@RequiredArgsConstructor
public class IrisMessageController {

	private final IrisMessageService irisMessageService;
	private final IrisMessageBuilderWeb irisMessageBuilder;

	private final IrisMessageDataProcessors messageDataProcessors;

	@GetMapping()
	public Page<IrisMessageListItemDto> getMessages(
			@RequestParam() IrisMessageFolderIdentifier folder,
			@RequestParam(required = false) @NoSignOfAttack(obfuscateLogging = false) String search,
			@PageableDefault(sort = "metadata.created", direction = Sort.Direction.DESC) Pageable pageable) {

		return this.irisMessageService.search(folder, search, pageable).map(IrisMessageListItemDto::fromEntity);
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<URI> createAndSendMessage(@RequestBody @Valid IrisMessageInsertDto irisMessageInsert,
			BindingResult bindingResult) {

		this.validateConstraints(bindingResult);
		this.validateIrisMessageInsert(irisMessageInsert);

		try {
			IrisMessage message = irisMessageBuilder.build(irisMessageInsert);
			IrisMessage sentMessage = irisMessageService.sendMessage(message);
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(sentMessage.getId())
					.toUri();
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			String errorMessage = e instanceof IrisMessageException ime
					? ime.getErrorMessage()
					: "iris_message.submission_error";
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		}
	}

	private void validateIrisMessageInsert(IrisMessageInsertDto irisMessageInsert) {

		if (irisMessageInsert.getDataAttachments() != null) {
			for (IrisMessageInsertDto.DataAttachment data : irisMessageInsert.getDataAttachments()) {
				try {

					this.messageDataProcessors.withProcessorFor(data.getDiscriminator())
							.validateExportSelection(data.getPayload());

				} catch (IrisMessageDataException e) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
				}
			}
		}
	}

	@GetMapping("/{messageId}")
	public ResponseEntity<IrisMessageDetailsDto> getMessageDetails(@PathVariable IrisMessageIdentifier messageId) {

		return irisMessageService.findById(messageId)
				.map(IrisMessageDetailsDto::fromEntity)
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);
	}

	@PatchMapping("/{messageId}")
	public ResponseEntity<IrisMessageDetailsDto> updateMessage(
			@PathVariable IrisMessageIdentifier messageId,
			@RequestBody @Valid IrisMessageUpdateDto irisMessageUpdate,
			BindingResult bindingResult) {

		this.validateConstraints(bindingResult);

		var updatedMessage = this.irisMessageService.updateReadState(messageId, irisMessageUpdate.getIsRead());

		return updatedMessage
				.map(IrisMessageDetailsDto::fromEntity)
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);
	}

	@GetMapping("/folders")
	public ResponseEntity<List<IrisMessageFolderDto>> getMessageFolders() {
		List<IrisMessageFolder> irisMessageFolders = irisMessageService.getFolders();
		// there should always be at least one inbox and one outbox folder. No folders at all = error
		if (irisMessageFolders.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(IrisMessageFolderDto.fromEntity(irisMessageFolders));
	}

	@GetMapping("/hd-contacts")
	public ResponseEntity<List<IrisMessageHdContact>> getMessageHdContacts(
			@RequestParam(required = false) @Size(max = 100) @NoSignOfAttack(obfuscateLogging = false) String search,
			@RequestParam(defaultValue = "false") boolean includeOwn) {

		try {
			List<IrisMessageHdContact> irisMessageContacts = new ArrayList<>(irisMessageService.getHdContacts(search));
			if (!includeOwn) {
				IrisMessageHdContact ownContact = this.irisMessageService.getOwnHdContact();
				irisMessageContacts.removeIf(c -> c.getId().equals(ownContact.getId()));
			}
			return ResponseEntity.ok(irisMessageContacts);
		} catch (Exception e) {

			String errorMessage = e instanceof IrisMessageException ime
					? ime.getErrorMessage()
					: "iris_message.missing_hd_contacts";

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		}
	}

	@GetMapping("/count/unread")
	public ResponseEntity<Integer> getUnreadMessageCount(
			@RequestParam(required = false) IrisMessageFolderIdentifier folder) {

		var count = folder == null
				? irisMessageService.getCountUnread()
				: irisMessageService.getCountUnreadByFolderId(folder);

		return ResponseEntity.ok(count);
	}

	private void validateConstraints(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String message = ErrorMessages.INVALID_INPUT + ": " + bindingResult.getFieldErrors().stream()
					.map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
					.collect(Collectors.joining(", "));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}
}
