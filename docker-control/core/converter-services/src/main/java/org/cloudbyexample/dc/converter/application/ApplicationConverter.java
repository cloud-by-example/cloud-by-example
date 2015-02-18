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
package org.cloudbyexample.dc.converter.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cloudbyexample.dc.converter.template.ImageTemplateConverter;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.dozer.Mapper;
import org.springbyexample.converter.AbstractMapperListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Application converter.
 *
 * @author David Winterfeldt
 */
@Component
public class ApplicationConverter extends AbstractMapperListConverter<org.cloudbyexample.dc.orm.entity.application.Application, Application> {

    private final ImageTemplateConverter imageTemplateConverter;

    @Autowired
    public ApplicationConverter(Mapper mapper, ImageTemplateConverter imageTemplateConverter) {
        super(mapper,
                org.cloudbyexample.dc.orm.entity.application.Application.class, Application.class);

        this.imageTemplateConverter = imageTemplateConverter;
    }

    @Override
    public Application convertTo(org.cloudbyexample.dc.orm.entity.application.Application source) {
        Application  application = super.convertTo(source);

        // handles ImageTemplate subclasses
        List<ImageTemplate> imageTemplates =
                imageTemplateConverter.convertListTo(source.getImageTemplates());
        application.setImageTemplates(imageTemplates);

        return application;
    }

    @Override
    public org.cloudbyexample.dc.orm.entity.application.Application convertFrom(Application source) {
        org.cloudbyexample.dc.orm.entity.application.Application application = super.convertFrom(source);

        // handles ImageTemplate subclasses
        Set<org.cloudbyexample.dc.orm.entity.template.ImageTemplate> imageTemplates =
                new HashSet<>(imageTemplateConverter.convertListFrom(source.getImageTemplates()));
        application.setImageTemplates(imageTemplates);

        return application;
    }

}
