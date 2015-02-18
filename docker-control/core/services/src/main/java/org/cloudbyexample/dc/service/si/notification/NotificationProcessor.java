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
package org.cloudbyexample.dc.service.si.notification;

import static org.cloudbyexample.dc.service.si.notification.NotificationTemplateConstants.PROVISION_TEMPLATE;
import static org.cloudbyexample.dc.service.si.provision.ProvisionConstants.PROVISION_HEADER;

import java.util.HashMap;
import java.util.Map;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.notification.Notification;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Container template processor.
 *
 * @author David Winterfeldt
 */
public class NotificationProcessor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final Sender sender;
    private final SimpleMailMessage simpleMailMessageg;

    @Autowired
    public NotificationProcessor(Sender sender, SimpleMailMessage simpleMailMessageg) {
        this.sender = sender;
        this.simpleMailMessageg = simpleMailMessageg;
    }

    public void process(Application application, @Header(PROVISION_HEADER) ProvisionTask task) {
        for (Notification notification : task.getNotifications()) {
            SimpleMailMessage msg = new SimpleMailMessage(simpleMailMessageg);
            msg.setSubject("Docker Control Provision Notification");
            msg.setTo(notification.getEmail());

            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put("application", application);
            vars.put("task", task);

            sender.send(msg, PROVISION_TEMPLATE, vars);

            logger.debug("Sent provision message to '{}' for '{}'.  id={}",
                    notification.getEmail(), application.getName(), task.getId());
        }
    }

}
