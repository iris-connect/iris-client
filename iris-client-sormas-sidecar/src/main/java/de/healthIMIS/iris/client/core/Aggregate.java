/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.healthIMIS.iris.client.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base class of aggregates in the sense of DDD
 * 
 * @author Jens Kutzsche
 */
@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public abstract class Aggregate<T extends Aggregate<T, ID>, ID extends Id> extends AbstractAggregateRoot<T>
		implements Persistable<ID> {

	protected @EmbeddedId ID id;
	private Metadata metadata = new Metadata();
	private @Transient boolean isNew = true;

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}
}
