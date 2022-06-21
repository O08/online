package com.et.be.online.controller;


import com.et.be.base.vo.ResponseVO;
import com.et.be.online.domain.dto.CustomerAddressDTO;
import com.et.be.online.service.CustomerAddressService;
import com.et.be.online.service.ShipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "电商-物流", tags = "电商")
@RequestMapping("api/v1/selfService")
@RestController
@Slf4j
public class ExpressDeleveryController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private CustomerAddressService addressService;

    @ApiOperation("运单查询")
    @ResponseBody
    @PostMapping(value = "trackShipment")
    public ResponseVO trackShipment(Long shipmentId) throws Exception {

        return new ResponseVO(shipmentService.getShipment(shipmentId));
    }

    @ApiOperation("配送地址查询")
    @ResponseBody
    @PostMapping(value = "address")
    public ResponseVO address()  {
        return new ResponseVO(addressService.getAddressList());
    }

    @ApiOperation("配送地址添加")
    @ResponseBody
    @PostMapping(value = "newAddress")
    public ResponseVO newAddress(@Validated CustomerAddressDTO addressDTO)  {
        return new ResponseVO(addressService.newAddress(addressDTO));
    }


    @ApiOperation("配送地址修改")
    @ResponseBody
    @PostMapping(value = "modifyAddress")
    public ResponseVO changeAddress(@Validated CustomerAddressDTO addressDTO)  {
        return new ResponseVO(addressService.modifyAddress(addressDTO));
    }

    @ApiOperation("配送地址删除")
    @ResponseBody
    @PostMapping(value = "removeAddress")
    public ResponseVO changeAddress(@RequestParam Long id)  {
        return new ResponseVO(addressService.removeAddress(id));
    }



}
