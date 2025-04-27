package top.yumoyumo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.yumoyumo.entity.Card;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 14:30
 **/
@Mapper
public interface CardMapper extends BaseMapper<Card> {

    /**
     * 查询某用户的所有卡片
     */
    List<Card> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询即将到期需要复习的卡片
     */
    List<Card> selectDueCards(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}