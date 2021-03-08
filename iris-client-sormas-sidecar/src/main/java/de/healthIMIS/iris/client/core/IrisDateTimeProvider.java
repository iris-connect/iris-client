/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.core;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Profile;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
		return Optional.of(LocalDateTime.now().plus(delta));
	}

	@ConstructorBinding
	@ConfigurationProperties("date-time")
	@RequiredArgsConstructor
//	@Profile("!prod")
	public static class DateTimeProperties {

		private final Period delta;

		public Period getDelta() {
			return delta != null ? delta : Period.ZERO;
		}
	}
}