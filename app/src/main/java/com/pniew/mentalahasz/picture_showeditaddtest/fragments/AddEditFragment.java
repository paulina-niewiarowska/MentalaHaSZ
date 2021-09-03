package com.pniew.mentalahasz.picture_showeditaddtest.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.database.entities.Type;
import com.pniew.mentalahasz.picture_showeditaddtest.LearnShowAddActivity;
import com.pniew.mentalahasz.utils.UtilMethods;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class AddEditFragment extends Fragment {

    //==============================================================================================
    // member fields:

    private AddEditViewModel viewModel;

    private EditText name;
    private EditText author;
    private EditText dating;
    private EditText location;

    private ImageButton addEditButton;
    private ImageButton cancelDeleteButton;

    private Spinner spinner_choose_art_period;
    private Spinner spinner_choose_movement;
    private Spinner spinner_choose_type;

    private boolean artPeriodPositionObtained;
    private boolean movementPositionObtained;
    private boolean typePositionObtained;

    LifecycleOwner myLifeCycleOwner;

    private int iWantTo;

    LiveData<Picture> picture;
    Activity act;

    ArrayAdapter<Movement> movementArrayAdapter;
    ArrayAdapter<ArtPeriod> artPeriodArrayAdapter;
    ArrayAdapter<Type> typeArrayAdapter;

    //==============================================================================================
    // constructor, kind of:

    public static AddEditFragment newInstance() {
        return new AddEditFragment();
    }

    //================================================================================================================================================
    //================================================================================================================================================
    // onCreate:

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_addedit_fragment, container, false);

        //==========================================================================================
        // initializing:

        myLifeCycleOwner = getViewLifecycleOwner();
        act = this.getActivity();

        viewModel = new ViewModelProvider(this).get(AddEditViewModel.class);

        artPeriodPositionObtained = false;
        movementPositionObtained = false;
        viewModel.setSpinnerTypeSelection(-1);
        viewModel.setSpinnerMovementSelection(-1);
        viewModel.setSpinnerArtPeriodSelection(-1);

        Bundle arguments = getArguments();
        iWantTo = arguments.getInt(I_WANT_TO, 0);
        viewModel.setCallingThing(arguments.getInt(CALLED_BY, 0));
        viewModel.setCallingThingId(arguments.getInt(CHILD_OF, 0));

        name = v.findViewById(R.id.edit_text_add_picture_name);
        author = v.findViewById(R.id.edit_text_add_picture_author);
        dating = v.findViewById(R.id.edit_text_add_picture_dating);
        location = v.findViewById(R.id.edit_text_add_picture_location);

        addEditButton = v.findViewById(R.id.button_add_new_edit_picture);
        cancelDeleteButton = v.findViewById(R.id.button_delete_cancel_picture);

        spinner_choose_art_period = v.findViewById(R.id.spinner_add_picture_choose_art_period);
        spinner_choose_movement = v.findViewById(R.id.spinner_add_picture_choose_movement);
        spinner_choose_type = v.findViewById(R.id.spinner_add_picture_choose_type);


        //==========================================================================================
        // if editing, get values from existing picture and put them into right fields:

        if(iWantTo == EDIT_A_PICTURE){
            viewModel.setPictureById(getArguments().getInt(PICTURE_ID));
            picture = viewModel.getPicture();
            picture.observe(myLifeCycleOwner, new Observer<Picture>() {
                @Override
                public void onChanged(Picture picture) {
                    viewModel.setId(picture.getPictureId());
                    viewModel.setName(picture.getPictureName());
                    viewModel.setAuthor(picture.getPictureAuthor());
                    viewModel.setLocation(picture.getPictureLocation());
                    viewModel.setDating(picture.getPictureDating());
                    viewModel.setArtPeriodId(picture.getPictureArtPeriod());
                    if(picture.getPictureMovement() != null) {
                        viewModel.setMovementId(picture.getPictureMovement());
                    }
                    viewModel.setTypeId(picture.getPictureType());
                    viewModel.setPath(picture.getPicturePath());

                    cancelDeleteButton.setImageResource(R.drawable.ic_delete_big);
                    //addEditButton.setText("Edit picture");
                    //cancelDeleteButton.setText("Delete picture");
                    name.setText(viewModel.getName());
                    author.setText(viewModel.getAuthor());
                    location.setText(viewModel.getLocation());
                    dating.setText(viewModel.getDating());

                    //==============================================================================
                    // set spinners:
                    // HAS TO be here, bc otherwise spinners are set when there is no data yet to be
                    // set in them. So, if there will be data that we're wainting for, we set spinners
                    // when the data is here, not earlier.

                    setSpinners();

                    //end of onChange()------------------------------------
                }
            });
        } else if (iWantTo == ADD_NEW_PICTURE && viewModel.getCallingThing() != MAIN_MENU /*przyszło z movement lub z art period*/){
            if(viewModel.getCallingThing() == ART_PERIOD){
                viewModel.itsArtPeriodCalling();
            } else if (viewModel.getCallingThing() == MOVEMENT){
                viewModel.itsMovementCalling(arguments.getInt(PARENT_PERIOD_ID, 0));
            }
            setSpinners();
        } else {
            setSpinners();
        }



        //==========================================================================================
        //button confirm listener:

        addEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpdatePicture();
            }
        });

        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePictureCancel();
            }
        });

        return v;
    }
    // end of onCreate()


    //==============================================================================================
    //==============================================================================================
    // methods:

    private void setSpinners() {
        setArtPeriodSpinner();
        setMovementSpinner();
        setTypeSpinner();
    }

    private void setTypeSpinner(){
        if(typeArrayAdapter == null) {
            typeArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
            typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_choose_type.setAdapter(typeArrayAdapter);
            spinner_choose_type.setTooltipText("Type of Artwork");
        }

        viewModel.getAllTypes().observe(getViewLifecycleOwner(), new Observer<List<Type>>() {
            @Override
            public void onChanged(List<Type> types) {
                typeArrayAdapter.clear();
                typeArrayAdapter.addAll(types);

                if(iWantTo == EDIT_A_PICTURE && !typePositionObtained) {
                    obtainAndSetPositionToTypeSpinner(types, typeArrayAdapter);
                }
            }
        });
        setTypeSpinnerOnClickListener();
    }

    //==============================================================================================
    //

    private void setArtPeriodSpinner() {
        if (artPeriodArrayAdapter == null) {
            artPeriodArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
            artPeriodArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_choose_art_period.setAdapter(artPeriodArrayAdapter);
            spinner_choose_art_period.setTooltipText("Art Periods to choose");
        }

        viewModel.getAllArtPeriods().observe(getViewLifecycleOwner(), new Observer<List<ArtPeriod>>() {
            @Override
            public void onChanged(List<ArtPeriod> artPeriods) {
                artPeriodArrayAdapter.clear();
                artPeriodArrayAdapter.addAll(artPeriods);

                // if we edit, we need to put cursor on the position that the picture already has:
                if((iWantTo == EDIT_A_PICTURE && !artPeriodPositionObtained)
                        || (iWantTo == ADD_NEW_PICTURE && (viewModel.getCallingThing() != MAIN_MENU))) {

                    obtainAndSetPositionToArtPeriodSpinner(artPeriods, artPeriodArrayAdapter);

                } else if(!artPeriods.isEmpty()) {
                    ArtPeriod artPeriod = (ArtPeriod) artPeriods.get(0);
                    viewModel.setArtPeriodId(artPeriod.getArtPeriodId());
                }
            }
        });

        setArtPeriodSpinnerOnClickListener();
    }

    //==============================================================================================
    //

    private void setMovementSpinner() {
        if (movementArrayAdapter == null) {
            movementArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
            movementArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_choose_movement.setAdapter(movementArrayAdapter);
            spinner_choose_movement.setTooltipText("Movements to choose");
        }

        if(viewModel.getArtPeriodId() != 0 && viewModel.getArtPeriodId() != -1) {
            viewModel.getMovementListByPeriodId(viewModel.getArtPeriodId()).observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
                @Override
                public void onChanged(List<Movement> movements) {
                    movementArrayAdapter.clear();
                    if(movements.isEmpty()) {
                        spinner_choose_movement.setEnabled(false);
                        spinner_choose_movement.setClickable(false);
                        viewModel.setMovementId(-1);
                    } else {
                        movementArrayAdapter.addAll(movements);

                        if ((iWantTo == EDIT_A_PICTURE && !movementPositionObtained)
                                || (iWantTo == ADD_NEW_PICTURE && (viewModel.getCallingThing() == MOVEMENT))) {
                            obtainAndSetPositionToMovementSpinner(movements, movementArrayAdapter);

                        } else if (!movements.isEmpty()){
                            Movement movement = movements.get(0);
                            viewModel.setMovementId(movement.getMovementId());
                        }
                    }
                }
            });
        }
        else {
            viewModel.getAllMovements().observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
                @Override
                public void onChanged(List<Movement> movements) {
                    movementArrayAdapter.clear();
                    movementArrayAdapter.addAll(movements);
                }
            });
        }


        setMovementSpinnerOnClickListener();

    }

    //==============================================================================================
    //

    private void setMovementSpinnerOnClickListener() {
        spinner_choose_movement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Movement chosen = (Movement) adapterView.getItemAtPosition(position);
                viewModel.setMovementId(chosen.getMovementId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (adapterView.getCount() < 1) {
                    viewModel.setMovementId(-1);
                } else {
                    int position = adapterView.getFirstVisiblePosition();
                    Movement chosen = (Movement) adapterView.getItemAtPosition(position);
                    viewModel.setMovementId(chosen.getMovementId());
                }
            }
        });
    }

    //==============================================================================================
    //

    private void setArtPeriodSpinnerOnClickListener() {
        spinner_choose_art_period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ArtPeriod chosen = (ArtPeriod) adapterView.getItemAtPosition(position);
                viewModel.setArtPeriodId(chosen.getArtPeriodId());
                setMovementSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int position = parent.getFirstVisiblePosition();
                ArtPeriod chosen = (ArtPeriod) parent.getItemAtPosition(position);
                viewModel.setArtPeriodId(chosen.getArtPeriodId());
            }
        });
    }

    //==============================================================================================
    //

    private void setTypeSpinnerOnClickListener() {
        spinner_choose_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Type chosen = (Type) adapterView.getItemAtPosition(position);
                viewModel.setTypeId(chosen.getTypeId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //==============================================================================================
    //


    private void obtainAndSetPositionToMovementSpinner(List<Movement> movements, ArrayAdapter<Movement> movementArrayAdapter) {

        if(viewModel.getSpinnerMovementSelection() == -1){
            Movement m = movements.stream().filter(movement -> movement.getMovementId() == viewModel.getMovementId()).findFirst().orElse(null);
            if(m != null) {
                int position = movementArrayAdapter.getPosition(m);
                viewModel.setSpinnerMovementSelection(position);
                movementPositionObtained = true;

                if(iWantTo == ADD_NEW_PICTURE && viewModel.getCallingThing() != MAIN_MENU){
                    spinner_choose_movement.setEnabled(false);
                    spinner_choose_movement.setClickable(false);
                }
            }
        }
        spinner_choose_movement.setSelection(viewModel.getSpinnerMovementSelection());
    }
    //==============================================================================================
    //

    private void obtainAndSetPositionToArtPeriodSpinner(List<ArtPeriod> artPeriods, ArrayAdapter<ArtPeriod> artPeriodArrayAdapter) {

        if(viewModel.getSpinnerArtPeriodSelection() == -1){
            ArtPeriod a = artPeriods.stream().filter(artPeriod -> artPeriod.getArtPeriodId() == viewModel.getArtPeriodId()).findFirst().orElse(null);
            if(a != null) {
                int position = artPeriodArrayAdapter.getPosition(a);
                viewModel.setSpinnerArtPeriodSelection(position);
                artPeriodPositionObtained = true;

                if(iWantTo == ADD_NEW_PICTURE && viewModel.getCallingThing() != MAIN_MENU){
                    spinner_choose_art_period.setEnabled(false);
                    spinner_choose_art_period.setClickable(false);
                }
            }
        }
        spinner_choose_art_period.setSelection(viewModel.getSpinnerArtPeriodSelection());
    }

    //==============================================================================================
    //

    private void obtainAndSetPositionToTypeSpinner(List<Type> types, ArrayAdapter<Type> typeArrayAdapter) {

        if(viewModel.getSpinnerTypeSelection() == -1) {
            Type t = types.stream().filter(type -> type.getTypeId() == viewModel.getTypeId()).findFirst().orElse(null);
            if(t != null) {
                int position = typeArrayAdapter.getPosition(t);
                viewModel.setSpinnerTypeSelection(position);
                typePositionObtained = true;
            }
        }
        spinner_choose_type.setSelection(viewModel.getSpinnerTypeSelection());
    }

    //==============================================================================================
    //

    private void saveUpdatePicture() {
        viewModel.setName(name.getText().toString());
        viewModel.setAuthor(author.getText().toString());
        viewModel.setLocation(location.getText().toString());
        viewModel.setDating(dating.getText().toString());

        int result = 0;
        if (iWantTo == ADD_NEW_PICTURE) {
            String path = getArguments().getString(PICTURE_PATH);
            viewModel.setPath(path);
            result = viewModel.savePicture();
        }
        if (iWantTo == EDIT_A_PICTURE) {
            boolean changed = getArguments().getBoolean(PICTURE_CHANGED);
            if(changed) {
                String path = getArguments().getString(PICTURE_PATH);
                viewModel.setPath(path);
            }
            result = viewModel.updatePicture();
        }

        if(result == 0) {
            UtilMethods.makeToast(getContext(), "Please fill all the fields");
        } else if (result == 1) {
            //Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            if(iWantTo == ADD_NEW_PICTURE) {
                UtilMethods.makeToast(getContext(), "Picture added");
                //todo: clearUp!
                if (act instanceof LearnShowAddActivity) {
                    ((LearnShowAddActivity) act).refreshAddingPicture();
                }
            } else if (iWantTo == EDIT_A_PICTURE) {
                UtilMethods.makeToast(getContext(), "Picture edited");
                if (act instanceof LearnShowAddActivity) {
                    if(viewModel.isChangedThing()){
                        ((LearnShowAddActivity) act).editFinishedChangedParticipation();
                    } else {
                        ((LearnShowAddActivity) act).editFinished();
                    }
                }
            }
        }

    }

    private void deletePictureCancel() {

        if(iWantTo == ADD_NEW_PICTURE) {
            if (act instanceof LearnShowAddActivity) {
                ((LearnShowAddActivity) act).closeThisActivity();
            }
        }

        if (iWantTo == EDIT_A_PICTURE) {

            //todo: asking, deleting, toast

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            picture.removeObservers(myLifeCycleOwner);
                            viewModel.deletePicture();
                            UtilMethods.makeToast(getContext(), "Picture deleted");
                            if (act instanceof LearnShowAddActivity) {
                                ((LearnShowAddActivity) act).closeThisActivity();
                            }

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            UtilMethods.makeToast(getContext(), "Deleting cancelled");
                            break;
                    }
                }
            };
            alert.setMessage("Do you want to delete this picture?").setPositiveButton("Yes", onClickListener).setNegativeButton("No", onClickListener).show();
        }
    }



}