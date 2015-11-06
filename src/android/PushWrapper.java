package com.streethawk.push;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.app.Service;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.streethawk.library.push.Push;
import com.streethawk.library.push.PushDataForApplication;
import com.streethawk.library.push.ISHObserver;
import android.util.Log;
import android.os.IBinder;
import android.content.Context;


/**
 * This class echoes a string called from JavaScript.
 */
public class PushWrapper extends Service implements ISHObserver{
	
	private final String TAG = "StreetHawk";
	private final String SUBTAG = "PushWrapper ";

	private static CallbackContext shRawJsonCallback;
	private static CallbackContext mPushDataCallback;
	private static CallbackContext mPushResultCallback;
	private static CallbackContext mNotifyNewPageCallback;

	private final String ACTION 		= "action";
	private final String MSGID			= "msgid";
	private final String TITLE			= "title";
	private final String MESSAGE		= "message";
	private final String DATA			= "data";
	private final String PORTION		= "portion";
	private final String ORIENTATION	= "orientation";
	private final String SPEED			= "speed";
	private final String SOUND			= "sound";
	private final String BADGE			= "badge";
	private final String JSON			= "json";
	private final String RESULT			= "result";
	private final String DISPLAY_WITHOTUT_DAILOG = "displaywihtoutdialog";


	private static PushWrapper mInstance=null;

	public static PushWrapper getInstance(){
		if(null==mInstance){
			mInstance = new PushWrapper();
		}
		return mInstance;
	}

	public void setRawJsonCallback(CallbackContext cb){
		shRawJsonCallback = cb;
	}
	public void setPushDataCallback(CallbackContext cb){
		mPushDataCallback = cb;
	}
	public void setPushResultCallback(CallbackContext cb){
		mPushResultCallback = cb;
	}
	public void setNotifyNewPageCallback(CallbackContext cb){
		mNotifyNewPageCallback = cb;
	}
	public void registerSHObserver(Context context){
		Push.getInstance(context).registerSHObserver(this);
	}


	public void shReceivedRawJSON(String title, String message, String json){
		if(null!=shRawJsonCallback){
			try{
				JSONObject customJson = new JSONObject();
				customJson.put(TITLE,title);
				customJson.put(MESSAGE,message);
				customJson.put(JSON,json);
				PluginResult result = new PluginResult(PluginResult.Status.OK,customJson);
				result.setKeepCallback(true);
				shRawJsonCallback.sendPluginResult(result);
			}catch(JSONException e){
				Log.e(TAG,SUBTAG+"JSONException "+e);
			}
		}
	}

	public void shNotifyAppPage(String pageName){
		Log.e("Anurag","shNotifyAppPage "+mNotifyNewPageCallback);
		if(null!=mNotifyNewPageCallback){
			PluginResult result = new PluginResult(PluginResult.Status.OK,pageName);
			result.setKeepCallback(true);
			mNotifyNewPageCallback.sendPluginResult(result);
		}
	}

	public void onReceivePushData(PushDataForApplication pushData){
		if(null!=mPushDataCallback){
			try{
				JSONObject pushDataJSON = new JSONObject();  	   
				pushDataJSON.put(ACTION,pushData.getAction());
				pushDataJSON.put(MSGID,pushData.getMsgId());
				pushDataJSON.put(TITLE,pushData.getTitle());
				pushDataJSON.put(MESSAGE,pushData.getMessage());
				pushDataJSON.put(DATA,pushData.getData());
				pushDataJSON.put(PORTION,pushData.getPortion());
				pushDataJSON.put(ORIENTATION,pushData.getOrientation());
				pushDataJSON.put(SPEED,pushData.getSpeed());    	    
				pushDataJSON.put(SOUND,pushData.getSound());
				pushDataJSON.put(BADGE,pushData.getBadge());
				pushDataJSON.put(DISPLAY_WITHOTUT_DAILOG,pushData.getDisplayWithoutConfirmation());

				PluginResult result = new PluginResult(PluginResult.Status.OK,pushDataJSON);
				result.setKeepCallback(true);
				mPushDataCallback.sendPluginResult(result);
			}catch(JSONException e){
				Log.e(TAG,SUBTAG+"JSONException "+e);
			}
		}
	}

	public void onReceiveResult(PushDataForApplication resultData,int result){
		if(null!=mPushResultCallback){
			try{
				JSONObject pushDataJSON = new JSONObject();  	   
				pushDataJSON.put(RESULT,result);
				pushDataJSON.put(ACTION,resultData.getAction());
				pushDataJSON.put(MSGID,resultData.getMsgId());
				pushDataJSON.put(TITLE,resultData.getTitle());
				pushDataJSON.put(MESSAGE,resultData.getMessage());
				pushDataJSON.put(DATA,resultData.getData());
				pushDataJSON.put(PORTION,resultData.getPortion());
				pushDataJSON.put(ORIENTATION,resultData.getOrientation());
				pushDataJSON.put(SPEED,resultData.getSpeed());    	    
				pushDataJSON.put(SOUND,resultData.getSound());
				pushDataJSON.put(BADGE,resultData.getBadge());
				pushDataJSON.put(DISPLAY_WITHOTUT_DAILOG,resultData.getDisplayWithoutConfirmation());

				PluginResult presult = new PluginResult(PluginResult.Status.OK,pushDataJSON);
				presult.setKeepCallback(true);
				mPushResultCallback.sendPluginResult(presult);
			}catch(JSONException e){
				Log.e(TAG,SUBTAG+"JSONException "+e);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}



}
