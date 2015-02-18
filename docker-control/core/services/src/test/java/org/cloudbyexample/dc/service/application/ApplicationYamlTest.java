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

import static org.cloudbyexample.dc.schema.beans.application.ApplicationType.STANDARD;

import java.io.StringWriter;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Application YAML test.
 *
 * @author David Winterfeldt
 */
public class ApplicationYamlTest {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testSimpleWrite() {
        Application simpleApplication =
                new Application().withName("hello-world-1.0").withVersion("1.0").withType(STANDARD)
                    .withImageTemplates(
                            new WebTemplate()
                                .withName("lb")
                                .withFileCopys(new FileCopy()
                                    .withName("index.html")
                                    .withUrl("http://www.cloudbyexample.org/repo/docker/images/apache-hello-world/index.html")
                                    .withDestination("/var/www/html")));

        logger.info("simple=\n{}", convertToYaml(simpleApplication));
    }

    @Test
    public void testWrite() {
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


        logger.info("application=\n{}", convertToYaml(application));
    }

    private String convertToYaml(Application application) {
        Representer representer = new Representer();
        representer.addClassTag(Application.class, new Tag("!application"));
        representer.addClassTag(ImageTemplate.class, new Tag("!image-template"));
        representer.addClassTag(DbTemplate.class, new Tag("!db-template"));
        representer.addClassTag(WebappTemplate.class, new Tag("!webapp-template"));
        representer.addClassTag(WebTemplate.class, new Tag("!web-template"));
//        representer.addClassTag(Wheel.class, Tag.MAP);;

        Constructor constructor = new Constructor(Application.class);//Car.class is root
        TypeDescription td = new TypeDescription(Application.class);
        td.putListPropertyType("imageTemplates", ImageTemplate.class);
//        td.putListPropertyType("imageTemplates", DbTemplate.class);
//        td.putListPropertyType("imageTemplates", WebappTemplate.class);
//        td.putListPropertyType("imageTemplates", WebTemplate.class);
        td.putListPropertyType("containerTemplates", ContainerTemplate.class);
        constructor.addTypeDescription(td);

        Yaml yaml = new Yaml(representer);
        StringWriter writer = new StringWriter();
        yaml.dump(application, writer);

        return writer.toString();
    }

}
