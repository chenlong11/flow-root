package com.nuvole.flow.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：16:49
 */
@Data
public class AbstractModel {

    public static final int MODEL_TYPE_BPMN = 0;
    public static final int MODEL_TYPE_FORM = 2;
    public static final int MODEL_TYPE_APP = 3;
    public static final int MODEL_TYPE_DECISION_TABLE = 4;

    protected String id;
    protected String name;
    protected String key;
    protected String description;
    protected Date created;
    protected Date lastUpdated;
    private String createdBy;
    private String lastUpdatedBy;
    protected int version;
    protected String modelEditorJson;
    protected String comment;
    protected Integer modelType;

    public AbstractModel() {
        this.created = new Date();
    }

}
