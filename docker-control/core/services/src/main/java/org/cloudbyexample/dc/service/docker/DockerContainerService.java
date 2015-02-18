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

import static org.springbyexample.contact.security.Role.ADMIN;
import static org.springbyexample.contact.security.Role.SYSTEM;
import static org.springbyexample.contact.security.Role.USER;

import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerRequest;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainerResponse;
import org.springframework.security.access.annotation.Secured;

/**
 * Docker container service interface.
 *
 * @author David Winterfeldt
 */
public interface DockerContainerService  {

	@Secured({ USER, SYSTEM })
	public DockerContainerFindResponse find();

	// FIXME: SI flow not keeping User context?
//	@Secured({ USER })
	public DockerContainerResponse createAndStart(DockerContainerRequest request);

	@Secured({ USER })
	public DockerContainerResponse start(DockerContainerRequest request);

	@Secured({ USER })
	public DockerContainerResponse stop(DockerContainerRequest request);

	@Secured({ ADMIN })
	public DockerContainerResponse remove(DockerContainerRequest request);

    @Secured({ USER })
    public DockerContainerResponse commit(DockerContainerRequest request);

}
