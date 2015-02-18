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
package org.cloudbyexample.dc.web.client.docker;

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.cloudbyexample.dc.web.service.docker.DockerContainerMarshallingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Docker container client.
 *
 * @author David Winterfeldt
 */
@Component
public class DockerContainerClient implements DockerContainerMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final RestClient client;
    
    @Autowired
    public DockerContainerClient(RestClient client) {
        this.client = client;
    }

    @Override
    public DockerContainerFindResponse find() {
        DockerContainerFindResponse response = null;

        String url = client.createUrl(FIND);

        logger.debug("REST client container find.");

        response = client.getRestTemplate().getForObject(url, DockerContainerFindResponse.class);

        return response;
    }
    
    @Override
    public DockerContainerResponse createAndStart(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        String url = client.createUrl(CREATE_START);

        logger.debug("REST client container create and start.");

        response = client.getRestTemplate().postForObject(url, request, DockerContainerResponse.class);

        return response;        
    }

    @Override
    public DockerContainerResponse start(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        String url = client.createUrl(START);

        logger.debug("REST client container start.");

        response = client.getRestTemplate().postForObject(url, request, DockerContainerResponse.class);

        return response;        
    }

    @Override
    public DockerContainerResponse stop(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        String url = client.createUrl(STOP);

        logger.debug("REST client container stop.");

        response = client.getRestTemplate().postForObject(url, request, DockerContainerResponse.class);

        return response;        
    }

    @Override
    public DockerContainerResponse remove(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        String url = client.createUrl(REMOVE);

        logger.debug("REST client container remove.");

        response = client.getRestTemplate().postForObject(url, request, DockerContainerResponse.class);

        return response;
    }

    @Override
    public DockerContainerResponse commit(DockerContainerRequest request) {
        DockerContainerResponse response = null;

        String url = client.createUrl(REMOVE);

        logger.debug("REST client container commit.");

        response = client.getRestTemplate().postForObject(url, request, DockerContainerResponse.class);

        return response;        
    }

}
