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
package de.healthIMIS.iris.hd_server.core;

import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!prod")
@Order(100)
class DataInitializerInvoker implements ApplicationRunner {

	private final @NonNull List<DataInitializer> initializers;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(@Nullable ApplicationArguments args) throws Exception {
		initializers.stream().peek(it -> log.info("Data initialization for " + AopUtils.getTargetClass(it))).forEach(DataInitializer::initialize);
	}
}
