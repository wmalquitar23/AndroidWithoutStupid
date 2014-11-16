/*
 * Android Without Stupid Java Library
 * Created by V. Subhash - http://www.MoralVolcano.com
 * Released as Public Domain Software in 2014
 */

package com.vsubhash.droid.androidwithoutstupid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;

import org.apache.http.util.EncodingUtils;

import android.os.Debug;
import android.os.Environment;

/**
 * This class provides routines for performing file I/O operations.
 * 
 * @author V. Subhash
 * @version 1.0
 */
public class MvFileIO {
	
  private static final long FILE_COPY_BUFFER_SIZE = 1024*1024;	
  
	/**
	 * Checks if a bitmap with the specified size fits in memory
	 * @param bmpwidth Bitmap width
	 * @param bmpheight Bitmap height
	 * @param bmpdensity Bitmap bpp (use 2 as default)
	 * @return true if the bitmap fits in memory false otherwise
	 */
	public static boolean checkBitmapFitsInMemory(long bmpwidth,long bmpheight, int bmpdensity ){
	    long reqsize=bmpwidth*bmpheight*bmpdensity;
	    long allocNativeHeap = Debug.getNativeHeapAllocatedSize();


	    final long heapPad=(long) Math.max(4*1024*1024,Runtime.getRuntime().maxMemory()*0.1);
	    if ((reqsize + allocNativeHeap + heapPad) >= Runtime.getRuntime().maxMemory())
	    {
	        return false;
	    }
	    return true;

	}
	
	
	/**
	 * Creates a directory with specified path.
	 * 
	 * @param asPathname path to the directory.
	 * @return information whether the operation was successful
	 */
	public static MvException createDirectory(String asPathname) {
		File oDir = new File(asPathname);
		MvException result = new MvException();

		if (!oDir.exists()) {
			if (oDir.mkdirs()) {
				result.mbSuccess = true;
				result.moResult = oDir;
			} else {
				result.mbSuccess = false;
				result.msProblem = "Could not create directory at " + asPathname;
			}
		} else {
			result.mbSuccess = true;
			result.msProblem = "Directory already exists";
		}
	
    return(result);
	}
	
