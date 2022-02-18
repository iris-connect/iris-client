package iris.client_bff.iris_messages.web;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.IrisMessage;
import iris.client_bff.iris_messages.IrisMessage.IrisMessageIdentifier;
import iris.client_bff.iris_messages.IrisMessageBuilder;
import iris.client_bff.iris_messages.IrisMessageException;
import iris.client_bff.iris_messages.IrisMessageFolder;
import iris.client_bff.iris_messages.IrisMessageFolder.IrisMessageFolderIdentifier;
import iris.client_bff.iris_messages.IrisMessageHdContact;
import iris.client_bff.iris_messages.IrisMessageService;
import iris.client_bff.ui.messages.ErrorMessages;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@AllArgsConstructor
@Validated
@RequestMapping("/iris-messages")
public class IrisMessageController {

	private static final String FIELD_SEARCH = "search";

	private static final String FIELD_HD_RECIPIENT = "hdRecipient";
	private static final String FIELD_SUBJECT = "subject";
	private static final String FIELD_BODY = "body";

	private IrisMessageService irisMessageService;
	private final IrisMessageBuilder irisMessageBuilder;
	private final ValidationHelper validationHelper;

	@GetMapping()
	public Page<IrisMessageListItemDto> getMessages(
			@RequestParam() IrisMessageFolderIdentifier folder,
			@RequestParam(required = false) String search,
			@PageableDefault(sort = "metadata.created", direction = Sort.Direction.DESC) Pageable pageable) {
		this.validateField(search, FIELD_SEARCH);
		return this.irisMessageService.search(folder, search, pageable).map(IrisMessageListItemDto::fromEntity);
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<URI> createAndSendMessage(@Valid @RequestBody IrisMessageInsertDto irisMessageInsert,
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
		} catch (Throwable e) {
			String errorMessage = e instanceof IrisMessageException ime
					? ime.getErrorMessage()
					: "iris_message.submission_error";
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
		}
	}

	private void validateIrisMessageInsert(IrisMessageInsertDto irisMessageInsert) {
		if (irisMessageInsert == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "iris_message.submission_error");
		}
		this.validateField(irisMessageInsert.getHdRecipient(), FIELD_HD_RECIPIENT);
		this.validateField(irisMessageInsert.getSubject(), FIELD_SUBJECT);
		this.validateField(irisMessageInsert.getBody(), FIELD_BODY);
	}

	@GetMapping("/{messageId}")
	public ResponseEntity<IrisMessageDetailsDto> getMessageDetails(@PathVariable IrisMessageIdentifier messageId) {
		Optional<IrisMessage> irisMessage = this.irisMessageService.findById(messageId);
		if (irisMessage.isPresent()) {
			IrisMessageDetailsDto messageDetailsDto = IrisMessageDetailsDto.fromEntity(irisMessage.get());
			return ResponseEntity.ok(messageDetailsDto);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/{messageId}")
	public ResponseEntity<IrisMessageDetailsDto> updateMessage(
			@PathVariable IrisMessageIdentifier messageId,
			@RequestBody @Valid IrisMessageUpdateDto irisMessageUpdate,
			BindingResult bindingResult) {
		this.validateConstraints(bindingResult);
		this.validateIrisMessageUpdate(irisMessageUpdate);

		var updatedMessage = this.irisMessageService.updateReadState(messageId, irisMessageUpdate.getIsRead());

		return updatedMessage
				.map(IrisMessageDetailsDto::fromEntity)
				.map(ResponseEntity::ok)
				.orElseGet(ResponseEntity.notFound()::build);
	}

	private void validateIrisMessageUpdate(IrisMessageUpdateDto irisMessageUpdate) {
		if (irisMessageUpdate == null || irisMessageUpdate.getIsRead() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
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
			@Valid @Size(max = 100) @RequestParam(required = false) String search,
			@RequestParam(defaultValue = "false") boolean includeOwn) {
		validateField(search, FIELD_SEARCH);
		try {
			ArrayList<IrisMessageHdContact> irisMessageContacts = new ArrayList<>(irisMessageService.getHdContacts(search));
			if (!includeOwn) {
				IrisMessageHdContact ownContact = this.irisMessageService.getOwnHdContact();
				irisMessageContacts.removeIf(c -> c.getId().equals(ownContact.getId()));
			}
			return ResponseEntity.ok(irisMessageContacts);
		} catch (Throwable e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "iris_message.missing_hd_contacts");
		}
	}

	@GetMapping("/count/unread")
	public ResponseEntity<Integer> getUnreadMessageCount(
			@RequestParam(required = false) IrisMessageFolderIdentifier folder) {
		if (folder == null) {
			return ResponseEntity.ok(irisMessageService.getCountUnread());
		}
		return ResponseEntity.ok(irisMessageService.getCountUnreadByFolderId(folder));
	}

	private void validateConstraints(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String message = ErrorMessages.INVALID_INPUT + ": " + bindingResult.getFieldErrors().stream()
					.map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
					.collect(Collectors.joining(", "));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}

	private void validateField(String value, String field) {
		if (validationHelper.isPossibleAttack(value, field, false)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_INPUT);
		}
	}
}
