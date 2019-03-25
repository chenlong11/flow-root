package com.nuvole.flow.controller.process;

import com.nuvole.flow.service.process.FlowService;
import lombok.extern.java.Log;
import org.flowable.bpmn.model.FlowElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by chenlong Date：2017/9/21 time：16:24
 */
@Controller
@RequestMapping("/rest/flow/processes")
@Log
public class ProcessController {

    @Autowired
    private FlowService flowService;

    @RequestMapping(value = "/{deploymentId}/config", method = RequestMethod.GET)
    public String config(Model model, @PathVariable String deploymentId) {
        model.addAttribute("deploymentId", deploymentId);
        List<FlowElement> flowElements = flowService.getFlowElementsByDeploymentId(deploymentId);
        model.addAttribute("flowElements", flowElements);
        return "/app/admin/flow/process/processConfig";
    }

    @RequestMapping(value = "/{deploymentId}/image", method = RequestMethod.GET)
    public void image(@PathVariable String deploymentId, HttpServletResponse response) {
        InputStream in = flowService.getProcessImageView(deploymentId);
        try {
            OutputStream out = response.getOutputStream();
            // 把图片的输入流程写入resp的输出流中
            byte[] b = new byte[1024];
            for (int len = -1; (len = in.read(b)) != -1;) {
                out.write(b, 0, len);
            }
            // 关闭流
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/{procInstId}/getBusinessKey", method = RequestMethod.GET)
    @ResponseBody
    public String getBusinessKey(@PathVariable String procInstId) {
        return flowService.getBusinessKey(procInstId);
    }
}
