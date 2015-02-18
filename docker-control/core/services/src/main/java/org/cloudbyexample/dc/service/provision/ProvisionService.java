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

import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.springbyexample.service.PersistenceService;
import org.springframework.security.access.annotation.Secured;


/**
 * Provision service interface.
 *
 * @author David Winterfeldt
 */
public interface ProvisionService extends PersistenceService<Provision, ProvisionResponse, ProvisionFindResponse> {

    @Override
    @Secured ({ USER })
    public ProvisionResponse findById(Integer id);

    //FIXME: remove SYSTEM
    @Override
    @Secured ({ USER, SYSTEM })
    public ProvisionFindResponse find();

    @Override
    @Secured ({ USER })
    public ProvisionFindResponse find(int page, int pageSize);

    @Override
    @Secured ({ USER })
    public ProvisionResponse create(Provision request);

    @Override
    @Secured ({ USER })
    public ProvisionResponse update(Provision request);

    /**
     * Provision an application with it's UUID
     * to create &amp; start the container scheduled
     * for the current time.
     */
    @Secured ({ USER })
    public ProvisionResponse provision(String uuid);

    @Override
    @Secured ({ ADMIN })
    public ProvisionResponse delete(Provision request);

}
