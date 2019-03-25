package com.nuvole.flow.controller.task;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.nuvole.flow.domain.TaskRepresentation;
import com.nuvole.flow.service.idm.AuthenticationService;
import com.nuvole.flow.service.process.FlowService;
import com.nuvole.flow.util.PageBean;
import com.nuvole.flow.util.ReturnVO;
import lombok.extern.java.Log;
import org.flowable.bpmn.model.FlowElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/flow/tasks")
@Log
public class TaskController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private FlowService flowService;

    @RequestMapping(value = "/candidate", method = RequestMethod.GET)
    @ResponseBody
    public PageBean candidateTasks(TaskRepresentation taskRepresentation, HttpServletRequest request) {
        String userId = authenticationService.getCurrentUserId();
        taskRepresentation.setAssignee(userId);
        PageHelper.startPage(Convert.toInt(request.getParameter("pageNumber")), Convert.toInt(request.getParameter("pageSize")));
        List<TaskRepresentation> tasks = flowService.getCandidateTasks(taskRepresentation);
        log.info("candidateTasks:" + JSON.toJSONString(tasks));

        return new PageBean(tasks);
    }

    @RequestMapping(value = "/assignee", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskRepresentation> AssigneeTasks(TaskRepresentation taskRepresentation, HttpServletRequest request) {
        String userId = authenticationService.getCurrentUserId();
        taskRepresentation.setAssignee(userId);
        PageHelper.startPage(Convert.toInt(request.getParameter("pageNumber")), Convert.toInt(request.getParameter("pageSize")));
        List<TaskRepresentation> tasks = flowService.getAssigneeTasks(taskRepresentation);
        log.info("assigneeTasks:" + JSON.toJSONString(tasks));
        return tasks;
    }

    @RequestMapping(value = "/{taskId}/claim")
    @ResponseBody
    public ReturnVO claimTask(@PathVariable String taskId) {
        String userId = authenticationService.getCurrentUserId();
        return new ReturnVO(flowService.claimTask(taskId, userId));
    }

    @RequestMapping(value = "/{taskId}/nextNodes", method = RequestMethod.GET)
    @ResponseBody
    public String nextNodes(@PathVariable String taskId) {
        List<FlowElement> nextNodes = flowService.getNextNodes(taskId);
        String res = "";
        for (int i = 0; i < nextNodes.size(); i++) {
            res += (i + 1) + ") " + nextNodes.get(i).getName() + "-" + nextNodes.get(i).getId();
        }
        return res;
    }

    @RequestMapping(value = "/{taskId}/toNext")
    @ResponseBody
    public ReturnVO toNext(@PathVariable String taskId) {
        String taskDefKey = flowService.getTaskDefKey(taskId);
        Map variables = new HashMap();
        if (taskDefKey.equals("user")){//排他网关示例
            variables.put("pass", 1);//0 退回到申请节点 1 发送到角色环节
        }
        return new ReturnVO(flowService.toNext(taskId, variables));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PageBean tasks(TaskRepresentation taskRepresentation, HttpServletRequest request) {
        String taskType = request.getParameter("taskType");
        String userId = authenticationService.getCurrentUserId();
        taskRepresentation.setAssignee(userId);
        PageBean pageBean = null;
        PageHelper.startPage(Convert.toInt(request.getParameter("pageNumber")), Convert.toInt(request.getParameter("pageSize")));
        if (taskType.equals("1")) {//待办
            List<TaskRepresentation> tasks = flowService.getCandidateOrAssignedTasks(taskRepresentation);
            pageBean = new PageBean(tasks);
        } else if(taskType.equals("2")){//已办
            List<TaskRepresentation> tasks = flowService.getCompleteTasks(taskRepresentation);
            pageBean = new PageBean(tasks);
        }
        return pageBean;
    }

}
