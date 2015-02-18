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
package org.cloudbyexample.dc.agent.handler;

import org.cloudbyexample.dc.agent.command.ContainerClient;
import org.cloudbyexample.dc.agent.command.ImageClient;
import org.cloudbyexample.dc.schema.beans.container.ContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Agent message handler.
 * 
 * @author David Winterfeldt
 */
@Component
public class AgentMessageHandler implements ErrorHandler {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final ImageClient imageClient;
    private final ContainerClient containerClient;
    private final String routingKey;
    private final AmqpTemplate template;


    @Autowired
    public AgentMessageHandler(ImageClient imageClient, ContainerClient containerClient,
            @Value("${notification.queue.name}") String routingKey, AmqpTemplate template) {
        this.imageClient = imageClient;
        this.containerClient = containerClient;

        this.routingKey = routingKey;
        this.template = template;
    }

    /**
     * Handle image find request.
     */
    public DockerImageFindResponse handle(DockerImageFindRequest request) {
        DockerImageFindResponse response = null;

        try {
            response = imageClient.find();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        return response;
    }

    /**
     * Handle container find request.
     */
    public DockerContainerFindResponse handle(DockerContainerFindRequest request) {
        DockerContainerFindResponse response = null;

        try {
            response = containerClient.find();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        return response;
    }

    /**
     * Handle image request.
     */
    public void handle(DockerImageRequest request) {
        DockerImageResponse response = null;

        try {

            switch (request.getCommand()) {
                case BUILD:
                    response = imageClient.build(request);

                    // send notification since this is a long running process
                    template.convertAndSend(routingKey, response);

                    break;
                default:
                    throw new IllegalArgumentException("Unsupported command.");
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Handle container request.
     */
    public DockerContainerResponse handle(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        try {
            switch (request.getCommand()) {
                case CREATE_START:
                    response = containerClient.createAndStart(request);
                    break;
                case START:
                    response = containerClient.start(request);
                    break;
                case STOP:
                    response = containerClient.stop(request);
                    break;
                case REMOVE:
                    response = containerClient.remove(request);
                    break;
                case COMMIT:
                    response = containerClient.commit(request);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported command.");
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        return response;
    }

    @Override
    public void handleError(Throwable t) {
        logger.error(t.getMessage());
    }

    public void handle(ContainerRequest request) {
        logger.error("Bad message type, user 'DockerContainerRequest'. '{}'", ContainerRequest.class);
    }
    
}
