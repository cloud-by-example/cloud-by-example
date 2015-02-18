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
package org.cloudbyexample.dc.service.provision;

import static org.springbyexample.contact.security.Role.ADMIN;
import static org.springbyexample.contact.security.Role.SYSTEM;
import static org.springbyexample.contact.security.Role.USER;

import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskResponse;
import org.springbyexample.service.PersistenceService;
import org.springframework.security.access.annotation.Secured;


/**
 * Provision task service interface.
 *
 * @author David Winterfeldt
 */
public interface ProvisionTaskService extends PersistenceService<ProvisionTask, ProvisionTaskResponse, ProvisionTaskFindResponse> {

    @Override
    @Secured ({ USER })
    public ProvisionTaskResponse findById(Integer id);

    @Override
    @Secured ({ USER })
    public ProvisionTaskFindResponse find();

    @Override
    @Secured ({ USER })
    public ProvisionTaskFindResponse find(int page, int pageSize);

    @Secured ({ SYSTEM })
    public ProvisionTaskFindResponse findScheduledTasks();

    @Secured ({ SYSTEM })
    public void markTaskStarted(ProvisionTask request);

    @Override
    @Secured ({ USER })
    public ProvisionTaskResponse create(ProvisionTask request);

    @Override
    @Secured ({ USER })
    public ProvisionTaskResponse update(ProvisionTask request);

    @Override
    @Secured ({ ADMIN })
    public ProvisionTaskResponse delete(ProvisionTask request);

}
