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

import org.junit.Test;
import org.springframework.shell.core.CommandResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CouchbaseClusterCommandsTest extends AbstractShellIntegrationTest {

    @Test
    public void shouldConnectToLocalhost() {
        CommandResult cr = executeCommand("connect");
        assertEquals("Connected.", cr.getResult().toString());
    }

    @Test
    public void shouldConnectWithCustomParams() {
        CommandResult cr = executeCommand("connect --hostname 127.0.0.1 --bucket default");
        assertEquals("Connected.", cr.getResult().toString());
    }

    @Test
    public void shouldFailConnectWithInvalidIP() {
        CommandResult cr = executeCommand("connect --hostname foobar");
        assertTrue(cr.getResult().toString().startsWith("Could not connect!"));
    }

    @Test
    public void shouldFailConnectWithInvalidBucket() {
        CommandResult cr = executeCommand("connect --hostname 127.0.0.1 --bucket foobar");
        assertTrue(cr.getResult().toString().startsWith("Could not connect!"));
    }

    @Test
    public void shouldFailConnectWithInvalidPassword() {
        CommandResult cr = executeCommand("connect --hostname 127.0.0.1 --bucket default --password foo");
        assertTrue(cr.getResult().toString().startsWith("Could not connect!"));
    }

    @Test
    public void shouldDisconnectAfterConnect() {
        CommandResult cr = executeCommand("connect");
        assertEquals("Connected.", cr.getResult().toString());

        cr = executeCommand("disconnect");
        assertEquals("Disconnected.", cr.getResult().toString());
    }

    @Test
    public void shouldNotAllowDisconnectWhenDisconnected() {
        executeCommandExpectingFailure("disconnect");
    }

    @Test
    public void shouldNotAllowConnectWhenConnected() {
        CommandResult cr = executeCommand("connect");
        assertEquals("Connected.", cr.getResult().toString());

        executeCommandExpectingFailure("connect");
    }
}
