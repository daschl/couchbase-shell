package com.couchbase.shell;

import net.spy.memcached.internal.GetFuture;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.ops.OperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class CouchbaseBucketCommands implements CommandMarker {

    @Autowired
    private CouchbaseShell shell;

    private static final String GET = "bucket get";
    private static final String SET = "bucket set";
    private static final String ADD = "bucket add";
    private static final String REPLACE = "bucket replace";
    private static final String DELETE = "bucket delete";
    private static final String COUNT_DOCS = "bucket count-docs";

    @CliAvailabilityIndicator({GET, SET, ADD, REPLACE, DELETE, COUNT_DOCS})
    public boolean isConnected() {
        return shell.isConnected();
    }

    @CliCommand(value = GET, help = "Retreive a document from the server.")
    public String get(
        @CliOption(
                mandatory = true, key = "key", help = "The unique name of the key in this bucket"
        ) String key
    ) {
        try {
            GetFuture<Object> future = shell.get(key);
            future.get();
            StringBuilder builder = new StringBuilder();
            builder.append(formatStatusLine(future.getStatus()));
            Object data = future.get();
            if (data != null) {
                builder.append("\n" + future.get());
            }
            return builder.toString();
        } catch (Exception ex) {
            return "Could not get document " + key + " because of an error!";
        }
    }

    @CliCommand(value = SET, help = "Store a value on the server")
    public String set(
        @CliOption(
            mandatory = true, key = "key", help = "The unique name of the key in this bucket"
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
                    mandatory = true, key = "key", help = "The unique name of the key in this bucket"
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
                    mandatory = true, key = "key", help = "The unique name of the key in this bucket"
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
                    mandatory = true, key = "key", help = "The unique name of the key in this bucket"
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

    private String formatStatusLine(OperationStatus status) {
        return "Success: " + status.isSuccess() + ", Message: " + status.getMessage();
    }


}
