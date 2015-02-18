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
package org.cloudbyexample.dc.orm.entity.application;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.cloudbyexample.dc.orm.entity.AbstractUuidEntity;
import org.cloudbyexample.dc.orm.entity.template.ContainerTemplate;
import org.cloudbyexample.dc.orm.entity.template.ImageTemplate;


/**
 * Application entity.
 *
 * @author David Winterfeldt
 */
@Entity
public class Application extends AbstractUuidEntity {

    private static final long serialVersionUID = -4830802921024521399L;

    private String name = null;
    private String description = null;
    private String version = null;
    private ApplicationType type = ApplicationType.STANDARD;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<ImageTemplate> imageTemplates = null;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<ContainerTemplate> containerTemplates = null;

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
     * Gets version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets application type.
     */
    public ApplicationType getType() {
        return type;
    }

    /**
     * Sets application type.
     */
    public void setType(ApplicationType type) {
        this.type = type;
    }

    /**
     * Gets list of <code>ContainerTemplate</code>s.
     */
    public List<ContainerTemplate> getContainerTemplates() {
        return containerTemplates;
    }

    /**
     * Sets list of <code>ContainerTemplate</code>s.
     */
    public void setContainerTemplates(List<ContainerTemplate> containerTemplates) {
        this.containerTemplates = containerTemplates;
    }

    /**
     * Gets list of <code>ImageTemplate</code>s.
     */
    public Set<ImageTemplate> getImageTemplates() {
        return imageTemplates;
    }

    /**
     * Sets list of <code>ImageTemplate</code>s.
     */
    public void setImageTemplates(Set<ImageTemplate> imageTemplates) {
        this.imageTemplates = imageTemplates;
    }

    /**
     * Find a container template by it's primary key.
     */
    public ContainerTemplate findContainerTemplateById(Integer id) {
        ContainerTemplate result = null;

        if (containerTemplates != null) {
            for (ContainerTemplate containerTemplate : containerTemplates) {
                if (containerTemplate.getId().equals(id)) {
                    result = containerTemplate;

                    break;
                }
            }
        }

        return result;
    }

//    /**
//     * Returns a string representation of the object.
//     */
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(this.getClass().getName() + "-");
//        sb.append("  id=" + getId());
//        sb.append("  firstName=" + firstName);
//        sb.append("  lastName=" + lastName);
//
//        sb.append("  addresses=[");
//
//        if (addresses != null) {
//            for (Address address : addresses) {
//                sb.append(address.toString());
//            }
//        }
//
//        sb.append("]");
//
//        sb.append("  lastUpdated=" + getLastModifiedDate());
//        sb.append("  lastUpdateBy=" + getLastModifiedBy());
//        sb.append("  created=" + getCreatedDate());
//        sb.append("  createdBy=" + getCreatedBy());
//
//        return sb.toString();
//    }

}
