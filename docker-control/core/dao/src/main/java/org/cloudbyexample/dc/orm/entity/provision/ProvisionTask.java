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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.cloudbyexample.dc.orm.entity.AbstractUuidEntity;
import org.cloudbyexample.dc.orm.entity.notification.Notification;
import org.joda.time.DateTime;


/**
 * Provision task entity.
 *
 * @author David Winterfeldt
 */
@Entity
public class ProvisionTask extends AbstractUuidEntity {

    private static final long serialVersionUID = 3967785339465508503L;

    @ManyToOne
    private Provision provision;

    private DateTime scheduled;
    private DateTime started;
    private DateTime ended;
    private ProvisionTaskType provisionTaskType = null;
    private ProvisionTaskStatus provisionTaskStatus = null;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<Notification> notifications = null;

    public Provision getProvision() {
        return provision;
    }

    public void setProvision(Provision provision) {
        this.provision = provision;
    }

    public DateTime getScheduled() {
        return scheduled;
    }

    public void setScheduled(DateTime scheduled) {
        this.scheduled = scheduled;
    }

    public DateTime getStarted() {
        return started;
    }

    public void setStarted(DateTime started) {
        this.started = started;
    }

    public DateTime getEnded() {
        return ended;
    }

    public void setEnded(DateTime ended) {
        this.ended = ended;
    }

    public ProvisionTaskType getProvisionTaskType() {
        return provisionTaskType;
    }

    public void setProvisionTaskType(ProvisionTaskType provisionTaskType) {
        this.provisionTaskType = provisionTaskType;
    }

    public ProvisionTaskStatus getProvisionTaskStatus() {
        return provisionTaskStatus;
    }

    public void setProvisionTaskStatus(ProvisionTaskStatus provisionTaskStatus) {
        this.provisionTaskStatus = provisionTaskStatus;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

}
