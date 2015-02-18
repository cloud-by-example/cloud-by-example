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
package org.cloudbyexample.dc.shell.command;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.notification.Notification;
import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskStatus;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskType;
import org.cloudbyexample.dc.shell.bean.CommandState;
import org.cloudbyexample.dc.web.client.docker.ProvisionClient;
import org.joda.time.DateTime;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Application commands.
 *
 * @author David Winterfeldt
 */
@Component
public class ProvisionCommand implements CommandMarker {

    private static final String PROVISION_LIST_CMD = "provision-list";
    private static final String PROVISION_LIST_HELP = "List provisions.";

    private static final String PROVISION_CMD = "provision";
    private static final String PROVISION_HELP = "Provision an application.";


    private final ProvisionClient client;
    private final CommandState commandState;

    @Autowired
    public ProvisionCommand(ProvisionClient client, CommandState commandState) {
        this.client = client;
        this.commandState = commandState;
    }

    @CliAvailabilityIndicator({ PROVISION_LIST_CMD, PROVISION_CMD })
    public boolean isCommandAvailable() {
        return true;
    }

    @CliCommand(value = PROVISION_LIST_CMD, help = PROVISION_LIST_HELP)
    public String find() {
        ProvisionFindResponse response = client.find();

        StringBuilder sb = new StringBuilder();

        for (Provision provision : response.getResults()) {
            sb.append(provision.getName());
            sb.append("\n");

            for (ProvisionTask task : provision.getProvisionTasks()) {
                sb.append("    ");
                sb.append("scheduled: ");
                sb.append(task.getScheduled());
                sb.append("status: ");
                sb.append(task.getProvisionTaskStatus());
                sb.append("type: ");
                sb.append(task.getProvisionTaskType());
                sb.append("started: ");
                sb.append(task.getStarted());
                sb.append("ended: ");
                sb.append(task.getEnded());
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    @CliCommand(value = PROVISION_CMD, help = PROVISION_HELP)
    public String provision(@CliOption(key = { "application-id" }, mandatory = true,
                                       help = "Application ID") int applicationId,
                            @CliOption(key = { "email" },
                                       help = "E-mail for notification when provisioning complete.") String email) {
        ProvisionTask task = new ProvisionTask()
            .withProvisionTaskStatus(ProvisionTaskStatus.SCHEDULED)
            .withScheduled(new DateTime())
            .withProvisionTaskType(ProvisionTaskType.CREATE_START);

        Provision request = new Provision()
            .withApplication(new PkEntityBase().withId(applicationId))
            .withProvisionTasks(task);

        if (StringUtils.isNotBlank(email)) {
            task.withNotifications(new Notification().withEmail(email));
        }

        ProvisionResponse response = client.create(request);

        StringBuilder sb = new StringBuilder();

        // FIXME: share
//        if (response.isErrors()) {
            for (Message msg : response.getMessageList()) {
                sb.append(msg.getMessageType());
                sb.append(msg.getMessageType());
                sb.append("/n");
            }
//        }

        return sb.toString();
    }

}
