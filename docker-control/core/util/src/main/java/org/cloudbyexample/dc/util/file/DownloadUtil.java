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
package org.cloudbyexample.dc.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Download URL util.
 * 
 * @author David Winterfeldt
 */
public class DownloadUtil {

    private final static Logger logger = LoggerFactory.getLogger(DownloadUtil.class);
    
    private DownloadUtil() {}

    /**
     * Create temp dir.
     */
    public static File createTempDir()  {
        // create unique tmp dir
        File dir = new File("/tmp/dc", UUID.randomUUID().toString());
        dir.mkdirs();
        
        return dir;
    }

    /**
     * Download URL to specified dir.
     */
    public static File download(File dir, String url) throws IOException {
        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        
        return download(dir, url, fileName);
    }

    /**
     * Download URL to specified dir using specified file name.
     */
    public static File download(File dir, String url, String fileName) throws IOException {
        FileOutputStream fos = null;
        
        File file = new File(dir, fileName);
        
        try {
            URL website = new URL(url);
            
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(file);
            // ex: '1 << 24' is 16MB for max download
//            fos.getChannel().transferFrom(rbc, 0, 1 << 24');
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            
            logger.debug("Downloaded '{}' from '{}'.", file, url);
        } finally {
            IOUtils.closeQuietly(fos);
        }
        
        return file;
    }

}
