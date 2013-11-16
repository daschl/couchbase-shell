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
        return "1.0.0-SNAPSHOT";
    }

    public String getWelcomeMessage() {
        return "Welcome to the interactive Couchbase Shell!";
    }

    @Override
    public String getProviderName() {
        return "cb-banner";
    }
}
