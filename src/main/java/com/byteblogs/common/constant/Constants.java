package com.byteblogs.common.constant;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class Constants {

    // 系统全局是否标识
    public static final int YES = 1;
    public static final int NO = 0;

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;

    // 系统默认ID，代表游客
    public static final String DEFAULT_USER_ID = "-1";
    // 默认文件路径分隔符
    public static final String PATH_SEPARATOR = "/";
    // token有效期
    public static final long EXPIRE_TIME = 7 * 24 * 3600 * 1000;
    // 分页默认起始页
    public static final int DEFAULT_PAGE_INDEX = 1;
    // 分页默认每页条数
    public static final int DEFAULT_PAGE_SIZE = 10;
    // 事务有效时间（持续300秒）
    public static final int TX_METHOD_TIMEOUT = 300;

    // 树形路径分隔符
    public static final String TREE_PATH = ".";

    // 网易云音乐配置项
    public static final String CLOUD_MUSIC_ID = "cloud_music_id";

    // 文件上传类型配置项
    public static final String STORE_TYPE = "store_type";
    public static final String DEFAULT_TYPE = "default";
    public static final String ALIYUN_OSS = "aliyun_oss";
    public static final String QINIU = "qiniu";
    public static final String COS = "cos";

    // 上传文件路径
    public static final String DEFAULT_PATH = "default_path";
    public static final String DEFAULT_IMAGE_DOMAIN = "default_image_domain";
    public static final String WIN_DEFAULT_PATH="D:/helloblog/blog/";
    public static final String OS_DEFAULT_PATH="/home/helloblog/blog/";
    public static final String FILE_URL = "/files/";

    // 七牛云配置
    public static final String QINIU_ACCESS_KEY = "qiniu_access_key";
    public static final String QINIU_SECRET_KEY = "qiniu_secret_key";
    public static final String QINIU_BUCKET = "qiniu_bucket";
    public static final String QINIU_IMAGE_DOMAIN = "qiniu_image_domain";

    // 腾讯COS配置
    public static final String COS_ACCESS_KEY = "cos_access_key";
    public static final String COS_SECRET_KEY = "cos_secret_key";
    public static final String COS_BUCKET = "cos_bucket";
    public static final String COS_REGION = "cos_region";
    public static final String COS_IMAGE_DOMAIN = "cos_image_domain";
    public static final String COS_PATH = "cos_path";

    // 阿里OSS配置
    public static final String ALIYUN_OSS_ACCESS_KEY = "aliyun_oss_access_key";
    public static final String ALIYUN_OSS_SECRET_KEY = "aliyun_oss_secret_key";
    public static final String ALIYUN_OSS_BUCKET = "aliyun_oss_bucket";
    public static final String ALIYUN_OSS_IMAGE_DOMAIN = "aliyun_oss_image_domain";
    public static final String ALIYUN_OSS_ENDPOINT = "aliyun_oss_endpoint";
    public static final String ALIYUN_OSS_PATH = "aliyun_oss_path";

    // header认证字段
    public static final String AUTHENTICATION = "Authorization";
    public static final String BYTE_BLOGS_URL = "https://www.byteblogs.com";

    //异常类型
    public static final String DELIMITER_TO = "@";
    public static final String DELIMITER_COLON = ":";

    // 正则表达式
    public static final String IP_REGEX = "(\\\\d{1,3}|\\\\*)";
}
