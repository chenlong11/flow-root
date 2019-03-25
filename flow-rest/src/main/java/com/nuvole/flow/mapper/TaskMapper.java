package com.nuvole.flow.mapper;

import com.nuvole.flow.domain.TaskRepresentation;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {

    @SelectProvider(type = TaskProvider.class, method = "getCandidateTasks")
    List<TaskRepresentation> getCandidateTasks(TaskRepresentation taskRepresentation);

    @SelectProvider(type = TaskProvider.class, method = "getAssigneeTasks")
    List<TaskRepresentation> getAssigneeTasks(TaskRepresentation taskRepresentation);

    @SelectProvider(type = TaskProvider.class, method = "getCompleteTasks")
    List<TaskRepresentation> getCompleteTasks(TaskRepresentation taskRepresentation);

    @SelectProvider(type = TaskProvider.class, method = "getCandidateOrAssignedTasks")
    List<TaskRepresentation> getCandidateOrAssignedTasks(TaskRepresentation taskRepresentation);


    @SelectProvider(type = TaskProvider.class, method = "getTaskId")
    String getTaskId(String procInstId);
}
