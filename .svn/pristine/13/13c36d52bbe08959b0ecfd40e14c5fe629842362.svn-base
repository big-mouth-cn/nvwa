/*
 * Copyright 2015 big-mouth.cn
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.bigmouth.nvwa.network.ftp.server;

import java.net.URL;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.bigmouth.nvwa.utils.BaseLifeCycleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPServer extends BaseLifeCycleSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPServer.class);
    private static final int PORT = 21;
    private final String host;
    private int port = PORT;
    
    private FtpServer server;
    private URL properties;
    
    public FTPServer(String host) {
        this(host, PORT);
    }

    public FTPServer(String host, int port) {
        this(host, port, FTPServer.class.getResource("/ftp-server.properties"));
    }
    
    public FTPServer(String host, int port, URL properties) {
        this.host = host;
        this.port = port;
        this.properties = properties;
    }

    @Override
    protected void doInit() {
        try {
            FtpServerFactory factory = new FtpServerFactory();
            PropertiesUserManagerFactory managerFactory = new PropertiesUserManagerFactory();
            
            ListenerFactory listenerFactory = new ListenerFactory();
            listenerFactory.setServerAddress(host);
            listenerFactory.setPort(port);
            factory.addListener("default", listenerFactory.createListener());
            
            if ( null == properties )
                throw new NullPointerException("properties");
            
            managerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
            managerFactory.setUrl(properties);
            factory.setUserManager(managerFactory.createUserManager());
            server = factory.createServer();
            server.start();
            String addr = getAddr();
            LOGGER.info("FTP Server {} has started successfully!", addr);
        }
        catch (Exception e) {
            throw new RuntimeException("FTP Server start failured!", e);
        }
    }

    private String getAddr() {
        return "ftp://" + host + ":" + port;
    }

    @Override
    protected void doDestroy() {
        if (null != server) {
            server.stop();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServer(FtpServer server) {
        this.server = server;
    }

    public void setProperties(URL properties) {
        this.properties = properties;
    }
}
