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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Application var processor.
 *
 * @author David Winterfeldt
 */
@Component
public class ApplicationVarProcessor {

    final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Process vars and generate unique image tag.
     */
    public void processVars(Application application) {
        Map<String, String> imageNames = new HashMap<>();
        Map<String, String> containerNames = new HashMap<>();

        // create unique image tag
        for (ImageTemplate imageTemplate : application.getImageTemplates()) {
            // FIXME: lookup user's group
//            String group = "dc";

            StringBuilder sb = new StringBuilder();
//            sb.append(group);
//            sb.append(":");
            sb.append(application.getName());
            sb.append("-");
            sb.append(imageTemplate.getName());
            sb.append("-");
            sb.append(application.getVersion());
//            sb.append("-");
//            sb.append(UUID.randomUUID());

            String tag = sb.toString();

            imageNames.put(imageTemplate.getName(), tag);

            imageTemplate.setTag(tag);
        }

        // create unique container template name
        for (ContainerTemplate containerTemplate : application.getContainerTemplates()) {
            if (StringUtils.isNotBlank(containerTemplate.getName())) {
                StringBuilder sb = new StringBuilder();
                sb.append(containerTemplate.getName());
                sb.append("-");
                sb.append(UUID.randomUUID());

                String name = sb.toString();

                containerNames.put(containerTemplate.getName(), name);

                containerTemplate.setName(name);
            }
        }

        // FIXME: processVars should possibly use spel and also ideally
        //        ContainerTemplate should get a ref to the image after it's built.
        for (String originalName : imageNames.keySet()) {
            for (ContainerTemplate ct : application.getContainerTemplates()) {
                String prefix = "${" + originalName;
                String tag = imageNames.get(originalName);

                // update image tag
                if (ct.getImage().startsWith(prefix)) {
                    ct.setImage(tag);
                }
            }
        }

        for (String originalName : containerNames.keySet()) {
            for (ContainerTemplate ct : application.getContainerTemplates()) {
                String varName = "${" + originalName + "}";
                String name = containerNames.get(originalName);

                // update link name
                for (Link link : ct.getLinks()) {
                    if (varName.equals(link.getName())) {
                        link.withName(name);
                    }
                }
            }
        }
    }

}
