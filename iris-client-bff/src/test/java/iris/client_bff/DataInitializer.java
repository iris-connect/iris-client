package iris.client_bff;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * For data initialization on application startup.
 *
 * @author Jens Kutzsche
 */
@Component
public interface DataInitializer {

  @Transactional
  void initialize();
}
