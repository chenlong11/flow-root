package com.nuvole.flow.controller.process;

import cn.hutool.core.util.RandomUtil;
import com.nuvole.flow.domain.OrderInst;
import com.nuvole.flow.service.idm.AuthenticationService;
import com.nuvole.flow.service.orderInst.OrderInstService;
import com.nuvole.flow.service.process.FlowService;
import com.nuvole.flow.util.ReturnVO;
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
import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    private OrderInstService orderInstService;

    @Autowired
    private AuthenticationService authenticationService;

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
            for (int len = -1; (len = in.read(b)) != -1; ) {
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

    @RequestMapping(value = "/{processDefKey}/start", method = RequestMethod.GET)
    @ResponseBody
    public ReturnVO start(@PathVariable String processDefKey, String businessId) {
        String currentUserId = authenticationService.getCurrentUserId();
        String procInstId = flowService.startFlow(processDefKey, businessId, currentUserId);
        OrderInst orderInst = new OrderInst();
        orderInst.setId(RandomUtil.simpleUUID());
        orderInst.setProcInstId(procInstId);
        String code = RandomUtil.randomNumbers(8);
        orderInst.setOrderCode(code);
        orderInst.setOrderName("工单-" + code);
        orderInst.setStatus(Short.valueOf("1"));
        orderInst.setBusinessKey(businessId);
        orderInst.setCreater(currentUserId);
        orderInst.setCreatedBy(currentUserId);
        orderInst.setCreateTime(new Date());
        orderInstService.insert(orderInst);
        HashMap content = new HashMap();
        content.put("procInstId", procInstId);
        return new ReturnVO(true, content);

    }
}
