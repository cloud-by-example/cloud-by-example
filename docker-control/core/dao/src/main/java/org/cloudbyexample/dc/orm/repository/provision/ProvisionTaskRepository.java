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
package org.cloudbyexample.dc.orm.repository.provision;

import java.util.List;

import org.cloudbyexample.dc.orm.entity.provision.ProvisionTask;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


/**
 * Provision task repository.
 *
 * @author David Winterfeldt
 */
public interface ProvisionTaskRepository extends JpaRepository<ProvisionTask, Integer> {

    /**
     * Find scheduled tasks that less than the current time.
     */
    public List<ProvisionTask> findScheduledTasks(@Param("currentTime") DateTime currentTime, Pageable page);

}
