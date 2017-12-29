package com.ciplafoundation.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.ciplafoundation.interfaces.MultipartPostCallback;
import com.ciplafoundation.model.FileDetails;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class MultipartPostRequest extends AsyncTask<Void, Void, String> {
	
	private ProgressDialog progressdialog;
	private Context mContext;
	private String url;
	private Map<String, String> map;
	private ArrayList<FileDetails> fileList;
	private String multipartArrayTag;
	public MultipartPostCallback mListener;

	public MultipartPostRequest(Context mContext, String url, Map<String, String> map, ArrayList<FileDetails> fileList,
			String multipartArrayTag) {
		this.mContext = mContext;
		this.url = url;
		this.map = map;
		this.fileList = fileList;
		this.multipartArrayTag = multipartArrayTag;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(mContext);
		progressdialog.setMessage("Please wait...");
		progressdialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {

		String response = postMultipartFile();
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressdialog.dismiss();
		mListener.onMultipartPost(result);
	}

	private String postMultipartFile() {

		/*Log.v("postMultipartFile", "" + fileList.size());
		Log.v("postMultipartFile", "" + map);*/

		String resultMessage = "";

		HttpClient httpclient = new DefaultHttpClient();

		// Sets the request agent as mobile Android
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
		HttpPost httppost = new HttpPost(url);

		try {
			AndroidMultiPartEntity entity = new AndroidMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
					new AndroidMultiPartEntity.ProgressListener() {

						@Override
						public void transferred(long num) {

						}
					});

			if (fileList.size() > 0) {
				int i = 0,j=0;
				for (FileDetails value : fileList) {

					if(value.getSelectedFilePath() != null)
					{
						//System.out.println("" + value.getSelectedFilePath());
						FileBody docFiles = new FileBody(new File(value.getSelectedFilePath()));
						entity.addPart(multipartArrayTag + "[" + j + "]", docFiles);
                       j++;
					}
					i++;
				}
			  }
				for (String key : map.keySet()) {
					entity.addPart(key, new StringBody(map.get(key)));
				}

			
			// totalSize = entity.getContentLength();
			httppost.setEntity(entity);			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {

				resultMessage = EntityUtils.toString(r_entity);
			} else {
				resultMessage = "Error occurred! Http Status Code: " + statusCode;
			}

		} catch (ClientProtocolException e) {

			e.printStackTrace();
			resultMessage = e.toString();
		} catch (IOException e) {

			e.printStackTrace();
			resultMessage = e.toString();
		}

		//Log.v("Response", "" + resultMessage);

		return resultMessage;

	}
	
	public static String getMimeType(String url) {
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
}