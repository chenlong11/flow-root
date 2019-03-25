package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.GroupRepresentation;
import com.nuvole.flow.domain.ResultListDataRepresentation;
import com.nuvole.flow.service.idm.RemoteIdmService;
import org.flowable.idm.api.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/flow")
public class WorkflowGroupsResource {

    @Autowired
    private RemoteIdmService remoteIdmService;

    @RequestMapping(value = "/workflow-groups", method = RequestMethod.GET)
    public ResultListDataRepresentation getGroups(@RequestParam(value = "filter", required = false) String filter) {

        List<? extends Group> matchingGroups = remoteIdmService.findGroupsByNameFilter(filter);
        List<GroupRepresentation> groupRepresentations = new ArrayList<GroupRepresentation>();
        for (Group group : matchingGroups) {
            groupRepresentations.add(new GroupRepresentation(group));
        }
        return new ResultListDataRepresentation(groupRepresentations);
    }

}
