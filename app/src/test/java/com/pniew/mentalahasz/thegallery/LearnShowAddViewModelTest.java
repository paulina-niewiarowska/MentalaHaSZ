package com.pniew.mentalahasz.thegallery;

import junit.framework.TestCase;

public class LearnShowAddViewModelTest extends TestCase {

    int[] picturesIdsArray = {5,2,7,4,1};
    int pictureId = 7;

    public void testItCyclesPictureIds() {
        getOneAfter();
        assertEquals(pictureId, 4);
        getOneAfter();
        assertEquals(pictureId, 1);
        getOneAfter();
        assertEquals(pictureId, 5);
        getOneAfter();
        assertEquals(pictureId, 2);
    }

    public void getOneAfter(){
        if (picturesIdsArray.length == 0) {
            pictureId = -1;
            return;
        }
        for (int i = 0; i < picturesIdsArray.length; i++) {
            if (picturesIdsArray[i]==pictureId) {
                if(i == picturesIdsArray.length - 1) {
                    pictureId = picturesIdsArray[0];
                } else {
                    pictureId = picturesIdsArray[i + 1];
                }
                return;
            }
        }
    }
}