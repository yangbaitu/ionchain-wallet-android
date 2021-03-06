package org.ionc.wallet.constant;

import java.math.BigDecimal;

/**
 * USER: binny
 * DATE: 2018/9/26
 * 描述: 常量类
 */
public final class ConstantParams {
    /**
     * 钱包实体类
     */
    public static final String SERIALIZABLE_DATA_WALLET_BEAN = "wallet_bean";
    public static final int REQUEST_MODIFY_WALLET_PWD = 100;


    /**
     * 传递钱包对象
     */
    public static final String PARCELABLE_WALLET_BEAN = "parcelable_data";
    public static final String PARCELABLE_TX_RECORD = "PARCELABLE_TX_RECORD";
    public static final String SERIALIZABLE_DATA1 = "serializable_data1";

    public static final String DB_NAME = "ionchainwallet";
    public static final String SPLIT = ",";
    public static final String IS = "=";
    public static final String DATA_ERROR = "data-error";
    public static final String JUMP_PARM_ISADDMODE = "isaddmode";
    public static final String user = "user_model";
    public static final String FROM_WELCOME = "welcome";

    public static final int FROM_SCAN = 999;


    public static final String PICTURE_FILE_NAME = "ionchainAddress";


    public static final int GAS_LIMIT_MAX_RANGE = 500000;
    public static final int GAS_LIMIT_DEFAULT = 100000;
    public static final int GAS_LIMIT_MIN = 300000;
    public static final BigDecimal GAS_PRICE_DEFAULT_WEI = BigDecimal.valueOf(20000000000L);

    public static final String CURRENT_ADDRESS = "address";
    public static final String CURRENT_BALANCE = "balance";
    public static final String CURRENT_KSP = "ksp";

    public static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    public static final int REQUEST_STORAGE_PERMISSIONS = 2;

    /*
     * 版本对话框的按钮事件
     * */
    public static final char VERSION_TAG_CHECK_FOR_UPDATE = 0;
    public static final char VERSION_TAG_DOWNLOAD = 1;


    public static final String INTENT_PARAME_WALLET_ADDRESS = "msg";


    /**
     * WEB 连接
     */

    public static final String URL_REQUEST_TYPE = "url_tag";//协议
    public static final char URL_TAG_PROTOCOL = 0;//协议
    public static final char URL_TAG_ABOUT_US = 1;//关于我们




    public static final String DOWNLOAD_MUST_UPDATE_NO = "0";//  不是必须更新，可以取消对话框
    public static final String DOWNLOAD_MUST_UPDATE_YES = "1";//   必须更新，不可取消


    public static final String DEFAULT_TRANSCATION_HASH_NULL = "null";//   必须更新，不可取消


    public static final int TX_ACTIVITY_FOR_RESULT_CODE = 10;
    public static final String TX_ACTIVITY_RESULT = "TX_ACTIVITY_RESULT";
    public static final String TX_HASH = "TX_HASH";
    public static final String TX_HASH_NULL = "null";

    public static final String TX_SUSPENDED = "TX_SUSPENDED";
    public static final String TX_FAILURE = "TX_FAILURE";

}
