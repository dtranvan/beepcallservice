package com.beepcall.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beepcall.utils.Constants;
import com.beepcall.utils.DateUtil;
import com.beepcall.utils.ResourceBundle;

public class FtpDeleteFileJob implements Job {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FtpUploadFileJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		Date executeDate = new Date();

		LOGGER.info("Begin delete folder... Start Time: "
				+ DateUtil
						.datetoString(executeDate, Constants.DATETIME_PATTERN));
		String parentDirName = ResourceBundle
				.getMessage(Constants.LOCAL_DIR_PATH_KEY);
		int count = 0;

		try {
			count = Integer
					.parseInt(ResourceBundle.getMessage("PDeleteFolder"));
		} catch (NumberFormatException e) {
			LOGGER.error(e.toString());
		}

		try {
			deleteFolders(parentDirName, executeDate, -count);
		} catch (IOException e) {
			LOGGER.error("deleteFolders: " + e.toString());
		}catch (Exception e) {
			LOGGER.error("deleteFolders: " + e.toString());
		}

	}

	private void deleteFolders(String dirPath, Date executeDate, int count)
			throws IOException {
		LOGGER.info("Begin to delete: " + dirPath + "Begin Time: "
				+ executeDate);
		try {

			File f = new File(dirPath);
			File[] files = f.listFiles();
			for (File fle : files) {
				if (DateUtil.check(fle.getPath(), executeDate, count)) {
					FileUtils.deleteQuietly(fle);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
