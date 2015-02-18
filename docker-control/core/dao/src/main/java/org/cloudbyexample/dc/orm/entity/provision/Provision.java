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
package org.cloudbyexample.dc.orm.entity.provision;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.cloudbyexample.dc.orm.entity.AbstractUuidEntity;
import org.cloudbyexample.dc.orm.entity.application.Application;


/**
 * Provision entity.
 *
 * @author David Winterfeldt
 */
@Entity
public class Provision extends AbstractUuidEntity {

    private static final long serialVersionUID = 7369180080014383180L;

    private String name = null;
    private String description = null;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Application application = null;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<ProvisionTask> provisionTasks = null;

    /**
     * Gets application name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets application name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets application (just used as a reference).
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Sets application (just used as a reference).
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * Get provision tasks.
     */
    public List<ProvisionTask> getProvisionTasks() {
        return provisionTasks;
    }

    /**
     * Set provision tasks.
     */
    public void setProvisionTasks(List<ProvisionTask> provisionTasks) {
        this.provisionTasks = provisionTasks;
    }

}
