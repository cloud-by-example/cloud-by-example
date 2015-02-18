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

import java.util.List;

import org.cloudbyexample.dc.converter.provision.ProvisionTaskConverter;
import org.cloudbyexample.dc.orm.repository.provision.ProvisionTaskRepository;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskResponse;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.service.AbstractPersistenceService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Provision task service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class ProvisionTaskServiceImpl extends AbstractPersistenceService<org.cloudbyexample.dc.orm.entity.provision.ProvisionTask, ProvisionTask,
                                                                         ProvisionTaskResponse, ProvisionTaskFindResponse>
        implements ProvisionTaskService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SAVE_MSG = "provision.task.save.msg";


    @Autowired
    public ProvisionTaskServiceImpl(ProvisionTaskRepository repository,
                                    ProvisionTaskConverter converter, MessageHelper messageHelper) {
        super(repository, converter, messageHelper);
    }

    @Override
    public ProvisionTaskFindResponse findScheduledTasks() {
        DateTime currentTime = new DateTime();
        int page = 0;
        // FIXME: throttle number of requests based on resources available
        int pageSize = 20;


        List<ProvisionTask> results =
                converter.convertListTo(((ProvisionTaskRepository)repository).findScheduledTasks(currentTime,
                        new PageRequest(page, pageSize)));

        return createFindResponse(results);
    }

    @Override
    @Transactional
    public void markTaskStarted(ProvisionTask request) {
        org.cloudbyexample.dc.orm.entity.provision.ProvisionTask bean = repository.findOne(request.getId());

        bean.setProvisionTaskStatus(org.cloudbyexample.dc.orm.entity.provision.ProvisionTaskStatus.QUEUED);
        bean.setStarted(new DateTime());

        repository.saveAndFlush(bean);
    }

    @Override
    protected ProvisionTaskResponse createSaveResponse(ProvisionTask result) {
        return new ProvisionTaskResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                    .withMessage(getMessage(SAVE_MSG, new Object[] { result.getProvisionTaskStatus(), "provision name" })))
                    .withResults(result);
    }

    @Override
    protected ProvisionTaskResponse createDeleteResponse() {
        return new ProvisionTaskResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessageKey(DELETE_MSG).withMessage(getMessage(DELETE_MSG)));
    }

    @Override
    protected ProvisionTaskResponse createResponse(ProvisionTask result) {
        return new ProvisionTaskResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessage(getMessage(SAVE_MSG, new Object[] { result.getProvisionTaskStatus(), "provision name" })))
                .withResults(result);
    }

    @Override
    protected ProvisionTaskFindResponse createFindResponse(List<ProvisionTask> results) {
        return new ProvisionTaskFindResponse().withResults(results).withCount(results.size());
    }

    @Override
    protected ProvisionTaskFindResponse createFindResponse(List<ProvisionTask> results, long count) {
        return new ProvisionTaskFindResponse().withResults(results).withCount(count);
    }

}
