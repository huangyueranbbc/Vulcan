package org.huangyr.project.vulcan.runner.net.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/*******************************************************************************
 *
 * @date 2019-12-18 2:45 PM
 * @author: <a href=mailto:huangyueran>黄跃然</a>
 * @Description: 重试策略，可重试一定次数，并增加重试之间的睡眠时间
 *
 * {@link RetryPolicy 重试策略接口}
 ******************************************************************************/
@Slf4j
public class ExponentialBackOffRetry implements RetryPolicy {

    private static final int MAX_RETRIES_LIMIT = 29;
    private static final int DEFAULT_MAX_SLEEP_MS = Integer.MAX_VALUE;

    private final Random random = new Random();
    private final long baseSleepTimeMs;
    private final int maxRetries;
    private final long maxSleepMs;

    public ExponentialBackOffRetry(int baseSleepTimeMs, int maxRetries) {
        this(baseSleepTimeMs, maxRetries, DEFAULT_MAX_SLEEP_MS);
    }

    public ExponentialBackOffRetry(long baseSleepTimeMs, int maxRetries, long maxSleepMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxRetries = maxRetries;
        this.maxSleepMs = maxSleepMs;
    }

    @Override
    public boolean allowRetry(int retryCount) {
        return retryCount < maxRetries;
    }

    /**
     * 根据指数退避算法进行重连
     *
     * @param retryCount current retry count
     * @return
     */
    @Override
    public long getSleepTimeMs(int retryCount) {
        log.warn("maxRetries too large ({}). Pinning to {}", maxRetries, MAX_RETRIES_LIMIT);

        if (retryCount < 0) {
            throw new IllegalArgumentException("retries count must greater than 0.");
        }
        if (retryCount > MAX_RETRIES_LIMIT) {
            log.warn("maxRetries too large ({}). Pinning to {}", maxRetries, MAX_RETRIES_LIMIT);
            retryCount = MAX_RETRIES_LIMIT;
        }
        long sleepMs = baseSleepTimeMs * Math.max(1, random.nextInt(1 << retryCount));
        if (sleepMs > maxSleepMs) {
            log.warn("Sleep extension too large ({}). Pinning to {}", sleepMs, maxSleepMs);
            sleepMs = maxSleepMs;
        }
        return sleepMs;
    }
}