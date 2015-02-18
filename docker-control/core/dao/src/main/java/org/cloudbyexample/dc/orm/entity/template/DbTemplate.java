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
package org.cloudbyexample.dc.orm.entity.template;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


/**
 * DB template entity.
 *
 * @author David Winterfeldt
 */
@Entity
@DiscriminatorValue("1")
public class DbTemplate extends ImageTemplate {

    private String dbName = null;
    private String username = null;
    private String password = null;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<FileCopy> sqlFiles = null;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FileCopy> getSqlFiles() {
        return sqlFiles;
    }

    public void setSqlFiles(List<FileCopy> sqlFiles) {
        this.sqlFiles = sqlFiles;
    }

}
