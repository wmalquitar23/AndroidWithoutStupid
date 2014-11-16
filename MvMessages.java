/*
 * Android Without Stupid Java Library
 * Created by V. Subhash - http://www.MoralVolcano.com
 * Released as Public Domain Software in 2014
 */

package com.vsubhash.droid.androidwithoutstupid;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;


/**
 * This class provides methods for easily displaying toast and notification
 * messages. To use the methods, create an instance of this class in your
 * activity and call the methods of the instance.
 * 
 * <blockquote><code><pre>
MvMessages oMessages = new MvMessages(this);
oMessages.showMessage("Hello, world!");
oMessages.showNotification("New Message",
                           "5 unread e-mail messages ",
                           "Check mail");
 </pre></code></blockquote>
 * 
 * 
 * @author V. Subhash
 * @version 2014.05.31
 * 
 */
public class MvMessages {
	
	public static String ERR_TAG = "MVUTILS.LOGD";
	public static int iMVM_NOTIFICATION_ID = 232343;
	
	Activity mCallingActivity = null;
	/**
	 * Notification manager for this activity.
	 */
	public NotificationManager mNotificationManager = null;
	/**
	 * Resource ID of the notification ID
	 */
	public int mNotificationIcon = 0;
	/**
	 * Notification ID 
	 */
	public int mNotificationID = iMVM_NOTIFICATION_ID;
	
	
	/**
	 * Constructs an instance of this class for use with specified activity.
	 * 
	 * @param activity
	 *          with which this instance needs to work with
	 */
	public MvMessages(Activity aActivity) {
		if (aActivity != null) {
		  mCallingActivity = aActivity;
		  mNotificationManager = (NotificationManager) mCallingActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		  if (mNotificationManager != null) {
		  	
		  } 
		}
	//	mNotificationIcon = R.drawable.icon_notification_bulb;
	}
	
	/**
	 * Constructs an instance of this class for use with specified activity and
	 * notification icon.
	 * 
	 * @param aActivity
	 *          activity with which this instance needs to work with
	 * @param aNotificationIcon
	 *          {@link Drawable} icon resource that needs to be used with the
	 *          notifications.
	 */
	public MvMessages(Activity aActivity, int aNotificationIcon) {
    this(aActivity);
		mNotificationIcon = aNotificationIcon;		
	}

	
	/**
	 * Write specified message to log. This is a static method.
	 *
	 * @param asMessage the message
	 */
	public static void logMessage(String asMessage) {
		Log.d(ERR_TAG, "\n" + asMessage);		
	}

	/**
	 * Display a toast message. This is an instance method.
	 * @param asMessage the message
	 */
	public void showMessage(String asMessage) {	  
		Context aContext;	  
	  if (mCallingActivity != null) {
	    aContext = mCallingActivity.getApplicationContext();
	    if (aContext != null) {	
		    Toast.makeText(aContext, asMessage, Toast.LENGTH_LONG).show();
	    }	    
	  }	  
	}
	
	/**
	 * Displays specified message from specified activity. [This is is a static
	 * method.]
	 * 
	 * @param aoCallingActivity
	 *          activity from which the message should pop up
	 * @param asMessage
	 *          text that needs to displayed as the message
	 */
	public static void showMessage(Activity aoCallingActivity, String asMessage) {	  
		Context aContext;	  
    aContext = aoCallingActivity.getApplicationContext();
    if (aContext != null) {	
      Toast.makeText(aContext, asMessage, Toast.LENGTH_LONG).show();
	  }	  
	}
	
	/**
	 * Display a notification. When the end-user selects the notification, the
	 * activity that launched the notification will be launched.
	 * 
	 * @param asTicker
	 *          text that flashes by when the notification first appears by
	 * @param asTitle
	 *          text appearing as the title of the notification
	 * @param asMessage
	 *          the message
	 * @param aiIconID
	 *          resource ID of the notification icon
	 * @return created notification instance
	 */	
	public Notification showNotification(String asTicker, String asTitle, String asMessage, int aiIconID) {
		Intent oNotificationIntent = 
				new Intent(mCallingActivity.getApplicationContext(), 
									 mCallingActivity.getClass());
		
		return(showNotification(asTicker, asTitle, asMessage, aiIconID, oNotificationIntent));	
	}
	
