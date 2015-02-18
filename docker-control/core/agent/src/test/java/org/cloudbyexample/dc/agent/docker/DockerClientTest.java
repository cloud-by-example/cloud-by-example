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
package org.cloudbyexample.dc.agent.docker;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Ignore
public class DockerClientTest {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private DockerClient client;
    
    @Test
    public void testCommand() {
//        Properties props = new Properties();
//        props.put("docker.io.url", "");
//        props.put("docker.io.version", "");
        
//        System.setProperty("docker.io.url", "http://172.16.78.128:10623");

//        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
//                .withVersion("1.13")
//                .build();
//
//        DockerClientConfig.overrideDockerPropertiesWithSystemProperties(new Properties());
        
//        config.withVersion("1.13");
//        config.withUsername("dockeruser");
//        config.withPassword("ilovedocker");
//        config.withEmail("dockeruser@github.com");
        
//        DockerClient client = new DockerClientImpl(config);
        
        List<Container> containers = client.listContainersCmd().exec();
        
        assertNotNull(containers);
        
        for (Container container : containers) {
            logger.info("id={}  image='{}'", container.getId(), container.getImage());
        }
    }
    
    @Test
    public void testContainerCreate() {
        CreateContainerResponse response = client.createContainerCmd("dc-apache").exec();
            
        client.startContainerCmd(response.getId())
            .withPortBindings(new Ports(new ExposedPort("tcp", 80), new Binding(8300)))
            .exec();
    }

//    private void findContainers() {
//      BufferedReader in = null;
//      
//      try {
//          URL oracle = new URL("http://172.16.78.128:10623/containers/json");
////          URL oracle = new URL("http://127.0.0.1:10623/containers/json");
//          URLConnection con = oracle.openConnection();
//          in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//          
//          String input = null;
//          
//          while ((input = in.readLine()) != null) { 
//              logger.info(input);
//          }
//      } catch (MalformedURLException e) {
//          logger.error(e.getMessage(), e);
//      } catch (IOException e) {
//          logger.error(e.getMessage(), e);
//      } finally {
//          try { in.close(); } catch (Exception e) {}
//      }        
//    }
    
}
