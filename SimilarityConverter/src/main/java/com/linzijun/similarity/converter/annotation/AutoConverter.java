package com.linzijun.similarity.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zijun.lzj on
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoConverter {

    Class<?> value();
}