	/**
	 * Shows a notification with specified settings.
	 * 
	 * @param asTicker
	 *          ticker text used by the notification
	 * @param asTitle
	 *          title of the notification
	 * @param asMessage
	 *          message of the notification
	 * @param aiIconID
	 *          icon used to display the notification
	 * @param aoIntentToLaunch
	 *          Intent that needs to be launched when the notification is selected
	 * @return notification instance that is created
	 */
	public Notification showNotification(String asTicker, String asTitle, String asMessage, int aiIconID, Intent aoIntentToLaunch) {
		 return(this.showNotification(asTicker, asTitle, asMessage, aiIconID, iMVM_NOTIFICATION_ID, aoIntentToLaunch));
	}
	
	
	/**
	 * Display a notification and sets the activity that needs to be launched when
	 * the user selects the notification.
	 * 
	 * @param asTicker
	 *          text that flashes by when the notification first appears by
	 * @param asTitle
	 *          text appearing as the title of the notification
	 * @param asMessage
	 *          the message
	 * @param aiIconID
	 *          resource ID of the notification icon
	 * @param aiNotificationID
	 *          request code of the notification
	 * @param aoIntentToLaunch
	 *          Intent that needs to be launched when the notification is selected
	 * @return created notification instance
	 */
	public Notification showNotification(
			String asTicker, String asTitle, String asMessage, 
			int aiIconID, int aiNotificationID, Intent aoIntentToLaunch) {
		return(this.showNotification(asTicker, asTitle, asMessage, aiIconID, aiNotificationID, aoIntentToLaunch, false));
	}
	
	/**
	 * Display a notification and sets the activity that needs to be launched when
	 * the user selects the notification.
	 * 
	 * @param asTicker
	 *          text that flashes by when the notification first appears by
	 * @param asTitle
	 *          text appearing as the title of the notification
	 * @param asMessage
	 *          the message
	 * @param aiIconID
	 *          resource ID of the notification icon
	 * @param aiNotificationID
	 *          request code of the notification
	 * @param aoIntentToLaunch
	 *          Intent that needs to be launched when the notification is selected
	 * @param bLaunchIntentBroadcast
	 *          Whether the notification should launch an activity or broadcast
	 *          Intent
	 * @return created notification instance
	 */
	public Notification showNotification(
			String asTicker, String asTitle, String asMessage, 
			int aiIconID, int aiNotificationID, Intent aoIntentToLaunch,
			boolean bLaunchIntentBroadcast) {
		
		mNotificationIcon = aiIconID;
		mNotificationID = aiNotificationID;
		PendingIntent oNotificationPendingIntent;
		
		if (bLaunchIntentBroadcast) {
			oNotificationPendingIntent = 
					PendingIntent.getBroadcast(
							mCallingActivity.getApplicationContext(),
							mNotificationID, 
							aoIntentToLaunch, 
							0);
			MvMessages.logMessage("Set broadcast");
		} else {
			aoIntentToLaunch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			oNotificationPendingIntent = 
					PendingIntent.getActivity(
							mCallingActivity, 
							mNotificationID, 
							aoIntentToLaunch, 
							PendingIntent.FLAG_UPDATE_CURRENT);
			MvMessages.logMessage("Set activity launch");
		}
		
		Notification oNotification = 
				new Notification(
						aiIconID, 
						asTicker, 
						System.currentTimeMillis());
		
		oNotification.setLatestEventInfo(
				mCallingActivity, 
				asTitle, 
				asMessage, 
				oNotificationPendingIntent);
		
		mNotificationManager.notify(aiNotificationID, oNotification);
		
		return(oNotification);
	}
	
	/**
	 * Sets an intent that needs to be launched when the user interacts with a
	 * notification.
	 * 
	 * @param aoNotification
	 *          notification that needs to be modified
	 * @param aoNotificationIntent
	 *          Intent that needs to be launched
	 */
	public void setNotificationContentLaunchIntent(Notification aoNotification, Intent aoNotificationIntent) {
		aoNotification.contentIntent = 
				PendingIntent.getActivity(
						mCallingActivity, 
						0, 
						aoNotificationIntent, 
						PendingIntent.FLAG_UPDATE_CURRENT);
		mNotificationManager.notify(mNotificationID, aoNotification);
	}
	
	/**
	 * 
	 * Updates a notification with specified ticker text.
	 * 
	 * @param aoNotification
	 *          notification that needs to be updated
	 * @param asTickerText
	 *          text that needs to be dsiplayed
	 */
	public void updateNotification(Notification aoNotification, String asTickerText) {
	  aoNotification.tickerText = asTickerText;
	  aoNotification.when = System.currentTimeMillis()+300;
	  mNotificationManager.notify(mNotificationID, aoNotification);
	}
	
