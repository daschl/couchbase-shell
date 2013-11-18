package com.couchbase.shell;

import com.couchbase.client.CouchbaseClient;
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
    private CouchbaseClient client;
    private String bucket;

    public CouchbaseShell() {
        connected = false;

        Properties systemProperties = System.getProperties();
        systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperties(systemProperties);
    }

    public boolean connect(String hostname, String bucket, String password) {
        this.bucket = bucket;
        if (connected) {
            return true;
        }

        try {
            client = new CouchbaseClient(
                    Arrays.asList(new URI("http://" + hostname + ":8091/pools")),
                    bucket,
                    password
            );
            connected = true;
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void disconnect() {
        client.shutdown();
        client = null;
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public int countDocs() {
        // TODO: load lis

        return 0;
    }

    public String getBucket() {
        return bucket;
    }

    GetFuture<Object> get(String key) {
        return client.asyncGet(key);
    }

    OperationFuture<Boolean> set(String key, Object value, int exp) {
        return client.set(key, exp, value);
    }

    OperationFuture<Boolean> add(String key, Object value, int exp) {
        return client.add(key, exp, value);
    }

    OperationFuture<Boolean> replace(String key, Object value, int exp) {
        return client.replace(key, exp, value);
    }

    OperationFuture<Boolean> delete(String key) {
        return client.delete(key);
    }

}
