package com.et.be.Feature.transaction.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.et.be.Entity.TaskItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface TaskItemDao extends BaseMapper<TaskItem> {
    @Update("update task_item set executor_email = #{executorEmail} where id = #{id}")
    int updateTaskItemExecutorEmailByID(@Param("id") String id, @Param("executorEmail") String executorEmail);

}
