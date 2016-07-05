package com.epam.cdp.batulin.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Date;

@JsonRootName(value = "user")
public class UserDto {
    private String name;

    private String username;

    private Date dateOfBirth;

    public UserDto() {
    }

    public UserDto(String name, String username, Date dateOfBirth) {
        this.name = name;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
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
        return "UserDto{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}