package iris.client_bff;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile({ "psql_compose_db", "h2_db" })
@Order(100)
class DataInitializerInvoker implements ApplicationRunner {

  private final @NonNull List<DataInitializer> initializers;

  /*
   * (non-Javadoc)
   * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
   */
  @Override
  public void run(@Nullable ApplicationArguments args) throws Exception {
	initializers.stream()
		.peek(it -> log.info("Data initialization for " + AopUtils.getTargetClass(it)))
		.forEach(DataInitializer::initialize);
  }
}
