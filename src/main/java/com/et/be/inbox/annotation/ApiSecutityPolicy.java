package com.et.be.inbox.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiSecutityPolicy {
    String value() ;

}
