package com.epam.cdp.batulin.cqrs.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddFriendCommand implements UserCommand {
    private String adderUsername;

    @JsonProperty(value = "username")
    private String friendUsername;

    @Override
    public String getUsername() {
        return adderUsername;
    }

    public void setAdderUsername(String adderUsername) {
        this.adderUsername = adderUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    @Override
    public String toString() {
        return "AddFriendCommand{" +
                "adderUsername='" + adderUsername + '\'' +
                ", friendUsername='" + friendUsername + '\'' +
                '}';
    }
}
