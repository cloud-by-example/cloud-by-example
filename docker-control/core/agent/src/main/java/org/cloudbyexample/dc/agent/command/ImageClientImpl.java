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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.agent.converter.DockerImageConverter;
import org.cloudbyexample.dc.agent.util.ArchiveUtil;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImage;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageArchive;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageDir;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageFindResponse;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageRequest;
import org.cloudbyexample.dc.schema.beans.docker.image.DockerImageResponse;
import org.cloudbyexample.dc.util.file.DownloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springbyexample.schema.beans.response.Message;
import org.springbyexample.schema.beans.response.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;

/**
 * Docker Container handler implementation.
 *
 * @author David Winterfeldt
 */
@Component
public class ImageClientImpl implements ImageClient {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String DOCKERFILE = "Dockerfile";

    private final DockerClient client;
    private final DockerImageConverter converter;
    private final ImageIdMatcher matcher;

    @Autowired
    public ImageClientImpl(DockerClient client, DockerImageConverter converter,
            ImageIdMatcher matcher) {
        this.client = client;
        this.converter = converter;

        this.matcher = matcher;
    }

    @Override
    public DockerImageFindResponse find() {
        logger.debug("Find images.");

        List<DockerImage> results = converter.convertListTo(client.listImagesCmd().exec());

        return new DockerImageFindResponse().withResults(results);
    }

    @Override
    public DockerImageResponse build(DockerImageRequest imageRequest) {
        DockerImage request = imageRequest.getRequest();

        if (request instanceof DockerImageArchive) {
            return buildFromArchive((DockerImageArchive) request);
        } else if (request instanceof DockerImageDir) {
            return buildFromDir((DockerImageDir) request);
        } else {
            throw new UnsupportedOperationException("Unknown docker image type '" + request.getClass() + "'.");
        }
    }

    private DockerImageResponse buildFromArchive(DockerImageArchive request) {
        String id = null;
        String tag = null;

        if (request.getRepoTags().size() > 0) {
            tag = request.getRepoTags().get(0);
        }

        logger.debug("Build image.  tag='{}'  archiveUrl='{}', ", tag, request.getArchiveUrl());

        InputStream imageInput = null;
        InputStream responseInput = null;
        String log = "";

        try {
            Resource resource = new UrlResource(request.getArchiveUrl());

            imageInput = new BufferedInputStream(resource.getInputStream());

            // FIXME: need to add no cache option
            if (tag == null) {
                responseInput = client.buildImageCmd(imageInput).withRemove(true).exec();
            } else {
                responseInput = client.buildImageCmd(imageInput).withTag(tag).withRemove(true).exec();
            }

//            responseInput = client.buildImageCmd(in).withNoCache().withTag(id).exec();

            log = convertInputStreamToString(responseInput);

            id = matcher.findId(log);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(imageInput);
            IOUtils.closeQuietly(responseInput);
        }

        DockerImageResponse reponse =  new DockerImageResponse()
                .withMessageList(new Message()
                .withMessageType(MessageType.INFO)
                .withMessage(log))
                .withResults(request.withId(id).withRepoTags(tag));

        return reponse;
    }

    private DockerImageResponse buildFromDir(DockerImageDir request) {
        DockerImageResponse response = null;

        File archive = null;
        File dir = null;

        try {
            dir = DownloadUtil.createTempDir();

            for (org.cloudbyexample.dc.schema.beans.application.File file : request.getFiles()) {
                String url = file.getUrl();

                File download = DownloadUtil.download(dir, url);

                logger.info("Downloaded '{}' from '{}'.", download, url);
            }

            if (StringUtils.isNotBlank(request.getDockerfile())) {
                FileOutputStream out = new FileOutputStream(new File(dir, "Dockerfile"));
                IOUtils.copy(new StringReader(request.getDockerfile()), out);
                IOUtils.closeQuietly(out);
            } else {
                // write Dockerfile last in case someone tries to have one as a file to bypass validation
                String url = request.getDockerfileUrl();
                File dockerfile = DownloadUtil.download(dir, request.getDockerfileUrl(), DOCKERFILE);
                logger.info("Downloaded docker file '{}' from '{}'.", dockerfile, url);
            }

            // unable to create an image adding files from a directory, so creating archive
            archive = new File("/tmp", "docker-image-" + UUID.randomUUID() + ".tar");
            ArchiveUtil.createArchive(archive, dir);

            String archiveUrl = "file://" + archive.getAbsolutePath();

            response = buildFromArchive(new DockerImageArchive()
                        .withId(request.getId())
                        .withUuid(request.getUuid())
                        .withArchiveUrl(archiveUrl)
                        .withRepoTags(request.getRepoTags()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            FileUtils.deleteQuietly(archive);

            try { FileUtils.deleteDirectory(dir); } catch (IOException e) {}
        }

        return response;
    }

    private String convertInputStreamToString(InputStream response)  {
        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(
                    response, "UTF-8");

            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));

                logger.info(line);
            }

            return logwriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

}
