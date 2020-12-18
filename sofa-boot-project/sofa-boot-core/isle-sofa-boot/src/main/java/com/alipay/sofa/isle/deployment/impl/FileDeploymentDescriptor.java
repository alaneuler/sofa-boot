/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.isle.deployment.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;

import com.alipay.sofa.boot.constant.SofaBootConstants;
import com.alipay.sofa.isle.deployment.DeploymentDescriptorConfiguration;

/**
 *
 * @author yangyanzhao
 * @version $Id: FileDescriptor.java, v 0.1 2012-1-11 17:41:52 yangyanzhao Exp $
 */
public class FileDeploymentDescriptor extends AbstractDeploymentDescriptor {
    public FileDeploymentDescriptor(URL url,
                                    Properties props,
                                    DeploymentDescriptorConfiguration deploymentDescriptorConfiguration,
                                    ClassLoader classLoader) {
        super(url, props, deploymentDescriptorConfiguration, classLoader);
    }

    @Override
    public void loadSpringXMLs() {
        springResources = new HashMap<>();

        try {
            // When path contains space characters (e.g., white space, Chinese), URL converts it to UTF8 code point
            // In order to processing correctly, create File from URI
            URI springXmlUri = new URI("file://"
                                       + url.getFile().substring(
                                           0,
                                           url.getFile().length()
                                                   - SofaBootConstants.SOFA_MODULE_FILE.length())
                                       + SofaBootConstants.SPRING_CONTEXT_PATH);
            File springXml = new File(springXmlUri);
            List<File> springFiles = new ArrayList<>();
            if (springXml.exists()) {
                listFiles(springFiles, springXml, ".xml");
            }

            for (File f : springFiles) {
                springResources.put(f.getAbsolutePath(), new FileSystemResource(f));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void listFiles(List<File> subFiles, File parent, String suffix) {
        File[] files = parent.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(suffix)) {
                subFiles.add(f);
            } else if (f.isDirectory()) {
                listFiles(subFiles, f, suffix);
            }
        }
    }
}
