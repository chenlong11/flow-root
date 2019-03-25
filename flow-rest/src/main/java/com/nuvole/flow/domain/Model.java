package com.nuvole.flow.domain;

import lombok.Data;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：16:50
 */
@Data
public class Model extends AbstractModel {

    public Model(String id, String deploymentId, String processDefId,String processDefKey) {
        this.id = id;
        this.deploymentId = deploymentId;
        this.processDefId = processDefId;
        this.processDefKey = processDefKey;
    }

    private byte[] thumbnail;

    private String deploymentId;

    private String processDefId;

    private String processDefKey;

    public Model() {
        super();
    }

}
