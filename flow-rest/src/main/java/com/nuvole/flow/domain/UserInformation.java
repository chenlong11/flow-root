package com.nuvole.flow.domain;

import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;

import java.util.List;

public class UserInformation {

    protected User user;
    protected List<Group> groups;
    protected List<String> privileges;

    public UserInformation() {

    }

    public UserInformation(User user, List<Group> groups, List<String> privileges) {
        this.user = user;
        this.groups = groups;
        this.privileges = privileges;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

}
