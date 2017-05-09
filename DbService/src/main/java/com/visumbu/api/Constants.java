/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api;

import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author varghees
 */
public class Constants {

    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final int EMAIL = 100;
    public static final int SMS = 101;
    // Job 
    public static final int JOB_ONE_TIME = 200;
    public static final int JOB_ONE_TIME_SPECIFIC_TIME = 201;
    public static final int JOB_REPEAT_FOR_EVER = 202;
    public static final int JOB_CRON_JOB = 203;

    // Mail
    public static final int UNKNOWN = -1;
    public static final int TEXT_MAIL = 300;
    public static final int HTML_MAIL = 301;
    public static final int TEXT_MAIL_WITH_ATTACHMENT = 302;
    public static final int HTML_WITH_ATTACHMENT = 303;
    public static final int HTML_WITH_EMBEDDED_IMAGE = 304;
    // File path
    
}
