package iris.client_bff.core.web.error;

import iris.client_bff.core.web.filter.ApplicationRequestSizeLimitFilter.BlockLimitExceededException;
import iris.client_bff.events.exceptions.IRISDataRequestException;
import iris.client_bff.search_client.exceptions.IRISSearchException;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController()
@RequestMapping(GlobalControllerExceptionHandlerIntegrationTest.PATH)
@Validated
@RequiredArgsConstructor
class TestController {

	private final Validator validator;

	@PostMapping
	public void withBody(@RequestBody @Valid Dto dto) {}

	@PutMapping
	public void withoutValid(@RequestBody Dto dto) {

		var violations = validator.validate(dto);

		throw new ConstraintViolationException(violations);
	}

	@GetMapping
	public void withoutBody(@RequestParam @Size(min = 2) String param) {}

	@DeleteMapping
	public void withException() {
		throw new TestExceptionWithResponseStatus();
	}

	@DeleteMapping("/a")
	public void withException2() {
		throw new TestException();
	}

	@DeleteMapping("/IRISSearchException")
	public void withIRISSearchException() {
		throw new IRISSearchException("Test", new RuntimeException());
	}

	@DeleteMapping("/IRISDataRequestException")
	public void withIRISDataRequestException() {
		throw new IRISDataRequestException("Test");
	}

	@DeleteMapping("/BlockLimitExceededException")
	public void withBlockLimitExceededException() {
		throw new BlockLimitExceededException("Test");
	}

	@DeleteMapping("/ResponseStatusException")
	public void withResponseStatusException() {
		throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Test");
	}

	static record Dto(@Size(min = 2) String text, @Size(min = 2) String text2) {}

	@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "Test")
	static class TestExceptionWithResponseStatus extends RuntimeException {}

	static class TestException extends RuntimeException {

		TestException() {
			super("Test");
		}
	}
}
