package com.et.be.online.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.et.be.base.security.UserInfo;
import com.et.be.online.domain.dto.CustomerAddressDTO;
import com.et.be.online.domain.mo.Customer;
import com.et.be.online.domain.mo.CustomerAddress;
import com.et.be.online.domain.vo.CustomerAddressVO;
import com.et.be.online.mapper.CustomerAddressMapper;
import com.et.be.online.mapper.CustomerMapper;
import com.et.be.online.service.CustomerAddressService;
import com.et.be.online.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress>  implements CustomerAddressService {
    @Autowired
    private CustomerAddressMapper customerAddressMapper;
    @Autowired
    private CustomerService customerService;
    @Override
    public List<CustomerAddressVO> getAddressList() {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());

        // get customer address
        QueryWrapper<CustomerAddress> addressQueryWrapper = Wrappers.query();
        addressQueryWrapper.eq("user_id",customer.getId());
        addressQueryWrapper.orderByDesc("modified_at");
        List<CustomerAddress> addressList = customerAddressMapper.selectList(addressQueryWrapper);

        //使用stream拷贝list
        List<CustomerAddressVO> addressVOList = addressList.stream()
                .map(e -> {
                    CustomerAddressVO d = new CustomerAddressVO();
                    BeanUtil.copyProperties(e, d);
                    String fullAddress = StrUtil.join(" ",e.getCountry(),e.getState(),e.getCity(),e.getAddressLine2(),e.getAddressLine1());
                    d.setFullAddress(fullAddress.replaceAll("null","")); // replace null to blank
                    return d;
                })
                .collect(Collectors.toList());

        return addressVOList;
    }

    @Override
    public int newAddress(CustomerAddressDTO addressDTO) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        CustomerAddress address = new CustomerAddress();
        BeanUtil.copyProperties(addressDTO,address);
        address.setUserId(customer.getId())
                .setCreatedAt(new Date())
                .setModifiedAt(new Date());
        int res = customerAddressMapper.insert(address);
        return res;
    }

    @Override
    public int modifyAddress(CustomerAddressDTO addressDTO) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        CustomerAddress address = new CustomerAddress();
        BeanUtil.copyProperties(addressDTO,address);
        address.setUserId(customer.getId())
                .setModifiedAt(new Date());
        int res = customerAddressMapper.updateById(address);
        return res;
    }

    @Override
    public int removeAddress(Long id) {
        // get user id
        Customer customer = customerService.getCustomerByEmail(UserInfo.getUsername());
        QueryWrapper<CustomerAddress> wrapper = Wrappers.query();
        wrapper.eq("id",id).eq("user_id",customer.getId());
        int res = customerAddressMapper.delete(wrapper);
        return res;
    }

}
