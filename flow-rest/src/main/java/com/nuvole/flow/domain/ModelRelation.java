package com.nuvole.flow.domain;

public class ModelRelation {

    private String id;
    private String parentModelId;
    private String modelId;
    private String type;

    public ModelRelation() {

    }

    public ModelRelation(String parentModelId, String modelId, String type) {
        this.parentModelId = parentModelId;
        this.modelId = modelId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentModelId() {
        return parentModelId;
    }

    public void setParentModelId(String parentModelId) {
        this.parentModelId = parentModelId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
