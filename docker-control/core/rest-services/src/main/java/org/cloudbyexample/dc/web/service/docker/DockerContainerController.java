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

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.cloudbyexample.dc.service.docker.DockerContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Docker container controller.
 *
 * @author David Winterfeldt
 */
@Controller
public class DockerContainerController implements DockerContainerMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final DockerContainerService service;

    @Autowired
    public DockerContainerController(DockerContainerService service) {
        this.service = service;
    }

    @Override
    @RequestMapping(value = FIND, method = RequestMethod.GET)
    public DockerContainerFindResponse find() {
        logger.info("Find containers");

        DockerContainerFindResponse response = service.find();

        return response;
    }



    @Override
    @RequestMapping(value = CREATE_START, method = RequestMethod.POST)
    public DockerContainerResponse createAndStart(@RequestBody DockerContainerRequest request) {
        logger.info("Create container from image and start.  image='{}'", request.getRequest().getImage());

        DockerContainerResponse response = service.createAndStart(request);

        return response;
    }

    @Override
    @RequestMapping(value = START, method = RequestMethod.POST)
    public DockerContainerResponse start(@RequestBody DockerContainerRequest request) {
        logger.info("Start container from id.  id='{}'", request.getRequest().getId());

        DockerContainerResponse response = service.start(request);

        return response;
    }

    @Override
    @RequestMapping(value = STOP, method = RequestMethod.POST)
    public DockerContainerResponse stop(@RequestBody DockerContainerRequest request) {
        logger.info("Stop container from id.  id='{}'", request.getRequest().getId());

        DockerContainerResponse response = service.stop(request);

        return response;
    }

    @Override
    @RequestMapping(value = REMOVE, method = RequestMethod.POST)
    public DockerContainerResponse remove(@RequestBody DockerContainerRequest request) {
        logger.info("Remove container from id.  id='{}'", request.getRequest().getId());

        DockerContainerResponse response = service.remove(request);

        return response;
    }

    @Override
    @RequestMapping(value = COMMIT, method = RequestMethod.POST)
    public DockerContainerResponse commit(@RequestBody DockerContainerRequest request) {
        logger.info("Commit container from id.  id='{}'", request.getRequest().getId());

        DockerContainerResponse response = service.remove(request);

        return response;
    }

}
