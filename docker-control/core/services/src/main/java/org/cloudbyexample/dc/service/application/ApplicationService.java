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
package org.cloudbyexample.dc.service.application;

import static org.springbyexample.contact.security.Role.ADMIN;
import static org.springbyexample.contact.security.Role.SYSTEM;
import static org.springbyexample.contact.security.Role.USER;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationFindResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.springbyexample.service.PersistenceService;
import org.springframework.security.access.annotation.Secured;


/**
 * Application service interface.
 *
 * @author David Winterfeldt
 */
public interface ApplicationService extends PersistenceService<Application, ApplicationResponse, ApplicationFindResponse> {

    @Override
    @Secured ({ USER })
    public ApplicationResponse findById(Integer id);

    @Override
    @Secured ({ USER })
    public ApplicationFindResponse find();

    @Override
    @Secured ({ USER })
    public ApplicationFindResponse find(int page, int pageSize);

    @Override
    @Secured ({ USER })
    public ApplicationResponse create(Application request);

    @Override
    @Secured ({ USER })
    public ApplicationResponse update(Application request);

    @Override
    @Secured ({ ADMIN })
    public ApplicationResponse delete(Application request);

    @Secured ({ SYSTEM })
    public void updateImageTemplate(String uuid, String imageId);

}
