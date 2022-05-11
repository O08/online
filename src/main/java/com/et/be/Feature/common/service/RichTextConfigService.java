package com.et.be.Feature.common.service;

import com.et.be.Feature.common.Entity.RichTextConfig;
import org.springframework.stereotype.Service;

@Service
public interface RichTextConfigService {
    RichTextConfig getRichTextConfig(String contentType,String contentNo);
}
