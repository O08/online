package com.et.be.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.et.be.online.domain.dto.CustomerAddressDTO;
import com.et.be.online.domain.mo.CustomerAddress;
import com.et.be.online.domain.vo.CustomerAddressVO;

import java.util.List;

public interface CustomerAddressService extends IService<CustomerAddress> {
    List<CustomerAddressVO> getAddressList();

    int newAddress(CustomerAddressDTO addressDTO);

    int modifyAddress(CustomerAddressDTO addressDTO);

    int removeAddress(Long id);
}
