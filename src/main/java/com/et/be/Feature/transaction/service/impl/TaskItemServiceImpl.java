package com.et.be.Feature.transaction.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.Entity.TaskItem;
import com.et.be.Feature.transaction.constant.TaskItemConstant;
import com.et.be.Feature.transaction.dao.TaskItemDao;
import com.et.be.Feature.transaction.service.TaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskItemServiceImpl implements TaskItemService {
    @Autowired
    private TaskItemDao taskItemDao;

    @Override
    public int saveTaskItem(TaskItem taskItem) {
           taskItem
                .setItemFlag(TaskItemConstant.VALID)
                .setCreatedate(new Date())
                .setModifydate(new Date());
        return taskItemDao.insert(taskItem);
    }

    @Override
    public int assignTaskItem(String ownerEmail, String executorEamil) {

        return taskItemDao.updateTaskItemExecutorEmailByID(ownerEmail,executorEamil);
    }

    @Override
    public List<TaskItem> displayLobbyTaskItem() {
        return taskItemDao.selectList(new QueryWrapper<>());
    }

    @Override
    public List<TaskItem> displayUserTaskItem(String type, String email) {
        QueryWrapper<TaskItem> wrapper = Wrappers.query();
        if(TaskItemConstant.CUSTOMER.equals(type)){
            wrapper.eq("owner_email",email);

        }
        if(TaskItemConstant.SELLER.equals(type)){
            wrapper.eq("executor_email",email);
        }
        wrapper.orderByAsc("createdate");
        return taskItemDao.selectList(wrapper);
    }


}
