package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.OrderInst;
import com.nuvole.flow.domain.OrderInstExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInstMapper {

    @SelectProvider(type=OrderInstSqlProvider.class, method="countByExample")
    int countByExample(OrderInstExample example);

    @DeleteProvider(type=OrderInstSqlProvider.class, method="deleteByExample")
    int deleteByExample(OrderInstExample example);

    @Insert({
        "insert into ORDER_INST (ID, BUSINESS_KEY, ",
        "PROC_INST_ID, ORDER_NAME, ",
        "ORDER_CODE, CREATER, ",
        "CREATED_BY, CREATE_TIME, ",
        "STATUS)",
        "values (#{id,jdbcType=NVARCHAR}, #{businessKey,jdbcType=NVARCHAR}, ",
        "#{procInstId,jdbcType=NVARCHAR}, #{orderName,jdbcType=NVARCHAR}, ",
        "#{orderCode,jdbcType=NVARCHAR}, #{creater,jdbcType=NVARCHAR}, ",
        "#{createdBy,jdbcType=NVARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{status,jdbcType=DECIMAL})"
    })
    int insert(OrderInst record);

    @InsertProvider(type=OrderInstSqlProvider.class, method="insertSelective")
    int insertSelective(OrderInst record);

    @SelectProvider(type=OrderInstSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.NVARCHAR),
        @Result(column="BUSINESS_KEY", property="businessKey", jdbcType=JdbcType.NVARCHAR),
        @Result(column="PROC_INST_ID", property="procInstId", jdbcType=JdbcType.NVARCHAR),
        @Result(column="ORDER_NAME", property="orderName", jdbcType=JdbcType.NVARCHAR),
        @Result(column="ORDER_CODE", property="orderCode", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATER", property="creater", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATED_BY", property="createdBy", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="STATUS", property="status", jdbcType=JdbcType.DECIMAL)
    })
    List<OrderInst> selectByExample(OrderInstExample example);

    @UpdateProvider(type=OrderInstSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") OrderInst record, @Param("example") OrderInstExample example);

    @UpdateProvider(type=OrderInstSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") OrderInst record, @Param("example") OrderInstExample example);
}