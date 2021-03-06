package com.epam.cdp.batulin.entity;

import com.epam.cdp.batulin.entity.validator.CreateUser;
import com.epam.cdp.batulin.entity.validator.UpdateUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 3, max = 128, message = "{Size.user.password}")
    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.password}")
    private String password;

    @NotNull(groups = {CreateUser.class}, message = "{NotNull.user.dateOfBirth}")
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @JsonProperty(value = "friends")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
    private List<User> friends;

    @ElementCollection
    private List<Long> noteIdList;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<Role>();

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
        this.noteIdList = new ArrayList<>();
    }

    public User(User user) {
        super();
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getNoteIdList() {
        return noteIdList;
    }

    public void setNoteIdList(List<Long> noteIdList) {
        this.noteIdList = noteIdList;
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
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}