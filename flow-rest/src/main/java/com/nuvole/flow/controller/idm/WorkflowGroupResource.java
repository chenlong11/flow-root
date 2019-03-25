package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.GroupRepresentation;
import com.nuvole.flow.service.exception.NotFoundException;
import com.nuvole.flow.service.idm.RemoteIdmService;
import org.flowable.idm.api.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/flow")
public class WorkflowGroupResource {

    @Autowired
    private RemoteIdmService remoteIdmService;

    @RequestMapping(value = "/workflow-groups/{groupId}", method = RequestMethod.GET)
    public GroupRepresentation getGroup(@PathVariable String groupId, HttpServletResponse response) {
        Group group = remoteIdmService.getGroup(groupId);

        if (group == null) {
            throw new NotFoundException("Group with id: " + groupId + " does not exist or is inactive");
        }

        return new GroupRepresentation(group);
    }

}
