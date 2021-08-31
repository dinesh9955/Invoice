package com.sirapp.FCM;

import android.util.Log;

import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplay;
import com.google.firebase.inappmessaging.FirebaseInAppMessagingDisplayCallbacks;
import com.google.firebase.inappmessaging.model.InAppMessage;

public class MyMessageDisplayImplementation implements
        FirebaseInAppMessagingDisplay {
    @Override
    public void displayMessage(InAppMessage inAppMessage
            , FirebaseInAppMessagingDisplayCallbacks
                                       firebaseInAppMessagingDisplayCallbacks) {
        Log.e("INAPP_MESSAGE","received an inapp message");
    }
}