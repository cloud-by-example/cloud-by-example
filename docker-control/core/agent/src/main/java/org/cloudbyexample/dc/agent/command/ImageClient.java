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

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;

/**
 * Docker Image handler.
 * 
 * @author David Winterfeldt
 */
public interface ImageClient {

    /**
     * Find all containers.
     */
    public DockerImageFindResponse find();
    
    /**
     * Create and start a container based on the specified image.
     */
    public DockerImageResponse build(DockerImageRequest request);
    
}
