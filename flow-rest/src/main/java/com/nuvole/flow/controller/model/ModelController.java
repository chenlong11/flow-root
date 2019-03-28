package com.nuvole.flow.controller.model;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.nuvole.flow.domain.Model;
import com.nuvole.flow.domain.ModelKeyRepresentation;
import com.nuvole.flow.domain.ModelRepresentation;
import com.nuvole.flow.domain.ModelVo;
import com.nuvole.flow.service.exception.BadRequestException;
import com.nuvole.flow.service.idm.AuthenticationService;
import com.nuvole.flow.service.model.ModelService;
import com.nuvole.flow.util.PageBean;
import com.nuvole.flow.util.ReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenlong
 * Date：2017/9/9
 * time：18:25
 */
@RestController
@RequestMapping("/rest/flow/models")
public class ModelController{

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ModelService modelService;

    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public PageBean modelData(ModelVo modelVo, HttpServletRequest request) {
        PageHelper.startPage(Convert.toInt(request.getParameter("pageNumber")), Convert.toInt(request.getParameter("pageSize")));
        return new PageBean(modelService.getModelsByParameters(modelVo));
    }

    /**
     * 新增
     * @param modelRepresentation
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,produces = "application/json")
    public ReturnVO saveModel(ModelRepresentation modelRepresentation) {
        modelRepresentation.setKey(modelRepresentation.getKey().trim());
        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(null, modelRepresentation.getModelType(), modelRepresentation.getKey());
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("模型key已存在: " + modelRepresentation.getKey());
        }
        String json = modelService.createModelJson(modelRepresentation);
        modelService.createModel(modelRepresentation, json, authenticationService.getCurrentUserId());
        return new ReturnVO(true);
    }

    @RequestMapping(value="/del",method = RequestMethod.POST)
    public ReturnVO delete(String ids) {
        if (StrUtil.isBlank(ids)) {
            return new ReturnVO(false);
        }
        return new ReturnVO(modelService.delModelByIds(ids));
    }

    /**
     * 修改Model
     * @param moel
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.POST)
    public ReturnVO editModel(Model moel) {
        modelService.saveModel(moel);
        return new ReturnVO(true);
    }

    /**
     *  获取Model
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ReturnVO getModel(@PathVariable String id) {
        Model model = modelService.getModel(id);
        return new ReturnVO(model);
    }

    /**
     * 流程发布
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}/public",method = RequestMethod.POST)
    public ReturnVO modelPublic(@PathVariable String id) {
        modelService.publicModel(id);
        return new ReturnVO(true);
    }

//    @ApiOperation(value="根据id查询详情", notes="根据id查询详情")
//    @RequestMapping(value = "/getById",method = RequestMethod.GET)
//    public ReturnVO getById(Integer id){
//        TestList TestList = testListMapper.selectByPrimaryKey(id);
//        return new ReturnVO(true, TestList);
//    }
//
//    @ApiOperation(value="根据id删除(多个id英文逗号分隔)", notes="根据id删除(多个id英文逗号分隔)")
//    @RequestMapping(value = "/del",method = RequestMethod.POST)
//    @Auth(value="testList:del")
//    public ReturnVO del(String ids){
//        String[] sid = ids.split(",");
//        for(String id : sid) {
//            testListMapper.deleteByPrimaryKey(Integer.valueOf(id));
//        }
//        return new ReturnVO(true);
//    }



}
