/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.dummy_web;

import java.net.InetAddress;
import java.time.LocalDate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Jens Kutzsche
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("iris")
@Getter
public class IrisProperties {

	private final @NonNull InetAddress serverAddress;
	private final @NonNull Integer serverPort;
	/**
	 * Name of the user - is used for check code
	 */
	private String name = "Max Muster";
	/**
	 * Date of birth of the user - is used for check code
	 */
	private LocalDate dateOfBirth = LocalDate.parse("1990-01-01");
	/**
	 * Random Checkcode - is used as fallback
	 */
	private String randomCode = "ABCDEFGHKL";
}
