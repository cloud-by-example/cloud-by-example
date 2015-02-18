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

import java.util.List;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Image template aggregator.
 * 
 * @author David Winterfeldt
 */
public class ImageTemplateAggregator {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public Application aggregate(List<ImageTemplate> imageTemplates, 
            @Header(ApplicationFlow.APPLICATION) Application application) {
        logger.info("received {} image templates", imageTemplates.size());
        
        application.setImageTemplates(imageTemplates);
        
        return application;
    }
    
}
