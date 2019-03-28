package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.Model;
import com.nuvole.flow.domain.ModelVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：17:55
 */
@Repository
public interface ModelMapper {

    @SelectProvider(type = ModelProvider.class, method = "save")
    void save(Model model);

    @Update({" update ACT_DE_MODEL set state = 0 where id = #{id} "})
    void delete(String id);

    @Select({" select id,name,model_key,description,created,created_by createdBy,last_updated lastUpdated," +
            " last_updated_by lastUpdatedBy,version,model_editor_json modelEditorJson,model_type modelType,thumbnail,proc_def_key processDefKey " +
            " from ACT_DE_MODEL where id = #{id} "})
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="model_key",property="key")
    })
    Model get(String id);

    @SelectProvider(type = ModelProvider.class, method = "getByParameters")
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="model_key",property="key")
    })
    List<ModelVo> getByParameters(ModelVo modelVo);

}
