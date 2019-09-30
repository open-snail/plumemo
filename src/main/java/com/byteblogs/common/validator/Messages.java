package com.byteblogs.common.validator;

/**
 * @author Andy Chen
 * @date 9/30/19 12:46 PM
 */
public interface Messages {
    /**
     * 类内部使用
     */
    String CK_RANG_MESSAGE_LENGTH_TYPE = "length must be between 0 and 11:%s";
    String CK_NUMERIC_TYPE = "field must be a number:%s";

    /**
     * 注解默认
     */
    String CK_NOT_BLANK_DEFAUL = "field can not be blank:{value}";
    String CK_NUMERIC_DEFAUL = "field must be a number:{value}";
    String CK_RANGE_DEFAUL = "field should be an integer,between {min} and {max}:{value}";

    /**
     * 注解使用
     */
    String CATEGORY_NAME_NOT_BLANK="name can not be blank:{value}";
    String ID_NOT_NULL="id can not be null:{value}";

}

