package com.nuvole.flow.mapper;

import org.flowable.bpmn.model.ReceiveTask;

public class ReceiveTaskInfoMapper extends AbstractInfoMapper {

    protected void mapProperties(Object element) {
        ReceiveTask receiveTask = (ReceiveTask) element;
        createListenerPropertyNodes("Execution listeners", receiveTask.getExecutionListeners());
    }
}
