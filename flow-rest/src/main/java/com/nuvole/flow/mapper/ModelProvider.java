package com.nuvole.flow.mapper;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.nuvole.flow.domain.Model;
import com.nuvole.flow.domain.ModelVo;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：18:28
 */
public class ModelProvider {

    public String save(Model model) {
        if (StrUtil.isNotBlank(model.getId())) {
            return update(model);
        } else {
            model.setId(RandomUtil.simpleUUID());
            return insert();
        }
    }

    public String insert() {
        StringBuilder builder = new StringBuilder(
                " insert into ACT_DE_MODEL ( " +
                        "            id, " +
                        "            name, " +
                        "            model_key, " +
                        "            description, " +
                        "            created, " +
                        "            created_by, " +
                        "            last_updated, " +
                        "            last_updated_by, " +
                        "            version, " +
                        "            model_type," +
                        "            state)  " +
                        "         values ( " +
                        "            #{id}, " +
                        "            #{name}, " +
                        "            #{key}, " +
                        "            #{description}, " +
                        "            #{created}, " +
                        "            #{createdBy}, " +
                        "            #{lastUpdated}, " +
                        "            #{lastUpdatedBy}, " +
                        "            #{version}, " +
                        "            #{modelType}," +
                        "            1) "
        );
        return builder.toString();
    }


    public String update(Model model) {

        StringBuilder builder = new StringBuilder(" update ACT_DE_MODEL set id = #{id} ");

        if (StrUtil.isNotBlank(model.getName())) {
            builder.append(" ,name = #{name} ");
        }

        if (StrUtil.isNotBlank(model.getKey())) {
            builder.append(" ,model_key = #{key} ");
        }

        if (StrUtil.isNotBlank(model.getDescription())) {
            builder.append(" ,description = #{description} ");
        }

        if (model.getLastUpdated() != null) {
            builder.append(" ,last_updated = #{lastUpdated} ");
        }

        if (StrUtil.isNotBlank(model.getLastUpdatedBy())) {
            builder.append(" ,last_updated_by = #{lastUpdatedBy} ");
        }

        if (StrUtil.isNotBlank(model.getModelEditorJson())) {
            builder.append(" ,model_editor_json = #{modelEditorJson} ");
        }

        if (model.getThumbnail() != null) {
            builder.append(" ,thumbnail = #{thumbnail}  ");
        }

        if (StrUtil.isNotBlank(model.getDeploymentId())) {
            builder.append(" ,deploy_id = #{deploymentId}  ");
        }

        if (StrUtil.isNotBlank(model.getProcessDefId())) {
            builder.append(" ,proc_def_id = #{processDefId}  ");
        }

        if (StrUtil.isNotBlank(model.getProcessDefKey())) {
            builder.append(" ,proc_def_key = #{processDefKey}  ");
        }

        builder.append(" where id = #{id} ");

        return builder.toString();
    }


    public String getByParameters(ModelVo modelVo) {

        StringBuilder builder = new StringBuilder();
        builder.append(" select a.id,a.name,a.model_key,a.description,a.created,a.created_by createdBy,a.last_updated lastUpdated, " +
                " a.last_updated_by lastUpdatedBy,a.version,a.model_editor_json modelEditorJson,a.model_type modelType,a.thumbnail," +
                " a.deploy_id deploymentId,proc_def_key processDefKey,b.first_ creater " +
                " from ACT_DE_MODEL a,ACT_ID_USER b where a.state = 1 and a.created_by = b.id_ ");

        if (modelVo.getModelType() != null) {
            builder.append(" and a.model_type = #{modelType} ");
        }

        if (StrUtil.isNotBlank(modelVo.getFilter())) {
            builder.append(" and (lower(a.name) like #{filter} or lower(a.description) like #{filter}) ");
        }

        if (StrUtil.isNotBlank(modelVo.getKey())) {
            builder.append(" and a.model_key = #{key} ");
        }

        return builder.toString();
    }

}
