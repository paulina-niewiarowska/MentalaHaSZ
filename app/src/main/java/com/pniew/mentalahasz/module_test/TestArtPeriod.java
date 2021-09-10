package com.pniew.mentalahasz.module_test;

import java.io.Serializable;
import java.util.ArrayList;

public class TestArtPeriod implements Serializable {
    private static final long serialVersionUID = 144L;
    public int id;
    public String artPeriodName;
    public boolean selected = false;
    public ArrayList<TestMovement> subList = new ArrayList<>();
}
