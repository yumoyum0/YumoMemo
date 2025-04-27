package top.yumoyumo.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * @Author: yumo
 * @Description: 卡片实体类
 * @DateTime: 25.12.2024 22:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("card")
@Alias("card")
public class Card {

    /**
     * 卡片编号
     */
    @JSONField(name = "card_id")
    @TableId(value = "card_id", type = IdType.AUTO)
    private Long cardId;

    /**
     * 用户编号
     */
    @JSONField(name = "user_id")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 卡片正面内容
     */
    @JSONField(name = "front")
    @TableField(value = "front")
    private String front;

    /**
     * 卡片背面内容
     */
    @JSONField(name = "back")
    @TableField(value = "back")
    private String back;

    /**
     * 卡片难度
     */
    @JSONField(name = "difficulty")
    @TableField(value = "difficulty")
    private Integer difficulty;

    /**
     * 记忆半衰期
     */
    @JSONField(name = "halflife")
    @TableField(value = "halflife")
    private Double halflife;

    /**
     * 回忆概率
     */
    @JSONField(name = "p_recall")
    @TableField(value = "p_recall")
    private Double pRecall;

    /**
     * 上次复习时间间隔
     */
    @JSONField(name = "delta_t")
    @TableField(value = "delta_t")
    private Integer deltaT;

    /**
     * 复习次数
     */
    @JSONField(name = "reps")
    @TableField(value = "reps")
    private Integer reps;

    /**
     * 遗忘次数
     */
    @JSONField(name = "lapses")
    @TableField(value = "lapses")
    private Integer lapses;

    /**
     * 最后一次复习时间
     */
    @JSONField(name = "last_date", format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "last_date")
    private LocalDateTime lastDate;

    /**
     * 下次复习时间
     */
    @JSONField(name = "due_date", format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "due_date")
    private LocalDateTime dueDate;

    /**
     * 回忆历史记录
     */
    @JSONField(name = "r_history")
    @TableField(value = "r_history")
    private String rHistory;

    /**
     * 时间间隔历史记录
     */
    @JSONField(name = "t_history")
    @TableField(value = "t_history")
    private String tHistory;

    /**
     * 卡片创建时间
     */
    @JSONField(name = "created_at", format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 最近更新时间
     */
    @JSONField(name = "updated_at", format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}
