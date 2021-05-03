package de.healthIMIS.iris.client.auth.db;

import de.healthIMIS.iris.client.users.entities.UserAccount;
import de.healthIMIS.iris.client.users.UserAccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class InitialAdminLoaderTest {

    @Mock
    private UserAccountsRepository repo;

    @Mock
    private DbAuthProperties conf;

    @Mock
    private PasswordEncoder encoder;

    private InitialAdminLoader loader;

    @BeforeEach
    public void init() {
        loader = new InitialAdminLoader(conf, repo, encoder);
    }

    @Test
    public void shouldCreateAnAdminUserIfItDoesNotExist() {
        // when
        Mockito.when(conf.getAdminUserName()).thenReturn("admin");
        Mockito.when(conf.getAdminUserPassword()).thenReturn("admin");
        Mockito.when(repo.findByUserName(eq("admin"))).thenReturn(Optional.empty());

        // then
        loader.initializeAdmin();

        // assert
        Mockito.verify(repo, Mockito.times(1)).save(any());

    }

    @Test
    public void shouldNotCreateAnAdminUserIfItDoesExist() {
        // when
        Mockito.when(conf.getAdminUserName()).thenReturn("admin");
        Mockito.when(conf.getAdminUserPassword()).thenReturn("admin");

        var existingUser = new UserAccount();
        existingUser.setUserName("admin");

        Mockito.when(repo.findByUserName(eq("admin"))).thenReturn(Optional.of(existingUser));

        // then
        loader.initializeAdmin();

        // assert
        Mockito.verify(repo, Mockito.never()).save(any());

    }

}