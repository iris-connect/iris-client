package iris.client_bff.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
public class IrisDateTimeProvider implements DateTimeProvider {

  private DateTimeProperties properties;
  private @Getter @Setter TemporalAmount delta;

  public IrisDateTimeProvider(@Nullable DateTimeProperties properties) {

	this.properties = properties;

	delta = properties != null ? properties.getDelta() : Period.ZERO;
  }

  public void reset() {
	this.delta = properties.getDelta();
  }

  /*
   * (non-Javadoc)
   * @see org.springframework.data.auditing.DateTimeProvider#getNow()
   */
  @Override
  public Optional<TemporalAccessor> getNow() {
	return Optional.of(Instant.now().plus(delta));
  }

  @ConstructorBinding
  @ConfigurationProperties("date-time")
  @RequiredArgsConstructor
  // @Profile("!prod")
  public static class DateTimeProperties {

	private final Period delta;

	public Period getDelta() {
	  return delta != null ? delta : Period.ZERO;
	}
  }
}
