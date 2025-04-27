package top.yumoyumo.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 19:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result implements Serializable {
    /**
     * 成功与否
     */
    @JSONField(name = "success")
    private Boolean success;

    /**
     * 状态码
     */
    @JSONField(name = "code")
    private Integer code;

    /**
     * 错误消息
     */
    @JSONField(name = "errMsg")
    private String errMsg;

    /**
     * 数据对象
     */
    @JSONField(name = "data")
    private Object data;

    public static Result success(Object data) {
        return new Result(true, 200, null, data);
    }

    public static Result failure(String errMsg) {
        return new Result(false, 500, errMsg, null);
    }

}