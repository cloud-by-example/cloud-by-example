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

import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.dozer.Mapper;
import org.springbyexample.converter.AbstractMapperListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Image template converter.
 *
 * @author David Winterfeldt
 */
@Component
public class ImageTemplateConverter extends AbstractMapperListConverter<org.cloudbyexample.dc.orm.entity.template.ImageTemplate, ImageTemplate> {

    private final DbTemplateConverter dbTemplateConverter;
    private final WebappTemplateConverter webappTemplateConverter;
    private final WebTemplateConverter webTemplateConverter;

    @Autowired
    public ImageTemplateConverter(Mapper mapper, DbTemplateConverter dbTemplateConverter,
            WebappTemplateConverter webappTemplateConverter, WebTemplateConverter webTemplateConverter) {
        super(mapper,
                org.cloudbyexample.dc.orm.entity.template.ImageTemplate.class, ImageTemplate.class);

        this.dbTemplateConverter = dbTemplateConverter;
        this.webappTemplateConverter = webappTemplateConverter;
        this.webTemplateConverter = webTemplateConverter;
    }

    @Override
    public ImageTemplate convertTo(org.cloudbyexample.dc.orm.entity.template.ImageTemplate source) {
        ImageTemplate template = null;

        if (source instanceof org.cloudbyexample.dc.orm.entity.template.DbTemplate) {
            template = dbTemplateConverter.convertTo((org.cloudbyexample.dc.orm.entity.template.DbTemplate) source);
        } else if (source instanceof org.cloudbyexample.dc.orm.entity.template.WebappTemplate) {
            template = webappTemplateConverter.convertTo((org.cloudbyexample.dc.orm.entity.template.WebappTemplate) source);
        } else if (source instanceof org.cloudbyexample.dc.orm.entity.template.WebTemplate) {
            template = webTemplateConverter.convertTo((org.cloudbyexample.dc.orm.entity.template.WebTemplate) source);
        } else {
            template = super.convertTo(source);
        }

        return template;
    }

    @Override
    public org.cloudbyexample.dc.orm.entity.template.ImageTemplate convertFrom(ImageTemplate source) {
        org.cloudbyexample.dc.orm.entity.template.ImageTemplate template = null;

        if (source instanceof DbTemplate) {
            template = dbTemplateConverter.convertFrom((DbTemplate) source);
        } else if (source instanceof WebappTemplate) {
            template = webappTemplateConverter.convertFrom((WebappTemplate) source);
        } else if (source instanceof WebTemplate) {
            template = webTemplateConverter.convertFrom((WebTemplate) source);
        } else {
            template = super.convertFrom(source);
        }

        return template;
    }

}
