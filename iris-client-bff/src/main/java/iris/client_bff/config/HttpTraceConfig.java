package iris.client_bff.config;

import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration(proxyBeanMethods = false)
public class HttpTraceConfig {

	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

	/**
	 * Without correcting the order, the call to <code>/login</code> will not be recorded. There is then no data available
	 * to analyze the case of many repeated login attempts.
	 */
	@Bean
	HttpTraceFilter httpTraceFilter(HttpTraceRepository repository, HttpExchangeTracer tracer) {

		var httpTraceFilter = new HttpTraceFilter(repository, tracer);
		httpTraceFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);

		return httpTraceFilter;
	}
}
