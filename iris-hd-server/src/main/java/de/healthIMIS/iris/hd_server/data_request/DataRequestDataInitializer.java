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
package de.healthIMIS.iris.hd_server.data_request;

import de.healthIMIS.iris.hd_server.core.DataInitializer;
import de.healthIMIS.iris.hd_server.core.DepartmentIdentifier;
import de.healthIMIS.iris.hd_server.core.SormasRefId;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.DataRequestIdentifier;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.Feature;
import de.healthIMIS.iris.hd_server.data_request.DataRequest.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataRequestDataInitializer implements DataInitializer {

	public static final DataRequestIdentifier REQ_ID_1 = DataRequestIdentifier
			.of(UUID.fromString("790b9a69-17f8-4ba7-a8ae-2f7bf34e0b80"));
	static final DataRequestIdentifier REQ_ID_2 = DataRequestIdentifier
			.of(UUID.fromString("2707fd28-9b4f-4140-b80e-d56d9aad831f"));
	static final DataRequestIdentifier REQ_ID_3 = DataRequestIdentifier
			.of(UUID.fromString("3907e730-af89-4944-8e75-fbe6ba60c904"));

	public static final DepartmentIdentifier DEPARTMENT_ID_1 = DepartmentIdentifier
			.of(UUID.fromString("a04d2e43-3d1a-464e-9926-e190ccf2dd03"));
	public static final DepartmentIdentifier DEPARTMENT_ID_2 = DepartmentIdentifier
			.of(UUID.fromString("6afbbe9b-938c-46d7-93e4-7c9e1f737273"));

	static final SormasRefId SORMAS_REF_ID_1 = SormasRefId.of("XBKA26-SUNKKV-UC4NS3-6EHFCCGI");
	static final SormasRefId SORMAS_REF_ID_2 = SormasRefId.of("XWV6EV-XUOBFT-CW2G3B-DFZIKLCU");
	static final SormasRefId SORMAS_REF_ID_3 = SormasRefId.of("T3NRIL-5Z2UFZ-ULHLDI-NYUMSMAI");

	private final DataRequestRepository requests;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		log.debug("Test data: creating data requests …");

		var list = new ArrayList<DataRequest>();

		list.add(new DataRequest(REQ_ID_1, DEPARTMENT_ID_1, SORMAS_REF_ID_1, Instant.now().minus(2, ChronoUnit.DAYS), null,
				"IRIS User ID", "User ID", EnumSet.of(Feature.Contacts), Status.Open));

		list.add(new DataRequest(REQ_ID_2, DEPARTMENT_ID_1, SORMAS_REF_ID_2, Instant.now().minus(4, ChronoUnit.DAYS),
				Instant.now().minus(2, ChronoUnit.DAYS), "IRIS User ID", "User ID",
				EnumSet.of(Feature.Contacts, Feature.Events), Status.Open));

		list.add(new DataRequest(REQ_ID_3, DEPARTMENT_ID_2, SORMAS_REF_ID_3, Instant.now().minus(4, ChronoUnit.DAYS),
				Instant.now().minus(2, ChronoUnit.DAYS), "IRIS User ID", "User ID", EnumSet.of(Feature.Contacts), Status.Open));

		requests.saveAll(list);
	}
}
