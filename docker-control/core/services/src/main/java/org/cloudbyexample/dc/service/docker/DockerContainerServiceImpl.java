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
package org.cloudbyexample.dc.service.docker;

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.springbyexample.service.AbstractService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Docker container service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class DockerContainerServiceImpl extends AbstractService
        implements DockerContainerService {

    private final AmqpTemplate template;
    private final String routingKey = "dc.test";
    
    @Autowired
    public DockerContainerServiceImpl(AmqpTemplate template,
//            @Value("${agent.queue.name}") String routingKey, 
            MessageHelper messageHelper) {
        super(messageHelper);

//        this.routingKey = routingKey;
        this.template = template;
    }
    
    @Override
    public DockerContainerFindResponse find() {
        DockerContainerFindRequest request = new DockerContainerFindRequest();
        
        DockerContainerFindResponse response = (DockerContainerFindResponse) template.convertSendAndReceive(routingKey, request);
        
        return response;
    }
    
    @Override
    public DockerContainerResponse createAndStart(DockerContainerRequest request) {
        return processContainerRequest(request);        
    }

    @Override
    public DockerContainerResponse start(DockerContainerRequest request) {
        return processContainerRequest(request);
    }

    @Override
    public DockerContainerResponse stop(DockerContainerRequest request) {
        return processContainerRequest(request);
    }

    @Override
    public DockerContainerResponse remove(DockerContainerRequest request) {
        return processContainerRequest(request);
    }

    @Override
    public DockerContainerResponse commit(DockerContainerRequest request) {
        return processContainerRequest(request);
    }

    /**
     * Send request to agent and get response.
     */
    private DockerContainerResponse processContainerRequest(DockerContainerRequest request) {
        Assert.notNull(request);
        
        DockerContainerResponse response = (DockerContainerResponse) template.convertSendAndReceive(routingKey, request);
        
        return response;                
    }
    
}
