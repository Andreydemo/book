package com.epam.cdp.web.controller;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.User;
import com.epam.cdp.model.UserAccount;
import com.epam.cdp.model.impl.UserImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller that receives requests for user entity.
 */
@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class);
    private static final String MAX_INT_AS_STRING = "2147483647";
    private BookingFacade bookingFacade;

    @Autowired
    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    /**
     * Gets user by its id.
     *
     * @param userId id of user to return.
     * @return User for specified id.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUserById(@PathVariable long userId) {
        User user = bookingFacade.getUserById(userId);
        logger.debug("Returning user by id: " + userId + ", " + user);
        return user;
    }

    /**
     * Gets user by its email. Email is strictly matched.
     *
     * @param email email to find user by.
     * @return User for specified id.
     */
    @RequestMapping(value = "/email/{email:.+}", method = RequestMethod.GET)
    public
    @ResponseBody
    User getUserByEmail(@PathVariable String email) {
        User user = bookingFacade.getUserByEmail(email);
        logger.debug("Returning user by email: " + email + ", " + user);
        return user;
    }

    /**
     * Get list of users by matching name. Name is matched using 'contains' approach.
     *
     * @param name     Users name or it's part.
     * @param pageSize Pagination param. Number of users to return on a page.
     * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
     * @return List of users for specified name of empty list if there are no users.
     */
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> getUsersByName(@PathVariable String name,
                              @RequestParam(defaultValue = MAX_INT_AS_STRING) int pageSize,
                              @RequestParam(defaultValue = "1") int pageNum) {
        List<User> users = bookingFacade.getUsersByName(name, pageSize, pageNum);
        logger.debug("Returning users by name: " + name + " with pageSize: " + pageSize + " and pageNum: " + pageNum + " " + users);
        return users;
    }

    /**
     * Creates new user. User id is auto-generated.
     *
     * @param user user to create.
     * @return Created User.
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    User createUser(@RequestBody UserImpl user) {
        User createdUser = bookingFacade.createUser(user);
        logger.debug("Creating user: " + user);
        return createdUser;
    }

    /**
     * Updates user using given data.
     *
     * @param user user to update.
     * @return Updated user.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    User updateUser(@RequestBody UserImpl user) {
        User updatedUser = bookingFacade.updateUser(user);
        logger.debug("Updating user: " + user);
        return updatedUser;
    }

    /**
     * Deletes user by its id.
     *
     * @param userId id of user to delete.
     * @return boolean value whether user was deleted.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long userId, HttpServletResponse response) {
        logger.debug("Deleting user by id " + userId);
        boolean deleted = bookingFacade.deleteUser(userId);
        if (!deleted)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Refills user account and returns refilled UserAccount.
     *
     * @param userId id of user to refill account.
     * @param amount amount to refill.
     * @return Refilled UserAccount.
     */
    @RequestMapping(value = "/{userId}/balance", method = RequestMethod.POST)
    public
    @ResponseBody
    UserAccount refillAccount(@PathVariable long userId, @RequestParam BigDecimal amount) {
        logger.debug("Refilled UserAccount for userId = " + userId + ", amount = " + amount);
        return bookingFacade.refillAccount(userId, amount);
    }
}