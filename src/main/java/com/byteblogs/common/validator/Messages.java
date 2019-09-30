package com.byteblogs.common.validator;

/**
 * @author Andy Chen
 * @date 9/30/19 12:46 PM
 */
public interface Messages {
    /**
     * 类内部使用,自定义reject value
     */
    String CK_RANG_MESSAGE_LENGTH_TYPE = "length must be between 0 and 11:%s";
    String CK_NUMERIC_TYPE = "field must be a number:%s";

    /**
     * 注解默认
     */
    String CK_NOT_BLANK_DEFAUL = "can not be blank";
    String CK_NUMERIC_DEFAUL = "must be a number";
    String CK_RANGE_DEFAUL = "should be an integer,between {min} and {max}";
    String ID_NOT_NULL="can not be null";
}

