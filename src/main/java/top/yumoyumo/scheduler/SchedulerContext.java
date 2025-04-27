package top.yumoyumo.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:08
 **/
@Component
public class SchedulerContext {

    /**
     * 保存所有调度策略的映射（策略名 -> 策略实现类实例）
     */
    private final Map<String, SchedulerStrategy> strategies;

    /**
     * 构造方法注入所有策略
     *
     * @param strategies Spring 自动注入的策略映射
     */
    @Autowired
    public SchedulerContext(Map<String, SchedulerStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * 根据方法名称获取对应的调度策略
     *
     * @param method 策略方法名（如 "SSP-MMC"、"THRESHOLD"、"ANKI"）
     * @return 对应的调度策略
     */
    public SchedulerStrategy getStrategy(String method) {
        SchedulerStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported scheduling method: " + method);
        }
        return strategy;
    }
}