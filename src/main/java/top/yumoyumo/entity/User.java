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
 * @Description: 用户实体类
 * @DateTime: 25.12.2024 22:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("user")
@Alias("user")
public class User {

    /**
     * 用户编号
     */
    @JSONField(name = "user_id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @JSONField(name = "username")
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @JSONField(name = "password")
    @TableField(value = "password")
    private String password;

    /**
     * 邮箱
     */
    @JSONField(name = "email")
    @TableField(value = "email")
    private String email;

    /**
     * 注册时间
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

    /**
     * 记忆算法
     */
    @JSONField(name = "method")
    @TableField(value = "method")
    private String method;
}