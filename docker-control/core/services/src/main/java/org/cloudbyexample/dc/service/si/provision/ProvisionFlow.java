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
package org.cloudbyexample.dc.service.si.provision;

import org.cloudbyexample.dc.schema.beans.provision.ProvisionTask;
import org.springframework.integration.annotation.Gateway;


/**
 * The entry point for the provision flow.
 *
 * @author David Winterfeldt
 */
public interface ProvisionFlow {

    public static final String PROVISION = "PROVISION";

    /**
     * Process a provision.
     */
	@Gateway(requestChannel="processProvision")
	public void process(ProvisionTask provisionTask);

}
