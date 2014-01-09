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
        ) String password,
        @CliOption(
            key = "query", help = "Optional N1QL hostname",
            unspecifiedDefaultValue = "", specifiedDefaultValue = "127.0.0.1"
        ) String query
    ) {
        try {
            shell.connect(hostname, bucket, password, query);
            return "Connected.";
        } catch(Exception ex) {
            return "Could not connect! " + ex.getMessage();
        }
    }

    @CliCommand(value = DISCONNECT, help = "Disconnect from a Cluster")
    public String disconnect() {
        shell.disconnect();
        return "Disconnected.";
    }

}