	/**
	 * Creates a file with specified pathname. This method returns false if the
	 * file already exists. So, check that file does not exist before calling 
	 * this method.
	 * 
	 * @param asPathname
	 *          file or directory that needs to be created
	 * @return whether it was successful or not
	 */
	public static MvException createNewFile(String asPathname) {
		MvException oRet = new MvException();
		File oFile = new File(asPathname);
		if (oFile.exists()) {
			oRet.mbSuccess = false;
			return(oRet);
		} else {
			try {
				oFile.createNewFile();
			} catch (Exception e) {
				oRet.mbSuccess = false;
				oRet.mException = e;
				return(oRet);
			}
			oRet.mbSuccess = true;
			oRet.moResult = oFile;
			return(oRet);
		}
	}
	
	
	/**
	 * Deletes the file or directory. As this method internally calls
	 * itself, use {@link isPathExist} afterwards to see if the method was
	 * successful.
	 * 
	 * @param asPath path of the file or directory
	 */
	public static boolean deletePath(String asPath) {
		File oPath = new File(asPath);
		if (oPath.exists()) {
    if (oPath.isDirectory()) {
      for (File oEntry : oPath.listFiles()) {
      	if (oEntry.isDirectory()) {
      		if (!deletePath(oEntry.getAbsolutePath())) {
      			return(false);      		
      		}
      	} else {
       		if (oEntry.delete()) {
       			MvMessages.logMessage(((oEntry.isFile())?"Deleted file":"Deleted directory") + " - " + 
       					oEntry.getAbsolutePath());
       		} else {	
       		  MvMessages.logMessage(((oEntry.isFile())?"Could not delete file":"Could not delete directory") + " - " + 
       		  		oEntry.getAbsolutePath());
       		  return(false);
       		}
      	}
      }
    }
    
 		if (oPath.delete()) {
 			MvMessages.logMessage(((oPath.isFile())?"Deleted file ":"Deleted Directory") + " - " + 
 														oPath.getAbsolutePath());
 			return(true);
 		} else {	
 		  MvMessages.logMessage(((oPath.isFile())?"Could not delete file ":"Could not delete directory") + " - " + 
 		  											oPath.getAbsolutePath());
 		  return(false);
 		}
		} else {
 		  MvMessages.logMessage(((oPath.isFile())?"Could not find file ":"Could not find directory") + " - " + 
						oPath.getAbsolutePath());
			return(false);
		}
	}
	
	
	/**
	 * Returns path of the external storage (SD Card).
	 * 
	 * @return result of the operation
	 */
	public static MvException getExternalStoragePath() {
		String sDirPath;
		
		MvException oResult = new MvException();
		
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			sDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			oResult.moResult = sDirPath;
			oResult.mbSuccess = true;			
		} else {
			oResult.mbSuccess = false;
			oResult.msProblem = "External storage is not ready";
			oResult.msPossibleSolution = "Insert SD card or wait for SD card to be recognized";
		}
		return(oResult);
	}
	
	
	/**
	 * Returns whether the path exists.
	 * 
	 * @param asPathname file or directory that needs to be checked
	 * @return whether the path exists
	 */
	public static boolean isPathExists(String asPathname) {
		if (asPathname != null) {
		  File oFile = new File(asPathname);
		  return(oFile.exists());
		}
		return false;
	} 
		
	
	/**
	 * Returns whether external storage (SD Card) is available.
	 * @return
	 */
	public static boolean isSdCardPresent() {
    return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
  }
	
	public static MvException saveToTextFile(String asTextToBeSaved, String asFileToBeSaved) {
		MvException oRet = new MvException();
		
		FileOutputStream oFOS;
		try {
			oFOS = new FileOutputStream(asFileToBeSaved);
			oFOS.write(EncodingUtils.getBytes(asTextToBeSaved, "utf-8"));
			oFOS.flush();
			oFOS.close();			
			oRet.mbSuccess = true;
			oRet.moResult = asFileToBeSaved;
		} catch (FileNotFoundException e) {
			oRet.msProblem = "File could not be opened for writing";
		} catch (IOException e) {
			oRet.msProblem = "File could not be saved";
		}
		return(oRet);
	}
	
  /**
   * Returns text content of specified text file.
   * 
   * @param asTextFile text file whose text content is required.
   * @return text file contexts
   */
  public static MvException getFileAsText(String asTextFile, int iSize) {
  	MvException oRet = new MvException();
  	
  	byte[] buf = new byte[iSize];
  	FileInputStream oInputStream;
		try {
			oInputStream = new FileInputStream(asTextFile);
			oInputStream.read(buf);
			oRet.moResult = EncodingUtils.getAsciiString(buf).trim();
			oInputStream.close();
			oRet.mbSuccess = true;
		} catch (FileNotFoundException e) {
			oRet.msProblem = "The file was not found.";
			oRet.msPossibleSolution = "Check the path";
			oRet.moResult = "";
			oRet.mException = e;
		} catch (IOException e) {
			oRet.msProblem = "The file could not be read.";
			oRet.msPossibleSolution = "Check permissions";
			oRet.moResult = "";
			oRet.mException = e;
		}		  	
  	return(oRet);
  }
  
  /**
   * Copies contents from a specified stream to a specified file.
   * 
   * @param aoFromStream
   * @param asTo
   * @return
   */
  public static MvException copyFile(InputStream aoFromStream, String asTo) {
    MvException oResult = new MvException();
    byte[] bBuffer = new byte[64*1024];
    int iReadCount=0;
    long iWriteSize =0;
    
    try {
      FileOutputStream oOutputFileStream = new FileOutputStream(asTo);
    	iReadCount = aoFromStream.read(bBuffer);
    	while (iReadCount != -1) {
    		oOutputFileStream.write(bBuffer, 0, iReadCount);
    		iWriteSize = iWriteSize + iReadCount;
    		iReadCount = aoFromStream.read(bBuffer);
    	}
    	aoFromStream.close();
    	oOutputFileStream.close();
		} catch (FileNotFoundException e) {
			oResult.mbSuccess = false;			
			oResult.mException = e;
			oResult.msProblem = "File not found";
		} catch (IOException e) {
			oResult.mbSuccess = false;
			oResult.mException = e;
			oResult.msProblem = "Unable to write to storage";
		} catch (Exception e) {
			oResult.mbSuccess = false;
			oResult.mException = e;
			oResult.msProblem = "Some other error";
		}
    
    return(oResult);
  }
  
  /**
   * Copies contents of a specified file to another specified 
   * file.
   * @param asFrom
   * @param asTo
   * @return
   */
  public static MvException copyFile(String asFrom, String asTo) {
    long lFileSize, lFilePosition = 0, lCount = 0;
    MvException oResult = new MvException();
  	FileInputStream oInputFileStream = null;
    FileOutputStream oOutputFileStream = null;
    FileChannel oInputFileChannel = null;
    FileChannel oOutputFileChannel = null;
    
    try {
      oInputFileStream = new FileInputStream(asFrom);
      oOutputFileStream = new FileOutputStream(asTo);
      oInputFileChannel  = oInputFileStream.getChannel();
      oOutputFileChannel = oOutputFileStream.getChannel();
      lFileSize = oInputFileChannel.size();
      while (lFilePosition < lFileSize) {
        lCount = lFileSize - lFilePosition > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : lFileSize - lFilePosition;
        lFilePosition += oOutputFileChannel.transferFrom(oInputFileChannel, lFilePosition, lCount);
      }
      oOutputFileChannel.close();
      oOutputFileStream.close();
      oInputFileChannel.close();
      oInputFileStream.close();
      oResult.mbSuccess = true;
      oResult.moResult = asTo;      
		} catch (FileNotFoundException e) {
			oResult.mbSuccess = false;			
			oResult.mException = e;
		} catch (IOException e) {
			oResult.mbSuccess = false;
			oResult.mException = e;			
		} 
    
    return(oResult);
  }
  
}
