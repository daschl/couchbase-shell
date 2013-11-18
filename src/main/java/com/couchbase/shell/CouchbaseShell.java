package com.couchbase.shell;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseClientIF;
import com.couchbase.client.CouchbaseQueryClient;
import com.couchbase.client.mapping.QueryResult;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

/**
 * Basic encapsulation for a {@link com.couchbase.client.CouchbaseClient}.
 */
@Component
public class CouchbaseShell {

    private boolean connected;
    private CouchbaseClientIF client;
    private String bucket;
    private boolean hasQuery;

    public CouchbaseShell() {
        connected = false;
        hasQuery = false;

        Properties systemProperties = System.getProperties();
        systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperties(systemProperties);
    }

    public boolean connect(String hostname, String bucket, String password, String query) {
        this.bucket = bucket;
        if (connected) {
            return true;
        }

        try {
            if (query != null && !query.isEmpty()) {
                client = new CouchbaseQueryClient(
                        Arrays.asList(hostname),
                        Arrays.asList(query),
                        bucket,
                        password);
                hasQuery = true;
            } else {
                client = new CouchbaseClient(
                        Arrays.asList(new URI("http://" + hostname + ":8091/pools")),
                        bucket,
                        password
                );
            }
            connected = true;
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean hasQuery() {
        return hasQuery;
    }

    public void disconnect() {
        client.shutdown();
        client = null;
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public QueryResult query(String query) {
        if (hasQuery) {
            return ((CouchbaseQueryClient) client).query(query);
        } else {
            throw new IllegalStateException("Not connected to N1QL");
        }
    }

    public int countDocs() {
        // TODO: load lis

        return 0;
    }

    public String getBucket() {
        return bucket;
    }

    GetFuture<Object> get(String key) {
        return (GetFuture<Object>) client.asyncGet(key);
    }

    OperationFuture<Boolean> set(String key, Object value, int exp) {
        return (OperationFuture<Boolean>) client.set(key, exp, value);
    }

    OperationFuture<Boolean> add(String key, Object value, int exp) {
        return (OperationFuture<Boolean>) client.add(key, exp, value);
    }

    OperationFuture<Boolean> replace(String key, Object value, int exp) {
        return (OperationFuture<Boolean>) client.replace(key, exp, value);
    }

    OperationFuture<Boolean> delete(String key) {
        return (OperationFuture<Boolean>) client.delete(key);
    }

}
