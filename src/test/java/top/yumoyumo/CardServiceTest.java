package top.yumoyumo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yumoyumo.scheduler.SchedulerContext;
import top.yumoyumo.scheduler.SchedulerStrategy;

/**
 * @Author: yumo
 * @Description: TODO
 * @DateTime: 26.12.2024 15:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YumoMemoApplication.class)
public class CardServiceTest {
    @Autowired
    private SchedulerContext schedulerContext;

    @Test
    public void testSchedulerStrategies() {
        SchedulerStrategy ssp = schedulerContext.getStrategy("SSP-MMC");
        SchedulerStrategy threshold = schedulerContext.getStrategy("THRESHOLD");
        SchedulerStrategy anki = schedulerContext.getStrategy("ANKI");

        double intervalSSP = ssp.getNextInterval(5, 18.0f, 5, 2);
        double intervalThreshold = threshold.getNextInterval(5, 18.0f, 5, 2);
        double intervalAnki = anki.getNextInterval(5, 18.0f, 5, 2);

        System.out.println("SSP-MMC Interval: " + intervalSSP);
        System.out.println("THRESHOLD Interval: " + intervalThreshold);
        System.out.println("ANKI Interval: " + intervalAnki);
    }

}
