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

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.service.AbstractService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Image service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class DockerImageServiceImpl extends AbstractService
        implements DockerImageService {

    private static final String IMAGE_BUILD_MSG = "image.build.msg";
    
    private final AmqpTemplate template;
    private final String routingKey;
    
    @Autowired
    public DockerImageServiceImpl(AmqpTemplate template, 
            @Value("${agent.queue.name}") String routingKey, 
            MessageHelper messageHelper) {
        super(messageHelper);
        
        this.template = template;
        this.routingKey = routingKey;
    }
    
    @Override
    public DockerImageFindResponse find() {
        DockerImageFindRequest request = new DockerImageFindRequest();
        
        DockerImageFindResponse response = (DockerImageFindResponse) template.convertSendAndReceive(routingKey, request);
        
        return response;
    }

    @Override
    public DockerImageResponse build(DockerImageRequest request) {
        return processImageRequest(request);        
    }

    /**
     * Send request to agent and get response.
     */
    private DockerImageResponse processImageRequest(DockerImageRequest request) {
        Assert.notNull(request);
        
        template.convertAndSend(routingKey, request);
        
        return new DockerImageResponse()
            .withMessageList(new Message().withMessageType(MessageType.INFO)
                                          .withMessage(getMessage(IMAGE_BUILD_MSG)))
            .withResults(request.getRequest());                
    }
    
}
