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


/**
 * Docker container marshalling service.
 *
 * @author David Winterfeldt
 */
public interface DockerContainerMarshallingService   {

    public final static String PATH_DELIM = "/";
    public final static String ID_VAR = "id";

    final static String PATH = "/docker/container";

    public final static String FIND = PATH;
    public final static String FIND_BY_ID_REQUEST = PATH + PATH_DELIM + "{" + ID_VAR + "}";
    public final static String CREATE_START = PATH + PATH_DELIM + "createAndStart";
    public final static String START = PATH + PATH_DELIM + "start";
    public final static String STOP = PATH + PATH_DELIM + "stop";
    public final static String REMOVE = PATH + PATH_DELIM + "remove";
    public final static String COMMIT = PATH + PATH_DELIM + "commit";

    public DockerContainerFindResponse find();

    public DockerContainerResponse createAndStart(DockerContainerRequest request);

    public DockerContainerResponse start(DockerContainerRequest request);

    public DockerContainerResponse stop(DockerContainerRequest request);

    public DockerContainerResponse remove(DockerContainerRequest request);

    public DockerContainerResponse commit(DockerContainerRequest request);

}
