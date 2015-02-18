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

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.cloudbyexample.dc.web.service.docker.DockerImageMarshallingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Docker image client.
 *
 * @author David Winterfeldt
 */
@Component
public class DockerImageClient implements DockerImageMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final RestClient client;
    
    @Autowired
    public DockerImageClient(RestClient client) {
        this.client = client;
    }

    @Override
    public DockerImageFindResponse find() {
        DockerImageFindResponse response = null;

        String url = client.createUrl(FIND);

        logger.debug("REST client image find.");

        response = client.getRestTemplate().getForObject(url, DockerImageFindResponse.class);

        return response;
    }
    
    @Override
    public DockerImageResponse build(DockerImageRequest request) {
        DockerImageResponse response = null;

        String url = client.createUrl(BUILD);

        logger.debug("REST client build image.");

        response = client.getRestTemplate().postForObject(url, request, DockerImageResponse.class);

        return response;        
    }

}
