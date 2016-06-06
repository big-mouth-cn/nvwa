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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.bigmouth.nvwa.network.ftp.FTPClientSupport;
import org.bigmouth.nvwa.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleFTPClient extends FTPClientSupport implements org.bigmouth.nvwa.network.ftp.FTPClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFTPClient.class);
    
    private final FTPClient client = new FTPClient();

    public SimpleFTPClient(String hostname, int port, String username, String password) {
        super(hostname, port, username, password);
    }

    public SimpleFTPClient(String hostname, int port) {
        super(hostname, port);
    }

    public SimpleFTPClient(String hostname) {
        super(hostname);
    }

    public void connect() throws SocketException, IOException {
        if (!client.isConnected()) {
            FTPClientConfig config = new FTPClientConfig();
            client.configure(config);
            
            LOGGER.debug("Beginning to connect the FTP server:" + hostname);
            
            client.connect(hostname, port);
            
            LOGGER.debug("Verifying authorize...");
            client.login(username, password);
            
            client.setControlEncoding(controlEncoding);
            client.enterLocalPassiveMode();
            
            String replyString = client.getReplyString();
            int reply = client.getReplyCode();
            
            if (!FTPReply.isPositiveCompletion(reply)) {
                disconnect();
                throw new IOException(replyString);
            }
        }
    }
    
    @Override
    public void test() throws IOException {
        try {
            connect();
        }
        finally {
            if (autoDisconnect)
                this.disconnect();
        }
    }

    public void disconnect() {
        if (client.isConnected()) {
            try {
                client.logout();
                client.disconnect();
                LOGGER.warn(hostname + " Has disconnect.");;
            }
            catch (IOException e) {
                LOGGER.error("disconnect: ", e);
            }
        }
    }
    
    @Override
    public File download(String remotePath, String remoteFileName) throws IOException {
        FileOutputStream output = null;
        try {
            connect();
            File file = new File(PathUtils.appendEndFileSeparator(System.getProperty("java.io.tmpdir")) + remoteFileName);
            if (client.changeWorkingDirectory(remotePath)) {
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.setBufferSize(BUFF_SIZE);
                output = FileUtils.openOutputStream(file);
                if (!client.retrieveFile(remoteFileName, output)) {
                    throw new IOException("File " + remoteFileName + " download failure!");
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("File " + remoteFileName + " Download successful.");
            }
            return file;
        }
        catch (IOException e) {
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
            this.connect();
            mkd(path);
        }
        catch (IOException e) {
            LOGGER.error("mkdir: ", e);
            throw e;
        }
        finally {
            if (autoDisconnect)
                this.disconnect();
        }
    }

    private boolean dirExists(String path) throws IOException {
        int reply = client.cwd(path);
        return (FTPReply.FILE_ACTION_OK == (reply));
    }

    /**
     * 递归创建目录
     * 
     * @param path
     * @throws IOException
     */
    private void mkd(String path) throws IOException {
        if (!dirExists(path)) {
            String[] dirs = StringUtils.split(path, "/");
            String full = "/";
            for (String dir : dirs) {
                full += dir + "/";
                if (!dirExists(full)) {
                    int reply = client.mkd(full);
                    if (FTPReply.isPositiveCompletion(reply)) {
                        LOGGER.info("mkdir " + full + " successful.");
                    }
                    else {
                        LOGGER.error("mkdir " + full + " failed! " + reply);
                        throw new IOException("mkdir " + full + " failed! Reply code: " + reply);
                    }
                }
            }
        }
    }

    @Override
    public boolean exists(String path, String fileName) throws IOException {
        try {
            this.connect();
            if (dirExists(path)) {
                FTPFile[] listFiles = client.listFiles(path);
                if (ArrayUtils.isNotEmpty(listFiles)) {
                    for (FTPFile ftpFile : listFiles) {
                        if (ftpFile.getName().equals(fileName))
                            return true;
                    }
                }
            }
            if (LOGGER.isWarnEnabled())
                LOGGER.warn("File " + path + fileName + " does not exist.");
            return false;
        }
        catch (IOException e) {
            LOGGER.error("exists: ", e);
            throw e;
        }
        finally {
            if (autoDisconnect)
                this.disconnect();
        }
    }

    @Override
    public void upload(String path, File file, String fileName) throws IOException {
        if (null == file) {
            throw new FileNotFoundException("File can not be null.");
        }
        try {
            if (file.exists()) {
                int c = 0;
                IOException ex = null;
                while (true) {
                    if (c == RETRY_NUM) {
                        throw ex;
                    }
                    try {
                        connect();
                        mkdir(path);
                        String remote = path + (StringUtils.isBlank(fileName) ? file.getName() : fileName);
                        InputStream inputStream = new FileInputStream(file);
                        client.setBufferSize(1024);
                        client.setFileType(FTPClient.BINARY_FILE_TYPE);
                        LOGGER.debug("Starting upload files to " + remote);
                        boolean storeFlag = client.storeFile(new String(remote.getBytes("UTF-8"), "ISO-8859-1"), inputStream);
                        if (storeFlag) {
                            LOGGER.info("File upload successful.");
                            break;
                        }
                        else {
                            LOGGER.warn("File upload failed!");
                        }
                    }
                    catch (IOException e) {
                        LOGGER.error("File upload failed!", e);
                        ex = e;
                    }
                    finally {
                        c++;
                    }
                }
            }
            else {
                throw new IOException("Local file " + file.getPath() + " does not exist.");
            }
        }
        catch (IOException e) {
            LOGGER.error("upload: ", e);
            throw e;
        }
        finally {
            if (autoDisconnect)
                this.disconnect();
        }
    }
    
    public FTPClient getClient() {
        return client;
    }

    public static void main(String[] args) {
        SimpleFTPClient ftp = new SimpleFTPClient("122.224.212.138", 20021, "skytemp", "skymobi");
        try {
            ftp.connect();
            ftp.setAutoDisconnect(false);
            int result = ftp.getClient().mkd("/test/resources/plugins/pcsuit");
            System.out.println(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ftp.disconnect();
        }
    }
}
