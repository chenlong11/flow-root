package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.GroupRepresentation;
import com.nuvole.flow.service.exception.NotFoundException;
import com.nuvole.flow.service.idm.GroupService;
import org.flowable.idm.api.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/flow/idm")
public class ApiGroupsResource {

    @Autowired
    protected GroupService groupService;
    
    @RequestMapping(value = "/groups/{groupId}", method = RequestMethod.GET, produces = { "application/json" })
    public GroupRepresentation getGroupInformation(@PathVariable String groupId) {
        Group group = groupService.getGroup(groupId);
        if (group != null) {
            return new GroupRepresentation(group);
            
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = { "application/json" })
    public List<GroupRepresentation> findGroupsByFilter(@RequestParam("filter") String filter) {
        List<GroupRepresentation> result = new ArrayList<GroupRepresentation>();
        List<Group> groups = groupService.getGroups(filter);
        for (Group group : groups) {
            result.add(new GroupRepresentation(group));
        }
        return result;
    }

}
