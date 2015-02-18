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
package org.cloudbyexample.dc.service.si.template;

import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_DB_POSTGRES_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_WEBAPP_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_WEB_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.TEMPLATE_KEY;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test velocity templates.
 *
 * @author David Winterfeldt
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class VelocityTemplateTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Template template;

    @Test
    public void testDbTemplate() {
        assertNotNull(template);

        WebTemplate webappTemplate = new WebTemplate()
            .withName("lb")
            .withFileCopys(new FileCopy()
                                .withName("index.html")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/images/apache-hello-world/index.html")
                                .withDestination("/var/www/html"));

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put(TEMPLATE_KEY, webappTemplate);

        template.process(DOCKER_DB_POSTGRES_TEMPLATE, vars);
    }

    @Test
    public void testWebappTemplate() {
        assertNotNull(template);

        WebTemplate webappTemplate = new WebTemplate()
            .withName("contact-webapp")
            .withFileCopys(new FileCopy()
                                .withName("contact.war")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/images/contact-multi-tier-webapp/contact.war")
                                .withDestination("/usr/local/tomcat/webapps/contact")
                                .withArchive(true),
                          new FileCopy()
                                .withName("contact-app.properties")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/contact-app.properties")
                                .withDestination("/usr/local/tomcat"),
                           new FileCopy()
                                .withName("setenv.sh")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/app/contact/setnev.sh")
                                .withDestination("/usr/local/tomcat/bin"));

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put(TEMPLATE_KEY, webappTemplate);

        template.process(DOCKER_WEBAPP_TEMPLATE, vars);
    }

    @Test
    public void testWebTemplate() {
        assertNotNull(template);

        WebTemplate webappTemplate = new WebTemplate()
            .withName("lb")
            .withFileCopys(new FileCopy()
                                .withName("index.html")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/images/apache-hello-world/index.html")
                                .withDestination("/var/www/html"));

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put(TEMPLATE_KEY, webappTemplate);

        template.process(DOCKER_WEB_TEMPLATE, vars);
    }

}