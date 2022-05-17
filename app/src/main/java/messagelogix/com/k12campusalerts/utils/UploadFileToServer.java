package messagelogix.com.k12campusalerts.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;


public class UploadFileToServer {

	Context mContext;

	HttpURLConnection connection;
	DataOutputStream outputStream;
	FileInputStream fileInputStream;

	// constructor
	public UploadFileToServer(Context context){
		this.mContext = context;
	}

	/**
	 * This function uploads anytype of file to the server using HTTP POST
	 * @param urlServer
	 * @param pathToOurFile
	 * @return Server Response Code
	 */
	public String upload(String urlServer, String pathToOurFile) {
		connection = null;
		outputStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		int bytesRead;
		byte[] buffer;
		int maxBufferSize = 8192;

		StringBuilder serverResponseMessage = new StringBuilder();
		InterruptThread interruptThread = new InterruptThread();
		try
		{
			File audioFile = new File(pathToOurFile);
			fileInputStream = new FileInputStream(audioFile);

			URL url = new URL(urlServer);
			Log.d("UploadFileToServer","urlServer: "+urlServer+"\npathtoourfile: "+pathToOurFile);

			connection = (HttpURLConnection) url.openConnection();
//			connection.setFixedLengthStreamingMode((int) audioFileTemp.length() + 152);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(45000);

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			interruptThread.start();

			//Original
			try {
				outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			} catch(Exception e) {
				Log.d("UploadFile","Exception encountered getting outputstream: "+e);
			}

			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.flush();

			buffer = new byte[maxBufferSize];
			while ((bytesRead = fileInputStream.read(buffer, 0, maxBufferSize)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			fileInputStream.close();

			//If these lines are included, cause file not found exception
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			outputStream.flush();
			outputStream.close();

			try {
				InputStreamReader inStreamReader = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
				String line;
				BufferedReader br = new BufferedReader(inStreamReader);
				while ((line = br.readLine()) != null) {
					serverResponseMessage.append(line);
				}
			} catch(Exception e) {
				Log.d("UploadFile","Exception encountered getting inputstream: "+e);
			}

			interruptThread.interrupt();

			Log.d("UploadFileToServer", "response from server: "+serverResponseMessage.toString());

		} catch(Exception e) {
			Log.d("UploadFileToServer", "uploadFileToServer failed - cause is: "+e.toString());
			interruptThread.interrupt();
		}

		connection.disconnect();
		return serverResponseMessage.toString();
	}

//	public String uploadFileIOS(HashMap<String,String> paramMap, String urlServer, String pathToOurFile) {
//		connection = null;
//		outputStream = null;
//
//		String lineEnd = "\r\n";
//		String twoHyphens = "--";
//		String boundary =  "*****";
//		String extendedBoundary = "---------------------------14737809831466499882746641449";
//
//		int bytesRead;
//		byte[] buffer;
//		int maxBufferSize = 8192;
//
//		StringBuilder serverResponseMessage = new StringBuilder();
//		InterruptThread interruptThread = new InterruptThread();
//		try
//		{
//			File audioFile = new File(pathToOurFile);
//			fileInputStream = new FileInputStream(audioFile);
//
//			URL url = new URL(urlServer);
//			Log.d("UploadFileToServer","urlServer: "+urlServer+"\npathtoourfile: "+pathToOurFile);
//
//			connection = (HttpURLConnection) url.openConnection();
////			connection.setFixedLengthStreamingMode((int) audioFileTemp.length() + 152);
//			connection.setConnectTimeout(5000);
//			connection.setReadTimeout(45000);
//
//			// Allow Inputs & Outputs
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			connection.setUseCaches(false);
//
//			// Enable POST method
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
//			//Original
//			outputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
//
//			Iterator it = paramMap.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry pair = (Map.Entry)it.next();
//				outputStream.writeBytes("--"+extendedBoundary+lineEnd);
//				outputStream.writeBytes("Content-Disposition: form-data; name=\""+pair.getKey()+"\""+lineEnd+lineEnd);
//				outputStream.writeBytes(pair.getValue()+lineEnd);
//				outputStream.flush();
//
//				//System.out.println(pair.getKey() + " = " + pair.getValue());
//				it.remove(); // avoids a ConcurrentModificationException
//			}
//
//			outputStream.writeBytes(lineEnd+"--"+extendedBoundary+lineEnd);
//			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""+pathToOurFile+"\""+lineEnd);
//			outputStream.writeBytes("Content-Type: audio/basic"+lineEnd+lineEnd);
//			outputStream.flush();
//
////			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////				Files.copy(audioFile.toPath(), outputStream);
////			}
//
//			buffer = new byte[maxBufferSize];
//			while ((bytesRead = fileInputStream.read(buffer, 0, maxBufferSize)) > 0) {
//				outputStream.write(buffer, 0, bytesRead);
//			}
//			fileInputStream.close();
//			outputStream.flush();
//			outputStream.writeBytes(lineEnd+"--"+extendedBoundary+"--"+lineEnd);
//			outputStream.flush();
//			outputStream.close();
//
//			interruptThread.start();
//
//			InputStreamReader inStreamReader = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
//
//			interruptThread.interrupt();
//
//			String line;
//			BufferedReader br = new BufferedReader(inStreamReader);
//			while ((line = br.readLine()) != null) {
//				serverResponseMessage.append(line);
//			}
//
//			Log.d("UploadFileToServer", "response from server: "+serverResponseMessage.toString());
//
//		} catch(Exception e) {
//			Log.d("UploadFileToServer", "uploadFileToServer failed - cause is: "+e.toString());
//			interruptThread.interrupt();
//		}
//
//		connection.disconnect();
//		return serverResponseMessage.toString();
//	}

	public class InterruptThread extends Thread implements Runnable{

		public void run() {
			Log.d("UploadFileToServer","interrupt thread was initiated");
			try {
				Thread thread = Thread.currentThread();
				Log.d("UploadFileToServer","thread name in method"+thread.getName());

				Thread.sleep(45000);

				connection.disconnect();
				Log.d("UploadFileToServer","Timer thread closed connection held by parent, exiting");
			} catch (Exception e) {
				Log.d("UploadFileToServer","Timer thread encountered an exception and could not close the app's connection to the server: "+e);
			}
		}
	}

}
