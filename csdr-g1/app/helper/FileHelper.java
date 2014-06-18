package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {
	
	public static boolean moveFile(File src, String destPath) {
		return moveFile(src, destPath, null);
	}
	
	public static boolean moveFile(File src, String destPath, String destFilename) {
		InputStream inStream = null;
		OutputStream outStream = null;
		
		File destinationFolder = new File(destPath);
		if(!destinationFolder.exists()) {
			try {
				destinationFolder.mkdir();
			} catch (SecurityException se) {
				System.out.println("Failed to create directory " + destPath);
				return false;
			}
		}
		
		String filepath = null;
		if(destFilename == null) {
			filepath = destPath + src.getName();
		} else {
			filepath = destPath + destFilename;
		}
		
		File dest = new File(filepath);
		try {
			inStream = new FileInputStream(src);
			outStream = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			while((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			//@TODO: Delete the original file, currently fails because of permission issues (no write permissions on the original file)
			// src.delete();

			return true;

		} catch(IOException ioe) {
			return false;
		}
	}

}
