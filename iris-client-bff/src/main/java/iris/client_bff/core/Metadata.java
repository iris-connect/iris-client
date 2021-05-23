package iris.client_bff.core;

import lombok.Getter;

import java.time.Instant;

import javax.persistence.Embeddable;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Jens Kutzsche
 */
@Getter
@Embeddable
public class Metadata {

  @CreatedDate
  Instant created;
  @LastModifiedDate
  Instant lastModified;
}
