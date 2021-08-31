package iris.client_bff.core.alert;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import iris.client_bff.IrisWebIntegrationTest;
import iris.client_bff.MemoryAppender;
import iris.client_bff.core.DummyJsonRpcClient;
import iris.client_bff.core.alert.AlertDto.AlertType;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Jens Kutzsche
 */
@IrisWebIntegrationTest
@RequiredArgsConstructor
class AlertServiceTests {

	final AlertService sut;

	@MockBean
	DummyJsonRpcClient rpcClient;

	@Captor
	ArgumentCaptor<AlertListDto> alertCaptor;

	private MemoryAppender memoryAppender;

	@BeforeEach
	public void setup() {
		Logger logger = (Logger) LoggerFactory.getLogger(AlertService.class);
		memoryAppender = new MemoryAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(memoryAppender);
		memoryAppender.start();
	}

	@Test
	void testMessage() throws Throwable {

		when(rpcClient.invoke(any(), any(), any())).thenReturn("OK");

		sut.createAlertMessage("Title", "Text");

		assertThat(memoryAppender.countEventsForLogger(AlertService.class)).isEqualTo(1);
		assertThat(memoryAppender.contains("Alert: Title - Text", Level.WARN)).isTrue();

		verify(rpcClient).invoke(eq("ls-1.postAlerts"), alertCaptor.capture(), eq(String.class));

		var value = alertCaptor.getValue();
		assertThat(value.alertList).hasSize(1)
				.element(0).extracting("title", "text", "alertType").contains("Title", "Text", AlertType.MESSAGE);
	}

	@Test
	void testTicketAndMessage() throws Throwable {

		when(rpcClient.invoke(any(), any(), any())).thenReturn("OK");

		sut.createAlertTicketAndMessage("Title", "Text");

		assertThat(memoryAppender.countEventsForLogger(AlertService.class)).isEqualTo(1);
		assertThat(memoryAppender.contains("Alert: Title - Text", Level.WARN)).isTrue();

		verify(rpcClient).invoke(eq("ls-1.postAlerts"), alertCaptor.capture(), eq(String.class));

		var value = alertCaptor.getValue();
		assertThat(value.alertList).hasSize(2)
				.extracting("title", "text", "alertType")
				.contains(tuple("Title", "Text", AlertType.MESSAGE),
						tuple("Title", "Text", AlertType.TICKET));
	}
}
