package com.nuvole.flow.mapper;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.SequenceFlow;

public class SequenceFlowInfoMapper extends AbstractInfoMapper {

    protected void mapProperties(Object element) {
        SequenceFlow sequenceFlow = (SequenceFlow) element;

        if (StringUtils.isNotEmpty(sequenceFlow.getConditionExpression())) {
            createPropertyNode("Condition expression", sequenceFlow.getConditionExpression());
        }

        createListenerPropertyNodes("Execution listeners", sequenceFlow.getExecutionListeners());
    }
}
