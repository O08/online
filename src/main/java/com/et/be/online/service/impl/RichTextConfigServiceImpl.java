package com.et.be.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.online.domain.mo.RichTextConfig;
import com.et.be.online.mapper.RichTextConfigMapper;
import com.et.be.online.service.RichTextConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RichTextConfigServiceImpl implements RichTextConfigService {
    @Autowired
    private RichTextConfigMapper richTextConfigMapper;
    @Override
    public RichTextConfig getRichTextConfig(String contentType, String contentNo) {
        QueryWrapper<RichTextConfig> wrapper = Wrappers.query();
        wrapper.eq("content_type",contentType)
                .eq("content_no",contentNo);
        return richTextConfigMapper.selectOne(wrapper);
    }
}
