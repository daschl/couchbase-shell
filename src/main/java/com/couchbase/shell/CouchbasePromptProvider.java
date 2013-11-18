package com.couchbase.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CouchbasePromptProvider implements PromptProvider {

    @Autowired
    private CouchbaseShell shell;

    @Override
    public String getPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("cb-shell");
        if (shell.isConnected()) {
            sb.append("[" + shell.getBucket() + "]");
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public String getProviderName() {
        return "cb-prompt";
    }
}