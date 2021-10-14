package iris.client_bff.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
@ConfigurationProperties("tokens")
@Validated
public class TokenConfig {

    @Value("${tokens.datLength: #{40}}")
    private int datLength = 40;

    @Value("${tokens.catLength: #{40}}")
    private int catLength = 40;

    @Value("${tokens.datSalt: 1ILvLVngzousCAm6r2YmB7qDwX7w5kwD}")
    private String datSalt;

    @Value("${tokens.catSalt: VtXKYoNmvme86V2zrjdWg9NglGQfAAUB}")
    private String catSalt;

}
