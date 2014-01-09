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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.shell.Bootstrap;
import org.springframework.shell.core.CommandResult;
import org.springframework.shell.core.JLineShellComponent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by michaelnitschinger on 09/01/14.
 */
public class AbstractShellIntegrationTest {

    private static final Log LOGGER = LogFactory.getLog(AbstractShellIntegrationTest.class);

    private JLineShellComponent shell;

    @Before
    public void startUp() {
        Bootstrap bootstrap = new Bootstrap();
        shell = bootstrap.getJLineShellComponent();
    }

    @After
    public void shutdown() {
        LOGGER.info("Stopping Shell");
        shell.stop();
    }

    public JLineShellComponent getShell() {
        return shell;
    }

    protected CommandResult executeCommand(String command) {
        CommandResult cr = getShell().executeCommand(command);
        assertTrue("Failure.  CommandResult = " + cr.toString(), cr.isSuccess());
        return cr;
    }

    protected CommandResult executeCommandExpectingFailure(String command) {
        CommandResult cr = getShell().executeCommand(command);
        assertFalse("Expected command to fail.  CommandResult = " + cr.toString(), cr.isSuccess());
        return cr;
    }
}
