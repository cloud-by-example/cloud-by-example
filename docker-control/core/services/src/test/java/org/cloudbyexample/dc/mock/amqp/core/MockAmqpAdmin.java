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
package org.cloudbyexample.dc.mock.amqp.core;

import java.util.Properties;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

/**
 * Mock Amqp Template.
 *
 * @author David Winterfeldt
 */
public class MockAmqpAdmin implements AmqpAdmin {

    @Override
    public void declareExchange(Exchange exchange) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean deleteExchange(String exchangeName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Queue declareQueue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String declareQueue(Queue queue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteQueue(String queueName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        // TODO Auto-generated method stub

    }

    @Override
    public void purgeQueue(String queueName, boolean noWait) {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareBinding(Binding binding) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeBinding(Binding binding) {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getQueueProperties(String queueName) {
        // TODO Auto-generated method stub
        return null;
    }

}
