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
package org.cloudbyexample.dc.orm.repository.provision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.cloudbyexample.dc.orm.entity.provision.Provision;
import org.cloudbyexample.dc.orm.entity.provision.ProvisionTask;
import org.cloudbyexample.dc.orm.entity.provision.ProvisionTaskStatus;
import org.cloudbyexample.dc.orm.entity.provision.ProvisionTaskType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.orm.repository.AbstractRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * Tests application repository.
 *
 * @author David Winterfeldt
 */
public class ProvisionTaskRepositoryTest extends AbstractRepositoryTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProvisionRepository provisionRepository;

    @Autowired
    private ProvisionTaskRepository repository;

    private DateTime currentTime = new DateTime();
    private int page = 0;
    private int pageSize = 20;


    @Test
    public void testFindScheduledTasks() {
        List<ProvisionTask> scheduledTasks = repository.findScheduledTasks(currentTime, new PageRequest(page, pageSize));

        assertNotNull(scheduledTasks);

        int expectedSize = 0;
        assertEquals(expectedSize, scheduledTasks.size());

        provisionRepository.saveAndFlush(createProvision(ProvisionTaskStatus.SCHEDULED, null));
        provisionRepository.saveAndFlush(createProvision(ProvisionTaskStatus.SCHEDULED, currentTime));
        provisionRepository.saveAndFlush(createProvision(ProvisionTaskStatus.QUEUED, currentTime));

        // update time
        currentTime = new DateTime();
        scheduledTasks = repository.findScheduledTasks(currentTime, new PageRequest(page, pageSize));

        assertNotNull(scheduledTasks);

        expectedSize = 1;
        assertEquals(expectedSize, scheduledTasks.size());
    }

    private Provision createProvision(ProvisionTaskStatus status, DateTime started) {
        ProvisionTask task = new ProvisionTask();
        task.setProvisionTaskStatus(status);
        task.setScheduled(currentTime);
        task.setProvisionTaskType(ProvisionTaskType.CREATE_START);
        task.setStarted(started);

        Provision provision = new Provision();
        provision.setName("test");
        provision.setProvisionTasks(Collections.singletonList(task));

        return provision;
    }

}
