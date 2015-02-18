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
package org.cloudbyexample.dc.agent.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.converter.container.ContainerConverter;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.PortBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;

/**
 * Docker Container handler implementation.
 *
 * @author David Winterfeldt
 */
@Component
public class ContainerClientImpl implements ContainerClient {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final DockerClient client;
    private final ContainerConverter converter;

    @Autowired
    public ContainerClientImpl(DockerClient client, ContainerConverter converter) {
        this.client = client;
        this.converter = converter;
    }

    @Override
    public DockerContainerFindResponse find() {
        logger.debug("Find containers.");

        List<DockerContainer> results = converter.convertListTo(client.listContainersCmd().exec());

        return new DockerContainerFindResponse().withResults(results);
    }

    @Override
    public DockerContainerResponse createAndStart(DockerContainerRequest containerRequest) {
        logger.debug("Creating container.");

        DockerContainer request = containerRequest.getRequest();
        String image = request.getImage();
        String name = request.getName();

        CreateContainerResponse response = null;

        if (StringUtils.isBlank(name)) {
            response = client.createContainerCmd(image).exec();
        } else {
            response = client.createContainerCmd(image).withName(name).exec();
        }

        String id = response.getId();
        execStartCommand(id, request);

        InspectContainerResponse icc = client.inspectContainerCmd(id).exec();

        // TODO handle state object
        // TODO handle network settings

        request.withId(id).withName(icc.getName());

        return new DockerContainerResponse().withResults(request);
    }
    @Override
    public DockerContainerResponse start(DockerContainerRequest containerRequest) {
        DockerContainer request = containerRequest.getRequest();

        String id = request.getId();
        execStartCommand(id, request);

        return new DockerContainerResponse().withResults(request.withId(id));
    }

    @Override
    public DockerContainerResponse stop(DockerContainerRequest containerRequest) {
        DockerContainer request = containerRequest.getRequest();

        String id = request.getId();

        client.stopContainerCmd(id).exec();

        return new DockerContainerResponse().withResults(request.withId(id));
    }

    @Override
    public DockerContainerResponse remove(DockerContainerRequest containerRequest) {
        DockerContainer request = containerRequest.getRequest();

        String id = request.getId();

        client.removeContainerCmd(id).exec();

        return new DockerContainerResponse().withResults(request.withId(id));
    }

    public DockerContainerResponse commit(DockerContainerRequest containerRequest) {
        DockerContainer request = containerRequest.getRequest();

        String id = request.getId();

        String imageId = client.commitCmd(id)
            .exec();

        // FIXME
//        client.tagImageCmd(imageId, "", request.getId());

        return new DockerContainerResponse().withResults(request.withId(imageId));
    }

    /**
     * <p>Create and exec start command.</p>
     *
     * <p><strong>Note</strong>: If expose ports is <code>true</code>, port bindings is ignored.</p>
     */
    private void execStartCommand(String id, DockerContainer request) {
        StartContainerCmd cmd = client.startContainerCmd(id);

        if (request.isExposePorts()) {
            cmd = cmd.withPublishAllPorts(true);
        } else {
            if (!CollectionUtils.isEmpty(request.getPorts())) {
                Ports ports = new Ports();

                for (PortBindings port : request.getPorts()) {
                    ports.bind(new ExposedPort("tcp", port.getPrivatePort()), new Binding(port.getPublicPort()));
                }

                cmd = cmd.withPortBindings(ports);
            }
        }

        if (!CollectionUtils.isEmpty(request.getLinks())) {
            List<Link> links = new ArrayList<>();

            for (org.cloudbyexample.dc.schema.beans.docker.container.Link link : request.getLinks()) {
                // use name for alias if no alias is set
                String name = link.getName();
                String alias = (StringUtils.isBlank(link.getAlias()) ? name : link.getAlias());

                links.add(new Link(name, alias));
            }

            cmd.withLinks(links.toArray(new Link[] {}));
        }

        cmd.exec();
    }

}
