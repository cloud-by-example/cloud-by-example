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

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Velocity template processor.
 *
 * @author David Winterfeldt
 */
@Component
public class VelocityTemplateImpl implements Template {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final VelocityEngine velocityEngine;

    @Autowired
    public VelocityTemplateImpl(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String process(String templatePath, Map<String, Object> vars) {
       String template = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, vars);

       logger.debug("template={}", template);

       return template;
    }

}