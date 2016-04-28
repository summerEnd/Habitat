package com.sumauto.habitat.callback;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/23 5.6.6 
 */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewId {
    int value() default 0;
}
