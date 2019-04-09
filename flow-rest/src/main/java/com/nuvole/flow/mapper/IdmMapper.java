package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.UserRepresentation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdmMapper {

    @Delete(" delete from ACT_ID_MEMBERSHIP where group_id_ = #{groupId} ")
    void delMembershipByGroupId(String groupId);

    @Select("select RES.ID_ id,RES.FIRST_ firstName,RES.LAST_ lastName,RES.EMAIL_ email from ACT_ID_USER RES WHERE RES.ID_ like '${filter}%' or (lower(CONCAT(CONCAT(RES.FIRST_ , ' '), RES.LAST_)) like '%${filter}%') order by RES.ID_ asc")
    List<UserRepresentation> getUsersByFilter(@Param("filter") String filter);

}


