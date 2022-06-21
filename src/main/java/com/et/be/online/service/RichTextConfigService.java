package com.et.be.online.service;

import com.et.be.online.domain.mo.RichTextConfig;
import org.springframework.stereotype.Service;

@Service
public interface RichTextConfigService {
    RichTextConfig getRichTextConfig(String contentType, String contentNo);
}
