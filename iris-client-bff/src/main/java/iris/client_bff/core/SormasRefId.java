package iris.client_bff.core;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.nio.ByteBuffer;

import javax.persistence.Embeddable;

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
