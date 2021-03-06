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

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>Provision task stateful job.</p>
 *
 * <p><strong>Note</strong>: Stateful jobs (aka <code>@DisallowConcurrentExecution</code>)
 * do not start again until the current job finishes processing.</p>
 *
 * @author David Winterfeldt
 */
@DisallowConcurrentExecution
public class ProvisionTaskStatefulJob extends QuartzJobBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private ProvisionScheduleProcessor provisionScheduleProcessor = null;

    public void setProvisionScheduleProcessor(ProvisionScheduleProcessor provisionScheduleProcessor) {
        this.provisionScheduleProcessor = provisionScheduleProcessor;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        try {
            provisionScheduleProcessor.process();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new JobExecutionException(e);
        }
    }

}
