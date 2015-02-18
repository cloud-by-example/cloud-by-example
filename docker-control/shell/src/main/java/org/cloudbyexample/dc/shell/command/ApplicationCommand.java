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
package org.cloudbyexample.dc.shell.command;

import static org.cloudbyexample.dc.shell.util.ContainerUtil.processLinks;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.application.Application;
import org.cloudbyexample.dc.schema.beans.application.ApplicationResponse;
import org.cloudbyexample.dc.schema.beans.application.ApplicationType;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.application.DbTemplate;
import org.cloudbyexample.dc.schema.beans.application.FileCopy;
import org.cloudbyexample.dc.schema.beans.application.ImageTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebTemplate;
import org.cloudbyexample.dc.schema.beans.application.WebappTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.cloudbyexample.dc.shell.bean.CommandState;
import org.cloudbyexample.dc.web.client.docker.ApplicationClient;
import org.springbyexample.schema.beans.response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Application commands.
 *
 * @author David Winterfeldt
 */
@Component
public class ApplicationCommand implements CommandMarker {

    private static final String APPLICATION_LIST_CMD = "app-list";
    private static final String APPLICATION_LIST_HELP = "List application.";

    private static final String APPLICATION_BEGIN_CMD = "app";
    private static final String APPLICATION_BEGIN_HELP = "Begin application create.";

    private static final String IMAGE_TEMPLATE_BEGIN_CMD = "image-template";
    private static final String IMAGE_TEMPLATE_BEGIN_HELP = "Begin image template.";
    private static final String IMAGE_TEMPLATE_END_CMD = "image-template-end";
    private static final String IMAGE_TEMPLATE_END_HELP = "End image template.";

    private static final String DB_TEMPLATE_BEGIN_CMD = "db-template";
    private static final String DB_TEMPLATE_BEGIN_HELP = "Begin DB template.";
    private static final String DB_TEMPLATE_END_CMD = "db-template-end";
    private static final String DB_TEMPLATE_END_HELP = "End DB template.";

    private static final String WEBAPP_TEMPLATE_BEGIN_CMD = "webapp-template";
    private static final String WEBAPP_TEMPLATE_BEGIN_HELP = "Begin webapp template.";
    private static final String WEBAPP_TEMPLATE_END_CMD = "webapp-template-end";
    private static final String WEBAPP_TEMPLATE_END_HELP = "End webapp template.";

    private static final String WEB_TEMPLATE_BEGIN_CMD = "web-template";
    private static final String WEB_TEMPLATE_BEGIN_HELP = "Begin web template.";
    private static final String WEB_TEMPLATE_END_CMD = "web-template-end";
    private static final String WEB_TEMPLATE_END_HELP = "End web template.";

    private static final String FILE_COPY_CMD = "file-copy";
    private static final String FILE_COPY_HELP = "URL of a file to copy to a specific location.";

    private static final String SQL_FILE_CMD = "sql-file";
    private static final String SQL_FILE_HELP = "URL of a SQL file.";

    private static final String WAR_CMD = "war";
    private static final String WAR_HELP = "URL of the WAR file.";

    private static final String CONTAINER_TEMPLATE_CMD = "container-template";
    private static final String CONTAINER_TEMPLATE_HELP = "Container template (used as a template when starting the container).";

    private static final String APPLICATION_END_CMD = "app-end";
    private static final String APPLICATION_END_HELP = "End application create.";

    private final ApplicationClient client;
    private final CommandState commandState;

    @Autowired
    public ApplicationCommand(ApplicationClient client, CommandState commandState) {
        this.client = client;
        this.commandState = commandState;
    }

    @CliAvailabilityIndicator({ APPLICATION_LIST_CMD, APPLICATION_BEGIN_CMD })
    public boolean isCommandAvailable() {
        return commandState.isCommandAvailable();
    }

    @CliAvailabilityIndicator({ IMAGE_TEMPLATE_BEGIN_CMD,
        DB_TEMPLATE_BEGIN_CMD, WEBAPP_TEMPLATE_BEGIN_CMD, WEB_TEMPLATE_BEGIN_CMD })
    public boolean isApplicationInProgress() {
        return commandState.isApplicationInProgress() && !commandState.isImageTemplateInProgress();
    }

    @CliAvailabilityIndicator({ FILE_COPY_CMD, SQL_FILE_CMD, WAR_CMD,
        IMAGE_TEMPLATE_END_CMD, DB_TEMPLATE_END_CMD, WEBAPP_TEMPLATE_END_CMD, WEB_TEMPLATE_END_CMD })
    public boolean isImageTemplateInProgress() {
        return commandState.isImageTemplateInProgress();
    }

