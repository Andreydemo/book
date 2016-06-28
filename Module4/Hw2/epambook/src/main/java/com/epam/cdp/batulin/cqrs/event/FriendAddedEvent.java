package com.epam.cdp.batulin.cqrs.event;

public class FriendAddedEvent extends UserEvent {
    private String friendUsername;

    public FriendAddedEvent(String username, String friendUsername) {
        super(username);
        this.friendUsername = friendUsername;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }
}