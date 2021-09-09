package com.pniew.mentalahasz.utils;

import android.content.Context;
import android.content.Intent;

import com.pniew.mentalahasz.picture_showeditaddtest.LearnShowAddActivity;
import com.pniew.mentalahasz.thelistofthings.addeditthings.AddEditThingActivity;

public abstract class CallsStringsIntents {
    public static final String I_WANT_TO = "I want to";
    public static final int ADD_NEW_PICTURE = 1;
    public static final int EDIT_A_PICTURE = 2;
    public static final int ADD_NEW_THING = 3;
    public static final int EDIT_A_THING = 4;
    public static final int LEARN = 5;
    public static final int TEST_ME = 6;

    public static final String PICTURE_ID = "Picture id";
    public static final String PICTURE_PATH = "Picture path";
    public static final String PICTURES_IDS_ARRAY = "Pictures array";
    public static final String PICTURE_CHANGED = "Picture changed";
    public static final String PARENT_PERIOD_ID = "Parent period";
    public static final String ART_PERIOD_STRING = "Art Period";
    public static final String MOVEMENT_STRING = "Movement";
    public static final String TYPE_STRING = "Type of Artwork";
    public static final String TOAST_STRING = "Toast";

    public static final String LOAD = "Load";
    public static final String CHILD_OF = "Child of";
    public static final String CALLED_BY = "Called by";
    public static final String NAME = "Name";
    public static final String TRIVIA = "Trivia";

    public static final int NULL = 0;
    public static final int ART_PERIOD = 1;
    public static final int MOVEMENT = 2;
    public static final int TYPE = 3;

    public static final int PICTURES = 4; //for example "to load"

    public static final int MAIN_MENU = 10;


    public static final String THING_ID = "Thing id";
    public static final String THING_NAME = "Thing name";
    public static final String THING_TYPE = "Thing type";
    public static final String THING_DATING = "Thing dating";
    public static final String THING_LOCATION = "Thing location";
    public static final String THING_PARENT_PERIOD_ID = "Thing parent period id";


    public static void startAddPictureActivity(Context context, int calledBy, int childOf, int parentPeriodId) {
        Intent intent = new Intent(context, LearnShowAddActivity.class);
        intent.putExtra(I_WANT_TO, ADD_NEW_PICTURE);
        intent.putExtra(CALLED_BY, calledBy);
        intent.putExtra(CHILD_OF, childOf);
        intent.putExtra(PARENT_PERIOD_ID, parentPeriodId);
        context.startActivity(intent);
    }

    public static void startEditPictureActivity(Context context, int pictureId) {
        Intent intent = new Intent(context, LearnShowAddActivity.class);
        intent.putExtra(I_WANT_TO, EDIT_A_PICTURE);
        intent.putExtra(PICTURE_ID, pictureId);
        context.startActivity(intent);
    }

    public static void startShowPictureActivity(Context context, int[] picturesIdsArray, int pictureId, int calledBy, int childOf) {
        Intent intent = new Intent(context, LearnShowAddActivity.class);
        intent.putExtra(CALLED_BY, calledBy);
        intent.putExtra(CHILD_OF, childOf);
        intent.putExtra(I_WANT_TO, LEARN);
        intent.putExtra(PICTURES_IDS_ARRAY, picturesIdsArray);
        intent.putExtra(PICTURE_ID, pictureId);
        context.startActivity(intent);
    }

    public static void startTestPictureActivity(Context context, int[] picturesIdsArray) {
        Intent intent = new Intent(context, LearnShowAddActivity.class);
        intent.putExtra(I_WANT_TO, TEST_ME);
        intent.putExtra(PICTURE_ID, picturesIdsArray[0]);
        intent.putExtra(PICTURES_IDS_ARRAY, picturesIdsArray);
        context.startActivity(intent);
    }

    public static void startAddMovementActivity(Context context, int calledBy, int childOf) {
        Intent intent = new Intent(context, AddEditThingActivity.class);
        intent.putExtra(I_WANT_TO, ADD_NEW_THING);
        intent.putExtra(CALLED_BY, calledBy);
        intent.putExtra(CHILD_OF, childOf);
        intent.putExtra(THING_TYPE, MOVEMENT_STRING);
        context.startActivity(intent);
    }
}
