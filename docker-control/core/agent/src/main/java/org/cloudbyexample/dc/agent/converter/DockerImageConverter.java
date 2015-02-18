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
package org.cloudbyexample.dc.agent.converter;

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImage;
import org.dozer.Mapper;
import org.springbyexample.converter.AbstractMapperListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Docker image converter.
 *
 * @author David Winterfeldt
 */
@Component
public class DockerImageConverter extends AbstractMapperListConverter<com.github.dockerjava.api.model.Image, DockerImage> {

    @Autowired
    public DockerImageConverter(Mapper mapper) {
        super(mapper,
                com.github.dockerjava.api.model.Image.class, DockerImage.class);
    }

}
