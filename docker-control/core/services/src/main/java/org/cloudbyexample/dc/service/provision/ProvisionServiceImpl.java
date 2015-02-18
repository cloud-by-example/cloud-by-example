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

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.converter.provision.ProvisionConverter;
import org.cloudbyexample.dc.orm.repository.application.ApplicationRepository;
import org.cloudbyexample.dc.orm.repository.provision.ProvisionRepository;
import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionFindResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskStatus;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.service.AbstractPersistenceService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * Provision service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class ProvisionServiceImpl extends AbstractPersistenceService<org.cloudbyexample.dc.orm.entity.provision.Provision, Provision,
                                                                     ProvisionResponse, ProvisionFindResponse>
        implements ProvisionService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SAVE_MSG = "provision.save.msg";

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ProvisionServiceImpl(ProvisionRepository repository, ProvisionConverter converter,
                                ApplicationRepository applicationRepository,
                                MessageHelper messageHelper) {
        super(repository, converter, messageHelper);

        this.applicationRepository = applicationRepository;
    }

    // FIXME: createDefaultSort() needs to be configurable through abstract constructor
    @Override
    public ProvisionFindResponse find() {
        List<Provision> results = converter.convertListTo(repository.findAll());
//        List<Provision> results = converter.convertListTo(repository.findAll(createDefaultSort()));

        return createFindResponse(results);
    }

    @Override
    @Transactional
    public ProvisionResponse create(Provision request) {
        Provision result = null;

        // FIXME: problem saving after Application PkEntityBase conversion, app entity created, but not merging with existing record
        // might need the lock version or try upgrading to latest hibernate version
        int applicationId = request.getApplication().getId();
        org.cloudbyexample.dc.orm.entity.application.Application application = applicationRepository.findOne(applicationId);

        // FIXME: if application not found, create error and return

        org.cloudbyexample.dc.orm.entity.provision.Provision convertedRequest = converter.convertFrom(request);
        convertedRequest.setApplication(application);

        if (StringUtils.isBlank(convertedRequest.getName())) {
            convertedRequest.setName(application.getName());
        }

        org.cloudbyexample.dc.orm.entity.provision.Provision bean = repository.saveAndFlush(convertedRequest);

        // FIXME: bi-directional relationships not working the same as with explicit schema
        for (org.cloudbyexample.dc.orm.entity.provision.ProvisionTask task : bean.getProvisionTasks()) {
            task.setProvision(bean);
        }
        bean = repository.saveAndFlush(bean);

        result = converter.convertTo(bean);

        return createSaveResponse(result);

    }

    @Override
    @Transactional
    public ProvisionResponse provision(String uuid) {
        Assert.notNull(uuid, "Application uuid required.");

        org.cloudbyexample.dc.orm.entity.application.Application application = applicationRepository.findByUuid(uuid);
        Assert.notNull(application, "Application with uuid '" + uuid + "' not found.");

        int applicationId = application.getId();


        ProvisionTask task = new ProvisionTask()
            .withProvisionTaskStatus(ProvisionTaskStatus.SCHEDULED)
            .withScheduled(new DateTime())
            .withProvisionTaskType(ProvisionTaskType.CREATE_START);

        Provision request = new Provision()
            .withApplication(new PkEntityBase().withId(applicationId))
            .withProvisionTasks(task);

        return create(request);
    }

    @Override
    protected ProvisionResponse createSaveResponse(Provision result) {
        return new ProvisionResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                    .withMessage(getMessage(SAVE_MSG, new Object[] { result.getName() })))
                    .withResults(result);
    }

    @Override
    protected ProvisionResponse createDeleteResponse() {
        return new ProvisionResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessageKey(DELETE_MSG).withMessage(getMessage(DELETE_MSG)));
    }

    @Override
    protected ProvisionResponse createResponse(Provision result) {
        return new ProvisionResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessage(getMessage(SAVE_MSG, new Object[] { result.getName() })))
                .withResults(result);
    }

    @Override
    protected ProvisionFindResponse createFindResponse(List<Provision> results) {
        return new ProvisionFindResponse().withResults(results).withCount(results.size());
    }

    @Override
    protected ProvisionFindResponse createFindResponse(List<Provision> results, long count) {
        return new ProvisionFindResponse().withResults(results).withCount(count);
    }

}
