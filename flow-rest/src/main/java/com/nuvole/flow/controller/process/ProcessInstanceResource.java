package com.nuvole.flow.controller.process;

import com.nuvole.flow.domain.ProcessInstanceRepresentation;
import com.nuvole.flow.service.process.FlowableProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/flow/process-instances")
public class ProcessInstanceResource {

    @Autowired
    protected FlowableProcessInstanceService processInstanceService;

    @RequestMapping(value = "/{processInstanceId}", method = RequestMethod.GET, produces = "application/json")
    public ProcessInstanceRepresentation getProcessInstance(@PathVariable String processInstanceId, HttpServletResponse response) {
        return processInstanceService.getProcessInstance(processInstanceId, response);
    }

}
