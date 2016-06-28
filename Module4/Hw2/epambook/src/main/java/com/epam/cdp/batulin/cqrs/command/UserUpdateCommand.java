package com.epam.cdp.batulin.cqrs.command;

import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserUpdateCommand implements UserCommand {
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 3, max = 128, message = "{Size.user.name}")
    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.name}")
    private String name;

    @Size(groups = {CreateUser.class}, min = 3, max = 128, message = "{Size.user.username}")
    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.username}")
    private String username;

    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.dateOfBirth}")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    public UserUpdateCommand(String name, String username, Date dateOfBirth) {
        this.name = name;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
    }

    public UserUpdateCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "UserUpdateCommand{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
