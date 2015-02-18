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
package org.cloudbyexample.dc.converter.template;

import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.dozer.Mapper;
import org.springbyexample.converter.AbstractMapperListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Web template converter.
 *
 * @author David Winterfeldt
 */
@Component
public class WebTemplateConverter extends AbstractMapperListConverter<org.cloudbyexample.dc.orm.entity.template.WebTemplate, WebTemplate> {

    @Autowired
    public WebTemplateConverter(Mapper mapper) {
        super(mapper,
                org.cloudbyexample.dc.orm.entity.template.WebTemplate.class, WebTemplate.class);
    }

}
