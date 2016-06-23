package com.epam.cdp.batulin.service;

import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    User createUser(User user);

    User getUserByUsername(String username);

    User addUserToFriendList(String username, String friendUserName);

    User addNoteToUsersTimeline(User user, Note note);

    @Transactional
    void checkIfUsersAreFriends(User user, User friend);

    User updateUser(User user);
}
