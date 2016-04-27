package org.apache.asyncweb.common.integration;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.asyncweb.common.codec.HttpCodecFactory;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Embedded;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public abstract class TomcatTest extends TestCase {

    protected final File BASEDIR = getBaseDir();
    protected final File CATALINAHOME = new File(BASEDIR, "src/test/catalina");
    protected final File WEBAPPS = new File(CATALINAHOME, "webapps");
    protected final File ROOT = new File(WEBAPPS, "ROOT");
    protected final File WORK = new File(BASEDIR, "target/working");
    
    protected final int port = AvailablePortFinder.getNextAvailable();
    
    protected Embedded server;

    protected SocketConnector connector;
    protected IoSession session;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        // Create Tomcat Server instance
        server = new Embedded();
        server.setCatalinaHome(CATALINAHOME.getAbsolutePath());

        Engine engine = server.createEngine();
        engine.setDefaultHost("localhost");

        Host host = server.createHost("localhost", WEBAPPS.getAbsolutePath());
        ((StandardHost)host).setWorkDir(WORK.getAbsolutePath());
        engine.addChild(host);

        addContexts(host);
        
        server.addEngine(engine);

        Connector http = server.createConnector("localhost", port, false);
        server.addConnector(http);

        server.start();
        
        // Create MINA connection
        IoConnector connector = new NioSocketConnector();
        connector.setHandler(new IoHandlerAdapter());
        connector.getFilterChain().addLast("HTTP", new ProtocolCodecFilter(new HttpCodecFactory()));
        session = connector.connect(new InetSocketAddress("localhost", port)).await().getSession();
        session.getConfig().setUseReadOperation(true);
    }
    
    @Override
    protected void tearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
        if (connector != null) {
            connector.dispose();
        }
    }

    protected void addContexts(Host host) {
        //Add the Root context
        StandardContext context = (StandardContext)server.createContext("", ROOT.getAbsolutePath());
        context.setParentClassLoader(Thread.currentThread().getContextClassLoader());
        //context.setPath("/");
        host.addChild(context);
    }
    
    protected URI getBaseURI() {
        return URI.create("http://localhost:" + port + "/"); 
    }
    
    protected File getBaseDir() {
        File dir;

        // If ${basedir} is set, then honor it
        String tmp = System.getProperty("basedir");
        if (tmp != null) {
            dir = new File(tmp);
        } else {
            // Find the directory which this class (or really the sub-class of TestSupport) is defined in.
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

            // We expect the file to be in target/test-classes, so go up 2 dirs
            dir = new File(path).getParentFile().getParentFile();

            // Set ${basedir} which is needed by logging to initialize
            System.setProperty("basedir", dir.getPath());
        }

        return dir;
    }
    
}
