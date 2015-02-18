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
package org.cloudbyexample.dc.scheduling;

import static org.cloudbyexample.dc.service.util.SecurityContextUtil.createSystemUser;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.cloudbyexample.dc.schema.beans.container.Container;
import org.cloudbyexample.dc.schema.beans.container.Link;
import org.cloudbyexample.dc.schema.beans.container.PortBindings;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.service.container.ContainerService;
import org.cloudbyexample.dc.service.docker.DockerContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processes a scheduled status event implementation.
 *
 * @author David Winterfeldt
 */
@Component("statusScheduleProcessor")
public class StatusScheduleProcessorImpl implements StatusScheduleProcessor, Serializable {

    private static final long serialVersionUID = -9132744305923171636L;

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final DockerContainerService dockerContainerService;
    private final ContainerService containerService;

    @Autowired
    public StatusScheduleProcessorImpl(DockerContainerService dockerContainerService, ContainerService containerService) {
        this.dockerContainerService = dockerContainerService;
        this.containerService = containerService;
    }

    @Override
    @Transactional
    public void process() {
        // FIXME: move to AOP
        createSystemUser();

        logger.trace("Processing provision task.");

        DockerContainerFindResponse dockerResponse = dockerContainerService.find();
        Set<String> exists = new HashSet<>();

        // long running processes like a build can cause a timeout
        if (dockerResponse != null) {
            for (DockerContainer dockerContainer : dockerResponse.getResults()) {
            // for debug
//            StringBuilder sb = new StringBuilder();
//
//            sb.append(dockerContainer.getId());
//            sb.append(" ");
//            sb.append(dockerContainer.getImage());
//            sb.append(" ");
//
//            for (org.cloudbyexample.dc.schema.beans.docker.container.PortBindings port : dockerContainer.getPorts()) {
//                sb.append("  ");
//                sb.append(port.getPublicPort());
//                sb.append("->");
//                sb.append(port.getPrivatePort());
//                sb.append("/tcp");
//            }
//
//            logger.info("docker container: '{}'", sb.toString());

                String name = dockerContainer.getId();

                exists.add(name);

                Container container = new Container()
                    .withName(name)
                    .withImage(dockerContainer.getImage())
                    .withSizeRootFs(dockerContainer.getSizeRootFs())
                    .withSizeRw(dockerContainer.getSizeRw());

                for (org.cloudbyexample.dc.schema.beans.docker.container.Link dockerLink : dockerContainer.getLinks()) {
                    container.withLinks(new Link()
                        .withName(dockerLink.getName())
                        .withAlias(dockerLink.getAlias()));
                }

                for (org.cloudbyexample.dc.schema.beans.docker.container.PortBindings port : dockerContainer.getPorts()) {
                    container.withPorts(new PortBindings()
                        .withPrivatePort(port.getPrivatePort())
                        .withPublicPort(port.getPublicPort())
                        .withType(port.getType()));
                }

//                if (containerService.findByName(name).getResults() == null) {
//                    containerService.create(container);
//                } else {
//                    // FIXME: update status
//                }
            }

//            ContainerFindResponse response = containerService.find();
//
//            for (Container container : response.getResults()) {
//                String key = container.getName();
//
//                if (!exists.contains(key)) {
//                    container.setInactive(true);
//
//                    containerService.update(container);
//                }
//            }
        }
    }

}
