package com.nuvole.flow.mapper;

import org.apache.ibatis.annotations.Delete;

public interface IdmMapper {

    @Delete(" delete from ACT_ID_MEMBERSHIP where group_id_ = #{groupId} ")
    void delMembershipByGroupId(String groupId);

}


