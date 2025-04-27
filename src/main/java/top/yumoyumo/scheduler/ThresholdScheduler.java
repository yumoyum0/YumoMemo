package top.yumoyumo.scheduler;

import org.springframework.stereotype.Component;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:08
 **/
@Component("THRESHOLD")
public class ThresholdScheduler implements SchedulerStrategy {

    @Override
    public double getNextInterval(int difficulty, double halflife, int reps, int lapses) {
        // 根据 THRESHOLD 策略，使用固定公式计算下次复习间隔
        return -halflife * Math.log(0.9) / Math.log(2);
    }
}
