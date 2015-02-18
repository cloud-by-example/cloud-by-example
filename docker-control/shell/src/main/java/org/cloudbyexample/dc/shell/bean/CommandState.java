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
package org.cloudbyexample.dc.shell.bean;

import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.springframework.stereotype.Component;

/**
 * Command state, primarily used for CLI availability.
 * 
 * @author David Winterfeldt
 */
@Component
public class CommandState {

    private Application application = null;
    private ImageTemplate imageTemplate = null;

    /**
     * Whether general commands are available.
     */
    public boolean isCommandAvailable() {
        boolean available = (application == null);
        
        return available;
    }

    /**
     * Whether or not an application create is in progress.
     */
    public boolean isApplicationInProgress() {
        return (application != null);
    }
    
    /**
     * Whether or not an image template is in progress.
     */
    public boolean isImageTemplateInProgress() {
        return (imageTemplate != null);
    }

    /**
     * Reset all state.
     */
    public void reset() {
        application = null;
        imageTemplate = null;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ImageTemplate getImageTemplate() {
        return imageTemplate;
    }

    public void setImageTemplate(ImageTemplate imageTemplate) {
        this.imageTemplate = imageTemplate;
    }
    
}
