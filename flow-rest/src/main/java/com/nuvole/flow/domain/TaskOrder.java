package com.nuvole.flow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskOrder {

    private String id;
    private String orderName;
    private String orderCode;
    private String creater;
    private String createdBy;
    private String createTime;

}
