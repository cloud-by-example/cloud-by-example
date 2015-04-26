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
package org.cloudbyexample.dc.converter.provision;

import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.dozer.Mapper;
import org.springbyexample.converter.AbstractMapperListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Provision converter.
 *
 * @author David Winterfeldt
 */
@Component
public class ProvisionConverter extends AbstractMapperListConverter<org.cloudbyexample.dc.orm.entity.provision.Provision, Provision> {

    @Autowired
    public ProvisionConverter(Mapper mapper) {
        super(mapper,
                org.cloudbyexample.dc.orm.entity.provision.Provision.class, Provision.class);
    }

}
