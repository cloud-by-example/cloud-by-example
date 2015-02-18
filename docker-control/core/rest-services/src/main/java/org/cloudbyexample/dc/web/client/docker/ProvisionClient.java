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

import java.util.Collections;
import java.util.Map;

import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.cloudbyexample.dc.web.service.docker.ProvisionMarshallingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.web.client.AbstractPersistenceClient;
import org.springbyexample.mvc.rest.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


/**
 * Provision client.
 *
 * @author David Winterfeldt
 */
@Component
public class ProvisionClient extends AbstractPersistenceClient<ProvisionResponse, ProvisionFindResponse, Provision>
        implements ProvisionMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ProvisionClient(RestClient client) {
        super(client,
              FIND_BY_ID_REQUEST, FIND_PAGINATED_REQUEST, FIND_REQUEST,
              SAVE_REQUEST, UPDATE_REQUEST, DELETE_PK_REQUEST, DELETE_REQUEST,
              ProvisionResponse.class, ProvisionFindResponse.class);
    }

    @Override
    public ProvisionResponse provision(String uuid) {
        ProvisionResponse response = null;

        String url = client.createUrl(PROVISION_REQUEST);

        logger.debug("REST client provision.  uuid={}  url='{}'", uuid, url);

//        response = client.getRestTemplate().postForObject(url, responseClazz, createUuidVar(uuid));
        Map<String, String> vars = createUuidVar(uuid);
        response = client.getRestTemplate().exchange(url, HttpMethod.POST, new HttpEntity(null), responseClazz, vars).getBody();

        return response;

    }

    /**
     * Create UUID request variable.
     */
    // FIXME: share
    public Map<String, String> createUuidVar(String uuid) {
        return Collections.singletonMap(ID_VAR, uuid);
    }

}
