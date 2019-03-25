package com.nuvole.flow.service.process;

import com.nuvole.flow.domain.TaskRepresentation;
import com.nuvole.flow.mapper.TaskMapper;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlong
 * Date：2017/9/22
 * time：9:54
 */
@Service
public class FlowServiceImpl implements FlowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdmIdentityService idmIdentityService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private HistoryService historyService;


    @Override
    public InputStream getProcessImageView(String deploymentId) {
        String defId = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult().getId();
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(defId);
        String source = processDefinition.getDiagramResourceName();
        return repositoryService.getResourceAsStream(deploymentId, source);
    }

    @Override
    public List<FlowElement> getFlowElementsByDeploymentId(String deploymentId) {
        String defId = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult().getId();
        BpmnModel model = repositoryService.getBpmnModel(defId);
        List list = new ArrayList<FlowElement>();
        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement flowElement : flowElements) {
                if (!(flowElement instanceof SequenceFlow) && !(flowElement instanceof EndEvent)) {//只获取节点
                    list.add(flowElement);
                }
            }
        }
        return list;
    }

    @Override
    public String startFlow(String processDefKey, String businessKey, String userId) {
        try {
            Authentication.setAuthenticatedUserId(userId);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefKey, businessKey);
            processInstance.getId();
            Authentication.setAuthenticatedUserId(null);
            return processInstance.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<TaskRepresentation> getCandidateTasks(TaskRepresentation taskRepresentation) {
        List<TaskRepresentation> taskRepresentations = taskMapper.getCandidateTasks(taskRepresentation);
        return taskRepresentations;
    }

    @Override
    public List<TaskRepresentation> getAssigneeTasks(TaskRepresentation taskRepresentation) {
        List<TaskRepresentation> taskRepresentations = taskMapper.getAssigneeTasks(taskRepresentation);
        return taskRepresentations;
    }

    @Override
    public List<TaskRepresentation> getCompleteTasks(TaskRepresentation taskRepresentation) {
        List<TaskRepresentation> taskRepresentations = taskMapper.getCompleteTasks(taskRepresentation);
        return taskRepresentations;
    }

    @Override
    public List<TaskRepresentation> getCandidateOrAssignedTasks(TaskRepresentation taskRepresentation) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().finished().list();
        List<TaskRepresentation> taskRepresentations = taskMapper.getCandidateOrAssignedTasks(taskRepresentation);
        return taskRepresentations;
    }

    @Override
    public boolean claimTask(String taskId, String userId) {
        try {
            taskService.claim(taskId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<FlowElement> getNextNodes(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ExecutionEntity ee = (ExecutionEntity) runtimeService.createExecutionQuery()
                .executionId(task.getExecutionId()).singleResult();
        // 当前审批节点
        String crruentActivityId = ee.getActivityId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(crruentActivityId);
        List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();

        List<FlowElement> flowElements = new ArrayList<FlowElement>();
        for (int i = 0; i < outFlows.size(); i++) {
            FlowElement targetFlow = outFlows.get(i).getTargetFlowElement();
            flowElements.add(targetFlow);
        }
        return flowElements;
    }

    @Override
    public boolean toNext(String taskId, Map variables) {
        try {
            taskService.complete(taskId, variables);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getTaskDefKey(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult().getTaskDefinitionKey();
    }

    @Override
    public boolean synUser(String userId, String userName) {
        try {
            User user = idmIdentityService.newUser(userId);
            user.setFirstName(userName.substring(0, 1));
            if (userName.length() > 1) {
                user.setLastName(userName.substring(1, userName.length()));
            } else {
                user.setLastName(" ");
            }
            idmIdentityService.saveUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean synGroup(String groupId, String groupName) {
        try {
            Group group = idmIdentityService.newGroup(groupId);
            group.setName(groupName);
            idmIdentityService.saveGroup(group);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean synMembership(String groupId, List<String> userIds) {
        if (userIds != null && userIds.size() > 0) {
            for (String userId : userIds) {
                CommandContextUtil.getMembershipEntityManager().deleteMembershipByUserId(userId);
                idmIdentityService.createMembership(userId, groupId);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delUser(String userId) {
        try {
            idmIdentityService.deleteUser(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delGroup(String groupId) {
        try {
            idmIdentityService.deleteGroup(groupId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cancelFlow(String procInstId) {
        try {
            runtimeService.deleteProcessInstance(procInstId, "");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public String getBusinessKey(String processInstId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstId)
                .singleResult()
                .getBusinessKey();
    }

}
