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

import com.couchbase.client.mapping.QueryError;
import com.couchbase.client.mapping.QueryResult;
import com.couchbase.client.mapping.QueryRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.spy.memcached.CASValue;
import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.ops.Operation;
import net.spy.memcached.ops.OperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class CouchbaseBucketCommands implements CommandMarker {

    @Autowired
    private CouchbaseShell shell;

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String ADD = "add";
    private static final String REPLACE = "replace";
    private static final String DELETE = "delete";
    private static final String COUNT_DOCS = "count-docs";
    private static final String QUERY = "query";
    private static final String GETPING = "gping";

    @CliAvailabilityIndicator({GET, SET, ADD, REPLACE, DELETE, COUNT_DOCS, GETPING})
    public boolean isConnected() {
        return shell.isConnected();
    }

    @CliAvailabilityIndicator({QUERY})
    public boolean hasQuery() {
        return shell.isConnected() && shell.hasQuery();
    }

    @CliCommand(value = GET, help = "Retreive a document from the server.")
    public String get(
        @CliOption(
                mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
        ) String key
    ) {
        try {
            OperationFuture<CASValue<Object>> future = shell.get(key);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            CASValue<Object> data = future.get();
            if (future.getStatus().isSuccess()) {
                builder.append(", CAS: " + data.getCas());
            }
            if (data != null) {
                builder.append("\n" + data.getValue());
            }
            return builder.toString();
        } catch (Exception ex) {
            return "Could not get document " + key + " because of an error! ";
        }
    }

    @CliCommand(value = GETPING, help = "Retreive a document from the server for a perioud and count the time.")
    public String getping(
            @CliOption(
                    mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
            ) String key,
            @CliOption(
                    key = "runs", help = "The number of pings to perform", unspecifiedDefaultValue = "10"
            ) String runs,
            @CliOption(
                    key = "delay", help = "Time to sleep between pings", unspecifiedDefaultValue = "0"
            ) String delay
    ) {
        try {
            int totalRuns = Integer.parseInt(runs);
            int del = Integer.parseInt(delay);

            StringBuilder builder = new StringBuilder();
            long total = 0;
            long totalMin = Integer.MAX_VALUE;
            long totalMax = Integer.MIN_VALUE;
            for (int i = 0; i < totalRuns; i++) {
                long start = System.nanoTime();
                OperationFuture<CASValue<Object>> future = shell.get(key);
                future.get();
                long end = System.nanoTime();
                builder.append(formatStatusLine(future.getStatus()));
                CASValue<Object> data = future.get();
                if (future.getStatus().isSuccess()) {
                    builder.append(", CAS: " + data.getCas());
                }
                long diff = end - start;
                total += diff;
                builder.append(", Time: " + TimeUnit.NANOSECONDS.toMicros(diff) + "µs");
                if (diff < totalMin) {
                    totalMin = diff;
                }
                if (diff > totalMax) {
                    totalMax = diff;
                }
                builder.append("\n");
                Thread.sleep(del);
            }
            builder.append("Total: " + TimeUnit.NANOSECONDS.toMicros(total) + "µs");
            builder.append(", Average: " + TimeUnit.NANOSECONDS.toMicros(total / totalRuns) + "µs");
            builder.append(", Min: " + TimeUnit.NANOSECONDS.toMicros(totalMin) + "µs");
            builder.append(", Max: " + TimeUnit.NANOSECONDS.toMicros(totalMax) + "µs");
            return builder.toString();
        } catch (Exception ex) {
            return "Could not get document " + key + " because of an error! ";
        }
    }

    @CliCommand(value = SET, help = "Store a value on the server")
    public String set(
        @CliOption(
            mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
        ) String key,
        @CliOption(
            mandatory = true, key = "value", help = "The actual value of the document"
        ) String value,
        @CliOption(
            key = "expiration", help = "The optional expiration time", unspecifiedDefaultValue = "0"
        ) String expiration
    ) {
        try {
            int exp = Integer.parseInt(expiration);
            OperationFuture<Boolean> future = shell.set(key, value, exp);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            return builder.toString();
        } catch(Exception ex) {
            return "Could not set document " + key + "because of an error!";
        }
    }


    @CliCommand(value = ADD, help = "Add a value on the server")
    public String add(
            @CliOption(
                    mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
            ) String key,
            @CliOption(
                    mandatory = true, key = "value", help = "The actual value of the document"
            ) String value,
            @CliOption(
                    key = "expiration", help = "The optional expiration time", unspecifiedDefaultValue = "0"
            ) String expiration
    ) {
        try {
            int exp = Integer.parseInt(expiration);
            OperationFuture<Boolean> future = shell.add(key, value, exp);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            return builder.toString();
        } catch(Exception ex) {
            return "Could not add document " + key + "because of an error!";
        }
    }


    @CliCommand(value = REPLACE, help = "Replace a value on the server")
    public String replace(
            @CliOption(
                    mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
            ) String key,
            @CliOption(
                    mandatory = true, key = "value", help = "The actual value of the document"
            ) String value,
            @CliOption(
                    key = "expiration", help = "The optional expiration time", unspecifiedDefaultValue = "0"
            ) String expiration
    ) {
        try {
            int exp = Integer.parseInt(expiration);
            OperationFuture<Boolean> future = shell.replace(key, value, exp);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            return builder.toString();
        } catch(Exception ex) {
            return "Could not replace document " + key + "because of an error!";
        }
    }

    @CliCommand(value = DELETE, help = "DELETE a value on the server")
    public String delete(
            @CliOption(
                    mandatory = true, key = { "", "key" }, help = "The unique name of the key in this bucket"
            ) String key
    ) {
        try {
            OperationFuture<Boolean> future = shell.delete(key);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            return builder.toString();
        } catch(Exception ex) {
            return "Could not delete document " + key + "because of an error!";
        }
    }

    @CliCommand(value = COUNT_DOCS, help = "Count the number of documents in the bucket")
    public String countDocs() {
        try {
            return shell.countDocs() + " documents found.";
        } catch(Exception ex) {
            return "Could not count the number of documents in the bucket!";
        }
    }

    @CliCommand(value = QUERY, help = "Query using N1QL")
    public String query(
        @CliOption(
                mandatory = true, key = { "", "query" }, help = "The actual query to run"
        ) String query
    ) {
        if (shell.hasQuery()) {
            StringBuilder sb = new StringBuilder();
            QueryResult res = shell.query(query);
            if (res.isSuccess()) {
                for (QueryRow row : res.getResult()) {
                    try {
                        sb.append(row.toString());
                    } catch (Exception ex) {
                        sb.append("Error while parsing: " + ex.getMessage());
                    }
                }
            } else {
                sb.append(formatQueryError(res.getError()));
            }
            return sb.toString();
        } else {
            return "Query engine is not connected.";
        }
    }

    private String formatStatusLine(OperationStatus status) {
        return "Success: " + status.isSuccess() + ", Message: " + status.getMessage();
    }

    private String formatQueryError(QueryError error) {
        return "Code: " + error.getCode() + ", Message: " + error.getMessage();
    }


}
