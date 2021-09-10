package com.pniew.mentalahasz.module_learn.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.Picture;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class PictureGalleryActivity extends AppCompatActivity {

    GalleryViewModel viewModel;
    RecyclerView recyclerView;

    int[] picturesIdsArray;

    Button addPicture;
    Button addMovement;
    TextView emptiness;
    FloatingActionButton buttonFloating;
    private EditText triviaEditText;

    boolean emptyArtPeriodList;
    boolean emptyMovementList;
    boolean emptyTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        //set variables ===================================================================================
        viewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication()))
                        .get(GalleryViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.gallery_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false));

        GalleryAdapter adapter = new GalleryAdapter();
        recyclerView.setAdapter(adapter);
        LifecycleOwner lifecycleOwner = this;

        addPicture = findViewById(R.id.button_no_pictures_add_something);
        addMovement = findViewById(R.id.button_no_movements_add_something);
        emptiness = findViewById(R.id.textview_text_for_empty_picture_gallery);
        buttonFloating = findViewById(R.id.add_new_button);


        getSupportActionBar().setHomeButtonEnabled(true);



        //get info about calling thing ===================================================================================
        Intent intent = getIntent();
        viewModel.setIdOfCallingThing(intent.getIntExtra(CHILD_OF, 0));
        viewModel.setCallingThing(intent.getIntExtra(CALLED_BY, 0));
        viewModel.setParentPeriodId(intent.getIntExtra(PARENT_PERIOD_ID, 0));
        viewModel.setTriviaText(intent.getStringExtra(TRIVIA));
        setTitle(intent.getStringExtra(NAME));

        //show pictures =========
        if(viewModel.callingThing() == ART_PERIOD) {
            viewModel.getPictureListByPeriodId(viewModel.idOfCallingThing()).observe(lifecycleOwner, pictures -> {
                if(pictures.isEmpty()){
                    theEmptinessMethod();
                } else {
                    picturesIdsArray = pictures.stream().mapToInt(Picture::getPictureId).toArray();
                    adapter.submitList(pictures);
                    addMovement.setVisibility(View.GONE);
                    emptiness.setVisibility(View.GONE);
                    addPicture.setVisibility(View.GONE);
                    buttonFloating.setVisibility(View.VISIBLE);
                }
            });
        } else if (viewModel.callingThing() == MOVEMENT) {
            viewModel.getPictureListByMovementId(viewModel.idOfCallingThing()).observe(this, pictures -> {
                picturesIdsArray = pictures.stream().mapToInt(Picture::getPictureId).toArray();
                adapter.submitList(pictures);
                if(pictures.isEmpty()){
                    theEmptinessMethod();
                } else {
                    addMovement.setVisibility(View.GONE);
                    emptiness.setVisibility(View.GONE);
                    addPicture.setVisibility(View.GONE);
                    buttonFloating.setVisibility(View.VISIBLE);
                }
            });
        }


        //pictures clickable ===================================================================================

        adapter.setOnPictureClickListener(p -> startShowPictureActivity(PictureGalleryActivity.this, picturesIdsArray, p.getPictureId(), viewModel.callingThing(), viewModel.idOfCallingThing()));

    }


    private void theEmptinessMethod() {
        if(viewModel.callingThing() == ART_PERIOD) {
            emptiness.setText(getString(R.string.no_picture_movement_in_this_art_period));
            addMovement.setVisibility(View.VISIBLE);
        } else if (viewModel.callingThing() == MOVEMENT) {
            emptiness.setText(getString(R.string.no_pictures_in_this_movement));
        } else {
            emptiness.setText(getString(R.string.empty_gallery_insert_sad_emoji));
        }
        emptiness.setVisibility(View.VISIBLE);
        addPicture.setVisibility(View.VISIBLE);
        buttonFloating.setVisibility(View.GONE);

    }


    //end of onCreate

    // method to close activity when back button clicked ======================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.funfackmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_funfack) { // fun fack selected
            AlertDialog.Builder builder = new AlertDialog.Builder(PictureGalleryActivity.this, R.style.TriviaDialogTheme);
            triviaEditText = new EditText(PictureGalleryActivity.this);
            triviaEditText.setText(viewModel.getTriviaText());
            builder.setView(triviaEditText);
            triviaEditText.setClickable(true);
            triviaEditText.setFocusable(true);
            builder.setTitle("Fun Fact");
            builder.setPositiveButton("Update", (dialog, which) -> {
                String trivia = triviaEditText.getText().toString();
                viewModel.setTriviaText(trivia);
                viewModel.updateTrivia();
            });
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNewPicture(View view) {
        startAddPictureActivity(this, viewModel.callingThing(), viewModel.idOfCallingThing(), viewModel.parentPeriodId());
    }


    public void addNewMovement(View view) {
        startAddMovementActivity(this, viewModel.callingThing(), viewModel.idOfCallingThing());
        this.finish();
    }
}