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

import java.util.Map;

import org.springframework.mail.SimpleMailMessage;

/**
 * Sends an e-mail message.
 *
 * @author David Winterfeldt
 */
public interface Sender {

    /**
     * Sends e-mail using Velocity template for the body and
     * the properties passed in as Velocity variables.
     *
     * @param   msg                 The e-mail message to be sent, except for the body.
     * @param   templatePath        Path to the velocity template.
     * @param   vars                Variables to use when processing the template.
     */
    public void send(SimpleMailMessage msg, String templatePath, Map<String, Object> vars);

}