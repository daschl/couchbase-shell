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

import org.springframework.shell.plugin.support.DefaultBannerProvider;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CouchbaseBannerProvider extends DefaultBannerProvider {

    public String getBanner() {
        StringBuffer buf = new StringBuffer();
        buf.append(".--.             .    .                      .-. .        . ."  + OsUtils.LINE_SEPARATOR);
        buf.append(":                |    |                     (   )|        | |" + OsUtils.LINE_SEPARATOR);
        buf.append("|    .-. .  . .-.|--. |.-.  .-.  .--. .-.    `-. |--. .-. | |" + OsUtils.LINE_SEPARATOR);
        buf.append(":   (   )|  |(   |  | |   )(   ) `--.(.-'   (   )|  |(.-' | |" + OsUtils.LINE_SEPARATOR);
        buf.append("`--'`-' `--`-`-''  `-'`-'  `-'`-`--' `--'   `-' '  `-`--'`-`-" + OsUtils.LINE_SEPARATOR);
        buf.append("Version:" + this.getVersion());
        return buf.toString();
    }

    public String getVersion() {
        return "0.1";
    }

    public String getWelcomeMessage() {
        return "Welcome to the interactive Couchbase Shell!";
    }

    @Override
    public String getProviderName() {
        return "cb-banner";
    }
}
