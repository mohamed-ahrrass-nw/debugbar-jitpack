package com.debugbar.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

//@Profile("dev")
@ComponentScan("com.debugbar")
public @interface EnableDebugbar {
//    @Value("${env == 'development'}")
//    boolean enable() default true; // CHECK IT OUT IMPORTANT !!!!
//
//    // THE NEED OF AN ASPECT TO GET THE CURRENT ENVIRONMENT
}
