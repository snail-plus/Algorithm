package com.whtriples.airPurge.mobile.exception;

import com.whtriples.airPurge.mobile.vo.BaseOutVo;


public enum AirPurgeError {
    //通用错误
    SUCCESS("0000", "正确返回"),
    ERROR("9999", "服务器未知错误"),
    TIMESTAMP_ERROR("9997", "手机时间设置有误"),
    CAPTCHA_ERROR("9996", "验证码错误或已过期"),
    IMEI_ERROR("9995", "IMEI格式错误"),
    BUSINESS_FORMAT_ERROR("8889", "业务方返回格式错误"),
    BUSINESS_ERROR("8888", "业务方调用错误"), 
    START_ERROR("0001", "分页参数查询起始值不正确"),
    COUNT_ERROR("0002", "类别参数查询总数不正确"),
    USER_NAME_ERROR("0003", "用户名不能为空"),
    PWD_EMPTY_ERROR("0004", "密码不能为空"),
    TOKEN_ERROR("0005", "token错误"),
    USER_NOT_EXIST_ERROR("0006", "用户不存在"),
    USER_ID_ERROR("0007", "用户ID不能为空"),
    PWD_ERROR("0008", "密码错误或用户不存在"),
    USER_EXIST_ERROR("0009", "用户已存在"),
    DEVICE_STATUS_ERROR("0010", "设备状态异常"),
    DEVICE_NOT_EXIST_ERROR("0011", "设备不存在"),
    DEVICE_GUID_EMPTY_ERROR("0012", "设备guid为空"),
    DEVICE_ALERD_BIND_ERROR("0013", "设备已经绑定过"),
    DEVICE_ID_ERROR("0014", "设备编号错误"),
    PARAMETER_ERROR("0015", "经纬度参数错误"),
    MOBILE_NO_ERROR("0016", "手机号码错误"),
    CAPTCHA_TYPE_ERROR("0017", "验证码类型错误"),
    MOBILE_EXIST_ERROR("0018", "手机号码已存在"),
    CONNECT_SEND_SMS_SERVICE_FAIL("0019","连接短信网关失败"),
    ONE_MINUNTE_MORE_CAPTHCA("0020", "一分钟内不允许重复获取验证码"),
    ONE_DAY_PASS_TEN("0021", "您当天获取验证码的次数已超过限制的次数"),
    DATA_TYPE_ERROR("0022","数据类型错误"),
    ;
    
    public String code;

    public String message;

    private AirPurgeError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException buildException() {
        return new BusinessException(this);
    }

    public void throwException() throws BusinessException {
        throw buildException();
    }

    public static boolean isSuccess(BaseOutVo outVo) {
        return SUCCESS.code.equals(outVo.getErrorCode());
    }

    public static boolean isError(BaseOutVo outVo) {
        System.out.println("error:" + outVo.getErrorCode() + " : " + outVo.getErrorMsg());
        return !isSuccess(outVo);
    }

}
