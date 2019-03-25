package com.nuvole.flow.domain;

import lombok.Data;

import java.util.Date;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：16:52
 */
@Data
public class ModelHistory extends AbstractModel {

    protected String modelId;
    protected Date removalDate;

    public ModelHistory() {
        super();
    }
}
