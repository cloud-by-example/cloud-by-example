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
package org.cloudbyexample.dc.service.si.handler;

import static org.cloudbyexample.dc.service.util.SecurityContextUtil.createSystemUser;

import org.cloudbyexample.dc.schema.beans.docker.image.DockerImage;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.cloudbyexample.dc.service.application.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Agent message handler.
 *
 * @author David Winterfeldt
 */
@Component
public class NotificationMessageHandler implements ErrorHandler {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationService applicationService;

    @Autowired
    public NotificationMessageHandler(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public void handle(DockerImageResponse request) {
        // FIXME: move to AOP
        createSystemUser();

        try {
            DockerImage dockerImage = request.getResults();

            logger.info("Received image build notification for '{}'.  uuid={} tags='{}'",
                    dockerImage.getId(), dockerImage.getUuid(),
                    dockerImage.getRepoTags());

            applicationService.updateImageTemplate(dockerImage.getUuid(), dockerImage.getId());
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void handleError(Throwable t) {
        logger.error(t.getMessage());
    }

}
