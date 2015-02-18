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
package org.cloudbyexample.dc.shell.command;

import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.COMMIT;
import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.CREATE_START;
import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.REMOVE;
import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.START;
import static org.cloudbyexample.dc.schema.beans.docker.container.ContainerCommandType.STOP;
import static org.cloudbyexample.dc.shell.util.ContainerUtil.processPortsAndLinks;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.PortBindings;
import org.cloudbyexample.dc.shell.bean.CommandState;
import org.cloudbyexample.dc.web.client.docker.DockerContainerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Docker container commands.
 *
 * @author David Winterfeldt
 */
@Component
public class DockerContainerCommand implements CommandMarker {

    private static final String CONTAINER_LIST_CMD = "container-list";
    private static final String CONTAINER_LIST_HELP = "List containers.";

    private static final String CONTAINER_CREATE_START_CMD = "container-create-start";
    private static final String CONTAINER_CREATE_START_HELP = "Create and start a container based on the image name.";

    private static final String CONTAINER_START_CMD = "container-start";
    private static final String CONTAINER_START_HELP = "Start a container based on the container id.";

    private static final String CONTAINER_STOP_CMD = "container-stop";
    private static final String CONTAINER_STOP_HELP = "Stop a container based on the container id.";

    private static final String CONTAINER_REMOVE_CMD = "container-remove";
    private static final String CONTAINER_REMOVE_HELP = "Remove a container based on the container id.";

    private static final String CONTAINER_COMMIT_CMD = "container-commit";
    private static final String CONTAINER_COMMIT_HELP = "Commit a container based on the container id.";

    private static final String ID_OPTION = "id";
    private static final String PORTS_OPTION = "ports";
    private static final String LINKS_OPTION = "links";

    private final DockerContainerClient client;
    private final CommandState commandState;

    @Autowired
    public DockerContainerCommand(DockerContainerClient client, CommandState commandState) {
        this.client = client;
        this.commandState = commandState;
    }

    @CliAvailabilityIndicator({ CONTAINER_LIST_CMD, CONTAINER_CREATE_START_CMD, CONTAINER_START_CMD,
            CONTAINER_STOP_CMD, CONTAINER_REMOVE_CMD })
    public boolean isCommandAvailable() {
        return commandState.isCommandAvailable();
    }

    @CliCommand(value = CONTAINER_LIST_CMD, help = CONTAINER_LIST_HELP)
    public String find() {
        DockerContainerFindResponse response = client.find();
        List<DockerContainer> results = response.getResults();

        StringBuilder sb = new StringBuilder();

        for (DockerContainer container : results) {
            sb.append(StringUtils.substring(container.getId(), 0, 10));
            sb.append(" ");
            sb.append(container.getImage());
            sb.append(" ");

            for (PortBindings port : container.getPorts()) {
                sb.append("  ");
                sb.append(port.getPublicPort());
                sb.append("->");
                sb.append(port.getPrivatePort());
                sb.append("/tcp");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    @CliCommand(value = CONTAINER_CREATE_START_CMD, help = CONTAINER_CREATE_START_HELP)
    public String createAndStart(@CliOption(key = { "image" }, mandatory = true,
                                            help = "Image name.") String image,
                                 @CliOption(key = { PORTS_OPTION },
                                            help = "Container ports to expose externally [private-port:public-port]. ex: '80:1080,8080:10080'") String portsParam,
                                 @CliOption(key = { LINKS_OPTION },
                                            help = "Comma delimitted links to bind to [name:alias].  ex: 'postgresql:db") String linksParam) {
        DockerContainer containerRequest = new DockerContainer().withImage(image);
        containerRequest = processPortsAndLinks(containerRequest, portsParam, linksParam);

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(CREATE_START)
            .withRequest(containerRequest);

        DockerContainerResponse response = client.createAndStart(request);
        DockerContainer container = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(container.getId());
        sb.append(" ");
        sb.append(container.getImage());
        sb.append("\n");

        return sb.toString();
    }

    @CliCommand(value = CONTAINER_START_CMD, help = CONTAINER_START_HELP)
    public String start(@CliOption(key = { ID_OPTION }, mandatory = true,
                                   help = "Container id.") String id,
                        @CliOption(key = { PORTS_OPTION },
                                   help = "Container ports to expose externally [private-port:public-port]. ex: '80:1080,8080:10080'") String portsParam,
                        @CliOption(key = { LINKS_OPTION },
                                   help = "Links to bind to [name:alias].  ex: 'postgresql:db") String linksParam) {
        DockerContainer containerRequest = new DockerContainer().withId(id);
        containerRequest = processPortsAndLinks(containerRequest, portsParam, linksParam);

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(START)
            .withRequest(containerRequest);

        DockerContainerResponse response = client.start(request);
        DockerContainer container = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(container.getId());
        sb.append("\n");

        return sb.toString();
    }

    @CliCommand(value = CONTAINER_STOP_CMD, help = CONTAINER_STOP_HELP)
    public String stop(@CliOption(key = { ID_OPTION }, mandatory = true,  help = "Container id.") String id) {
        DockerContainer containerRequest = new DockerContainer().withId(id);

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(STOP)
            .withRequest(containerRequest);

        DockerContainerResponse response = client.stop(request);
        DockerContainer container = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(container.getId());
        sb.append("\n");

        return sb.toString();
    }

    @CliCommand(value = CONTAINER_REMOVE_CMD, help = CONTAINER_REMOVE_HELP)
    public String remove(@CliOption(key = { ID_OPTION }, mandatory = true,  help = "Container id.") String id) {
        DockerContainer containerRequest = new DockerContainer().withId(id);

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(REMOVE)
            .withRequest(containerRequest);

        DockerContainerResponse response = client.remove(request);
        DockerContainer container = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(container.getId());
        sb.append("\n");

        return sb.toString();
    }

    @CliCommand(value = CONTAINER_COMMIT_CMD, help = CONTAINER_COMMIT_HELP)
    public String commit(@CliOption(key = { ID_OPTION }, mandatory = true,  help = "Container id.") String id,
                         @CliOption(key = { "tag" }, mandatory = true,  help = "New image tag.") String tag) {
        DockerContainer containerRequest = new DockerContainer()
            .withId(id).withImage(tag);

        DockerContainerRequest request = new DockerContainerRequest()
            .withCommand(COMMIT)
            .withRequest(containerRequest);

        DockerContainerResponse response = client.commit(request);
        DockerContainer container = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(container.getId());
        sb.append("\n");

        return sb.toString();
    }

}
