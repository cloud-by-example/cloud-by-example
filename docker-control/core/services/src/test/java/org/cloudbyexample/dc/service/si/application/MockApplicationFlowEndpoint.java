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
package org.cloudbyexample.dc.service.si.application;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

/**
 * The endpoint for the application flow.
 *
 * @author David Winterfeldt
 */
public class MockApplicationFlowEndpoint {

    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Process an application.
     */
	public void processMessage(Message<Application> message) {
	    Application application =  message.getPayload();

        logger.debug("Processed application '{}' {}.  imageTemplateSize={}",
                new Object[] { application.getName(),
                               application.getVersion(),
                               application.getImageTemplates().size()});
	}

}
