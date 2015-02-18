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
package org.cloudbyexample.dc.orm.entity.template;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.cloudbyexample.dc.orm.entity.AbstractUuidEntity;


/**
 * Container template entity
 *
 * @author David Winterfeldt
 */
@Entity
public class ContainerTemplate extends AbstractUuidEntity {

    private static final long serialVersionUID = -744836552997308088L;

    private String image = null;
    private String name = null;
    private boolean exposePorts = false;
    private int clusterSize = 0;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Link> links = null;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExposePorts() {
        return exposePorts;
    }

    public void setExposePorts(boolean exposePorts) {
        this.exposePorts = exposePorts;
    }

    public int getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(int clusterSize) {
        this.clusterSize = clusterSize;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
