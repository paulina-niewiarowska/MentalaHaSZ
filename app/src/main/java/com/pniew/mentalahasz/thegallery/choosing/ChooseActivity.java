package com.pniew.mentalahasz.thegallery.choosing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.thegallery.gallery.PictureGalleryActivity;

import java.util.List;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class ChooseActivity extends AppCompatActivity {

    private ChooseViewModel chooseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // variable nothing to show will be set to true when lists got from the livedata will turn
        // out to be empty. Then the method nothingToShow() can be called, so that the user can see
        // what he/she can do now (add new items). This value will be set to false on every onchange()
        // of the livedata which returns any value in the livedata lists (both of art periods and
        // of movements)

        RecyclerView recyclerView = findViewById(R.id.choose_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chooseViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).
                get(ChooseViewModel.class);
        chooseViewModel.setNothingToShow(false);


        int itemsToLoad = getIntent().getIntExtra(LOAD, 0);


        // =========================================================================================
        //WE LOAD ART PERIODS:
        if(itemsToLoad == ART_PERIOD && !chooseViewModel.isNothingToShow()){
            ItemToChooseAdapter<ArtPeriod> adapter = new ItemToChooseAdapter<>();
            recyclerView.setAdapter(adapter);

            //load all art periods and put them into the adapter which then injects them
            // into the recycler view so the user can see and click them:
            chooseViewModel.getAllArtPeriods().observe(this, new Observer<List<ArtPeriod>>() {
                @Override
                public void onChanged(List<ArtPeriod> artPeriods) {
                    if (!artPeriods.isEmpty()) {
                        chooseViewModel.setNothingToShow(false);
                        adapter.submitList(artPeriods);
                    } else {
                        //the art period list is empty, we want to suggest user to add something:
                        nothingToShow();
                    }
                }
            });

            //there is a list that user sees, we have just set it. We want it to be clickable:
            if (!chooseViewModel.isNothingToShow()) {
                adapter.setOnItemClickListener(this::artPeriodClicked);
            }

        // =========================================================================================

        // WE LOAD MOVEMENTS:
        } else if (itemsToLoad == MOVEMENT && !chooseViewModel.isNothingToShow()) {
            ItemToChooseAdapter<Movement> adapter = new ItemToChooseAdapter<>();
            recyclerView.setAdapter(adapter);

            // at this point we got the id of calling art period in the intent
            // (createIntentAndStartActivity() method took care of that). We take it out
            // and load list of movements that fit to the id of the parent.
            chooseViewModel.setParentPeriodId(getIntent().getIntExtra(CHILD_OF, 0));
            chooseViewModel.getMovementListByPeriodId(). // <-- here is the list. We make it click'able by submitting it to the adapter
                    observe(ChooseActivity.this, new Observer<List<Movement>>() {
                @Override
                public void onChanged(List<Movement> movements) {
                    if (!movements.isEmpty()) {
                        // place the list into the adapter so it can show it:
                        chooseViewModel.setNothingToShow(false);
                        adapter.submitList(movements);
                    } else {
                        // we shouldn't get here because if art period doesn't have the movements,
                        // it should call picture gallery instead. But if it happens anyway,
                        // we suggest user to add something here:
                        nothingToShow();
                    }
                }
            });

            //and now we want the movement cards to be click'able:
            if (!chooseViewModel.isNothingToShow()) {
                adapter.setOnItemClickListener(new ItemToChooseAdapter.onItemClickListener<Movement>() {
                    @Override
                    public void onItemClick(Movement item) {
                        createIntentAndStartActivity(PICTURES, MOVEMENT, item.getMovementId());
                    }
                });
            }

        // =========================================================================================
        //WE LOAD NOTHING or got PICTURES to load even if we were not supposed to:
        } else if (itemsToLoad == 0) {
            nothingToShow();
            Toast.makeText(this, "Cannot read what to show :(", Toast.LENGTH_LONG).show();
        }
    }


    // ============================ the end of onCreate() method ===================================================================



    //METHODS TIME:
    //----------------------------------------------------------------------------------------------

    private void artPeriodClicked(ArtPeriod item) {
        //the art period card has been clicked. We want to load its movements list
        // if there are any (like modernism). If the art period does not contain
        // any movements (like prehistory), we want to load its pictures.

        //load this period's list of movements
        chooseViewModel.setParentPeriodId(item.getArtPeriodId());
        chooseViewModel.getMovementListByPeriodId().observe(ChooseActivity.this, new Observer<List<Movement>>() {
            @Override
            public void onChanged(List<Movement> movements) {
                boolean isMovementListEmpty = movements.isEmpty();

                // no movements under this period, go to picture gallery of this art period:
                if (isMovementListEmpty) {
                    createIntentAndStartActivity(PICTURES, ART_PERIOD, item.getArtPeriodId());

                //there are movements under this period, load them
                } else {
                    createIntentAndStartActivity(MOVEMENT, ART_PERIOD, item.getArtPeriodId());
                }
            }
        });
    }


    //----------------------------------------------------------------------------------------------

    private void createIntentAndStartActivity(int toLoad, int calledBy, int childOf) {
        Intent intent;
        if (toLoad == MOVEMENT) {
            intent = new Intent(ChooseActivity.this, ChooseActivity.class);
        } else {
            intent = new Intent(ChooseActivity.this, PictureGalleryActivity.class);
        }
        if(calledBy == MOVEMENT) {
            intent.putExtra(PARENT_PERIOD_ID, chooseViewModel.getParentPeriodId());
        }
        intent.putExtra(LOAD, toLoad);
        intent.putExtra(CALLED_BY, calledBy);
        intent.putExtra(CHILD_OF, childOf);
        ChooseActivity.this.startActivity(intent);

    }


    //----------------------------------------------------------------------------------------------

    private void nothingToShow(){
        //todo: here message that nothing is created yet and you can add new art periods
        // and their movements by clicking here and then you can add new pictures here
        // and if you want you can download some pre-added stuff from web here
        // or you can do this all from the main menu.
        // It would be good if there was a fragment that holds all the things:
        // the text and the buttons. For now it can be just text that says that
        // you can do all of this from the main menu.
        chooseViewModel.setNothingToShow(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }
}