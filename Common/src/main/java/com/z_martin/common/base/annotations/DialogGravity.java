package com.z_martin.common.base.annotations;

import android.view.Gravity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogGravity {
    int value() default Gravity.TOP;
}
