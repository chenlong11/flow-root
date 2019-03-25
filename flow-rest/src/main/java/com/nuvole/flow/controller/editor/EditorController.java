package com.nuvole.flow.controller.editor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nuvole.flow.service.exception.BadRequestException;
import com.nuvole.flow.service.exception.InternalServerErrorException;
import com.nuvole.flow.service.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chenlong
 * Date：2017/9/7
 * time：9:23
 */
@Controller
@RequestMapping("/rest/flow/editor")
public class EditorController {

    private static final Logger logger = LoggerFactory.getLogger(EditorController.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    protected ObjectMapper objectMapper;

    @RequestMapping(value = "/{modelId}/json", method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public ObjectNode getModelJSON(@PathVariable String modelId) {
        com.nuvole.flow.domain.Model model = modelService.getModel(modelId);
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put("modelId", model.getId());
        modelNode.put("name", model.getName());
        modelNode.put("key", model.getKey());
        modelNode.put("description", model.getDescription());
        modelNode.put("lastUpdated", model.getLastUpdated().toString());
        modelNode.put("lastUpdatedBy", model.getLastUpdatedBy());
        if (StringUtils.isNotEmpty(model.getModelEditorJson())) {
            try {
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
                editorJsonNode.put("modelType", "model");
                modelNode.set("model", editorJsonNode);
            } catch (Exception e) {
                logger.error("Error reading editor json {}", modelId, e);
                throw new InternalServerErrorException("Error reading editor json " + modelId);
            }
        } else {
            ObjectNode editorJsonNode = objectMapper.createObjectNode();
            editorJsonNode.put("id", "canvas");
            editorJsonNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorJsonNode.put("modelType", "model");
            editorJsonNode.put("stencilset", stencilSetNode);
            modelNode.set("model", editorJsonNode);
        }
        return modelNode;
    }

    @RequestMapping(value = "/{modelId}/json", method = RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public ResponseEntity updateModel(@PathVariable String modelId, com.nuvole.flow.domain.Model model) {
        try {
            //model.setLastUpdatedBy(WebUtil.getUserId());
            modelService.saveModel(model);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error saving model {}", model.getId(), e);
            throw new BadRequestException("Process model could not be saved " + model.getId());
        }
    }

}
