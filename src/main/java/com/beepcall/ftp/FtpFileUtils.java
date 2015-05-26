package com.beepcall.ftp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beepcall.model.PokeCall;
import com.beepcall.utils.Constants;
import com.beepcall.utils.DateUtil;
import com.beepcall.utils.ResourceBundle;

/**
 * @author DungTV
 *
 */
public class FtpFileUtils {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileUtils.class);

	public static void writeToFile(List<String> col, String fileName,
			String pathDir) throws IOException {
		/**
		 * Make dir
		 */
		File fileDir = new File(pathDir);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		LOGGER.info("Begin Writing to file....Filename " + fileName
				+ " DirPath " + pathDir);
		File file = createNewFile(pathDir, fileName);
		FileUtils.writeLines(file, col);
		LOGGER.info("End Writing to file....Filename " + fileName + " DirPath "
				+ pathDir);
	}

	public static List<String> readFile(File file) throws IOException {
		LineIterator iter = FileUtils.lineIterator(file);
		List<String> list = new ArrayList<String>();
		while (iter.hasNext()) {
			list.add(String.valueOf(iter.next()));
		}
		return list;
	}

	public static String getfileName(Date executeDate) {
		String fileName = DateUtil.datetoString(executeDate,
				Constants.DATE_FORMAT_PATTERN);
		return fileName + ".txt";
	}

	public static File createNewFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.createNewFile()) {
			LOGGER.info("File is created!");
		} else {
			LOGGER.info("File already exists");
		}
		return file;
	}

	public static boolean deleteFile(String fileName) {
		return FileUtils.deleteQuietly(new File(fileName));
	}

	public static List<String> populateList(List<PokeCall> pokeCalls) {
		if (pokeCalls == null)
			return null;
		List<String> lStr = new ArrayList<String>();
		for (PokeCall pk : pokeCalls) {
			lStr.add(new StringBuffer().append(pk.getTransId()).append("|")
					.append(pk.getCaller()).append("|").append(pk.getMsisdn())
					.append("|").append(DateUtil.datetoString(pk.getRequestTime(), Constants.DATE_FORMAT_PATTERN) ).append("|")
					.append(pk.getResponse()).toString());
		}

		return lStr;
	}

	public static String getPath(String dirPath, String fileName, String os) {
		StringBuffer filePath = new StringBuffer();
		if (StringUtils.isBlank(fileName))
			return dirPath;

		if (os.equalsIgnoreCase("WINDOW"))
			filePath.append(dirPath).append("\\").append(fileName);
		if (os.equalsIgnoreCase("LINUX"))
			filePath.append(dirPath).append("/").append(fileName);

		return filePath.toString();
	}

	public static File createNewFile(String dir, String fileName)
			throws IOException {
		/**
		 * Make dir
		 */
		File fileDir = new File(dir);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		/**
		 * Make file
		 */
		String osRemote = ResourceBundle.getMessage(Constants.OS_REMOTE_KEY);
		String fName = getPath(dir, fileName, osRemote);
		File f = new File(fName);
		f.createNewFile();
		return f;
	}
}
