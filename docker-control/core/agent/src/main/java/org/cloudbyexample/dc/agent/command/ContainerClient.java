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

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;

/**
 * Docker Container handler.
 *
 * @author David Winterfeldt
 */
public interface ContainerClient {

    /**
     * Find all containers.
     */
    public DockerContainerFindResponse find();

    /**
     * Create and start a container based on the specified image.
     */
    public DockerContainerResponse createAndStart(DockerContainerRequest request);

    /**
     * Start a container based on the container id.
     */
    public DockerContainerResponse start(DockerContainerRequest request);

    /**
     * Stop a container based on the container id.
     */
    public DockerContainerResponse stop(DockerContainerRequest request);

    /**
     * Remove a container based on the container id.
     */
    public DockerContainerResponse remove(DockerContainerRequest request);

    /**
     * Commit a container based on the container id.
     */
    public DockerContainerResponse commit(DockerContainerRequest request);

}
