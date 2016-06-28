package com.epam.cdp.batulin.entity;

import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@JsonIgnoreProperties({"id", "timeline", "friends"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 3, max = 128, message = "{Size.user.name}")
    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.name}")
    private String name;

    @Size(groups = {CreateUser.class}, min = 3, max = 128, message = "{Size.user.username}")
    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.username}")
    private String username;

    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.dateOfBirth}")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @JsonProperty(value = "friends")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
    private List<User> friends;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Note> timeline;

    public User() {
    }

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public User(String username, String name, Date dateOfBirth) {
        this(username, name);
        this.dateOfBirth = dateOfBirth;
        this.friends = new ArrayList<>();
        timeline = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Note> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<Note> timeline) {
        this.timeline = timeline;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(username, user.username) &&
                Objects.equals(dateOfBirth, user.dateOfBirth) &&
                Objects.equals(friends, user.friends) &&
                Objects.equals(timeline, user.timeline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, dateOfBirth, friends, timeline);
    }
}