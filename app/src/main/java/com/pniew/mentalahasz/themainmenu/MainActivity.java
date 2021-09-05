package com.pniew.mentalahasz.themainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.thelistofthings.ListOfThingsActivity;
import com.pniew.mentalahasz.thegallery.choosing.ChooseActivity;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.thetest.TestActivity;

import java.util.List;

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

        LifecycleOwner owner = this;
        Button buttonAddCard = findViewById(R.id.button_add_new_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityViewModel viewModel = new ViewModelProvider(MainActivity.this).get(MainActivityViewModel.class);
                viewModel.getAllArtPeriods().observe(owner, new Observer<List<ArtPeriod>>() {
                    @Override
                    public void onChanged(List<ArtPeriod> artPeriods) {
                        if(artPeriods.isEmpty()) {
                            showDialog();
                            viewModel.getAllArtPeriods().removeObservers(owner);
                        } else {
                            viewModel.getAllTypes().observe(owner, new Observer<List<Type>>() {
                                @Override
                                public void onChanged(List<Type> types) {
                                    if(types.isEmpty()) {
                                        showDialog();
                                        viewModel.getAllTypes().removeObservers(owner);
                                    } else {
                                        startAddPictureActivity(MainActivity.this, MAIN_MENU, NULL, NULL);
                                    }
                                }
                            });
                        }
                        viewModel.getAllArtPeriods().removeObservers(owner);
                    }
                });
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

    private void showDialog() {
        new AlertDialog.Builder(MainActivity.this, R.style.HaSZDialogTheme)
                .setTitle("Cannot find periodization items")
                .setMessage("Looks like you lack some periodization items.\n" +
                        "To be able to add new picture you have to have at least one Art Period and at least one Type of item.\n" +
                        "Please add some periodization items before you come here to add new pictures.")
                .show();
    }
}