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

import static org.cloudbyexample.dc.schema.beans.docker.image.ImageCommandType.BUILD;
import static org.cloudbyexample.dc.shell.util.ContainerUtil.COMMA_DELIM;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.application.File;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImage;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageArchive;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageDir;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.cloudbyexample.dc.shell.bean.CommandState;
import org.cloudbyexample.dc.web.client.docker.DockerImageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Docker image commands.
 *
 * @author David Winterfeldt
 */
@Component
public class DockerImageCommand implements CommandMarker {

    private static final String IMAGE_LIST_CMD = "image-list";
    private static final String IMAGE_LIST_HELP = "List image.";

    private static final String IMAGE_BUILD_CMD = "image-build";
    private static final String IMAGE_BUILD_HELP = "Build an image from a tar file.";

    private static final String IMAGE_BUILD_URL_CMD = "image-build-url";
    private static final String IMAGE_BUILD_URL_HELP = "Build an image from one or more URLs, at specifying a Dockerfile.";

    private final DockerImageClient client;
    private final CommandState commandState;

    @Autowired
    public DockerImageCommand(DockerImageClient client, CommandState commandState) {
        this.client = client;
        this.commandState = commandState;
    }

    @CliAvailabilityIndicator({ IMAGE_LIST_CMD, IMAGE_BUILD_CMD, IMAGE_BUILD_URL_CMD })
    public boolean isCommandAvailable() {
        return commandState.isCommandAvailable();
    }

    @CliCommand(value = IMAGE_LIST_CMD, help = IMAGE_LIST_HELP)
    public String find() {
        DockerImageFindResponse response = client.find();
        List<DockerImage> results = response.getResults();

        StringBuilder sb = new StringBuilder();

        for (DockerImage image : results) {
            // FIXME: move length of id substring to parent or util
            sb.append(StringUtils.substring(image.getId(), 0, 10));
            sb.append(" ");
            sb.append(Arrays.toString(image.getRepoTags().toArray(new String[] {})));
            sb.append("\n");
        }

        return sb.toString();
    }

    @CliCommand(value = IMAGE_BUILD_CMD, help = IMAGE_BUILD_HELP)
    public String build(@CliOption(key = { "tag" }, mandatory = true,
                                            help = "Tag name.") String tag,
                        @CliOption(key = { "archive-url" }, mandatory = true,
                                            help = "Archive file url (only supports tar, protocols http and ftp).") String archiveUrl) {
        DockerImage imageRequest = new DockerImageArchive()
            .withRepoTags(tag)
            .withArchiveUrl(archiveUrl);

        DockerImageRequest request = new DockerImageRequest()
            .withCommand(BUILD)
            .withRequest(imageRequest);

        DockerImageResponse response = client.build(request);
        DockerImage image = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(image.getId());
        sb.append("  ");
        sb.append(Arrays.toString(image.getRepoTags().toArray(new String[] {})));
        sb.append(" ");
        sb.append(response.getMessageList().get(0).getMessage());
        sb.append("\n");

        return sb.toString();
    }

    @CliCommand(value = IMAGE_BUILD_URL_CMD, help = IMAGE_BUILD_URL_HELP)
    public String buildUrl(@CliOption(key = { "tag" }, mandatory = true,
                                      help = "Tag name.") String tag,
                           @CliOption(key = { "dockerfile-url" }, mandatory = true,
                                      help = "Dockerfile URL.") String dockerfileUrl,
                           @CliOption(key = { "urls" },
                                      help = "Comma delimitted list of URLs (files to add to Dockerfile directory).") String urlParams) {
        DockerImageDir imageRequest = new DockerImageDir()
            .withRepoTags(tag)
            .withDockerfileUrl(dockerfileUrl);

        if (StringUtils.isNotBlank(urlParams)) {
            String[] urls = StringUtils.split(urlParams, COMMA_DELIM);

            for (String url : urls) {
                imageRequest = imageRequest.withFiles(new File().withUrl(url));
            }
        }

        DockerImageRequest request = new DockerImageRequest()
            .withCommand(BUILD)
            .withRequest(imageRequest);

        DockerImageResponse response = client.build(request);
        DockerImage image = response.getResults();

        StringBuilder sb = new StringBuilder();

        sb.append(image.getId());
        sb.append("  ");
        sb.append(Arrays.toString(image.getRepoTags().toArray(new String[] {})));
        sb.append(" ");
        sb.append(response.getMessageList().get(0).getMessage());
        sb.append("\n");

        return sb.toString();
    }

}
