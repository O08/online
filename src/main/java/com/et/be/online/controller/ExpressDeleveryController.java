package com.et.be.online.controller;


import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.constant.SceneConstant;
import com.et.be.online.service.ShipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "电商-物流", tags = "电商")
@RequestMapping("api/v1/online")
@RestController
@Slf4j
public class ExpressDeleveryController {

    @Autowired
    private ShipmentService shipmentService;

    @ApiOperation("运单查询")
    @ResponseBody
    @PostMapping(value = "trackShipment")
    public ResponseVO trackShipment(String shipNO) throws Exception {

        return new ResponseVO(shipmentService.getShipment(shipNO));
    }
}
