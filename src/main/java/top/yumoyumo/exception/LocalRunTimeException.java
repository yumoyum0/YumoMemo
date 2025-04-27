package top.yumoyumo.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import top.yumoyumo.enums.ErrorEnum;


@EqualsAndHashCode(callSuper = true)
@Data
public class LocalRunTimeException extends RuntimeException {
    private ErrorEnum errorEnum;

    //默认错误
    public LocalRunTimeException(String message) {
        super(message);
    }

    public LocalRunTimeException(ErrorEnum errorEnum) {
        super(errorEnum.getErrMsg());
        this.errorEnum = errorEnum;
    }
}