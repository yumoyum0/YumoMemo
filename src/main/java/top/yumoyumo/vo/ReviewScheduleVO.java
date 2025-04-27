package top.yumoyumo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:23
 **/
@Data
public class ReviewScheduleVO {
    private Long cardId;
    private String front;
    private String back;
    private LocalDateTime dueDate;
}