	/**
	 * Sets specified text for title and content for specified notification.
	 * 
	 * @param aoNotification Notification that needs to be updated.
	 * @param asTitleText New title text.
	 * @param asContentText New content text.
	 */
	public void updateNotification(
			Notification aoNotification, 
			String asTitleText,
			String asContentText) {
	  aoNotification.setLatestEventInfo(
	  		mCallingActivity.getApplicationContext(), 
	  		asTitleText,
	  		asContentText, 
	  		aoNotification.contentIntent);
	  aoNotification.when = System.currentTimeMillis();
	  mNotificationManager.notify(mNotificationID, aoNotification);		
	}	
	
	/**
	 * Sets specified text for ticker, title, and content text of specified
	 * notification.
	 * 
	 * @param aoNotification
	 *          Notification that needs to be updated.
	 * @param asTickerText
	 *          New ticker text.
	 * @param asTitleText
	 *          New title text.
	 * @param asContentText
	 *          New content text.
	 */
	public void updateNotification(
			Notification aoNotification, 
			String asTickerText,
			String asTitleText,
			String asContentText) {
	  aoNotification.tickerText = asTickerText;
	  aoNotification.setLatestEventInfo(
	  		mCallingActivity.getApplicationContext(), 
	  		asTitleText,
	  		asContentText, 
	  		aoNotification.contentIntent);
	  aoNotification.when = System.currentTimeMillis();
	  mNotificationManager.notify(mNotificationID, aoNotification);		
	}
	
	
	
	/**
	 * Returns a menu dialog containing specified options, and displays
	 * the menu if specified.
	 * 
	 * @param asTitle
	 *          title of the dialog
	 * @param asaOptions
	 *          options to be listed in the menu
	 * @param aoListener
	 *          {@link DialogInterface.OnClickListener} listener that handles user
	 *          interactions with the dialog
	 * @param abAutoShow whether the menu needs to be automatically displayed after it is
	 *          created
	 * @return the new menu
	 */
	public AlertDialog.Builder showOptionsMenu(
			String asTitle, String[] asaOptions, 
			DialogInterface.OnClickListener aoListener, boolean abAutoShow) {
		AlertDialog.Builder alert;

		alert = new AlertDialog.Builder(mCallingActivity);
		alert.setInverseBackgroundForced(true);
		alert.setTitle(asTitle);

		if (asaOptions !=null) {			
			if (asaOptions.length > 0) {
				alert.setItems(asaOptions, aoListener);

				if (abAutoShow) {
					alert.show();
				}
				
			}
		}
		return(alert);		
	}
	
	
	/**
	 * Shows a dialog with a menu containing specified options.
	 * 
	 * @param asTitle
	 *          title of the dialog
	 * @param aoOptionsList
	 *          options to be listed in the menu
	 * @param aoListener
	 *          {@link DialogInterface.OnClickListener} listener that handles user
	 *          interactions with the dialog
	 * @param abAutoShow
	 *          whether the menu needs to be automatically displayed after it is
	 *          created
	 * @return the new menu
	 */
	public AlertDialog.Builder showOptionsMenu(String asTitle, List<String> aoOptionsList, 
			DialogInterface.OnClickListener aoListener, boolean abAutoShow) {
		String[] arrsOptions = new String[] {};
		
		if (aoOptionsList != null) {
			if (aoOptionsList.size() > 0) {
				arrsOptions = aoOptionsList.toArray(new String[aoOptionsList.size()]);				
			}
		}
		return(showOptionsMenu(asTitle, arrsOptions, aoListener, abAutoShow));
	}
	

	/**
	 * Displays a menu of options.
	 * 
	 * @param asTitle
	 *          title of the menu
	 * @param asaOptions
	 *          array containing the text content of the options
	 * @param aoListener
	 *          "onclick" listener for the menu options
	 */
	public void showOptionsMenu(String asTitle, String[] asaOptions, DialogInterface.OnClickListener aoListener) {
		showOptionsMenu(asTitle, asaOptions, aoListener, true);
	}

	/**
	 * Displays a menu of options.
	 * 
	 * @param asTitle
	 *          title of the menu
	 * @param aoOptionsList
	 *          list containing the text content of the options
	 * @param aoListener
	 *          "onclick" listener for the menu options
	 */
	public void showOptionsMenu(String asTitle, List<String> aoOptionsList, 
			DialogInterface.OnClickListener aoListener) {
		showOptionsMenu(asTitle, aoOptionsList, aoListener, true);
	}	
	
	/**
	 * Prompts for a yes-or-no choice for specified question.
	 * 
	 * @param asTitle
	 *          text displayed in the title of the prompt
	 * @param asMessage
	 *          text displayed as the message of the prompt
	 * @return a {@link MvMessages.DialogInterfaceCaller} instance handling user
	 *         interactivity of the prompt
	 */
	public DialogInterfaceCaller showPrompt(String asTitle, String asMessage) {
	  DialogInterfaceCaller oDialog = new DialogInterfaceCaller(mCallingActivity, asTitle, asMessage);
		return(oDialog);
	}
	
