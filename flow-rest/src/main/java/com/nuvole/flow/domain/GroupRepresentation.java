package com.nuvole.flow.domain;

import org.flowable.idm.api.Group;

public class GroupRepresentation extends AbstractRepresentation {

    protected String id;
    protected String name;
    protected String type;

    public GroupRepresentation() {
    }

    public GroupRepresentation(Group group) {
        setId(group.getId());
        setName(group.getName());
        setType(group.getType());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
