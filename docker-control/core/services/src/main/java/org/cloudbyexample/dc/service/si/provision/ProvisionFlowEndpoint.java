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
package org.cloudbyexample.dc.service.si.provision;

import static org.cloudbyexample.dc.service.si.provision.ProvisionConstants.PROVISION_HEADER;

import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;

/**
 * The endpoint for the provision flow.
 *
 * @author David Winterfeldt
 */
public class ProvisionFlowEndpoint {

    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Process a provision.
     */
	public void processMessage(@Header(PROVISION_HEADER) ProvisionTask task) {

	    // FIXME: mark task ended

        logger.debug("Processed task. id={} scheduled={}",
                new Object[] { task.getId(),
                               task.getScheduled() });
	}

}
