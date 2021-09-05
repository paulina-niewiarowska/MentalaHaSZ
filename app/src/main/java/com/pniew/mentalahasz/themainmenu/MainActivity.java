package com.pniew.mentalahasz.themainmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pniew.mentalahasz.thelistofthings.ListOfThingsActivity;
import com.pniew.mentalahasz.thegallery.choosing.ChooseActivity;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.thetest.TestActivity;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLearn = findViewById(R.id.button_learn);
        buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itemsToLoad = new Intent(MainActivity.this, ChooseActivity.class);
                itemsToLoad.putExtra(LOAD, ART_PERIOD);
                itemsToLoad.putExtra(CALLED_BY, MAIN_MENU);
                MainActivity.this.startActivity(itemsToLoad);
            }
        });

        Button buttonTest = findViewById(R.id.button_test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        Button buttonWeb = findViewById(R.id.button_learn_from_web);
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button buttonAddCard = findViewById(R.id.button_add_new_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddPictureActivity(MainActivity.this, MAIN_MENU, NULL, NULL);
            }
        });


        Button buttonEditThings = findViewById(R.id.button_show_all_things);
        buttonEditThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDo = new Intent(MainActivity.this, ListOfThingsActivity.class);
                MainActivity.this.startActivity(toDo);
            }
        });


    }
}