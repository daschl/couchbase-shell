package com.couchbase.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CouchbasePromptProvider implements PromptProvider {

    @Autowired
    private CouchbaseShell shell;

    @Override
    public String getPrompt() {
        return "cb-shell>";
    }

    @Override
    public String getProviderName() {
        return "cb-prompt";
    }
}