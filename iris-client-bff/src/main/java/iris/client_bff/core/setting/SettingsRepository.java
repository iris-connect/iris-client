package iris.client_bff.core.setting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Setting, Setting.Name> {}
