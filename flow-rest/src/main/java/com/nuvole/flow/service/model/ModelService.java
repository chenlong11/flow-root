package com.nuvole.flow.service.model;


import com.nuvole.flow.domain.Model;
import com.nuvole.flow.domain.ModelKeyRepresentation;
import com.nuvole.flow.domain.ModelRepresentation;
import com.nuvole.flow.domain.ModelVo;

import java.util.List;

/**
 * Created by chenlong
 * Date：2017/9/10
 * time：11:30
 */
public interface ModelService {

    String createModelJson(ModelRepresentation modelRepresentation);

    Model createModel(ModelRepresentation model, String editorJson, String userId);

    ModelKeyRepresentation validateModelKey(Model model, Integer modelType, String key);

    List<ModelVo> getModelsByParameters(ModelVo modelVo);

    void delModelById(String id);

    Model getModel(String modelId);

    void saveModel(Model model);

    void publicModel(String id);


}
