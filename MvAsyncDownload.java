/*
 * Android Without Stupid Java Library
 * Created by V. Subhash - http://www.MoralVolcano.com
 * Released as Public Domain Software in 2014
 */

package com.vsubhash.droid.androidwithoutstupid;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.webkit.URLUtil;

/**
 * This class can be used to asynchronously download files from the Internet. To
 * download a file, call the constructor with the URL of the file and the full
 * pathname to which the file needs to be saved to. The constructor automatically 
 * starts the download. As this class extends {@link android.os.AsyncTask}, 
 * override its methods to handle download events.
<blockquote><code><pre>
MvAsyncDownload dl = 
   new MvAsyncDownload(
      "http://www.example.com/rss.xml", 
      "/mnt/sdcard/rss.xml") { 
  {@Override}Override 
  protected void onPostExecute(MvException result) { 
    if (result.mbSuccess) { 
      MvMessages.showMessage(this, "Downloaded"); 
    } else { 
      MvMessages.showMessage(this, "Failed " + result.msProblem );
    } 
    super.onPostExecute(result); 
  } 
}; 
</pre></code></blockquote>
 * @see android.os.AsyncTask
 * @author V. Subhash
 * @version 2014.03.22
 * 
 */
public class MvAsyncDownload extends AsyncTask<String, Integer, MvException> {
	public String mRemoteFileURL;
	public String mLocalFilePath;
	HttpURLConnection mURLConnection;
	public String msMimeType;
	public int miFileSize;
	int miBytesRead = 0;	
	
	BufferedInputStream in = null;
	FileOutputStream of = null;
	
	private MvAsyncDownload() {
		super();
	}	
	
	/**
	 * Asynchronously downloads from specified URL and save the download to
	 * specified file.
	 * 
	 * @param asURL
	 *          download URL
	 * @param asFile
	 *          pathname of local file to which the download needs to be saved
	 */
	public MvAsyncDownload(String asURL, String asFile) {
	  this();
		mRemoteFileURL = asURL;
		mLocalFilePath = asFile;
		this.execute(mRemoteFileURL, mLocalFilePath);
	}
	
	/**
	 * Asynchronously downloads from specified URL, guesses the filename if
	 * specified, and saves the download to specfied directory.
	 * 
	 * @param asURL
	 *          download URL
	 * @param asPath
	 *          if abGuessFileName is true, asPath represents the pathname of the
	 *          directory to which the download needs to be saved; if
	 *          abGuessFileName is false, asPath represents the file name to which
	 *          the download needs to be saved
	 * @param abGuessFileName
	 *          whether the filename needs to be guessed
	 * @param asMimeType
	 *          mimetype of the download (used only for guessing the filename)
	 */
	public MvAsyncDownload(String asURL, String asPath, boolean abGuessFileName, String asMimeType) {
	  this();
	  String sDownloadedFile, sDownloadPath;
	  if (abGuessFileName) {
	  	sDownloadedFile = URLUtil.guessFileName(asURL, null, asMimeType);
		  sDownloadPath = asPath + "/" + sDownloadedFile;	  	
	  } else {
	  	sDownloadPath = asPath;	  	
	  }
		mRemoteFileURL = asURL;
		mLocalFilePath = sDownloadPath;
		this.execute(mRemoteFileURL, mLocalFilePath);	  
	}	
	  
	@Override
	protected void onCancelled() {
		try {
			in.close();
			of.flush();
			of.close();
		} catch (IOException e) {
			MvMessages.logMessage("IO Exception closing streams.");
		}
		
		super.onCancelled();
	}
	
	@Override
	protected MvException doInBackground(String... asLinks) {
		URL oURL;
		MvException oRet = new MvException();
		byte[] buf = new byte[1024];
		int n = 0, iTries = 0;
		
		if (asLinks.length == 2) {
			try {
				oURL = new URL(asLinks[0]);
				mURLConnection = (HttpURLConnection) oURL.openConnection();
				mURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (KHTML, like Gecko) Firefox");
				
				mURLConnection.setConnectTimeout(4000);	
				mURLConnection.connect();	
				miFileSize = mURLConnection.getContentLength();
				
				MvMessages.logMessage(miFileSize + " to be downloaded;");
				
				for (iTries = 1; iTries < 6; iTries++) {
					if (iTries > 1) {						
						mURLConnection.disconnect();
						mURLConnection = (HttpURLConnection) oURL.openConnection();
						mURLConnection.setRequestProperty("Range", "bytes=" + miBytesRead + "-");
						mURLConnection.connect();
					} else {
						of = new FileOutputStream(asLinks[1]);
					}
					
					try {						
						in = new BufferedInputStream(mURLConnection.getInputStream());			
						do {
							if (isCancelled()) {
								oRet.mbSuccess = false;
								oRet.msProblem = "Download cancelled.";
								oRet.msPossibleSolution = "None required";
								break;
							}
							
							n = in.read(buf, 0, 1024);
							miBytesRead = miBytesRead + n;
							if (n != -1) {
								of.write(buf, 0, n);
								if (miFileSize > 0) {
								  publishProgress(miBytesRead);
								}
							} else {
								of.flush();
								in.close();
								of.close();
								msMimeType = mURLConnection.getContentType();
								oRet.mbSuccess = true;
							}
						} while(n != -1);					
						oRet.moResult = asLinks[1];
						break;
				  } catch (IOException e) {
					  oRet.mbSuccess = false;
						oRet.mException = e;
						oRet.msProblem = "Download failed. Tries: " + iTries;
						oRet.msPossibleSolution = "A better download URL or network conditions.";
						MvMessages.logMessage(oRet.msProblem);
						e.printStackTrace();					
					}
				}
			} catch (MalformedURLException e) {
				oRet.mbSuccess = false;
				oRet.mException = e;
				oRet.msProblem = "This is an invalid URL (link).";
				oRet.msPossibleSolution = "A valid URL (link) is required.";
				e.printStackTrace();
			} catch (UnknownHostException e) {
				oRet.mbSuccess = false;
				oRet.mException = e;
				oRet.msProblem = "There is no Internet connection or the website does not exist.";
				oRet.msPossibleSolution = "An working Internet connection or a valid website address is required.";
				MvMessages.logMessage("Have you added INTERNET permission?");
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				oRet.mbSuccess = false;
				oRet.mException = e;
				oRet.msProblem = "The link (URL) is broken or missing.";
				oRet.msPossibleSolution = "An existing link (URL) is required.";
				e.printStackTrace();
			} catch (IOException e) {
				oRet.mbSuccess = false;
				oRet.mException = e;
				oRet.msProblem = "There is no network connection.";
				oRet.msPossibleSolution = "A good connection to the network is required.";
				e.printStackTrace();
			}
		}		
		return oRet;
  }
}
