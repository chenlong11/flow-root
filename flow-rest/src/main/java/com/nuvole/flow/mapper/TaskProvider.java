package com.nuvole.flow.mapper;

import cn.hutool.core.util.StrUtil;
import com.nuvole.flow.domain.TaskRepresentation;

public class TaskProvider {

    private String commonColumn = " RES.ID_ id,RES.NAME_ taskName,TASK_DEF_KEY_ taskKey,ASSIGNEE_ assignee,P.ID_ procInstId,P.PROC_DEF_ID_ procDefId," +
            " R.CREATE_TIME orderCreateTime,RES.CREATE_TIME_ taskCreateTime,R.ORDER_NAME,R.ORDER_CODE,R.BUSINESS_KEY ";

    public String getCandidateTasks(TaskRepresentation taskRepresentation) {
        StringBuilder sql = new StringBuilder(
                " select distinct " + commonColumn +
                        " from ACT_RU_TASK RES " +
                        " inner join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ " +
                        " inner join ACT_HI_PROCINST P on P.ID_ = RES.PROC_INST_ID_ " +
                        " inner join ORDER_INST R on R.PROC_INST_ID = P.ID_ " +
                        " WHERE RES.ASSIGNEE_ is null and I.TYPE_ = 'candidate' " +
                        " and ( I.USER_ID_ = #{assignee} or I.GROUP_ID_ IN (select group_ID_ from ACT_ID_MEMBERSHIP where USER_ID_ = #{assignee} ) ) ");
        getWhere(sql, taskRepresentation);
        sql.append(" order by RES.CREATE_TIME_ desc ");
        return sql.toString();
    }

    public String getAssigneeTasks(TaskRepresentation taskRepresentation) {
        StringBuilder sql = new StringBuilder(
                " select " + commonColumn +
                        " from ACT_RU_TASK RES " +
                        " inner join ACT_HI_PROCINST P on P.ID_ = RES.PROC_INST_ID_ " +
                        " inner join ORDER_INST R on R.PROC_INST_ID = P.ID_ " +
                        " WHERE RES.ASSIGNEE_ = #{assignee} ");
        getWhere(sql, taskRepresentation);
        sql.append(" order by RES.CREATE_TIME_ desc ");
        return sql.toString();
    }

    public String getCompleteTasks(TaskRepresentation taskRepresentation) {
        StringBuilder sql = new StringBuilder(
                " select RES.ID_ id,RES.NAME_ taskName,TASK_DEF_KEY_ taskKey,ASSIGNEE_ assignee,P.ID_ procInstId,P.PROC_DEF_ID_ procDefId," +
                " R.CREATE_TIME orderCreateTime,RES.START_TIME_ taskCreateTime,R.ORDER_NAME,R.ORDER_CODE,R.BUSINESS_KEY " +
                        " from ACT_HI_TASKINST RES " +
                        " inner join ACT_HI_PROCINST P on P.ID_ = RES.PROC_INST_ID_ " +
                        " inner join ORDER_INST R on R.PROC_INST_ID = P.ID_ " +
                        " WHERE RES.ASSIGNEE_ = #{assignee} and RES.END_TIME_ is not null ");
        getWhere(sql, taskRepresentation);
        sql.append(" order by RES.START_TIME_ desc ");
        return sql.toString();
    }

    public String getCandidateOrAssignedTasks(TaskRepresentation taskRepresentation) {
        StringBuilder sql = new StringBuilder(
                " select distinct " + commonColumn +
                        " from ACT_RU_TASK RES " +
                        " left join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ " +
                        " inner join ACT_HI_PROCINST P on P.ID_ = RES.PROC_INST_ID_ " +
                        " inner join ORDER_INST R on R.PROC_INST_ID = P.ID_ " +
                        " WHERE (" +
                        "   RES.ASSIGNEE_ = #{assignee} " +
                        "   OR " +
                        "   (" +
                        "       RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' " +
                        "       AND " +
                        "       (I.USER_ID_ = #{assignee} or I.GROUP_ID_ IN (select group_ID_ from ACT_ID_MEMBERSHIP where USER_ID_ = #{assignee} ))" +
                        "   )" +
                        " )");

        getWhere(sql, taskRepresentation);
        sql.append(" order by RES.CREATE_TIME_ desc ");
        return sql.toString();
    }

    private void getWhere(StringBuilder sql,TaskRepresentation taskRepresentation) {
        if (StrUtil.isNotBlank(taskRepresentation.getOrderName())) {
            sql.append(" and R.ORDER_NAME like '%'||#{orderName}||'%' ");
        }
        if(StrUtil.isNotBlank(taskRepresentation.getOrderCode())){
            sql.append(" and R.ORDER_CODE like '%'||#{orderCode}||'%' ");
        }
    }

    public String getTaskId(String procInstId) {
        StringBuilder sql = new StringBuilder(" select ID_ from ACT_RU_TASK WHERE PROC_INST_ID_ = #{procInstId}");
        return sql.toString();
    }

}
