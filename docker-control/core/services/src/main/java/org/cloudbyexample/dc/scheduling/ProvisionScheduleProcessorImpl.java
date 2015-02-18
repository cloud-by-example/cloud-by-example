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
package org.cloudbyexample.dc.scheduling;

import static org.cloudbyexample.dc.service.util.SecurityContextUtil.createSystemUser;

import java.io.Serializable;

import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskFindResponse;
import org.cloudbyexample.dc.service.provision.ProvisionTaskService;
import org.cloudbyexample.dc.service.si.provision.ProvisionFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Processes a scheduled provision event implementation.
 *
 * @author David Winterfeldt
 */
@Component("provisionScheduleProcessor")
public class ProvisionScheduleProcessorImpl implements ProvisionScheduleProcessor, Serializable {

    private static final long serialVersionUID = -117074038306083924L;

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProvisionFlow flow;
    private final ProvisionTaskService provisionTaskService;

    @Autowired
    public ProvisionScheduleProcessorImpl(ProvisionFlow flow, ProvisionTaskService provisionTaskService) {
        this.flow = flow;
        this.provisionTaskService = provisionTaskService;
    }

    @Override
    public void process() {
        // FIXME: move to AOP
        createSystemUser();

        logger.trace("Processing provision task.");

        ProvisionTaskFindResponse response = provisionTaskService.findScheduledTasks();

        for (ProvisionTask provisionTask : response.getResults()) {
            logger.info("provision '{}'", provisionTask.getProvisionTaskStatus());

            provisionTaskService.markTaskStarted(provisionTask);

            // FIXME: queue to rabbit
            flow.process(provisionTask);
        }
    }

}
