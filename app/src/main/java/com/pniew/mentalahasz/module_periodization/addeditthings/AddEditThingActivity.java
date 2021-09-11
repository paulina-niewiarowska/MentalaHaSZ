package com.pniew.mentalahasz.module_periodization.addeditthings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.module_periodization.ListOfThingsActivity;

import static com.pniew.mentalahasz.module_periodization.addeditthings.AddEditThingViewModel.*;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class AddEditThingActivity extends AppCompatActivity {

    private AddEditThingViewModel addEditViewModel;

    private EditText textViewName;
    private EditText textViewDating;
    private EditText textViewLocation;
    private Spinner spinnerChooseType;
    private Spinner spinnerChooseParentPeriod;
    private EditText triviaEditText;

    private final int SPINNER_OF_TYPE = 2;
    private final int SPINNER_OF_MOVEMENT = 1;
    private final int SPINNER_OF_ART_PERIOD = 0;

    private boolean onPause;
    private boolean done;
    private boolean finishing;
    private boolean back;

    private boolean positionObtained;
    private int iWantTo;
    private String itemType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        // set member variables and fields:

        addEditViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(AddEditThingViewModel.class);

        onPause = false;
        done = false;
        positionObtained = false;
        finishing = false;

        textViewName = findViewById(R.id.field_name_add_edit);
        textViewDating = findViewById(R.id.field_dating_add_edit);
        textViewLocation = findViewById(R.id.field_location_add_edit);

        spinnerChooseType = findViewById(R.id.spinner_period_or_movement);
        spinnerChooseParentPeriod = findViewById(R.id.spinner_choose_art_period_parent);

        AppCompatButton buttonAdd = findViewById(R.id.button_confirm_and_add);

        //set Observer:
        addEditViewModel.bindOnInsertedIdChanged(this);

        //spinner that lets you choose type of object you want to add:

        setChooseTypeSpinner();

        //spinner for movements - lets you choose art period for movement:

        setChooseParentPeriodSpinner();

        //we take data from intent, so we know what caller wants:

        Intent intent = getIntent();
        iWantTo = intent.getIntExtra(I_WANT_TO, 0);
        itemType = intent.getStringExtra(THING_TYPE);

        //if we're here to edit a thing:

        if (iWantTo == EDIT_A_THING) {
            setTitle("Edit Periodization Item");
            if(addEditViewModel.getSpinnerTypeSelection() == -1) {
                switch (itemType) {
                    case ART_PERIOD_STRING:
                        addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_ART_PERIOD);
                        break;
                    case MOVEMENT_STRING:
                        addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_MOVEMENT);
                        break;
                    case TYPE_STRING:
                        addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_TYPE);
                        break;
                }
            }
            spinnerChooseType.setSelection(addEditViewModel.getSpinnerTypeSelection());
            spinnerChooseType.setEnabled(false);
            spinnerChooseType.setClickable(false);

            buttonAdd.setText(R.string.confirm_and_edit);

            addEditViewModel.setId(intent.getIntExtra(THING_ID, 0));
            textViewName.setText(intent.getStringExtra(THING_NAME));

            textViewLocation.setText(intent.getStringExtra(THING_LOCATION));
            textViewDating.setText(intent.getStringExtra(THING_DATING));
            addEditViewModel.setTriviaText(intent.getStringExtra(TRIVIA));


        } else if ((itemType != null) && (iWantTo == ADD_NEW_THING) && (itemType.equals(MOVEMENT_STRING))) {
            setTitle("Add Periodization Item");
            addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_MOVEMENT);
            spinnerChooseType.setSelection(addEditViewModel.getSpinnerTypeSelection());
            spinnerChooseType.setEnabled(false);
            spinnerChooseType.setClickable(false);

        } else if ((itemType != null) && (iWantTo == ADD_NEW_THING) && (itemType.equals(TYPE_STRING))) {
            setTitle("Add Periodization Item");
            addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_TYPE);
            spinnerChooseType.setSelection(addEditViewModel.getSpinnerTypeSelection());
            spinnerChooseType.setEnabled(false);
            spinnerChooseType.setClickable(false);

        } else if ((itemType != null) && (iWantTo == ADD_NEW_THING) && (itemType.equals(ART_PERIOD_STRING))) {
            setTitle("Add Periodization Item");
            addEditViewModel.setSpinnerTypeSelection(SPINNER_OF_ART_PERIOD);
            spinnerChooseType.setSelection(addEditViewModel.getSpinnerTypeSelection());
            spinnerChooseType.setEnabled(false);
            spinnerChooseType.setClickable(false);

    } else if (iWantTo == ADD_NEW_THING) {
            setTitle("Add Periodization Item");
            addEditViewModel.setId(0);
        }

        //spinners and button listens to changes and clicks:
        spinnerChooseType.setOnItemSelectedListener(getOnItemTypeSelectedListener());
        spinnerChooseParentPeriod.setOnItemSelectedListener(getOnArtPeriodSelectedListener());
        buttonAdd.setOnClickListener(this::onButtonClickAdd);

    }

    private void setChooseTypeSpinner() {
        ArrayAdapter<String> chooseTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        chooseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseType.setAdapter(chooseTypeAdapter);
        chooseTypeAdapter.add(ART_PERIOD_STRING);
        chooseTypeAdapter.add(MOVEMENT_STRING);
        chooseTypeAdapter.add(TYPE_STRING);
    }

    //this will be shown only if we edit movements, but we can prepare it now:
    private void setChooseParentPeriodSpinner() {
        ArrayAdapter<ArtPeriod> artPeriodArrayAdapter = new ArrayAdapter<>(AddEditThingActivity.this, android.R.layout.simple_spinner_item);
        artPeriodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseParentPeriod.setAdapter(artPeriodArrayAdapter);

        //if art period list changes, recyclerview's adapter cleans up the list and adds it again:
        addEditViewModel.getAllArtPeriods().observe(AddEditThingActivity.this, artPeriods -> {
            artPeriodArrayAdapter.clear();
            artPeriodArrayAdapter.addAll(artPeriods);

            //if we want to edit, we want the field to be set as it is in the thing:
            int iWantTo = getIntent().getIntExtra(I_WANT_TO, 0);

            if((!positionObtained && (iWantTo == EDIT_A_THING)) || ((itemType != null) && (iWantTo == ADD_NEW_THING) && (itemType.equals(MOVEMENT_STRING)))) {
                if(addEditViewModel.getSpinnerParentSelection() == -1) {
                    int artPeriodId;
                    if(iWantTo == EDIT_A_THING) {
                        artPeriodId = getIntent().getIntExtra(THING_PARENT_PERIOD_ID, 0);
                    } else {
                        artPeriodId = getIntent().getIntExtra(CHILD_OF, 0);
                    }
                    ArtPeriod a = artPeriods.stream().filter(artPeriod -> artPeriod.getArtPeriodId() == artPeriodId).findFirst().orElse(null);
                    if (a != null) {
                        int position = artPeriodArrayAdapter.getPosition(a);
                        addEditViewModel.setSpinnerParentSelection(position);
                        positionObtained = true;
                    }
                    if(iWantTo == ADD_NEW_THING && !artPeriods.isEmpty()) {
                        spinnerChooseParentPeriod.setClickable(false);
                        spinnerChooseParentPeriod.setEnabled(false);
                    }
                }
                spinnerChooseParentPeriod.setSelection(addEditViewModel.getSpinnerParentSelection());
            }
        });
    }

    //if we choose the art period in the spinner, put it immediately in the movement's data:
    private AdapterView.OnItemSelectedListener getOnArtPeriodSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long viewId) {
                ArtPeriod chosen = (ArtPeriod) adapterView.getItemAtPosition(position);
                addEditViewModel.setIdOfParent(chosen.getArtPeriodId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
    }

    //IMPORTAMT: depending of type of thing we choose, the view shows us different layout:
    private AdapterView.OnItemSelectedListener getOnItemTypeSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long viewId) {
                String chosen = adapterView.getItemAtPosition(position).toString();
                switch (chosen) {
                    case (MOVEMENT_STRING):
                        spinnerChooseParentPeriod.setVisibility(View.VISIBLE);
                        findViewById(R.id.text_for_spinner2).setVisibility(View.VISIBLE);
                        addEditViewModel.setTypeOfItemChosen(CHOSEN_MOVEMENT);
                        textViewDating.setVisibility(View.VISIBLE);
                        textViewLocation.setVisibility(View.VISIBLE);
                        break;

                    case (ART_PERIOD_STRING):
                        spinnerChooseParentPeriod.setVisibility(View.GONE);
                        findViewById(R.id.text_for_spinner2).setVisibility(View.GONE);
                        addEditViewModel.setTypeOfItemChosen(CHOSEN_ART_PERIOD);
                        textViewDating.setVisibility(View.VISIBLE);
                        textViewLocation.setVisibility(View.GONE);
                        break;

                    case (TYPE_STRING):
                        spinnerChooseParentPeriod.setVisibility(View.GONE);
                        findViewById(R.id.text_for_spinner2).setVisibility(View.GONE);
                        addEditViewModel.setTypeOfItemChosen(CHOSEN_TYPE);
                        textViewDating.setVisibility(View.GONE);
                        textViewLocation.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    //if we click "add" that means we're finished, so...

    public void onButtonClickAdd(View view) {
        finishing = true;
        saveItem();
    }
    //method to save, if "finished" then close the activity also:

    private void saveItem() {
        String name = textViewName.getText().toString();
        String dating = textViewDating.getText().toString();
        String location = textViewLocation.getText().toString();
        Result result;

        result = addEditViewModel.addEditThings(name, dating, location);
        int toast = result.getResultOfInsertion();

        if (toast <= 4) {
            Toast.makeText(AddEditThingActivity.this, result.getToast(), Toast.LENGTH_SHORT).show();
        }

        if (toast > 4) {
            Toast.makeText(AddEditThingActivity.this, result.getToast(), Toast.LENGTH_SHORT).show();
            if(finishing) {
                done = true;
                Intent intent = new Intent(AddEditThingActivity.this, ListOfThingsActivity.class);
                setResult(RESULT_OK, intent);
                AddEditThingActivity.this.finish();
            }
        }

    }

    @Override
    public void onPause() {
        onPause = true;
        if (!done && !back) {
            saveItem();}
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.funfackmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            back = true;
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_funfack) { // fun fack selected
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddEditThingActivity.this, R.style.TriviaDialogTheme);
            triviaEditText = new EditText(AddEditThingActivity.this);
            triviaEditText.setText(addEditViewModel.getTriviaText());
            builder.setView(triviaEditText);
            triviaEditText.setClickable(true);
            triviaEditText.setFocusable(true);
            builder.setTitle("Fun Fact");
            builder.setPositiveButton("Update", (dialog, which) -> {
                String trivia = triviaEditText.getText().toString();
                addEditViewModel.setTriviaText(trivia);
                addEditViewModel.updateTrivia();
            });
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancelDelete(View view) {
        if (iWantTo == EDIT_A_THING) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddEditThingActivity.this, R.style.HaSZErrorDialogTheme);
            DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                Result result;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        result = addEditViewModel.deleteThing();
                        done = true;
                        Toast.makeText(AddEditThingActivity.this, result.getToast(), Toast.LENGTH_SHORT).show();
                        AddEditThingActivity.this.finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        done = true;
                        break;
                }
            };
            alertBuilder
                    .setTitle("Warning! Cascade delete!")
                    .setMessage("Do you want to permanently remove this item?\nThis is cascade delete. All the items and pictures under this item will be removed as well.\nYou will not be able to undo this operation.")
                    .setPositiveButton("Yes, delete", onClickListener)
                    .setNegativeButton("No, cancel", onClickListener)
                    .show();
        } else {
            done = true;
            AddEditThingActivity.this.finish();
        }
    }
}



