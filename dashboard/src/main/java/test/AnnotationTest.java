/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 *
 * @author dashience
 */
@Documented
@Target (ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)

public @interface AnnotationTest {
    
String method() default "GET";
String value();

}
