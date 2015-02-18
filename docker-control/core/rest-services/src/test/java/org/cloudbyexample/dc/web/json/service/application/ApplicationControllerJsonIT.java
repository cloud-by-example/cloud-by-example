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
package org.cloudbyexample.dc.web.json.service.application;

import static org.springbyexample.mvc.test.constants.ProfileConstants.REST_JSON;

import org.cloudbyexample.dc.web.service.application.ApplicationControllerTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests application controller with JSON serialization.
 *
 * @author David Winterfeldt
 */
@ActiveProfiles(profiles= { REST_JSON }, inheritProfiles=true)
public class ApplicationControllerJsonIT extends ApplicationControllerTest {

}
