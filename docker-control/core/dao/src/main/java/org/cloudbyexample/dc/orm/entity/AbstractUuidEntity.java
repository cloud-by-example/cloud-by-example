/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloudbyexample.dc.orm.entity;

import java.util.UUID;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.springbyexample.contact.orm.entity.AbstractVersionedEntity;

/**
 * Abstract UUID column with auditable and optimistic lock enabled entity.
 *
 * @author David Winterfeldt
 */
@MappedSuperclass
@SuppressWarnings("serial")
public class AbstractUuidEntity extends AbstractVersionedEntity {

    private String uuid = null;
    private boolean inactive;

    @PrePersist
    public void updateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

}
