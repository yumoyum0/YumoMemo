package top.yumoyumo.vo;

import lombok.Data;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:24
 **/
@Data
public class ReviewFeedbackVO {
    private long cardId;
    private int recallResult; // 记住或忘记
    private String method; // 调度方法（如 "SSP-MMC", "THRESHOLD", "ANKI"）
}
