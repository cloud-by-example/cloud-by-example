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
package org.cloudbyexample.dc.agent.command;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Image Id matcher.
 * 
 * @author David Winterfeldt
 */
@Component
public class ImageIdMatcher {
    
    final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String pattern = "Successfully built ";

    public String findId(String line) {
        String id = null;

        int matchIndex = StringUtils.indexOf(line, pattern);
        
        if (matchIndex > -1) {
            int start = matchIndex + pattern.length();
            int end = line.length() -4;
            
            id = StringUtils.substring(line, start, end);
        }
        
        return id;
    }
    
}
