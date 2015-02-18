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
package org.cloudbyexample.dc.web.service.docker;

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.cloudbyexample.dc.service.docker.DockerImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Docker image controller.
 *
 * @author David Winterfeldt
 */
@Controller
public class DockerImageController implements DockerImageMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final DockerImageService service;
    
    @Autowired
    public DockerImageController(DockerImageService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(value = FIND, method = RequestMethod.GET)
    public DockerImageFindResponse find() {
        logger.info("Find images");

        DockerImageFindResponse response = service.find();
        
        return response;
    }

    @Override
    @RequestMapping(value = BUILD, method = RequestMethod.POST)
    public DockerImageResponse build(@RequestBody DockerImageRequest request) {
        logger.info("Build image.  repoTags='{}'", request.getRequest().getRepoTags());
        
        DockerImageResponse response = service.build(request);
        
        return response;
    }

}
