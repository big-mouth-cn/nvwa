/*
 * Copyright (C) 2009 Peter Monks (pmonks@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.bigmouth.nvwa.log4mongo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.spi.ErrorCode;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * Log4J Appender that writes log events into a MongoDB document oriented database. Log events are fully parsed and
 * stored as structured records in MongoDB (this appender does not require, nor use a Log4J layout).
 * 
 * The appender does <u>not</u> create any indexes on the data that's stored - it is assumed that if query performance
 * is required, those would be created externally (e.g., in the MongoDB shell or other external application).
 * 
 * @author Peter Monks (pmonks@gmail.com)
 * @see <a href="http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Appender.html">Log4J Appender
 *      Interface</a>
 * @see <a href="http://www.mongodb.org/">MongoDB</a>
 */
public class MongoDbAppender extends BsonAppender {

    private final static ExecutorService SERVICE = Executors.newCachedThreadPool();
    private final static String DEFAULT_MONGO_DB_HOSTNAME = "localhost";
    private final static String DEFAULT_MONGO_DB_PORT = "27017";
    private final static String DEFAULT_MONGO_DB_DATABASE_NAME = "log4mongo";
    private final static String DEFAULT_MONGO_DB_COLLECTION_NAME = "logevents";
    private WriteConcern concern;

    private String hostname = DEFAULT_MONGO_DB_HOSTNAME;
    private String port = DEFAULT_MONGO_DB_PORT;
    private String databaseName = DEFAULT_MONGO_DB_DATABASE_NAME;
    private String collectionName = DEFAULT_MONGO_DB_COLLECTION_NAME;
    private boolean separate = true;
    private String userName = null;
    private String password = null;
    private String writeConcern = null;
    private boolean writeStack = false;
    private Mongo mongo = null;
    private DBCollection collection = null;

    private boolean initialized = false;

    /**
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    public boolean requiresLayout() {
        return (false);
    }

    /**
     * @see org.apache.log4j.AppenderSkeleton#activateOptions()
     */
    @Override
    public void activateOptions() {
        try {
            // Close previous connections if reactivating
            if (mongo != null) {
                close();
            }

            List<ServerAddress> addresses = getServerAddresses(hostname, port);
            mongo = getMongo(addresses);

            DB database = getDatabase(mongo, databaseName);

            if (userName != null && userName.trim().length() > 0) {
                if (!database.authenticate(userName, password.toCharArray())) {
                    throw new RuntimeException("Unable to authenticate with MongoDB server.");
                }

                // Allow password to be GCed
                password = null;
            }

            setCollection(database.getCollection(collectionName));
            initialized = true;
        }
        catch (Exception e) {
            errorHandler
                    .error("Unexpected exception while initialising MongoDbAppender.", e, ErrorCode.GENERIC_FAILURE);
        }
    }

    /*
     * This method could be overridden to provide the DB instance from an existing connection.
     */
    protected DB getDatabase(Mongo mongo, String databaseName) {
        return mongo.getDB(databaseName);
    }

    /*
     * This method could be overridden to provide the Mongo instance from an existing connection.
     */
    protected Mongo getMongo(List<ServerAddress> addresses) {
        if (addresses.size() < 2) {
            return new Mongo(addresses.get(0));
        }
        else {
            // Replica set
            return new Mongo(addresses);
        }
    }

    /** 30 days */
    private static final long THIRTY_DAYS = 1*24*60*60*30;
    
    /** 1 years */
    private static final long ONE_YEARS = THIRTY_DAYS * 12;
    
    /**
     * Note: this method is primarily intended for use by the unit tests.
     * 
     * @param collection The MongoDB collection to use when logging events.
     */
    public void setCollection(final DBCollection collection) {
        assert collection != null : "collection must not be null";
        String indexName = "_timestamp";
        
        if (!isExistsIndex(collection, indexName)) {
            createIndex(collection, "timestamp", indexName, 1, true, ONE_YEARS);
        }
        this.collection = collection;
    }
    
