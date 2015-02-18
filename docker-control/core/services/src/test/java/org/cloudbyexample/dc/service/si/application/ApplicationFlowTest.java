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

import static org.cloudbyexample.dc.schema.beans.application.ApplicationType.STANDARD;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the application flow.
 *
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ApplicationFlowTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final int count = 1; //00 * 1000;

    @Autowired
    private ApplicationFlow applicationFlow = null;

    @Test
    public void testApplicationFlow() {
        long start = System.nanoTime();

        Application application = createApplication();

        applicationFlow.process(application);

        long end = System.nanoTime();

        // waiting for SI threads to end
        try { Thread.sleep(6000); } catch (InterruptedException e) {}

        logger.info("Application flow test took {}ns", (end - start)/count);
    }

    private Application createApplication() {
        Application application =
                new Application().withName("contact").withVersion("1.0").withType(STANDARD)
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