package iris.client_bff.core.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

	@Id
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Name name;

	@Size(max = 1000)
	private String value;

	private LocalDate savedAt = LocalDate.now();

	public enum Name {
		JWT_SECRET, REFRESH_SECRET;
	}
}
