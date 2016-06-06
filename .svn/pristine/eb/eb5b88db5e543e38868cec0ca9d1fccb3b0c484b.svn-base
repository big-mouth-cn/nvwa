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
package org.bigmouth.nvwa.network.ftp.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.network.ftp.FTPClient;
import org.bigmouth.nvwa.network.ftp.FTPClientSupport;
import org.bigmouth.nvwa.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;


/**
 * 
 * @since 1.0
 * @author Allen 
 */
public class SSHFTPClient extends FTPClientSupport implements org.bigmouth.nvwa.network.ftp.FTPClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SSHFTPClient.class);
    
    private int timeout = 10000;
    private Session session = null;
    private ChannelSftp channel = null;

    public SSHFTPClient(String hostname, int port, String username, String password) {
        super(hostname, port, username, password);
    }

    public SSHFTPClient(String hostname, int port) {
        super(hostname, port);
    }

    public SSHFTPClient(String hostname) {
        super(hostname);
    }

    public void connect() throws IOException {
        try {
            if (null == channel || channel.isClosed()) {
                LOGGER.debug("Beginning to connect the SSH FTP server:" + hostname);
                JSch jsch = new JSch();
                session = jsch.getSession(username, hostname, port);
                if (StringUtils.isNotBlank(password)) {
                    session.setPassword(password);
                }
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(timeout);
                LOGGER.debug("Verifying authorize...");
                session.connect();
    
                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
                LOGGER.debug("Connect successful.");
            }
        }
        catch (JSchException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public File download(String remotePath, String remoteFileName) throws IOException {
        FileOutputStream output = null;
        try {
            connect();
            channel.cd(remotePath);
            File file = new File(PathUtils.appendEndFileSeparator(System.getProperty("java.io.tmpdir")) + remoteFileName);
            output = FileUtils.openOutputStream(file);
            channel.get(remoteFileName, output);
            return file;
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        finally {
            IOUtils.closeQuietly(output);
            if (autoDisconnect)
                disconnect();
        }
    }

    @Override
    public void mkdir(String path) throws IOException {
        try {
            connect();
            mkd(path);
        }
        catch (SftpException e) {
            LOGGER.error("mkdir: ", e);
            throw new IOException(e);
        }
        finally {
            if (autoDisconnect)
                disconnect();
        }
    }
    
    private boolean dirExists(String path) throws IOException {
        try {
            channel.cd(path);
            return true;
        }
        catch (SftpException e) {
            return false;
        }
    }
    
    /**
     * 递归创建目录
     * 
     * @param path
     * @throws IOException
     * @throws SftpException 
     */
    private void mkd(String path) throws IOException, SftpException {
        if (!dirExists(path)) {
            String[] dirs = StringUtils.split(path, "/");
            String full = "/";
            for (String dir : dirs) {
                full += dir + "/";
                if (!dirExists(full)) {
                    channel.mkdir(full);
                }
            }
        }
    }

    @Override
    public boolean exists(String path, String fileName) throws IOException {
        try {
            connect();
            Vector<?> ls = channel.ls(path);
            for (Object object : ls) {
                if (object instanceof LsEntry) {
                    if (StringUtils.equals(((LsEntry)object).getFilename(), fileName))
                        return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            LOGGER.error("exists: ", e);
            throw new IOException(e);
        }
        finally {
            if (autoDisconnect)
                disconnect();
        }
    }

    @Override
    public void upload(String path, File file, String fileName) throws IOException {
        OutputStream outstream = null;
        InputStream instream = null;
        try {
            int c = 0;
            Exception ex = null;
            while (true) {
                if (c == RETRY_NUM) {
                    throw ex;
                }
                try {
                    if (file.exists()) {
                        connect();
                        String realName = (StringUtils.isBlank(fileName) ? file.getName() : fileName);
                        String remote = path + realName;
                        mkdir(path);
                        channel.cd(path);
                        LOGGER.debug("Starting upload files to " + remote);
                        outstream = channel.put(realName);
                        instream = new FileInputStream(file);
                        byte b[] = new byte[1024];
                        int n;
                        while ((n = instream.read(b)) != -1) {
                            outstream.write(b, 0, n);
                        }
                        outstream.flush();
                        LOGGER.info("File upload successful.");
                        break;
                    }
                    else {
                        throw new IOException("Local file " + file.getPath() + " does not exist.");
                    }
                }
                catch (Exception e) {
                    LOGGER.error("upload: " + e.getMessage());
                    ex = e;
                }
                finally {
                    c++;
                }
            }
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        finally {
            IOUtils.closeQuietly(instream);
            IOUtils.closeQuietly(outstream);
            if (autoDisconnect)
                disconnect();
        }
    }

    @Override
    public void test() throws IOException {
        try {
            connect();
        }
        finally {
            if (autoDisconnect)
                disconnect();
        }
    }

    @Override
    public void disconnect() {
        if (null != channel)
            channel.disconnect();
        if (null != session)
            session.disconnect();
        LOGGER.info("Connection has closed.");
    }

    public static void main(String[] args) {
        FTPClient ftp = new SSHFTPClient("124.160.11.211", 8022, "user1", "qwerty!234");
        try {
            ftp.setAutoDisconnect(false);
            String path = "/user1/allen.hu/jvector/maps/china/";
            ftp.upload(path, new File("C:\\Users\\allen.hu\\Desktop\\Struts2.0中文教程.chm"), "Struts2.0中文教程.chm");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            ftp.disconnect();
        }
    }
}
