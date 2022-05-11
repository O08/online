package com.et.be.controller;

import cn.hutool.core.bean.BeanUtil;
import com.et.be.Entity.TaskItem;
import com.et.be.Feature.transaction.domain.dto.TaskItemDTO;
import com.et.be.Feature.transaction.service.TaskItemService;
import com.et.be.inbox.domain.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "平台基础功能-任务单", tags = "基础功能")
@RequestMapping("api/v1/common")
@RestController
@Slf4j
public class TaskItemController {

    @Autowired
    private TaskItemService taskItemService;

    @ApiOperation("发布任务单")
    @ResponseBody
    @PostMapping(value = "pubTaskItem")
    public ResponseVO pubTaskItem(@RequestBody TaskItemDTO taskItemDTO) {
        TaskItem taskItem = new TaskItem();
        BeanUtil.copyProperties(taskItemDTO,taskItem);
        taskItemService.saveTaskItem(taskItem);
        return new ResponseVO("success");
    }

    /**
     *
     * @param id 任务单ID
     * @param eamil 订单执行人
     * @return
     */
    @ApiOperation("指派任务单")
    @ResponseBody
    @PostMapping(value = "assignTaskItem")
    public ResponseVO assignTaskItem(String id,String eamil) {
        taskItemService.assignTaskItem(id,eamil);
        return new ResponseVO("success");
    }


    @ApiOperation("查询任务单墙")
    @ResponseBody
    @PostMapping(value = "getTaskItemWall")
    public ResponseVO getTaskItemWall() {

        return new ResponseVO(taskItemService.displayLobbyTaskItem());
    }

    @ApiOperation("查询任务单")
    @ResponseBody
    @PostMapping(value = "getTaskItem")
    public ResponseVO getTaskItem(String type, String email) {

        return new ResponseVO(taskItemService.displayUserTaskItem(type,email));
    }

}
