package com.et.be.Feature.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.et.be.Feature.common.Entity.Ticket;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

public interface TicketMapper extends BaseMapper<Ticket> {

    @Update("update sys_ticket set access_code = #{accessCode} ,modifydate = #{modifydate} where email = #{email}")
    int updateTicketByEmail(@Param("email") String email, @Param("accessCode") String accessCode, @Param("modifydate") Date modifydate);
}
