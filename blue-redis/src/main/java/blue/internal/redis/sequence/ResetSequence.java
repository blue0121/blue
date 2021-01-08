package blue.internal.redis.sequence;

import blue.core.util.AssertUtil;
import blue.redis.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Jin Zheng
 * @since 1.0 2019-11-08
 */
public class ResetSequence implements InitializingBean, DisposableBean
{
	private static Logger logger = LoggerFactory.getLogger(ResetSequence.class);

	public static final String CRON = "0 0 0 ? * *";

	private String cron;
	private TaskScheduler taskScheduler;
	private ScheduledFuture<?> scheduledFuture;
	private List<Sequence> sequences;

	public ResetSequence()
	{
	}

	private void reset()
	{
		logger.info(">>>>> start reset");
		for (Sequence sequence : sequences)
		{
			sequence.reset();
		}
		logger.info("<<<<< end reset");
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (cron == null)
		{
			cron = CRON;
		}
		AssertUtil.notEmpty(sequences, "Sequence");
		CronTrigger trigger = new CronTrigger(cron);
		scheduledFuture = taskScheduler.schedule(() -> this.reset(), trigger);
		logger.info("start schedule, cron: {}, sequence size: {}", cron, sequences.size());
	}

	@Override
	public void destroy() throws Exception
	{
		if (scheduledFuture != null)
		{
			scheduledFuture.cancel(false);
		}
		logger.info("cancel schedule");
	}

	public void setCron(String cron)
	{
		this.cron = cron;
	}

	public String getCron()
	{
		return cron;
	}

	public TaskScheduler getTaskScheduler()
	{
		return taskScheduler;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler)
	{
		this.taskScheduler = taskScheduler;
	}

	public List<Sequence> getSequences()
	{
		return sequences;
	}

	public void setSequences(List<Sequence> sequences)
	{
		this.sequences = sequences;
	}
}
