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
package org.cloudbyexample.dc.shell.util;

import org.apache.commons.lang.StringUtils;
import org.cloudbyexample.dc.schema.beans.application.ContainerTemplate;
import org.cloudbyexample.dc.schema.beans.docker.container.DockerContainer;
import org.cloudbyexample.dc.schema.beans.docker.container.Link;
import org.cloudbyexample.dc.schema.beans.docker.container.PortBindings;

/**
 * Container bean utils.
 *
 * @author David Winterfeldt
 */
public class ContainerUtil {

    public static final String COMMA_DELIM = ",";
    public static final String COLON_DELIM = ":";

    private ContainerUtil() {}

    /**
     * Process ports and links input params.
     */
    public static DockerContainer processPortsAndLinks(DockerContainer containerRequest,
                                                 String portsParam, String linksParam) {

        processPorts(containerRequest, portsParam);
        processLinks(containerRequest, linksParam);

        return containerRequest;
    }
    /**
     * Process ports input param.
     */
    public static DockerContainer processPorts(DockerContainer containerRequest, String portsParam) {
        if (StringUtils.isNotBlank(portsParam)) {
            if (portsParam.indexOf(COMMA_DELIM) > 0) {
                String[] ports = StringUtils.split(portsParam, COMMA_DELIM);

                for (int i = 0; i < ports.length; i++) {
                    String[] privatePublicPort = StringUtils.split(portsParam, COLON_DELIM);

                    containerRequest = containerRequest.withPorts(
                            new PortBindings().withPrivatePort(Integer.parseInt(privatePublicPort[0]))
                                              .withPublicPort(Integer.parseInt(privatePublicPort[1])));
                }
            } else {
                String[] privatePublicPort = StringUtils.split(portsParam, COLON_DELIM);

                containerRequest = containerRequest.withPorts(
                        new PortBindings().withPrivatePort(Integer.parseInt(privatePublicPort[0]))
                                          .withPublicPort(Integer.parseInt(privatePublicPort[1])));
            }
        }

        return containerRequest;
    }

    /**
     * Process links input param.
     */
    public static DockerContainer processLinks(DockerContainer containerRequest,  String linksParam) {
        if (StringUtils.isNotBlank(linksParam)) {
            if (linksParam.indexOf(COMMA_DELIM) > 0) {
                String[] links = StringUtils.split(linksParam, COMMA_DELIM);

                for (int i = 0; i < links.length; i++) {
                    String[] nameAlias = StringUtils.split(linksParam, COLON_DELIM);

                    containerRequest = containerRequest.withLinks(
                            new Link().withName(nameAlias[0]).withAlias(nameAlias[1]));
                }
            } else {
                String[] nameAlias = StringUtils.split(linksParam, COLON_DELIM);

                containerRequest = containerRequest.withLinks(
                        new Link().withName(nameAlias[0]).withAlias(nameAlias[1]));
            }
        }

        return containerRequest;
    }

    /**
     * Process links input param.
     */
    public static ContainerTemplate processLinks(ContainerTemplate containerRequest,  String linksParam) {
        if (StringUtils.isNotBlank(linksParam)) {
            if (linksParam.indexOf(COMMA_DELIM) > 0) {
                String[] links = StringUtils.split(linksParam, COMMA_DELIM);

                for (int i = 0; i < links.length; i++) {
                    String[] nameAlias = StringUtils.split(linksParam, COLON_DELIM);

                    containerRequest = containerRequest.withLinks(
                            new Link().withName(nameAlias[0]).withAlias(nameAlias[1]));
                }
            } else {
                String[] nameAlias = StringUtils.split(linksParam, COLON_DELIM);

                containerRequest = containerRequest.withLinks(
                        new Link().withName(nameAlias[0]).withAlias(nameAlias[1]));
            }
        }

        return containerRequest;
    }

}
