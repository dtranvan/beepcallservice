package com.beepcall.startup;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.beepcall.config.BeepCallApplicationContext;
import com.beepcall.job.FtpDeleteFileJob;
import com.beepcall.job.FtpUploadFileJob;
import com.beepcall.model.PokeCall;
import com.beepcall.service.PokeCallService;
import com.beepcall.utils.Constants;
import com.beepcall.utils.ResourceBundle;
import com.beepcall.utils.Utils;

public class SchedulerRunner {

	private static volatile SchedulerRunner instance = null;
	private static ApplicationContext appCtx;
	private static PokeCallService pokeCallService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchedulerRunner.class);

	public static SchedulerRunner getInstance() {
		if (null == instance) {
			synchronized (ResourceBundle.class) {
				if (instance == null) {
					instance = new SchedulerRunner();
				}
			}
		}
		return instance;

	}

	private SchedulerRunner() {
		appCtx = new AnnotationConfigApplicationContext(
				BeepCallApplicationContext.class);
		pokeCallService = appCtx.getBean(PokeCallService.class);
	}

	public List<PokeCall> search(Date beginTime, Date endTime) {
		return pokeCallService.search(beginTime, endTime);
	}

	public static void main(String[] args) throws IOException,
			SchedulerException {
		initializeLogger();
		LOGGER.info("Begin start service!");

		String exportTime = ResourceBundle.getMessage(Constants.EXPORT_TIME);

		//String deleteTime = ResourceBundle.getMessage(Constants.DELETE_TIME);

		JobDetail job = JobBuilder.newJob2(FtpUploadFileJob.class)
				.withIdentity("UploadFileJobToFTP", "group1").build();

		JobDetail jobDelete = JobBuilder.newJob(FtpDeleteFileJob.class)
				.withIdentity("FtpDeleteFileJob", "group2").build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("TriggerUploadFileJobToFTP", "group1")
				.withSchedule(
						CronScheduleBuilder.cronSchedule(Utils
								.createExp(exportTime))).startNow().build();

		Trigger deleteTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("TriggerDeleteFile", "group2")
				.withSchedule(
						CronScheduleBuilder.cronSchedule(Utils.createExp())).build();

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		scheduler.scheduleJob(jobDelete, deleteTrigger);

		LOGGER.info("Service Started!");

	}

	private static void initializeLogger() {
		Properties logProperties = new Properties();

		try {
			logProperties.load(new FileInputStream("conf/log4j.properties"));
			PropertyConfigurator.configure(logProperties);
			LOGGER.info("Logging initialized.");
		} catch (IOException e) {
			throw new RuntimeException("Unable to load logging property "
					+ "log4j.properties");
		}
	}
}
