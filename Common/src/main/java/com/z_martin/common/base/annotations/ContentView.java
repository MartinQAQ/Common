package com.z_martin.common.base.annotations;


import androidx.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
    @LayoutRes
    int value() default ResId.DEFAULT_VALUE;
}
