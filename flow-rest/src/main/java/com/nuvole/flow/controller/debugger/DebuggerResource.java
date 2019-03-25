package com.nuvole.flow.controller.debugger;

import com.nuvole.flow.domain.BreakpointRepresentation;
import com.nuvole.flow.domain.ExecutionRepresentation;
import com.nuvole.flow.service.debugger.DebuggerRestVariable;
import com.nuvole.flow.service.debugger.DebuggerService;
import org.flowable.engine.event.EventLogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/rest/flow/debugger")
public class DebuggerResource {

    @Autowired
    protected DebuggerService debuggerService;

    @Autowired
    protected Environment environment;

    @RequestMapping(value = "/breakpoints", method = RequestMethod.GET, produces = "application/json")
    public Collection<BreakpointRepresentation> getBreakpoints() {
        assertDebuggerEnabled();
        return debuggerService.getBreakpoints();
    }

    @RequestMapping(value = "/breakpoints", method = RequestMethod.POST)
    public void addBreakPoints(@RequestBody BreakpointRepresentation breakpointRepresentation) {
        assertDebuggerEnabled();
        debuggerService.addBreakpoint(breakpointRepresentation);
    }

    @RequestMapping(value = "/breakpoints/{executionId}/continue", method = RequestMethod.PUT)
    public void continueExecution(@PathVariable String executionId) {
        assertDebuggerEnabled();
        debuggerService.continueExecution(executionId);
    }

    @RequestMapping(value = "/breakpoints", method = RequestMethod.DELETE)
    public void deleteBreakPoints(@RequestBody BreakpointRepresentation breakpointRepresentation) {
        assertDebuggerEnabled();
        debuggerService.removeBreakpoint(breakpointRepresentation);
    }

    @RequestMapping(value = "/eventlog/{processInstanceId}", method = RequestMethod.GET)
    public List<EventLogEntry> getEventLog(@PathVariable String processInstanceId) {
        assertDebuggerEnabled();
        return debuggerService.getProcessInstanceEventLog(processInstanceId);
    }

    @RequestMapping(value = "/variables/{executionId}", method = RequestMethod.GET)
    public List<DebuggerRestVariable> getExecutionVariables(@PathVariable String executionId) {
        assertDebuggerEnabled();
        return debuggerService.getExecutionVariables(executionId);
    }

    @RequestMapping(value = "/executions/{processInstanceId}", method = RequestMethod.GET)
    public List<ExecutionRepresentation> getExecutions(@PathVariable String processInstanceId) {
        assertDebuggerEnabled();
        return debuggerService.getExecutions(processInstanceId);
    }

    @RequestMapping(value = "/evaluate/expression/{executionId}", method = RequestMethod.POST, produces = "application/text")
    public String evaluateExpression(@PathVariable String executionId, @RequestBody String expression) {
        assertDebuggerEnabled();
        return debuggerService.evaluateExpression(executionId, expression).toString();
    }

    @RequestMapping(value = "/evaluate/{scriptLanguage}/{executionId}", method = RequestMethod.POST)
    public void evaluateScript(@PathVariable String executionId, @PathVariable String scriptLanguage, @RequestBody String script) {
        assertDebuggerEnabled();
        debuggerService.evaluateScript(executionId, scriptLanguage, script);
    }

    @RequestMapping(method = RequestMethod.GET)
    public boolean isDebuggerAllowed() {
        return environment.getProperty("flowable.experimental.debugger.enabled", Boolean.class, false);
    }

    protected void assertDebuggerEnabled() {
        if (!environment.getProperty("flowable.experimental.debugger.enabled", Boolean.class, false)) {
            throw new RuntimeException("property flowable.experimental.debugger.enabled is not enabled");
        }
    }

}
