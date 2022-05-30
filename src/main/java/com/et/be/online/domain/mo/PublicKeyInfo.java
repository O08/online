package com.et.be.online.domain.mo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PublicKeyInfo {


    private Long time;


    private String publicKey;
}
