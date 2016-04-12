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
import com.streethawk.library.push.InteractivePush;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;

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
    private static CallbackContext mNonSHPushPayloadCallback;
    
    private static  ArrayList<InteractivePush> mAppPairs = null;
    
	private final String ACTION 		            = "action";
	private final String MSGID			            = "msgid";
	private final String TITLE			            = "title";
	private final String MESSAGE		            = "message";
	private final String DATA			            = "data";
	private final String PORTION		            = "portion";
	private final String ORIENTATION	            = "orientation";
	private final String SPEED			            = "speed";
	private final String SOUND			            = "sound";
	private final String BADGE			            = "badge";
	private final String JSON			            = "json";
	private final String RESULT			            = "result";
	private final String DISPLAY_WITHOTUT_DAILOG    = "displaywihtoutdialog";
    
    private final String CONTENTAVAILABLE			= "contentavailable";
    private final String CATEGORY			        = "category";
    
    private final String B1TITLE                    = "b1title";
    private final String B2TITLE                    = "b2title";
    private final String B3TITLE                    = "b3title";
    
    private final String B1ICON                     = "b1icon";
    private final String B2ICON                     = "b2icon";
    private final String B3ICON                     = "b3icon";
    
    
    
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
	public void registerSHObserver(Activity activity){
		if(null==activity)
            return;
        Context context = activity.getApplicationContext();    
        Intent intent = new Intent(activity,PushWrapper.class);
        context.startService(intent);
        Push.getInstance(context).registerSHObserver(this);
	}
    
    public void registerNonSHPushPayloadObserver(CallbackContext cb){
        mNonSHPushPayloadCallback=cb;
    }
    
    public void addInteractivePushButtonPair(String t1,String t2, String name){
        addInteractivePushButtonPairWithIcon(t1,null,t2,null,name);
    }
    
    public void addInteractivePushButtonPairWithIcon(String b1Title, String b1Icon,
                String b2Title, String b2Icon,String pairTitle){
    if(null==mAppPairs)
        mAppPairs = new ArrayList<InteractivePush>();
    mAppPairs.add(new InteractivePush(b1Title, Push.getInstance(this).getIcon(b1Icon),
                 b2Title, Push.getInstance(this).getIcon(b2Icon),pairTitle));                                             
   }
   
   public void setInteractivePushBtnPair(){
       if(null==mAppPairs){
           Log.e(TAG,SUBTAG+" mAppPairs is null. returning...");
       }
       Push.getInstance(this).setInteractivePushBtnPairs(mAppPairs);
   }
   
   
    @Override
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

    @Override
	public void shNotifyAppPage(String pageName){
		if(null!=mNotifyNewPageCallback){
			PluginResult result = new PluginResult(PluginResult.Status.OK,pageName);
			result.setKeepCallback(true);
			mNotifyNewPageCallback.sendPluginResult(result);
		}
	}

    @Override
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
                
                //Added in 1.8.0
                pushDataJSON.put(CONTENTAVAILABLE,pushData.getContentAvailable());
                pushDataJSON.put(CATEGORY,pushData.getCategory());
                pushDataJSON.put(B1TITLE,pushData.getBtn1Title());
                pushDataJSON.put(B2TITLE,pushData.getBtn2Title());
                pushDataJSON.put(B3TITLE,pushData.getBtn3Title());
                pushDataJSON.put(B1ICON,pushData.getBtn1Icon());
                pushDataJSON.put(B2ICON,pushData.getBtn2Icon());
                pushDataJSON.put(B3ICON,pushData.getBtn3Icon());

				PluginResult result = new PluginResult(PluginResult.Status.OK,pushDataJSON);
				result.setKeepCallback(true);
				mPushDataCallback.sendPluginResult(result);
			}catch(JSONException e){
				Log.e(TAG,SUBTAG+"JSONException "+e);
			}
		}
	}

    @Override
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
      
                //Added in 1.8.0    
                pushDataJSON.put(CONTENTAVAILABLE,resultData.getContentAvailable());
                pushDataJSON.put(CATEGORY,resultData.getCategory());
                pushDataJSON.put(B1TITLE,resultData.getBtn1Title());
                pushDataJSON.put(B2TITLE,resultData.getBtn2Title());
                pushDataJSON.put(B3TITLE,resultData.getBtn3Title());          
                pushDataJSON.put(B1ICON,resultData.getBtn1Icon());
                pushDataJSON.put(B2ICON,resultData.getBtn2Icon());
                pushDataJSON.put(B3ICON,resultData.getBtn3Icon());

				PluginResult presult = new PluginResult(PluginResult.Status.OK,pushDataJSON);
				presult.setKeepCallback(true);
				mPushResultCallback.sendPluginResult(presult);
			}catch(JSONException e){
				Log.e(TAG,SUBTAG+"JSONException "+e);
			}
		}
	}
    
    @Override
    public void onReceiveNonSHPushPayload(Bundle pushPayload) {
        if(null!=mNonSHPushPayloadCallback){
            /*
            TODO send pushpayload 
            */
        }
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}



}
