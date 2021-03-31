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
package de.healthIMIS.iris.client.core.sync;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sync_times")
@EqualsAndHashCode(of = "dataType")
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class SyncTimes {

	@Id
	// private int id;
	@Enumerated(EnumType.STRING)
	private DataTypes dataType;
	private Instant lastSync;

	// static SyncTimes of(DataTypes dataType, int lastSync) {
	// return new SyncTimes(dataType.ordinal(), dataType, lastSync);
	// }

	public enum DataTypes {
		Persons, Cases, Contacts, Tasks, Submissions
	}
}
