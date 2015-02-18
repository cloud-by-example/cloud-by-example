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

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests sending an e-mail using a velocity template.
 *
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class VelocityEmailSenderIT {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private final Sender sender = null;

    @Autowired
    private final SimpleMailMessage msg = null;

    @Test
    public void testMessage() {
        assertNotNull("VelocityEmailSender is null.", sender);
        assertNotNull("SimpleMailMessage is null.", msg);

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("firstName", "Joe");
        vars.put("lastName", "Smith");

        sender.send(msg, "/email/welcome.vm", vars);
    }

}