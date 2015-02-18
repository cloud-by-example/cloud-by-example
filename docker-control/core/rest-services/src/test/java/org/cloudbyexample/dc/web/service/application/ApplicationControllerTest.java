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
package org.cloudbyexample.dc.web.service.application;

import static org.cloudbyexample.dc.schema.beans.application.ApplicationType.STANDARD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationFindResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationType;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.cloudbyexample.dc.web.client.docker.ApplicationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.web.service.AbstractPersistenceContactControllerTest;
import org.springbyexample.mvc.rest.service.PersistenceMarshallingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests application client
 *
 *  [against an embedded REST service
 * with the main Spring context as the parent context].
 *
 * @author David Winterfeldt
 */
public class ApplicationControllerTest extends AbstractPersistenceContactControllerTest<ApplicationResponse, ApplicationFindResponse, Application> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SIMPLE_VERSION = "1.0";
    private static final String SIMPLE_NAME = "apache-test";
    private static final ApplicationType APPLICATION_TYPE = STANDARD;
    private static final String NEW_VERSION = "1.1";

    @Autowired
    private final ApplicationClient client = null;

    public ApplicationControllerTest() {
        super(1, 1);
    }

    @Override
    protected PersistenceMarshallingService<ApplicationResponse, ApplicationFindResponse, Application> getClient() {
        return client;
    }

    @Override
    protected Application generateCreateRequest() {
        return createSimpleApplication();
    }

    @Override
    protected Application generateUpdateRequest(Application request) {
        return request.withVersion(NEW_VERSION);
    }

    @Override
    protected Application generateDeleteRequest() {
        return new Application().withId(id);
    }

    @Override
    protected void verifyRecord(Application record, boolean save, boolean update) {
        assertNotNull("Result is null.", record);

        verifyPrimaryKey(record.getId(), save);

        assertEquals("'name'", SIMPLE_NAME, record.getName());
        assertEquals("'type'", APPLICATION_TYPE, record.getType());

        if (!update) {
            assertEquals("'version'", SIMPLE_VERSION, record.getVersion());
        } else {
            assertEquals("'version'", NEW_VERSION, record.getVersion());
        }

        verifyAuditInfo(record.getLastUpdated(), record.getLastUpdateUser(), record.getCreated(), record.getCreateUser());

        logger.debug("id=" + record.getId() +
                     "  name=" + record.getName() +
                     "  version=" + record.getVersion() +
                     "  type=" + record.getType() +
                     "  lastUpdated=" + record.getLastUpdated() +
                     "  lastUpdateUser=" + record.getLastUpdateUser() +
                     "  created=" + record.getCreated() +
                     "  createUser=" + record.getCreateUser());
    }

    private Application createSimpleApplication() {
        Application simpleApplication =
            new Application().withName(SIMPLE_NAME).withVersion(SIMPLE_VERSION).withType(APPLICATION_TYPE)
                .withImageTemplates(
                        new WebTemplate()
                            .withName("lb")
                            .withFileCopys(new FileCopy()
                                .withName("index.html")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/images/apache-hello-world/index.html")
                                .withDestination("/var/www/html")));

        return simpleApplication;
    }

    private Application createApplication() {
        Application application =
                new Application().withName("contact").withVersion(SIMPLE_VERSION).withType(STANDARD)
                .withImageTemplates(
                        new DbTemplate()
                        .withName("contact-db")
                        .withDbName("contact").withUsername("contact").withPassword("contact")
                        .withSqlFiles(new FileCopy().withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/schema.sql"),
                                new FileCopy().withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/security_schema.sql")),
                                new WebappTemplate()
                        .withName("contact-webapp")
                        .withWar(new FileCopy()
                        .withName("contact.war")
                        .withUrl("http://www.cloudbyexample.org/repo/docker/images/contact-multi-tier-webapp/contact.war"))
                        .withFileCopys(new FileCopy()
                        .withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/contact-app.properties")
                        .withDestination("/usr/local/tomcat"),
                        new FileCopy()
                        .withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/setnev.sh")
                        .withDestination("/usr/local/tomcat/bin")))
                        .withContainerTemplates(
                                new ContainerTemplate()
                                .withImage("${contact-db}"),
                                new ContainerTemplate()
                                .withImage("${contact-webapp}")
                                .withExposePorts(true)
                                .withLinks(new Link().withName("contact-db").withAlias("contact-db")));

        return application;
    }

}