    @CliAvailabilityIndicator({ APPLICATION_END_CMD })
    public boolean isApplicationEndable() {
        return commandState.isApplicationInProgress() && !commandState.isImageTemplateInProgress();
    }

//    @CliCommand(value = APPLICATION_LIST_CMD, help = APPLICATION_LIST_HELP)
//    public String find() {
//        ApplicationFindResponse response = client.find();
//
//        StringBuilder sb = new StringBuilder();
//
//        for (Application app : response.getResults()) {
//            sb.append(app.getName());
//            sb.append("  ");
//            sb.append(app.getVersion());
//            sb.append("\n");
//        }
//
//        return sb.toString();
//    }

    @CliCommand(value = APPLICATION_BEGIN_CMD, help = APPLICATION_BEGIN_HELP)
    public void appBegin(@CliOption(key = { "name" }, mandatory = true,
                                    help = "Application name. ex: 'hello-world-1.0'") String name,
                         @CliOption(key = { "version" }, mandatory = true,
                                    help = "Application version.") String version,
                         @CliOption(key = { "type" }, mandatory = true,
                                    help = "Application type ('OS', 'SOFTWARE', 'STANDARD'.") ApplicationType type) {
        // FIXME: need autocomplete for type
        commandState.setApplication(new Application().withName(name).withVersion(version).withType(type));
    }

    @CliCommand(value = IMAGE_TEMPLATE_BEGIN_CMD, help = IMAGE_TEMPLATE_BEGIN_HELP)
    public void imageTemplateBegin(@CliOption(key = { "name" }, mandatory = true,
                                                help = "Image template name.") String name) {
        commandState.setImageTemplate(new ImageTemplate().withName(name));
    }

    @CliCommand(value = DB_TEMPLATE_BEGIN_CMD, help = DB_TEMPLATE_BEGIN_HELP)
    public void dbTemplateBegin(@CliOption(key = { "name" }, mandatory = true,
                                           help = "DB template name.") String name,
                                           @CliOption(key = { "db-name" }, mandatory = true,
                                           help = "DB name.") String dbName,
                                           @CliOption(key = { "username" }, mandatory = true,
                                           help = "DB username.") String username,
                                           @CliOption(key = { "password" }, mandatory = true,
                                           help = "DB password.") String password) {
        commandState.setImageTemplate(new DbTemplate().withName(name)
                .withDbName(dbName)
                .withUsername(username)
                .withPassword(password));
    }

    @CliCommand(value = WEBAPP_TEMPLATE_BEGIN_CMD, help = WEBAPP_TEMPLATE_BEGIN_HELP)
    public void webappTemplateBegin(@CliOption(key = { "name" }, mandatory = true,
                                            help = "Webapp template name.") String name) {
        commandState.setImageTemplate(new WebappTemplate().withName(name));
    }

    @CliCommand(value = WEB_TEMPLATE_BEGIN_CMD, help = WEB_TEMPLATE_BEGIN_HELP)
    public void webTemplateBegin(@CliOption(key = { "name" }, mandatory = true,
                                    help = "Web template name.") String name) {
        commandState.setImageTemplate(new WebTemplate().withName(name));
    }

    @CliCommand(value = IMAGE_TEMPLATE_END_CMD, help = IMAGE_TEMPLATE_END_HELP)
    public void imageTemplateEnd() {
        commandState.getApplication()
            .withImageTemplates(commandState.getImageTemplate());

        // reset
        commandState.setImageTemplate(null);
    }

    @CliCommand(value = DB_TEMPLATE_END_CMD, help = DB_TEMPLATE_END_HELP)
    public void dbTemplateEnd() {
        commandState.getApplication()
            .withImageTemplates(commandState.getImageTemplate());

        // reset
        commandState.setImageTemplate(null);
    }

    @CliCommand(value = WEBAPP_TEMPLATE_END_CMD, help = WEBAPP_TEMPLATE_END_HELP)
    public void webappTemplateEnd() {
        commandState.getApplication()
            .withImageTemplates(commandState.getImageTemplate());

        // reset
        commandState.setImageTemplate(null);
    }

    @CliCommand(value = WEB_TEMPLATE_END_CMD, help = WEB_TEMPLATE_END_HELP)
    public void webTemplateEnd() {
        commandState.getApplication()
            .withImageTemplates(commandState.getImageTemplate());

        // reset
        commandState.setImageTemplate(null);
    }

    @CliCommand(value = FILE_COPY_CMD, help = FILE_COPY_HELP)
    public void fileCopy(@CliOption(key = { "name" }, mandatory = true,
                                      help = "Final file name. ex: 'index.html'") String name,
                           @CliOption(key = { "url" }, mandatory = true,
                                      help = "File URL. ex: 'http://bit.ly/index.html'") String url,
                           @CliOption(key = { "destination" }, mandatory = true,
                                      help = "Destination path. ex: '/var/www/html'") String destination) {
        commandState.getImageTemplate()
            .withFileCopys(new FileCopy()
                            .withName(name)
                            .withUrl(url)
                            .withDestination(destination));
    }

