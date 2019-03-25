package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.ModelRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenlong
 * Date：2017/9/11
 * time：17:48
 */
@Repository
public interface ModelRelationMapper {

    @Select({" select id,parent_model_id parentModelId,model_id modelId,relation_type type " +
            " from ACT_DE_MODEL_RELATION where parent_model_id = #{parentModelId} and relation_type = #{type}  "})
    List<ModelRelation> findByParentModelIdAndType(@Param("parentModelId") String parentModelId, @Param("type") String type);

    @Delete({"delete from ACT_DE_MODEL_RELATION where id = #{id} "})
    void delete(ModelRelation modelRelation);

    @Insert({" insert into ACT_DE_MODEL_RELATION (id, parent_model_id, model_id, relation_type) " +
            "  values (#{id},#{parentModelId},#{modelId},#{type}) "})
    void save(ModelRelation modelRelation);

}
