package top.yumoyumo.scheduler;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:07
 **/
public interface SchedulerStrategy {
    /**
     * 获取下次复习的时间间隔
     *
     * @param difficulty 卡片难度
     * @param halflife 当前半衰期
     * @param reps 复习次数
     * @param lapses 遗忘次数
     * @return 下次复习的时间间隔（天数）
     */
    double getNextInterval(int difficulty, double halflife, int reps, int lapses);

}
