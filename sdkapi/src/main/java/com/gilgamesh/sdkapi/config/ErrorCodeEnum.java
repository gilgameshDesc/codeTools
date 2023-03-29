package com.gilgamesh.sdkapi.config;

public enum ErrorCodeEnum {
    SUCCESS("0", "请求成功"),
    ERROR("-1", "系统错误"),

    COMMON_TOKEN_EMPTY("1001", "缺少token参数"),
    COMMON_TOKEN_ERROR("1002", "token校验失败"),
    COMMON_ACCOUNT_EMPTY("1003", "获取账号信息失败"),
    COMMON_ACCOUNT_ERROR("1004", "账号或密码错误"),
    COMMON_ACCOUNT_NULL("1005", "不存在的账号"),
    COMMON_PARAM_EMPTY("2101", "必填参数为空"),
    COMMON_PARAM_ERROR("2201", "参数格式错误"),
    COMMON_PARAM_LENGTH("2301", "参数长度超过限制"),
    COMMON_PARAM_RANGE("2302", "参数范围错误"),
    COMMON_PARAM_ENCRYPT("2303", "返回值加密异常"),

    FILE_NOT_EXIST("1101", "文件不存在"),

    HTTP_URL_EMPTY("4000", "接口地址为空"),
    HTTP_CALL_ERROR("4001", "远端服务不可用"),
    HTTP_RESPONSE_ERROR("4002", "解析JSON内容错误"),
    HTTP_RESPONSE_EMPTY("4003", "接口返回为空"),
    HTTP_RESULT_MSG_ERROR("4005", "接口返回错误"),

    BUS_BIND_NONE("5001", "未查询到绑定关系"),
    BUS_BIND_DUP("5002", "已存在绑定关系"),

    SYSTEM_UNKNOWN_ERROR("-1", "系统繁忙，请稍后再试....");

    private String code;
    private String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static ErrorCodeEnum getErrorCodeEnum(String errorCode) {
        for (ErrorCodeEnum ele : values()) {
            if (ele.getCode().equals(errorCode)) {
                return ele;
            }
        }
        return ErrorCodeEnum.ERROR;
    }

}
