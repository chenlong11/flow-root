package com.nuvole.flow.domain;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

public class CommonJumpTaskCmd implements Command<Void> {

    protected String taskId;
    protected String target;

    public CommonJumpTaskCmd(String taskId, String target) {
        this.taskId = taskId;
        this.target = target;
    }


    @Override
    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        TaskService taskService = CommandContextUtil.getTaskService();
        TaskEntity task = taskService.getTask(taskId);
        ExecutionEntity ee = executionEntityManager.findById(task.getExecutionId());
        Process process = ProcessDefinitionUtil.getProcess(ee.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(target);
        ee.setCurrentFlowElement(targetFlowElement);
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        agenda.planContinueProcessInCompensation(ee);
        //taskService.deleteTask(task, true);
        return null;
    }
}
