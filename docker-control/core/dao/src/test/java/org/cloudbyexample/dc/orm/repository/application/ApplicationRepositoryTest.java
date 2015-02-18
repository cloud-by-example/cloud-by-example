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
package org.cloudbyexample.dc.orm.repository.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.cloudbyexample.dc.orm.entity.application.Application;
import org.cloudbyexample.dc.orm.entity.application.ApplicationType;
import org.cloudbyexample.dc.orm.entity.template.ContainerTemplate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.orm.repository.AbstractRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests application repository.
 *
 * @author David Winterfeldt
 */
public class ApplicationRepositoryTest extends AbstractRepositoryTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String NAME = "hello-world";
    private static final String VERSION = "1.0";

    @Autowired
    private ApplicationRepository repository;


    @Test
    public void testCreate() {
        createApp();

        Application result = repository.findAll().get(1);

        assertNotNull(result);
        assertEquals(NAME, result.getName());
        assertEquals(VERSION, result.getVersion());
        assertNotNull(result.getUuid());

        assertEquals(1, result.getContainerTemplates().size());
    }

    @Test
    public void testFindStandard() {
        createApp();

        List<Application> results = repository.findStandard();
        assertNotNull(results);

        int expectedSize = 2;
        assertEquals(expectedSize, results.size());
    }

    @Test
    public void testFindOsOrSoftware() {
        createApp();
        createApp(ApplicationType.OS);

        List<Application> results = repository.findOsOrSoftware();
        assertNotNull(results);

        int expectedSize = 1;
        assertEquals(expectedSize, results.size());
    }

    private void createApp() {
        createApp(ApplicationType.STANDARD);
    }

    private void createApp(ApplicationType type) {
        Application application = new Application();
        application.setName(NAME);
        application.setVersion(VERSION);
        application.setType(type);

        List<ContainerTemplate> containerTemplates = new ArrayList<>();
        ContainerTemplate ct = new ContainerTemplate();
        ct.setImage("lb");
        ct.setExposePorts(true);
        containerTemplates.add(ct);

        application.setContainerTemplates(containerTemplates);

        repository.saveAndFlush(application);
    }

}
