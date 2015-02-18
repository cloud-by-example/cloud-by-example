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
package org.cloudbyexample.dc.web.client.docker;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationFindResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.web.service.docker.ApplicationMarshallingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.web.client.AbstractPersistenceClient;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Application client.
 *
 * @author David Winterfeldt
 */
@Component
public class ApplicationClient extends AbstractPersistenceClient<ApplicationResponse, ApplicationFindResponse, Application>
        implements ApplicationMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ApplicationClient(RestClient client) {
        super(client,
              FIND_BY_ID_REQUEST, FIND_PAGINATED_REQUEST, FIND_REQUEST,
              SAVE_REQUEST, UPDATE_REQUEST, DELETE_PK_REQUEST, DELETE_REQUEST,
              ApplicationResponse.class, ApplicationFindResponse.class);
    }

}
