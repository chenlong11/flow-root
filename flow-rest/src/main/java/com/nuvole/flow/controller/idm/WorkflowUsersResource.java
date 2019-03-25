package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.ResultListDataRepresentation;
import com.nuvole.flow.domain.UserRepresentation;
import com.nuvole.flow.service.idm.RemoteIdmService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Rest resource for managing users, specifically related to tasks and processes.
 */
@RestController
@RequestMapping("/rest/flow")
public class WorkflowUsersResource {

    @Autowired
    private RemoteIdmService remoteIdmService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/workflow-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter,
                                                 @RequestParam(value = "excludeTaskId", required = false) String excludeTaskId,
                                                 @RequestParam(value = "excludeProcessId", required = false) String excludeProcessId) {

        List<? extends User> matchingUsers = remoteIdmService.findUsersByNameFilter(filter);

        // Filter out users already part of the task/process of which the ID has been passed
        if (excludeTaskId != null) {
            filterUsersInvolvedInTask(excludeTaskId, matchingUsers);
        } else if (excludeProcessId != null) {
            filterUsersInvolvedInProcess(excludeProcessId, matchingUsers);
        }

        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>(matchingUsers.size());
        for (User user : matchingUsers) {
            userRepresentations.add(new UserRepresentation(user));
        }

        return new ResultListDataRepresentation(userRepresentations);

    }

    protected void filterUsersInvolvedInProcess(String excludeProcessId, List<? extends User> matchingUsers) {
        Set<String> involvedUsers = getInvolvedUsersAsSet(
                runtimeService.getIdentityLinksForProcessInstance(excludeProcessId));
        removeinvolvedUsers(matchingUsers, involvedUsers);
    }

    protected void filterUsersInvolvedInTask(String excludeTaskId, List<? extends User> matchingUsers) {
        Set<String> involvedUsers = getInvolvedUsersAsSet(taskService.getIdentityLinksForTask(excludeTaskId));
        removeinvolvedUsers(matchingUsers, involvedUsers);
    }

    protected Set<String> getInvolvedUsersAsSet(List<IdentityLink> involvedPeople) {
        Set<String> involved = null;
        if (involvedPeople.size() > 0) {
            involved = new HashSet<String>();
            for (IdentityLink link : involvedPeople) {
                if (link.getUserId() != null) {
                    involved.add(link.getUserId());
                }
            }
        }
        return involved;
    }

    protected void removeinvolvedUsers(List<? extends User> matchingUsers, Set<String> involvedUsers) {
        if (involvedUsers != null) {
            // Using iterator to be able to remove without ConcurrentModExceptions
            Iterator<? extends User> userIt = matchingUsers.iterator();
            while (userIt.hasNext()) {
                if (involvedUsers.contains(userIt.next().getId())) {
                    userIt.remove();
                }
            }
        }
    }

}
