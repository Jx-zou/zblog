package xyz.jxzou.zblog.mail.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.mail.config.ThreadPool;

/**
 * MailTreadPoolTaskExecutor
 *
 * @author Jx
 */
@Component("mailTreadPoolTaskExecutor")
public class MailTreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = -7071021297991383051L;

    private static final String THREAD_NAME_PREFIX = "[Mail] mailTaskExecutor--";
    private ThreadPool config;

    /**
     * Instantiates a new Mail tread pool task executor.
     *
     * @param config the config
     */
    @Autowired
    public MailTreadPoolTaskExecutor(ThreadPool config) {
        this.config = config;
    }

    /**
     * Instantiates a new Mail tread pool task executor.
     */
    public MailTreadPoolTaskExecutor() {
        super.setCorePoolSize(config.getCorePoolSize());
        super.setMaxPoolSize(config.getMaxPoolSize());
        super.setQueueCapacity(config.getQueueCapacity());
        super.setKeepAliveSeconds(config.getKeepAliveSeconds());
        super.setThreadNamePrefix(THREAD_NAME_PREFIX);
        super.setWaitForTasksToCompleteOnShutdown(config.isWaitForTasksToCompleteOnShutdown());
        super.setAwaitTerminationMillis(config.getAwaitTerminationMillis());
    }
}
