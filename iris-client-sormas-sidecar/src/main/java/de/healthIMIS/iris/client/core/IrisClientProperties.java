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

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Jens Kutzsche
 *
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("iris.client")
@Getter
public class IrisClientProperties {

	private final @NonNull UUID clientId;
	private final @NonNull String departmentName;
	private final @NonNull String rkiCode;

	private Resource keyStore;
	private String keyStorePassword = RandomStringUtils.randomAlphabetic(10);
}