    /**
     * @param dbCollection
     * @param fieldName 字段名称
     * @param indexName 索引名称
     * @param order 1 升序、-1 降序
     * @param expire 是否允许过期（默认30天）
     */
    public static void createIndex(DBCollection dbCollection, String fieldName, String indexName, int order, boolean expire, long expireSeconds) {
        DBObject option = new BasicDBObject();
        option.put("name", indexName);
        option.put("ns", dbCollection.getFullName());
        if (expire)
            option.put("expireAfterSeconds", expireSeconds);
        
        option.put("backgroud", true);
        dbCollection.ensureIndex(new BasicDBObject(fieldName, order), option);
    }

    public static boolean isExistsIndex(DBCollection dbCollection, String indexName) {
        List<DBObject> indexes = dbCollection.getIndexInfo();
        for (DBObject obj : indexes) {
            Object value = obj.get("name");
            if (StringUtils.equals(String.valueOf(value), indexName)) {
                return true;
            }
        }
        return false;
    }

    private String getInetAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        }
        catch (UnknownHostException e) {
            return "";
        }
    }

    /**
     * @see org.apache.log4j.Appender#close()
     */
    public void close() {
        if (mongo != null) {
            collection = null;
            mongo.close();
        }
    }

    /**
     * @return The hostname of the MongoDB server <i>(will not be null, empty or blank)</i>.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname The MongoDB hostname to set <i>(must not be null, empty or blank)</i>.
     */
    public void setHostname(final String hostname) {
        assert hostname != null : "hostname must not be null";
        assert hostname.trim().length() > 0 : "hostname must not be empty or blank";

        this.hostname = hostname;
    }

    /**
     * @return The port of the MongoDB server <i>(will be > 0)</i>.
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port The port to set <i>(must not be null, empty or blank)</i>.
     */
    public void setPort(final String port) {
        assert port != null : "port must not be null";
        assert port.trim().length() > 0 : "port must not be empty or blank";

        this.port = port;
    }

    /**
     * @return The database used in the MongoDB server <i>(will not be null, empty or blank)</i>.
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param databaseName The database to use in the MongoDB server <i>(must not be null, empty or blank)</i>.
     */
    public void setDatabaseName(final String databaseName) {
        assert databaseName != null : "database must not be null";
        assert databaseName.trim().length() > 0 : "database must not be empty or blank";

        this.databaseName = databaseName;
    }

    /**
     * @return The collection used within the database in the MongoDB server <i>(will not be null, empty or blank)</i>.
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param collectionName The collection used within the database in the MongoDB server <i>(must not be null, empty
     *            or blank)</i>.
     */
    public void setCollectionName(final String collectionName) {
        assert collectionName != null : "collection must not be null";
        assert collectionName.trim().length() > 0 : "collection must not be empty or blank";

        if (separate) {
            StringBuilder collectionNameSnapshot = new StringBuilder(128);
            String tmp = collectionName.endsWith("_") ? collectionName : collectionName + "_";
            collectionNameSnapshot.append(tmp);
            String inetAddress = getInetAddress();
            if (StringUtils.isNotBlank(inetAddress)) {
                collectionNameSnapshot.append(StringUtils.replace(inetAddress, ".", "_"));
            }
            this.collectionName = collectionNameSnapshot.toString();
        }
        else {
            this.collectionName = collectionName;
        }
    }

    /**
     * @return The userName used to authenticate with MongoDB <i>(may be null)</i>.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The userName to use when authenticating with MongoDB <i>(may be null)</i>.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @param password The password to use when authenticating with MongoDB <i>(may be null)</i>.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return the writeConcern setting for Mongo.
     */
    public String getWriteConcern() {
        return writeConcern;
    }

    /**
     * @param writeConcern The WriteConcern setting for Mongo.<i>(may be null). If null, set to default of
     *            dbCollection's writeConcern.</i>
     */
    public void setWriteConcern(final String writeConcern) {
        this.writeConcern = writeConcern;
        concern = WriteConcern.valueOf(writeConcern);
    }

    public WriteConcern getConcern() {
        if (concern == null) {
            concern = getCollection().getWriteConcern();
        }
        return concern;
    }

    public void setWriteStack(boolean writeStack) {
        this.writeStack = writeStack;
    }

    @Override
    protected boolean isWriteStack() {
        return this.writeStack;
    }
    
    public boolean isSeparate() {
        return separate;
    }
    
    public void setSeparate(boolean separate) {
        this.separate = separate;
    }

    /**
     * @param bson The BSON object to insert into a MongoDB database collection.
     */
    @Override
    public void append(final DBObject bson) {

        SERVICE.submit(new Runnable() {

            public void run() {
                if (initialized && bson != null) {
                    try {
                        getCollection().insert(bson, getConcern());
                    }
                    catch (MongoException e) {
                        errorHandler.error("Failed to insert document to MongoDB", e, ErrorCode.WRITE_FAILURE);
                    }
                }
            }
        });

    }

    /**
     * Returns true if appender was successfully initialized. If this method returns false, the appender should not
     * attempt to log events.
     * 
     * @return true if appender was successfully initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * 
     * @return The MongoDB collection to which events are logged.
     */
    protected DBCollection getCollection() {
        return collection;
    }

    /**
     * Returns a List of ServerAddress objects for each host specified in the hostname property. Returns an empty list
     * if configuration is detected to be invalid, e.g.:
     * <ul>
     * <li>Port property doesn't contain either one port or one port per host</li>
     * <li>After parsing port property to integers, there isn't either one port or one port per host</li>
     * </ul>
     * 
     * @param hostname Blank space delimited hostnames
     * @param port Blank space delimited ports. Must specify one port for all hosts or a port per host.
     * @return List of ServerAddresses to connect to
     */
    private List<ServerAddress> getServerAddresses(String hostname, String port) {
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();

        String[] hosts = hostname.split(" ");
        String[] ports = port.split(" ");

        if (ports.length != 1 && ports.length != hosts.length) {
            errorHandler.error("MongoDB appender port property must contain one port or a port per host", null,
                    ErrorCode.ADDRESS_PARSE_FAILURE);
        }
        else {
            List<Integer> portNums = getPortNums(ports);
            // Validate number of ports again after parsing
            if (portNums.size() != 1 && portNums.size() != hosts.length) {
                errorHandler.error("MongoDB appender port property must contain one port or a valid port per host",
                        null, ErrorCode.ADDRESS_PARSE_FAILURE);
            }
            else {
                boolean onePort = (portNums.size() == 1);

                int i = 0;
                for (String host : hosts) {
                    int portNum = (onePort) ? portNums.get(0) : portNums.get(i);
                    try {
                        addresses.add(new ServerAddress(host.trim(), portNum));
                    }
                    catch (UnknownHostException e) {
                        errorHandler.error("MongoDB appender hostname property contains unknown host", e,
                                ErrorCode.ADDRESS_PARSE_FAILURE);
                    }
                    i++;
                }
            }
        }
        return addresses;
    }

    private List<Integer> getPortNums(String[] ports) {
        List<Integer> portNums = new ArrayList<Integer>();

        for (String port : ports) {
            try {
                Integer portNum = Integer.valueOf(port.trim());
                if (portNum < 0) {
                    errorHandler.error("MongoDB appender port property can't contain a negative integer", null,
                            ErrorCode.ADDRESS_PARSE_FAILURE);
                }
                else {
                    portNums.add(portNum);
                }
            }
            catch (NumberFormatException e) {
                errorHandler.error("MongoDB appender can't parse a port property value into an integer", e,
                        ErrorCode.ADDRESS_PARSE_FAILURE);
            }

        }

        return portNums;
    }

}
