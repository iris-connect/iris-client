package de.healthIMIS.iris.dummy_web.web;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import de.healthIMIS.iris.dummy_web.Contacts;
import de.healthIMIS.iris.dummy_web.IrisProperties;
import de.healthIMIS.iris.dummy_web.RequestCode;
import de.healthIMIS.iris.dummy_web.Selection;
import de.healthIMIS.iris.dummy_web.web.submissions.ContactPerson;
import de.healthIMIS.iris.dummy_web.web.submissions.ContactPersonList;
import de.healthIMIS.iris.dummy_web.web.submissions.DataSubmissionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.client.Hop;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

	static final String ENCRYPTION_ALGORITHM = "AES";
	static final int ENCRYPTION_KEY_LENGTH = 256;
	static final String KEY_ENCRYPTION_ALGORITHM = "RSA";
	static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	private final LinkDiscoverer discoverer = new HalLinkDiscoverer();
	private final ObjectMapper mapper = new ObjectMapper();
	private final RestTemplate rest;
	private final Traverson traverson;
	private final IrisProperties properties;

	@GetMapping
	public String get(Model model) {
		model.addAttribute("code", new RequestCode());
		return "home";
	}

	@PostMapping
	public String process(@Valid @ModelAttribute("code") RequestCode code, Errors errors, Model model) {
		if (errors.hasErrors()) {
			return "home";
		}

		log.info("Processing code: " + code.getCode());

		// get data request object
		var getByCodeHop = Hop.rel("GetDataRequestByCode").withParameter("code", code.getCode());

		var dataRequest = traverson.follow(getByCodeHop).toObject(DataRequestDto.class);

		log.info("\nData request from healt department {} for the period {} to {}\n\n", dataRequest.getHealthDepartment(),
				dataRequest.getStart(), dataRequest.getEnd());

		// determines the existing links for the next POST operation
		var response = traverson.follow(getByCodeHop).toObject(String.class);

		var postContacts = discoverer.findLinkWithRel("PostContactsSubmission", response);
		var postEvents = discoverer.findLinkWithRel("PostEventsSubmission", response);
		var postGuests = discoverer.findLinkWithRel("PostGuestsSubmission", response);

		var links = Stream.of(postContacts, postEvents, postGuests).flatMap(Optional<Link>::stream)
				.collect(Collectors.toList());

		if (links.isEmpty()) {
			// exit the method if there is no link
			return "home";
		}

		model.addAttribute("options", links.stream().map(Link::getTitle).collect(Collectors.toList()));
		var selection = new Selection();
		selection.setCode(code.getCode());
		selection.setHd(dataRequest.getHealthDepartment());
		model.addAttribute("selection", selection);

		return "selection";
	}

	@PostMapping("/selection")
	public String processSelection(@Valid @ModelAttribute("") Selection selection, Errors errors, Model model) {
		if (errors.hasErrors()) {
			return "selection";
		}

		log.info("Processing selection: " + selection.getOption());

		if (selection.getOption().equals("Contacts")) {

			var contacts = new Contacts();
			contacts.setCode(selection.getCode());
			model.addAttribute("contacts", contacts);

			return "contacts";
		}

		return "redirect:/";
	}

	@PostMapping("/contacts")
	public String processContacts(@Valid @ModelAttribute("") Contacts contacts, Errors errors, Model model)
			throws JsonMappingException, JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		if (errors.hasErrors()) {
			return "contacts";
		}

		log.info("Processing Contacts: " + contacts);

		var getByCodeHop = Hop.rel("GetDataRequestByCode").withParameter("code", contacts.getCode());

		// determines the existing links for the next POST operation
		var response = traverson.follow(getByCodeHop).toObject(String.class);
		var dataRequest = traverson.follow(getByCodeHop).toObject(DataRequestDto.class);

		var link = discoverer.findLinkWithRel("PostContactsSubmission", response).get();

		// generates the key for data transmission and encrypts it with the public key of the health department
		var kf = KeyFactory.getInstance(KEY_ENCRYPTION_ALGORITHM);
		var keyBytes = Base64.getDecoder().decode(dataRequest.getKeyOfHealthDepartment());
		var publicKey = kf.generatePublic(new X509EncodedKeySpec(keyBytes));

		var secretKey = generateSymmetricKey();
		var encryptedSecretKey = Base64.getEncoder().encodeToString(encryptSecretKey(secretKey, publicKey));

		var content = mapper.writeValueAsString(createContacts(contacts));
		content = encryptContent(content, secretKey);
		postSubmission(link, content, encryptedSecretKey, dataRequest.getKeyReferenz());

		return "redirect:/";
	}

	private ContactPersonList createContacts(@Valid Contacts contacts) {

		ContactPersonList list = new ContactPersonList();

		var contactPerson = new ContactPerson();

		contactPerson.firstName(contacts.getFirstName());
		contactPerson.lastName(contacts.getLastName());

		list.addContactPersonsItem(contactPerson);

		return list;
	}

	/**
	 * Encrypts the content with the given key from data request.
	 */
	private String encryptContent(String content, SecretKey secretKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		var encryptedArray = encryptText(content, secretKey);

		var ret = Base64.getEncoder().encodeToString(encryptedArray);

		log.debug("Text to encrypt:           %s\n\n", content);
		log.debug("Text encrypted:            %s\n\n", ret);

		return ret;
	}

	SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {

		var generator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
		generator.init(ENCRYPTION_KEY_LENGTH);

		return generator.generateKey();
	}

	byte[] encryptText(String textToEncrypt, SecretKey secretKey) throws IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {

		var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		return cipher.doFinal(textToEncrypt.getBytes());
	}

	byte[] encryptSecretKey(SecretKey secretKey, PublicKey publicKey) throws IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {

		var cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.PUBLIC_KEY, publicKey);

		return cipher.doFinal(secretKey.getEncoded());
	}

	/**
	 * Send a POST request for the given data submission to the given Link to the API.
	 */
	private void postSubmission(Link link, String content, String encryptedSecretKey, String keyReferenz) {

		var submission = new DataSubmissionDto().checkCode(determineCheckcode()).keyReferenz(keyReferenz)
				.secret(encryptedSecretKey).encryptedData(content);

		log.info("\nData submission is sent to healt department with key referenz '%s'", keyReferenz);
		log.debug("\nContent of the data submission unencrypted:\n %s", content);
		log.info("\n\n");

		var headers = new HttpHeaders();
		headers.setContentType(new MediaType(APPLICATION_JSON, UTF_8));

		rest.postForObject(link.getHref(), new HttpEntity<>(submission, headers), DataRequestDto.class);
	}

	/**
	 * Determines the check codes from the user data name and date of birth.
	 * 
	 * @return
	 */
	private List<String> determineCheckcode() {

		var ret = new ArrayList<String>();

		var name = properties.getName();
		var dateOfBirth = properties.getDateOfBirth();
		var randomCode = properties.getRandomCode();

		if (name != null) {

			var nameMod = name.toLowerCase().replaceAll("[^\\pL\\pN]", "");
			var nameHash = DigestUtils.md5Hex(nameMod);
			ret.add(nameHash);

			log.debug("\nCheck code for name is MD5 of '%s' = '%s'\n\n", nameMod, nameHash);
		}

		if (dateOfBirth != null) {

			var date = dateOfBirth.getYear() * 10000 + dateOfBirth.getMonthValue() * 100 + dateOfBirth.getDayOfMonth();
			var dateHash = DigestUtils.md5Hex(Integer.toString(date));
			ret.add(dateHash);

			log.debug("\nCheck code for date of birth is MD5 of '%s' = '%s'\n\n", date, dateHash);
		}

		if (randomCode != null) {
			ret.add(randomCode);
		}

		return ret;
	}
}
