package com.et.be.Feature.transaction.service;

import com.et.be.Entity.TaskItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskItemService {

    int saveTaskItem(TaskItem taskItem);

    /**
     * 指派任务单
     * @param id 订单id
     * @param eamil 订单执行人
     * @return
     */
    int assignTaskItem(String id,String eamil);

    /**
     * 任务单墙
     * @return
     */
    List<TaskItem> displayLobbyTaskItem();

    /**
     * 客户任务单
     * @param type 1 客户发布任务单 2 服务商执行的任务单
     * @param email
     * @return
     */
    List<TaskItem> displayUserTaskItem(String type,String email);





}
