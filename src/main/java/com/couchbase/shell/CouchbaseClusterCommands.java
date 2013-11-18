package com.couchbase.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class CouchbaseClusterCommands implements CommandMarker {

    @Autowired
    private CouchbaseShell shell;

    private static final String CONNECT = "connect";

    private static final String DISCONNECT = "disconnect";

    @CliAvailabilityIndicator({DISCONNECT})
    public boolean isConnected() {
        return shell.isConnected();
    }

    @CliAvailabilityIndicator({CONNECT})
    public boolean isDisconnected() {
        return !shell.isConnected();
    }

    @CliCommand(value = CONNECT, help = "Connect to a Cluster")
    public String connect(
        @CliOption(
            key = "hostname", help = "The hostname/ip from one node in the cluster",
            unspecifiedDefaultValue = "127.0.0.1"
        ) String hostname,
        @CliOption(
            key = "bucket", help = "Name of the bucket in the cluster",
            unspecifiedDefaultValue = "default"
        ) String bucket,
        @CliOption(
            key = "password", help = "The password of the bucket in the cluster",
            unspecifiedDefaultValue = ""
        ) String password
    ) {
        if (shell.connect(hostname, bucket, password)) {
            return "Connected.";
        } else {
            return "Could not connect!";
        }
    }

    @CliCommand(value = DISCONNECT, help = "Disconnect from a Cluster")
    public String disconnect() {
        shell.disconnect();
        return "Disconnected.";
    }

}
