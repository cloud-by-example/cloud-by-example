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
package org.cloudbyexample.dc.service.si.provision;

import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.CREATE_START;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.converter.application.ApplicationConverter;
import org.cloudbyexample.dc.orm.repository.provision.ProvisionTaskRepository;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.service.docker.DockerContainerService;
import org.cloudbyexample.dc.service.util.SecurityContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Container template processor.
 *
 * @author David Winterfeldt
 */
public class ContainerTemplateProcessor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final DockerContainerService service;

    // FIXME: go through a service
    private final ProvisionTaskRepository provisionTaskRepository;
    private final ApplicationConverter applicationConverter;

    @Autowired
    public ContainerTemplateProcessor(DockerContainerService service,
            ProvisionTaskRepository provisionTaskRepository, ApplicationConverter applicationConverter) {
        this.service = service;
        this.provisionTaskRepository = provisionTaskRepository;
        this.applicationConverter = applicationConverter;
    }

    @Transactional(readOnly=true)
    public Application process(ProvisionTask task) {
        // FIXME: use services instead of repositories directly
        org.cloudbyexample.dc.orm.entity.application.Application bean =
                provisionTaskRepository.findOne(task.getId()).getProvision().getApplication();

        Application application = applicationConverter.convertTo(bean);

        String previousImage = null;

        for (ContainerTemplate containerTemplate : application.getContainerTemplates()) {

            // FIXME: HACK (previousImage) to get around contact-two-tier has 4 of each container?
            if (previousImage == null || !containerTemplate.getImage().equals(previousImage)) {
                int clusterSize = (containerTemplate.getClusterSize() > 1 ? containerTemplate.getClusterSize() : 1);
                boolean clustered = (clusterSize > 1);

                // FIXME: ACL/policy to limit cluster size
                for (int i = 0; i < clusterSize; i++) {
                    process(containerTemplate, clustered, i);
                }

                previousImage = containerTemplate.getImage();
            }
        }

        return application;
    }

    private void process(ContainerTemplate containerTemplate, boolean clustered, int count) {
        SecurityContextUtil.createSystemUser();

        logger.info("Processing container template.  image='{}'", containerTemplate.getImage());

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(CREATE_START);

        String name = containerTemplate.getName();
        if (clustered && StringUtils.isNotBlank(name)) {
            name += "-" + count;
        }

        DockerContainer dockerContainer = new DockerContainer()
            .withImage(containerTemplate.getImage())
            .withName(name)
            .withExposePorts(containerTemplate.isExposePorts());

        for (Link link : containerTemplate.getLinks()) {
            dockerContainer = dockerContainer.withLinks(
                    new Link().withName(link.getName()).withAlias(link.getAlias()));
        }

        request.withRequest(dockerContainer);

        service.createAndStart(request);

        logger.info("Called create & start for image '{}'.", containerTemplate.getImage());
    }

}