    @CliCommand(value = SQL_FILE_CMD, help = SQL_FILE_HELP)
    public void sqlFile(@CliOption(key = { "name" }, mandatory = true,
                                   help = "SQL file name. ex: 'schema.sql'") String name,
                        @CliOption(key = { "url" }, mandatory = true,
                                   help = "SQL File URL. ex: 'http://bit.ly/schema.sql'") String url) {
        DbTemplate dbTemplate = (DbTemplate) commandState.getImageTemplate();

        commandState.setImageTemplate(
                dbTemplate.withSqlFiles(new FileCopy()
                    .withName(name)
                    .withUrl(url)));
    }

    @CliCommand(value = WAR_CMD, help = WAR_HELP)
    public void war(@CliOption(key = { "name" }, mandatory = true,
                               help = "Final file name. ex: 'contact.war'") String name,
                    @CliOption(key = { "url" }, mandatory = true,
                               help = "File URL. ex: 'http://bit.ly/contact.war'") String url) {
        WebappTemplate webappTemplate = (WebappTemplate) commandState.getImageTemplate();

        commandState.setImageTemplate(
            webappTemplate.withWar(new FileCopy()
                .withName(name)
                .withUrl(url)
                .withArchive(true)));
    }

    @CliCommand(value = CONTAINER_TEMPLATE_CMD, help = CONTAINER_TEMPLATE_HELP)
    public void containerTemplate(@CliOption(key = { "image" }, mandatory = true,
                                             help = "Image template name.") String image,
                                  @CliOption(key = { "name" },
                                             help = "Name of the container.") String name,
                                  @CliOption(key = { "expose-ports" },
                                             help = "Expose any available ports externally.") String exposePortsParam,
                                  @CliOption(key = { "cluster-size" },
                                             help = "Cluster size (number of containers to make from this container template).") Integer clusterSize,
                                  @CliOption(key = { "links" },
                                             help = "Comma delimitted links to bind to [name:alias].  ex: 'postgresql:db.") String linksParam) {
        boolean exposePorts = BooleanUtils.toBoolean(exposePortsParam);

        ContainerTemplate containerTemplate = new ContainerTemplate()
            .withImage(image)
            .withExposePorts(exposePorts)
            .withClusterSize((clusterSize != null ? clusterSize : 1));

        if (StringUtils.isNotBlank(name)) {
            containerTemplate = containerTemplate.withName(name);
        }

        processLinks(containerTemplate, linksParam);

        commandState.getApplication()
            .withContainerTemplates(containerTemplate);
    }

    @CliCommand(value = APPLICATION_END_CMD, help = APPLICATION_END_HELP)
    public String appEnd() {
        Application request = commandState.getApplication();

        ApplicationResponse response = client.create(request);
        Application application = response.getResults();

        commandState.reset();
        
        StringBuilder sb = new StringBuilder();

        // FIXME: share
        if (response.isErrors()) {
            for (Message msg : response.getMessageList()) {
                sb.append(msg.getMessageType());
                sb.append(msg.getMessage());
                sb.append("/n");
            }

            return sb.toString();
        }


        // debug
        sb.append(application.getName());
        sb.append("  ");
        sb.append(application.getVersion());
        sb.append("  ");
        sb.append("(id=");
        sb.append(application.getId());
        sb.append("  ");
        sb.append("(uuid=");
        sb.append(application.getUuid());
        sb.append(")");
        sb.append("\n");

        for (ImageTemplate template : application.getImageTemplates()) {
            sb.append("    ");
            sb.append("image-template: ");
            sb.append(template.getName());
            sb.append("\n");

            for (FileCopy fc : template.getFileCopys()) {
                sb.append("        ");
                sb.append("file-copy: ");
                sb.append(fc.getName());
                sb.append("  ");
                sb.append(fc.getUrl());
                sb.append("  ");
                sb.append(fc.getDestination());
                sb.append("\n");
            }
            sb.append(template.getName());
        }

        for (ContainerTemplate template : application.getContainerTemplates()) {
            sb.append("    ");
            sb.append("container-template: ");
            sb.append(template.getImage());
            sb.append("  exposedPorts=");
            sb.append(template.isExposePorts());
            sb.append("  links=");
            for (Link link : template.getLinks()) {
                sb.append(link.getName());
                sb.append(":");
                sb.append(link.getAlias());
                sb.append(",");
            }

            sb.append("\n");
        }
        sb.append("\n");

        for (Message message : response.getMessageList()) {
            sb.append(message.getMessage());
            sb.append("\n");
        }

        return sb.toString();
    }

}
