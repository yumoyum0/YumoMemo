package top.yumoyumo.enums;


public enum ErrorEnum {

    PARAMS_LOSS(1000, "传参缺失"),

    COMMON_ERROR(5000, "错误"),

    NO_USER_ERROR(2000, "没有该用户");

    private Integer errCode;
    private String errMsg;

    ErrorEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
}