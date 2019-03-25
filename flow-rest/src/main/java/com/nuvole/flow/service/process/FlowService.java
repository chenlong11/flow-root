package com.nuvole.flow.service.process;

import com.nuvole.flow.domain.TaskRepresentation;
import org.flowable.bpmn.model.FlowElement;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlong
 * Date：2017/9/22
 * time：9:53
 */
public interface FlowService {

    /**
     * 根据发布id获取流程预览图
     * @param deploymentId
     * @return
     */
    InputStream getProcessImageView(String deploymentId);

    List<FlowElement> getFlowElementsByDeploymentId(String deploymentId);

    /**
     * 根据流程定义key启动流程
     * @param processDefKey 流程定义key
     * @param businessKey 业务key
     * @param userId 流程发起人id
     * @return
     */
    String startFlow(String processDefKey, String businessKey, String userId);

    /**
     * 查询待办事项
     * @param taskRepresentation
     * @return
     */
    List<TaskRepresentation> getCandidateTasks(TaskRepresentation taskRepresentation);

    /**
     * 查询在办事项
     * @param taskRepresentation
     * @return
     */
    List<TaskRepresentation> getAssigneeTasks(TaskRepresentation taskRepresentation);

    /**
     * 查询已办事项
     * @param taskRepresentation
     * @return
     */
    List<TaskRepresentation> getCompleteTasks(TaskRepresentation taskRepresentation);

    /**
     * 查询进行中事项（包括待办和在办）
     * @param taskRepresentation
     * @return
     */
    List<TaskRepresentation> getCandidateOrAssignedTasks(TaskRepresentation taskRepresentation);


    /**
     * 认领待办事项
     * @param taskId
     * @param userId
     * @return
     */
    boolean claimTask(String taskId, String userId);

    List<FlowElement> getNextNodes(String taskId);

    /**
     * 根据processId获取业务id
     * @param processId
     * @return
     */
    String getBusinessKey(String processId);

    /**
     * 发送到下个节点
     * @param taskId
     * @param variables
     * @return
     */
    boolean toNext(String taskId, Map variables);

    /**
     * 根据task id 获取定义key
     * @param taskId
     * @return
     */
    String getTaskDefKey(String taskId);

    /**
     * 同步用户
     * @return
     * @param userId
     * @param userName
     */
    boolean synUser(String userId, String userName);

    /**
     * 同步组/角色
     * @return
     */
    boolean synGroup(String groupId, String groupName);

    /**
     * 同步用户，组关系
     * @param groupId
     * @param userIds
     * @return
     */
    boolean synMembership(String groupId, List<String> userIds);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    boolean delUser(String userId);

    /**
     * 删除组/角色
     * @param groupId
     * @return
     */
    boolean delGroup(String groupId);

    /**
     * 取消流程
     * @param procInstId
     * @return
     */
    boolean cancelFlow(String procInstId);
}
