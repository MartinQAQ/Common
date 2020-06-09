package com.z_martin.common.base.annotations;


import androidx.annotation.FloatRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface DialogHeight {
    
    @FloatRange(from=0.0, to=1)
    double value() default 1;
}
