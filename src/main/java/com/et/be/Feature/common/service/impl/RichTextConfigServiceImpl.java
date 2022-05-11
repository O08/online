package com.et.be.Feature.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.et.be.Feature.common.Entity.RichTextConfig;
import com.et.be.Feature.common.dao.RichTextConfigMapper;
import com.et.be.Feature.common.service.RichTextConfigService;
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
