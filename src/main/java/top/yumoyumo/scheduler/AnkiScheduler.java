package top.yumoyumo.scheduler;

import org.springframework.stereotype.Component;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:08
 **/
@Component("ANKI")
public class AnkiScheduler implements SchedulerStrategy {

    @Override
    public double getNextInterval(int difficulty, double halflife, int reps, int lapses) {
        // ANKI 策略公式：间隔随着复习次数增加呈指数增长
        return Math.max(2.5 - 0.15 * lapses, 1.2) * Math.pow(2, reps);
    }
}