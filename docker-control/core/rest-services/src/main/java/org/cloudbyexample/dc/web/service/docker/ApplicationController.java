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
package org.cloudbyexample.dc.web.service.docker;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationFindResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.service.application.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.web.service.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Application form controller.
 *
 * @author David Winterfeldt
 */
@Controller
public class ApplicationController extends AbstractController<Application, ApplicationResponse, ApplicationFindResponse>
        implements ApplicationMarshallingService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ApplicationController(ApplicationService service) {
        super(service);
    }

    @Override
    @RequestMapping(value = FIND_BY_ID_REQUEST, method = RequestMethod.GET)
    public ApplicationResponse findById(@PathVariable(ID_VAR) Integer id) {
        logger.info("Find Application.  id={}", id);

        return service.findById((int)id);
    }

    @Override
    @RequestMapping(value = FIND_PAGINATED_REQUEST, method = RequestMethod.GET)
    public ApplicationFindResponse find(@PathVariable(PAGE_VAR) int page,
                                   @PathVariable(PAGE_SIZE_VAR) int pageSize) {
        logger.info("Find application page.  page={}  pageSize={}", page, pageSize);

        return service.find(page, pageSize);
    }

    @Override
    @RequestMapping(value = FIND_REQUEST, method = RequestMethod.GET)
    public ApplicationFindResponse find() {
        logger.info("Find all applications.");

        return service.find();
    }

    @Override
    @RequestMapping(value = SAVE_REQUEST, method = RequestMethod.POST)
    public ApplicationResponse create(@RequestBody Application request) {
        Assert.isTrue(!isPrimaryKeyValid(request), "Create should not have a valid primary key.");

        logger.info("Save application.  id={}", request.getId());

        return service.create(request);
    }

    @Override
    @RequestMapping(value = UPDATE_REQUEST, method = RequestMethod.PUT)
    public ApplicationResponse update(@RequestBody Application request) {
        Assert.isTrue(isPrimaryKeyValid(request), "Update should have a valid primary key.");

        logger.info("Update application.  id={}", request.getId());

        return service.update(request);
    }

    @Override
    @RequestMapping(value = DELETE_PK_REQUEST, method = RequestMethod.DELETE)
    public ApplicationResponse delete(@PathVariable(ID_VAR) Integer id) {
        logger.info("Delete application.  id={}", id);

        return service.delete(new Application().withId(id));
    }

    @Override
    @RequestMapping(value = DELETE_REQUEST, method = RequestMethod.DELETE)
    public ApplicationResponse delete(@RequestBody Application request) {
        Assert.isTrue((request.getId() > 0), "Delete should have a valid primary key");

        int id = request.getId();

        return delete(id);
    }

}
