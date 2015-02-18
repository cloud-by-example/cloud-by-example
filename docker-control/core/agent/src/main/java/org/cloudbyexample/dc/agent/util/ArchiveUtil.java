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
package org.cloudbyexample.dc.agent.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Archive util.
 * 
 * @author David Winterfeldt
 */
public class ArchiveUtil {

    private ArchiveUtil() {}
    
    /**
     * <p>Create an archive from a directory.</p>
     * 
     * <p><strong>Note</strong>: Archive can not be created in the directory being archived.</p>
     */
    public static File createArchive(File archive, File dir) throws IOException {
        TarArchiveOutputStream taos = null;

        try {
            taos = new TarArchiveOutputStream(new FileOutputStream(archive));

            addFiles(taos, dir, "");            
        } finally {
            IOUtils.closeQuietly(taos);
        }     
        
        return archive;
    }
 
    /**
     * Add a file to the archive or recurse if a directory is specified.
     */
    private static void addFiles(TarArchiveOutputStream taos, File file, String dir) throws IOException{
        taos.putArchiveEntry(new TarArchiveEntry(file, dir));

        if (file.isFile()) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(bis, taos);
            taos.closeArchiveEntry();
            bis.close();
        } else if (file.isDirectory()) {
            taos.closeArchiveEntry();
            for (File childFile : file.listFiles()) {
                addFiles(taos, childFile, childFile.getName());
            }
        }
    }
    
}
