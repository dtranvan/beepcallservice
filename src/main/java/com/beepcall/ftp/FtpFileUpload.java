package com.beepcall.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beepcall.utils.Constants;
import com.beepcall.utils.ResourceBundle;

/**
 * @author DungTV
 *
 */
public class FtpFileUpload {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FtpFileUpload.class);

	private static String serverName;
	private static int port;
	private static String userName;
	private static String password;

	public static String osLocal;
	public static String osRemote;
	public static String localDirPath;
	public static String remoteDirPath;	

	private static FTPClient ftpClient;
	public static long sequenceMax;
	private static volatile FtpFileUpload instance = null;

	private FtpFileUpload() {
		serverName = ResourceBundle.getMessage(Constants.FTP_SERVER_KEY);
		port = Integer.parseInt(ResourceBundle
				.getMessage(Constants.FTP_PORT_KEY));
		userName = ResourceBundle.getMessage(Constants.FTP_USER_KEY);
		password = ResourceBundle.getMessage(Constants.FTP_PASSWORD_KEY);

		ftpClient = new FTPClient();

		osLocal = ResourceBundle.getMessage(Constants.OS_LOCAL_KEY);
		osRemote = ResourceBundle.getMessage(Constants.OS_REMOTE_KEY);
		remoteDirPath = ResourceBundle
				.getMessage(Constants.REMOTE_DIR_PATH_KEY);
		localDirPath = ResourceBundle.getMessage(Constants.LOCAL_DIR_PATH_KEY);	
		sequenceMax = Long.parseLong(ResourceBundle.getMessage(Constants.MAX));

		try {
			connect();
			createParentDir(remoteDirPath, osRemote, true);
			createParentDir(localDirPath, osLocal, false);
		} catch (IOException e) {
			LOGGER.error("IOException", e.toString());
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				LOGGER.error(ex.toString());
			}
		}

	}

	public static FtpFileUpload getInstance() {
		if (null == instance) {
			synchronized (FtpFileUpload.class) {
				if (instance == null) {
					instance = new FtpFileUpload();
				}
			}
		}
		return instance;

	}

	public void connect() throws IOException {
		try {
			ftpClient.connect(serverName, port);
			boolean b = ftpClient.login(userName, password);
			if (b == false) {
				LOGGER.error("Login to FTP Server failed. Please contact admin");
				return;
			}
			ftpClient.enterLocalPassiveMode();

			ftpClient.setFileType(FTP.TELNET_TEXT_FORMAT);
		} catch (IOException ex) {
			LOGGER.error(ex.toString());
			throw ex;
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
			throw ex;
		}
	}

	public Boolean uploadSingleFile(String localDir, String remoteDir,
			String fileName) throws IOException {
		boolean uploaded;
		try {
			String localFile = FtpFileUtils.getPath(localDir, fileName, FtpFileUpload.osLocal);
			String remoteFile = FtpFileUtils.getPath(remoteDir, fileName, FtpFileUpload.osRemote);
			
			InputStream inputStream = new FileInputStream(localFile);
			LOGGER.info("Start uploading file " + fileName + " to: "
					+ remoteDir);
			
			uploaded = ftpClient.storeFile(remoteFile, inputStream);
			
			inputStream.close();
			if (uploaded) {
				LOGGER.info("The file " + fileName
						+ " is uploaded successfully.");
			}

		} catch (IOException ex) {
			LOGGER.error(ex.toString());
			throw ex;
		} catch (Exception ex) {
			LOGGER.error(ex.toString());
			throw ex;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				LOGGER.error(ex.toString());
				throw ex;
			}
		}

		return uploaded;

	}

	public boolean makeDir(String dirPath) throws IOException {
		if (!checkDirectoryExists(dirPath)) {
			ftpClient.makeDirectory(dirPath);
			return showServerReply(ftpClient);
		} else {
			return true;
		}

	}

	public static boolean showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				LOGGER.info("SERVER: " + aReply);
				return !aReply.contains("550");
			}
		}
		return false;
	}

	boolean checkFileExists(String filePath) throws IOException {
		int returnCode;
		InputStream inputStream = ftpClient.retrieveFileStream(filePath);
		returnCode = ftpClient.getReplyCode();
		if (inputStream == null || returnCode == 550) {
			return false;
		}
		return true;
	}

	boolean checkDirectoryExists(String dirPath) throws IOException {
		int returnCode;

		ftpClient.changeWorkingDirectory(dirPath);
		returnCode = ftpClient.getReplyCode();
		if (returnCode == 550) {
			ftpClient.changeToParentDirectory();
			return false;
		}
		ftpClient.changeToParentDirectory();
		return true;
	}

	public static void makeLocalDir(String dir) throws IOException {
		/**
		 * Make dir
		 */
		File fileDir = new File(dir);
		if(!fileDir.exists()) {
			fileDir.mkdir();
		}
		
		if(fileDir.exists()) {
			LOGGER.info("Created dir local successfully");
		}else{
			LOGGER.info("Has error create dir local");
		}
	}
	
	private void createParentDir(String path, String os, boolean flag) {
		LOGGER.info("Begin create Dir Parent: Path " + path + "; OS: " + os
				+ "; flag: " + flag);
		try {
			String dirs[];
			if (os.equalsIgnoreCase("WINDOW")) {
				File file = new File(path);
				if (!file.exists()) {
					file.mkdir();
				}
			}
			if (os.equalsIgnoreCase("LINUX") && flag) {
				dirs = path.split("/");
				String parentDir = null;
				for (String dir : dirs) {
					if (dir == null || dir.length() == 0)
						continue;
					if (parentDir != null)
						dir = parentDir + "/" + dir;
					if (!checkDirectoryExists(dir)) {
						makeDir(dir);
					}
					parentDir = dir;
				}
			}
			if (os.equalsIgnoreCase("LINUX") && !flag) {
				makeLocalDir(path);
			}
		} catch (IOException ex) {
			LOGGER.error("createParentDir", ex);
		}catch (Exception ex) {
			LOGGER.error("createParentDir", ex);
		}
		LOGGER.info("End create Dir Parent: Path " + path + "; OS: " + os
				+ "; flag: " + flag);

	}

}