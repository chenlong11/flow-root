package com.nuvole.flow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRepresentation {

    private String id;
    private String taskName;
    private String taskKey;
    private String procInstId;
    private String procDefId;
    private String businessKey;
    private String assignee;
    private String orderName;
    private String orderCode;
    private Date orderCreateTime;
    private Date taskCreateTime;

}
