/**
 * Copyright (C) 2014 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.couchbase.shell;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseClientIF;
import com.couchbase.client.CouchbaseQueryClient;
import com.couchbase.client.mapping.QueryResult;
import net.spy.memcached.CASValue;
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

    public boolean connect(String hostname, String bucket, String password, String query) throws Exception {
        this.bucket = bucket;
        if (connected) {
            return true;
        }

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

    GetFuture<CASValue<Object>> get(String key) {
        return (GetFuture<CASValue<Object>>) client.asyncGets(key);
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
