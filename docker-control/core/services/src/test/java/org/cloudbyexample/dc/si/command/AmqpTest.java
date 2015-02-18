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
package org.cloudbyexample.dc.si.command;

import static org.junit.Assert.assertNotNull;

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Ignore
public class AmqpTest {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    //private final static String exchangeKey = "dc.command";
    private final static String routingKey = "dc.test";

    @Autowired private AmqpAdmin admin;
    @Autowired private AmqpTemplate template;

    @Test
    public void testCommand() {
        DockerContainerFindRequest request = new DockerContainerFindRequest();
        
        DockerContainerFindResponse response = (DockerContainerFindResponse) template.convertSendAndReceive(routingKey, request);
        
        assertNotNull(response);
        assertNotNull(response.getResults());
        
        for (DockerContainer container : response.getResults()) {
            logger.info("id={}  image='{}'", container.getId(), container.getImage());
        }        
    }
        
}
