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

import static org.cloudbyexample.dc.schema.beans.application.ApplicationType.STANDARD;
import static org.junit.Assert.assertNotNull;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.provision.Provision;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionResponse;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskStatus;
import org.cloudbyexample.dc.schema.beans.provision.ProvisionTaskType;
import org.cloudbyexample.dc.service.application.ApplicationService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.contact.service.AbstractServiceTest;
import org.springbyexample.schema.beans.entity.PkEntityBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests provision service.
 *
 * @author David Winterfeldt
 */
public class ProvisionServiceTest extends AbstractServiceTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ProvisionService service;

//    @Test
//    public void testFindById() {
//        ProvisionResponse response = service.findById(FIRST_ID);
//        Provision provision = response.getResults();
//
//        testProvisionOne(provision);
//    }
//
//    @Test
//    public void testFindByLastName() {
//        ProvisionFindResponse response = service.findByLastName(LAST_NAME);
//        List<Provision> results = response.getResults();
//
//        int expectedCount = 1;
//
//        assertNotNull("Provision list is null.", results);
//        assertEquals("Number of provisions should be " + expectedCount + ".", expectedCount, results.size());
//
//        Provision provision = response.getResults().get(0);
//
//        testProvisionOne(provision);
//    }
//
//    @Test
//    public void testFind() {
//        ProvisionFindResponse response = service.find();
//        assertNotNull("Provision response is null.", response);
//
//        Collection<Provision> provisions = response.getResults();
//
//        assertNotNull("Provision list is null.", provisions);
//        assertEquals("Number of provisions should be " + EXPECTED_COUNT + ".", EXPECTED_COUNT, provisions.size());
//
//        for (Provision provision : provisions) {
//            logger.debug(provision.toString());
//
//            if (FIRST_ID.equals(provision.getId())) {
//                testProvisionOne(provision);
//            } else if (SECOND_ID.equals(provision.getId())) {
//                testProvisionTwo(provision);
//            }
//        }
//    }

    @Test
    public void testCreate() {
        String firstName = "Jack";
        String lastName = "Johnson";

        ApplicationResponse applicationResponse = createApplication();
        assertNotNull("Application response is null.", applicationResponse);
        assertNotNull("Application is null.", applicationResponse.getResults());

        ProvisionResponse response = createProvision(applicationResponse.getResults());
        assertNotNull("Provision response is null.", response);

        Provision provision = response.getResults();

        logger.info("provision '{}'", provision.getName());

//        // test saved provision
//        testProvision(provision,
//                   firstName, lastName);
//
//        ProvisionFindResponse findResponse = service.find();
//        assertNotNull("Provision response is null.", findResponse);
//
//        Collection<Provision> provisions = findResponse.getResults();
//
//        int expectedCount = EXPECTED_COUNT + 1;
//
//        assertNotNull("Provision list is null.", provisions);
//        assertEquals("Number of provisions should be " + expectedCount + ".", expectedCount, provisions.size());
    }

//    @Test
//    public void testUpdate() {
//        ProvisionResponse response = service.findById(FIRST_ID);
//        assertNotNull("Provision response is null.", response);
//
//        Provision provision = response.getResults();
//
//        testProvisionOne(provision);
//
//        String lastName = "Jones";
//        provision.setLastName(lastName);
//
//        service.update(provision);
//
//        response = service.findById(FIRST_ID);
//        assertNotNull("Provision response is null.", response);
//
//        provision = response.getResults();
//
//        testProvisionOne(provision, lastName);
//    }
//
//    @Test
//    public void testDelete() {
//        service.delete(new Provision().withId(FIRST_ID));
//
//        // provision should be null after delete
//        ProvisionResponse response = service.findById(FIRST_ID);
//        assertNotNull("Provision response is null.", response);
//
//        Provision provision = response.getResults();
//
//        assertNull("Provision is not null.", provision);
//    }

    /**
     * Create provision.
     */
    private ProvisionResponse createProvision(Application application) {
        Provision request =
                new Provision()
                    .withName(application.getName())
                    .withApplication(new PkEntityBase().withId(application.getId()))
                    .withProvisionTasks(
                            new ProvisionTask()
                                .withProvisionTaskStatus(ProvisionTaskStatus.SCHEDULED)
                                .withScheduled(new DateTime())
                                .withProvisionTaskType(ProvisionTaskType.CREATE_START));

        ProvisionResponse response = service.create(request);

        return response;
    }

    private ApplicationResponse createApplication() {
        Application application =
                new Application().withName("hello-world").withVersion("1.0").withType(STANDARD)
                    .withImageTemplates(
                        new WebTemplate()
                            .withName("lb")
                            .withFileCopys(new FileCopy()
                                .withName("index.html")
                                .withUrl("http://www.cloudbyexample.org/repo/docker/images/apache-hello-world/index.html")
                                .withDestination("/var/www/html")))
                    .withContainerTemplates(
                        new ContainerTemplate()
                            .withImage("${lb.id}")
                            .withExposePorts(true));

        ApplicationResponse response = applicationService.create(application);

        return response;
    }

//    /**
//     * Tests provision with a PK of one.
//     */
//    private void testProvisionOne(Provision provision) {
//        testProvisionOne(provision, LAST_NAME);
//    }
//
//    /**
//     * Tests provision with a PK of one.
//     */
//    private void testProvisionOne(Provision provision, String lastName) {
//        testProvision(provision,
//                   FIRST_NAME, lastName);
//    }
//
//    /**
//     * Tests provision with a PK of two.
//     */
//    private void testProvisionTwo(Provision provision) {
//        String firstName = "John";
//        String lastName = "Wilson";
//
//        testProvision(provision,
//                   firstName, lastName);
//    }
//
//    /**
//     * Tests provision.
//     */
//    private void testProvision(Provision provision,
//                            String firstName, String lastName) {
//        assertNotNull("Provision is null.", provision);
//
//        assertEquals("firstName", firstName, provision.getFirstName());
//        assertEquals("lastName", lastName, provision.getLastName());
//
//        testAuditable(provision);
//    }

    /**
     * Tests auditable entity.
     */
    private void testAuditable(PkEntityBase auditRecord) {
//        assertNotNull("lastUpdated", auditRecord.getLastUpdated());
//        assertNotNull("lastUpdatedBy", auditRecord.getLastUpdateUser());
//        assertNotNull("created", auditRecord.getCreated());
//        assertNotNull("createdBy", auditRecord.getCreateUser());
    }

}
