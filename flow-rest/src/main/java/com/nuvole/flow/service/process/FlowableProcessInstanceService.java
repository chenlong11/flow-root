package com.nuvole.flow.service.process;

import com.nuvole.flow.domain.ProcessInstanceRepresentation;
import org.flowable.content.api.ContentService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.form.api.FormInfo;
import org.flowable.form.api.FormService;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class FlowableProcessInstanceService {

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected FormService formService;

    @Autowired
    protected ContentService contentService;

    @Autowired
    private IdmIdentityService idmIdentityService;

    public ProcessInstanceRepresentation getProcessInstance(String processInstanceId, HttpServletResponse response) {

        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        User user = idmIdentityService.createUserQuery()
                .userId(processInstance.getStartUserId())
                .singleResult();

        ProcessInstanceRepresentation processInstanceResult = new ProcessInstanceRepresentation(processInstance, processDefinition, processDefinition.isGraphicalNotationDefined(), user);

        if (processDefinition.hasStartFormKey()) {
            FormInfo formInfo = runtimeService.getStartFormModel(processInstance.getProcessDefinitionId(), processInstance.getId());
            if (formInfo != null) {
                processInstanceResult.setStartFormDefined(true);
            }
        }
        return processInstanceResult;
    }

}
