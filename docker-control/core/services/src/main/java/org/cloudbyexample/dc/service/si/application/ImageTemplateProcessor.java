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

import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_DB_POSTGRES_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_IMAGE_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_WEBAPP_DIR;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_WEBAPP_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.DOCKER_WEB_TEMPLATE;
import static org.cloudbyexample.dc.service.si.application.VelocityTemplateConstants.TEMPLATE_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.File;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageDir;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.ImageCommandType;
import org.cloudbyexample.dc.service.docker.DockerImageService;
import org.cloudbyexample.dc.service.si.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Image template processor.
 *
 * @author David Winterfeldt
 */
public class ImageTemplateProcessor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final DockerImageService service;
    private final Template template;

    @Autowired
    public ImageTemplateProcessor(DockerImageService service, Template template) {
        this.service = service;
        this.template = template;
    }

    public ImageTemplate process(ImageTemplate imageTemplate,
            @Header(ApplicationFlow.APPLICATION) Application application) {
        logger.info("Processing image template '{}'.  {}", imageTemplate.getName(), imageTemplate.getClass());

        DockerImageRequest request = null;

        if (imageTemplate instanceof DbTemplate) {
            DbTemplate dbTemplate = (DbTemplate) imageTemplate;

            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put(TEMPLATE_KEY, imageTemplate);

            // FIXME: extract all velocity related processing behind interface
            String dockerfile = template.process(DOCKER_DB_POSTGRES_TEMPLATE, vars);

            logger.info("Dockerfile: \n{}", dockerfile);

            List<File> files = new ArrayList<>();

            // sql files
            for (FileCopy fileCopy : dbTemplate.getSqlFiles()) {
                files.add(new File().withUrl(fileCopy.getUrl()));
            }

            // files
            for (FileCopy fileCopy : imageTemplate.getFileCopys()) {
                files.add(new File().withUrl(fileCopy.getUrl()));
            }

            request = new DockerImageRequest()
                .withCommand(ImageCommandType.BUILD)
                .withRequest(new DockerImageDir()
                    .withUuid(imageTemplate.getUuid())
                    .withRepoTags(imageTemplate.getTag())
                    .withDockerfile(dockerfile)
                    .withFiles(files));
        } else if (imageTemplate instanceof WebappTemplate) {
            WebappTemplate webappTemplate = (WebappTemplate) imageTemplate;

            // add war file
            FileCopy war = webappTemplate.getWar();
            String context = StringUtils.split(war.getName(), ".")[0];

            imageTemplate.withFileCopys(
                    war.withDestination(DOCKER_WEBAPP_DIR + "/" + context));

            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put(TEMPLATE_KEY, imageTemplate);

            // FIXME: extract all velocity related processing behind interface
            String dockerfile = template.process(DOCKER_WEBAPP_TEMPLATE, vars);

            logger.info("Dockerfile: \n{}", dockerfile);

            List<File> files = new ArrayList<>();

            for (FileCopy fileCopy : imageTemplate.getFileCopys()) {
                files.add(new File().withUrl(fileCopy.getUrl()));
            }

            request = new DockerImageRequest()
                .withCommand(ImageCommandType.BUILD)
                .withRequest(new DockerImageDir()
                    .withUuid(imageTemplate.getUuid())
                    .withRepoTags(imageTemplate.getTag())
                    .withDockerfile(dockerfile)
                    .withFiles(files));
        } else if (imageTemplate instanceof WebTemplate) {
            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put(TEMPLATE_KEY, imageTemplate);

            // FIXME: extract all velocity related processing behind interface
            String dockerfile = template.process(DOCKER_WEB_TEMPLATE, vars);

            logger.info("Dockerfile: \n{}", dockerfile);

            List<File> files = new ArrayList<>();
            for (FileCopy fileCopy : imageTemplate.getFileCopys()) {
                files.add(new File().withUrl(fileCopy.getUrl()));
            }

            request = new DockerImageRequest()
                .withCommand(ImageCommandType.BUILD)
                .withRequest(new DockerImageDir()
                    .withUuid(imageTemplate.getUuid())
                    .withRepoTags(imageTemplate.getTag())
                    .withDockerfile(dockerfile)
                    .withFiles(files));
        } else {
            if (!imageTemplate.getFileCopys().isEmpty()) {
                Map<String, Object> vars = new HashMap<String, Object>();
                vars.put(TEMPLATE_KEY, imageTemplate);

                // FIXME: extract all velocity related processing behind interface
                String dockerfile = template.process(DOCKER_IMAGE_TEMPLATE, vars);

                logger.info("Dockerfile: \n{}", dockerfile);

                List<File> files = new ArrayList<>();
                for (FileCopy fileCopy : imageTemplate.getFileCopys()) {
                    files.add(new File().withUrl(fileCopy.getUrl()));
                }

                request = new DockerImageRequest()
                    .withCommand(ImageCommandType.BUILD)
                    .withRequest(new DockerImageDir()
                        .withUuid(imageTemplate.getUuid())
                        .withRepoTags(imageTemplate.getTag())
                        .withDockerfile(dockerfile)
                        .withFiles(files));
            }
        }

        // image template that represents an image doesn't need any processing
        if (request != null) {
            service.build(request);

            logger.info("Called build for image with tag '{}'.", imageTemplate.getTag());
        }

        return imageTemplate;
    }

}
