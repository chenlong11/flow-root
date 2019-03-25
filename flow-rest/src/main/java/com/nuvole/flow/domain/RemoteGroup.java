
package com.nuvole.flow.domain;

import org.flowable.idm.api.Group;

public class RemoteGroup implements Group {

    protected String id;
    protected String name;

    public RemoteGroup() {

    }

    public RemoteGroup(String id, String name) {
        this.id = id;
        this.name = name;
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
        // not supported
        return null;
    }

    public void setType(String string) {
        // not supported
    }

}
