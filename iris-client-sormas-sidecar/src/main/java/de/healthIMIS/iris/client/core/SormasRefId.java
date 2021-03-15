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

import java.io.Serializable;
import java.nio.ByteBuffer;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SormasRefId implements Serializable {

	private static final long serialVersionUID = 6897690346724487232L;

	final String refId;

	public static SormasRefId random() {

		java.util.UUID randomUuid = java.util.UUID.randomUUID();

		long leastSignificantBits = randomUuid.getLeastSignificantBits();
		long mostSignificantBits = randomUuid.getMostSignificantBits();

		byte[] bytes = longToBytes(leastSignificantBits, mostSignificantBits);

		return of(Base32.encode(bytes, 6));
	}

	private static byte[] longToBytes(long x, long y) {

		ByteBuffer buffer = ByteBuffer.allocate(2 * Long.SIZE / 8);
		buffer.putLong(x);
		buffer.putLong(y);
		return buffer.array();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return refId;
	}
}
