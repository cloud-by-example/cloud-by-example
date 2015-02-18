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
package org.cloudbyexample.dc.service.application;

import java.util.List;

import org.cloudbyexample.dc.converter.application.ApplicationConverter;
import org.cloudbyexample.dc.orm.repository.application.ApplicationRepository;
import org.cloudbyexample.dc.orm.repository.template.ImageTemplateRepository;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationFindResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.service.si.application.ApplicationFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springbyexample.service.AbstractPersistenceService;
import org.springbyexample.service.util.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Application service implementation.
 *
 * @author David Winterfeldt
 */
@Service
public class ApplicationServiceImpl extends AbstractPersistenceService<org.cloudbyexample.dc.orm.entity.application.Application, Application,
                                                                       ApplicationResponse, ApplicationFindResponse>
        implements ApplicationService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SAVE_MSG = "app.save.msg";

    private final ApplicationFlow flow;
    private final ApplicationVarProcessor varProcessor;

    private final ImageTemplateRepository imageTemplateRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository repository,
                                  ApplicationConverter converter,
                                  ApplicationFlow flow, ApplicationVarProcessor varProcessor,
                                  ImageTemplateRepository imageTemplateRepository,
                                  MessageHelper messageHelper) {
        super(repository, converter, messageHelper);

        this.flow = flow;
        this.varProcessor = varProcessor;

        this.imageTemplateRepository = imageTemplateRepository;
    }

    // FIXME: createDefaultSort() needs to be configurable through abstract constructor
    @Override
    public ApplicationFindResponse find() {
        List<Application> results = converter.convertListTo(repository.findAll(createDefaultSort()));

        return createFindResponse(results);
    }

    // FIXME: createDefaultSort() needs to be configurable through abstract constructor
    @Override
    public ApplicationFindResponse find(int page, int pageSize) {
        Page<org.cloudbyexample.dc.orm.entity.application.Application> pageResults =
                repository.findAll(new PageRequest(page, pageSize, createDefaultSort()));

        List<Application> results = converter.convertListTo(pageResults.getContent());

        return createFindResponse(results, pageResults.getTotalElements());
    }

    @Override
    @Transactional
    public ApplicationResponse create(Application request) {
        String name = request.getName();
        String version = request.getVersion();

        logger.info("Creating application '{}' {}.", name, version);

        varProcessor.processVars(request);

        ApplicationResponse response = super.create(request);

        flow.process(response.getResults());

        return response;
    }

    @Override
    @Transactional
    public void updateImageTemplate(String uuid, String imageId) {
        org.cloudbyexample.dc.orm.entity.template.ImageTemplate bean = imageTemplateRepository.findByUuid(uuid);
        bean.setImageId(imageId);

        imageTemplateRepository.saveAndFlush(bean);
    }

    @Override
    protected ApplicationResponse createSaveResponse(Application result) {
        return new ApplicationResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                    .withMessage(getMessage(SAVE_MSG, new Object[] { result.getName(), result.getVersion()})))
                    .withResults(result);
    }

    @Override
    protected ApplicationResponse createDeleteResponse() {
        return new ApplicationResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessageKey(DELETE_MSG).withMessage(getMessage(DELETE_MSG)));
    }

    @Override
    protected ApplicationResponse createResponse(Application result) {
        return new ApplicationResponse().withMessageList(new Message().withMessageType(MessageType.INFO)
                .withMessage(getMessage(SAVE_MSG, new Object[] { result.getName(), result.getVersion()})))
                .withResults(result);
    }

    @Override
    protected ApplicationFindResponse createFindResponse(List<Application> results) {
        return new ApplicationFindResponse().withResults(results).withCount(results.size());
    }

    @Override
    protected ApplicationFindResponse createFindResponse(List<Application> results, long count) {
        return new ApplicationFindResponse().withResults(results).withCount(count);
    }

    private Sort createDefaultSort() {
        return new Sort("name", "version", "type");
    }

}
