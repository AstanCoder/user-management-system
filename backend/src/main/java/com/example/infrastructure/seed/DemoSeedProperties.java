package com.example.infrastructure.seed;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.seed")
public class DemoSeedProperties {

    private boolean enabled = false;
    private boolean exitAfterRun = false;
    private int targetContacts = 1200;
    private int targetUsers = 24;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isExitAfterRun() {
        return exitAfterRun;
    }

    public void setExitAfterRun(boolean exitAfterRun) {
        this.exitAfterRun = exitAfterRun;
    }

    public int getTargetContacts() {
        return targetContacts;
    }

    public void setTargetContacts(int targetContacts) {
        this.targetContacts = targetContacts;
    }

    public int getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(int targetUsers) {
        this.targetUsers = targetUsers;
    }
}
