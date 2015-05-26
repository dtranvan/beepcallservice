package com.beepcall.job;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.beepcall.ftp.FtpFileUpload;
import com.beepcall.ftp.FtpFileUtils;
import com.beepcall.model.PokeCall;
import com.beepcall.startup.SchedulerRunner;
import com.beepcall.utils.Constants;
import com.beepcall.utils.DateUtil;
import com.beepcall.utils.LoadDynParam;
import com.beepcall.utils.Utils;

@Component
public class FtpUploadFileJob implements Job {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FtpUploadFileJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		/**
		 * Make dictionary And sent file to remote FTP server
		 */
		FtpFileUpload fUploadFile = FtpFileUpload.getInstance();
		Date executeDate = new Date();

		LoadDynParam ldp = new LoadDynParam();
		String uploadRemoteDir = FtpFileUpload.remoteDirPath;
		String uploadLocalDir = FtpFileUpload.localDirPath;
		String requestTime = ldp.getRequestTime();
		long count = Long.parseLong(ldp.getCount());

		try {

			Date beginDate = getBeginDate(requestTime);

			List<PokeCall> pokeCalls = SchedulerRunner.getInstance().search(
					beginDate, executeDate);

			if (Utils.isEmpty(pokeCalls)) {
				LOGGER.info("Data list are empty...... Stopped!");
				return;
			}

			if (FtpFileUpload.sequenceMax < count) {
				count = 0;
			}

			String fileName = "beepcall_"
					+ DateUtil.datetoString(executeDate,
							Constants.DATE_FORMAT_PATTERN) + "_"
					+ StringUtils.leftPad(String.valueOf(count), 8, "0")
					+ ".txt";

			count++;
			LOGGER.info("Begin upload data: fileName: " + fileName
					+ "; dirPath: " + uploadRemoteDir + "; From Date: "
					+ beginDate + "; To Date: " + executeDate);

			FtpFileUtils.writeToFile(FtpFileUtils.populateList(pokeCalls),
					fileName, uploadLocalDir);

			fUploadFile.connect();
			boolean created = fUploadFile.makeDir(uploadRemoteDir);
			if (created == false) {
				LOGGER.error("Create directory operation failed(550): Name "
						+ uploadRemoteDir);
				return;
			}

			boolean uploaded = fUploadFile.uploadSingleFile(uploadLocalDir,
					uploadRemoteDir, fileName);
			// FtpFileUtils.deleteFile(localFile);
			if (uploaded) {
				updateKeyToResource(Constants.REQUEST_TIME,
						DateUtil.datetoString(executeDate,
								Constants.DATETIME_PATTERN));
				updateKeyToResource(Constants.COUNT, String.valueOf(count));
				LOGGER.info("The file have been upload to " + uploadRemoteDir);
			} else {
				LOGGER.info("The file " + fileName + " upload to "
						+ uploadRemoteDir + " has errors");
			}
		} catch (IOException e) {
			LOGGER.error("IOException", e);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		} finally {
			// FtpFileUtils.deleteFile(localFile);
		}

	}

	private Date getBeginDate(String requestTime) {
		Date beginDate;

		if (!requestTime.equals("0")) {
			beginDate = DateUtil.secondToDate(DateUtil.stringToDate(
					requestTime, Constants.DATETIME_PATTERN), 1);
		} else {
			beginDate = DateUtil.stringToDate(Constants.MIN_DATE,
					Constants.DATETIME_PATTERN);
		}
		return beginDate;
	}

	private void updateKeyToResource(String key, String value) {
		updateResourceBundle(key, value);
	}

	public void updateResourceBundle(String key, String value) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(
					Constants.CONFIG_DYN_PARAM);

			config.setProperty(key, value);
			config.save();
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
	}

}