	/**
	 * This class helps in using the {@link MvMessages#showPrompt(String, String)}
	 * method.
	 * 
	 * @author V. Subhash
	 * @version 1.0
	 */
	public class DialogInterfaceCaller  {
		AlertDialog.Builder oAlertBuilder;	
		public DialogInterface.OnClickListener onOkay = null;
		public DialogInterface.OnClickListener onNotOkay = null;
			
		
		/**
		 * Constructs an instance of this class for use with specified activity.
		 * 
		 * @param aoCallingActivity activity with which this instance needs to be used
		 * @param asTitle title of the prompt
		 * @param asMessage message of the prompt
		 */
		public DialogInterfaceCaller(Activity aoCallingActivity, String asTitle, String asMessage) {
			oAlertBuilder = new AlertDialog.Builder(aoCallingActivity);
		  oAlertBuilder
		        .setTitle(asTitle)
		        .setMessage(asMessage);
		}
		
		/**
		 * Sets up the event handlers and displays the prompt.
		 */
		public void show() {
			if (onNotOkay == null)  {
				oAlertBuilder.setNegativeButton("No",  null);
			} else {
				oAlertBuilder.setNegativeButton("No",  onNotOkay);
			}
			if (onOkay != null)  {
				oAlertBuilder
					.setPositiveButton("Yes", onOkay)
					.show();
			}
		}

	}

	
	/**
	 * Creates a menu containing specified options and icons, and displays it if
	 * specified.
	 * 
	 * @param asMenuTitle
	 *          title of the menu
	 * @param aoMenuOptionsIconList
	 *          list containing menu option {@link Drawable icons}
	 * @param asMenuOptionsTextList
	 *          list containing menu option text
	 * @param aiRowLayoutId
	 *          layout used to display the menu options
	 * @param aiOptionIconId
	 *          ImageView in the layout that needs to be used by the menu options
	 * @param aiOptionTextId
	 *          TextView in the layout that needs to be used by the menu options
	 * @param aoMenuOptionClickListener
	 *          "onclick" listener for the menu options
	 * @param abAutoShow
	 *          whether the menu should be automatically displayed after it is
	 *          constructed
	 * @return the new menu
	 */
	public AlertDialog.Builder showOptionsMenuWithIcons(
			String asMenuTitle,
			final ArrayList<Drawable> aoMenuOptionsIconList, 
			final ArrayList<String> asMenuOptionsTextList, 
			final int aiRowLayoutId,
			final int aiOptionIconId, final int aiOptionTextId,
			DialogInterface.OnClickListener aoMenuOptionClickListener,
			boolean abAutoShow) {
		
		AlertDialog.Builder oMenu = new AlertDialog.Builder(mCallingActivity);
		
		class MvMenuOptionContents {
	    ImageView icon;
	    TextView title;
	  }
		
		ListAdapter oListAdaptor = new ArrayAdapter<String>(
				mCallingActivity.getApplicationContext(), 
				aiRowLayoutId, 
				asMenuOptionsTextList) {
			
			@Override
			public View getView(int aiIndex, View aoOptionView, ViewGroup aoParentViewGroup) {
				MvMenuOptionContents oOptionContents;						
				
				if (aoOptionView == null) {
					aoOptionView = LayoutInflater.from(mCallingActivity.getApplicationContext()).inflate(aiRowLayoutId, null);
					oOptionContents = new MvMenuOptionContents();
					oOptionContents.icon = (ImageView) aoOptionView.findViewById(aiOptionIconId);
					oOptionContents.title = (TextView) aoOptionView.findViewById(aiOptionTextId);
					aoOptionView.setTag(oOptionContents);
				  //return super.getView(position, convertView, parent);
				} else {
					oOptionContents = (MvMenuOptionContents) aoOptionView.getTag();							
				}
				
				oOptionContents.icon.setImageDrawable(aoMenuOptionsIconList.get(aiIndex));
				oOptionContents.title.setText(asMenuOptionsTextList.get(aiIndex));
				
				return(aoOptionView); 
				// return super.getView(aiIndex, aoOptionView, aoParentViewGroup);
			}
			
		};
		
		oMenu.setTitle(asMenuTitle);
		oMenu.setAdapter(oListAdaptor, aoMenuOptionClickListener);
		if (abAutoShow) {
			oMenu.show();
		}
		return(oMenu);	
	}
	
	
}
