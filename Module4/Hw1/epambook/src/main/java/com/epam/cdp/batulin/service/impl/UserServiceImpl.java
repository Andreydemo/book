package com.epam.cdp.batulin.service.impl;

        import com.epam.cdp.batulin.entity.Note;
        import com.epam.cdp.batulin.entity.User;
        import com.epam.cdp.batulin.exception.UserNotCreatedException;
        import com.epam.cdp.batulin.exception.UserNotFoundException;
        import com.epam.cdp.batulin.exception.UsersAreNotFriendsException;
        import com.epam.cdp.batulin.repository.UserRepository;
        import com.epam.cdp.batulin.service.UserService;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.warn("Cannot create user, username is taken");
            throw new UserNotCreatedException("User with such username already exists");
        }

        User createdUser = userRepository.save(user);
        logger.debug("Created user = " + createdUser);
        return createdUser;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Cannot find user by username = " + username);
            throw new UserNotFoundException("user with username = " + username + " cannot be found");
        }

        logger.debug("Returning user by username = " + username + ", " + user);
        return user;
    }

    @Override
    @Transactional
    public User addUserToFriendList(String username, String friendUserName) {
        logger.debug("Adding user " + friendUserName + " to " + username + "'s friend list");
        User user = getUserByUsername(username);
        User friend = getUserByUsername(friendUserName);
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        userRepository.save(friend);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User addNoteToUsersTimeline(User user, Note note) {
        User userFromRepository = getUserByUsername(user.getUsername());
        userFromRepository.getTimeline().add(note);
        logger.debug("Adding note " + note + " to " + user.getUsername() + "'s timeline");
        return userRepository.save(userFromRepository);
    }

    @Override
    @Transactional
    public void checkIfUsersAreFriends(User user, User friend) {
        if (!user.getFriends().contains(friend) && !friend.getFriends().contains(user)) {
            String message = "Users " + user.getUsername() + " and " + friend.getUsername() + " are not friends, throwing UsersAreNotFriendsException";
            logger.warn(message);
            throw new UsersAreNotFriendsException(message);
        }
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}