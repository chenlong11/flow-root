package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.OrderInst;
import com.nuvole.flow.domain.OrderInstExample;
import com.nuvole.flow.domain.OrderInstExample.Criteria;
import com.nuvole.flow.domain.OrderInstExample.Criterion;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class OrderInstSqlProvider {

    public String countByExample(OrderInstExample example) {
        BEGIN();
        SELECT("count(*)");
        FROM("ORDER_INST");
        applyWhere(example, false);
        return SQL();
    }

    public String deleteByExample(OrderInstExample example) {
        BEGIN();
        DELETE_FROM("ORDER_INST");
        applyWhere(example, false);
        return SQL();
    }

    public String insertSelective(OrderInst record) {
        BEGIN();
        INSERT_INTO("ORDER_INST");
        
        if (record.getId() != null) {
            VALUES("ID", "#{id,jdbcType=NVARCHAR}");
        }
        
        if (record.getBusinessKey() != null) {
            VALUES("BUSINESS_KEY", "#{businessKey,jdbcType=NVARCHAR}");
        }
        
        if (record.getProcInstId() != null) {
            VALUES("PROC_INST_ID", "#{procInstId,jdbcType=NVARCHAR}");
        }
        
        if (record.getOrderName() != null) {
            VALUES("ORDER_NAME", "#{orderName,jdbcType=NVARCHAR}");
        }
        
        if (record.getOrderCode() != null) {
            VALUES("ORDER_CODE", "#{orderCode,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreater() != null) {
            VALUES("CREATER", "#{creater,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreatedBy() != null) {
            VALUES("CREATED_BY", "#{createdBy,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("CREATE_TIME", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            VALUES("STATUS", "#{status,jdbcType=DECIMAL}");
        }
        
        return SQL();
    }

    public String selectByExample(OrderInstExample example) {
        BEGIN();
        if (example != null && example.isDistinct()) {
            SELECT_DISTINCT("ID");
        } else {
            SELECT("ID");
        }
        SELECT("BUSINESS_KEY");
        SELECT("PROC_INST_ID");
        SELECT("ORDER_NAME");
        SELECT("ORDER_CODE");
        SELECT("CREATER");
        SELECT("CREATED_BY");
        SELECT("CREATE_TIME");
        SELECT("STATUS");
        FROM("ORDER_INST");
        applyWhere(example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            ORDER_BY(example.getOrderByClause());
        }
        
        return SQL();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        OrderInst record = (OrderInst) parameter.get("record");
        OrderInstExample example = (OrderInstExample) parameter.get("example");
        
        BEGIN();
        UPDATE("ORDER_INST");
        
        if (record.getId() != null) {
            SET("ID = #{record.id,jdbcType=NVARCHAR}");
        }
        
        if (record.getBusinessKey() != null) {
            SET("BUSINESS_KEY = #{record.businessKey,jdbcType=NVARCHAR}");
        }
        
        if (record.getProcInstId() != null) {
            SET("PROC_INST_ID = #{record.procInstId,jdbcType=NVARCHAR}");
        }
        
        if (record.getOrderName() != null) {
            SET("ORDER_NAME = #{record.orderName,jdbcType=NVARCHAR}");
        }
        
        if (record.getOrderCode() != null) {
            SET("ORDER_CODE = #{record.orderCode,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreater() != null) {
            SET("CREATER = #{record.creater,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreatedBy() != null) {
            SET("CREATED_BY = #{record.createdBy,jdbcType=NVARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("CREATE_TIME = #{record.createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getStatus() != null) {
            SET("STATUS = #{record.status,jdbcType=DECIMAL}");
        }
        
        applyWhere(example, true);
        return SQL();
    }

    public String updateByExample(Map<String, Object> parameter) {
        BEGIN();
        UPDATE("ORDER_INST");
        
        SET("ID = #{record.id,jdbcType=NVARCHAR}");
        SET("BUSINESS_KEY = #{record.businessKey,jdbcType=NVARCHAR}");
        SET("PROC_INST_ID = #{record.procInstId,jdbcType=NVARCHAR}");
        SET("ORDER_NAME = #{record.orderName,jdbcType=NVARCHAR}");
        SET("ORDER_CODE = #{record.orderCode,jdbcType=NVARCHAR}");
        SET("CREATER = #{record.creater,jdbcType=NVARCHAR}");
        SET("CREATED_BY = #{record.createdBy,jdbcType=NVARCHAR}");
        SET("CREATE_TIME = #{record.createTime,jdbcType=TIMESTAMP}");
        SET("STATUS = #{record.status,jdbcType=DECIMAL}");
        
        OrderInstExample example = (OrderInstExample) parameter.get("example");
        applyWhere(example, true);
        return SQL();
    }

    protected void applyWhere(OrderInstExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            WHERE(sb.toString());
        }
    }
}