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

import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.springbyexample.contact.mvc.rest.service.PersistenceContactMarshallingService;

/**
 * Provision marshalling service.
 *
 * @author David Winterfeldt
 */
public interface ProvisionMarshallingService extends PersistenceContactMarshallingService<ProvisionResponse, ProvisionFindResponse, Provision> {

    final static String PATH = "/provision";

    public final static String FIND_BY_ID_REQUEST = PATH + PATH_DELIM + "{" + ID_VAR + "}";
    public final static String FIND_PAGINATED_REQUEST = PATH + PAGINATED;
    public final static String FIND_REQUEST = PATH;
    public final static String SAVE_REQUEST = PATH;
    public final static String UPDATE_REQUEST = FIND_BY_ID_REQUEST;
    public final static String PROVISION_REQUEST = FIND_BY_ID_REQUEST;
    public final static String DELETE_PK_REQUEST = FIND_BY_ID_REQUEST;
    public final static String DELETE_REQUEST = PATH;

    /**
     * Provision an application with it's UUID.
     */
    public ProvisionResponse provision(String uuid);

}
