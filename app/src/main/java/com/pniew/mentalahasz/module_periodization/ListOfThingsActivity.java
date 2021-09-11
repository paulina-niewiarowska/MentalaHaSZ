package com.pniew.mentalahasz.module_periodization;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.module_periodization.addeditthings.AddEditThingActivity;
import com.pniew.mentalahasz.model.database.Things;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class ListOfThingsActivity extends AppCompatActivity {

    private ListOfThingsViewModel thingsViewModel;
    Intent data;

    ActivityResultLauncher<Intent> addEditActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    data = result.getData();
                    assert data != null;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_things);

        RecyclerView recyclerView = findViewById(R.id.things_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        thingsViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).
                get(ListOfThingsViewModel.class);

        ListOfThingsAdapter adapter = new ListOfThingsAdapter();
        recyclerView.setAdapter(adapter);
        thingsViewModel.getAllThings().observe(this, adapter::submitList);


        adapter.setOnThingClickListener(thing -> {
            Intent intent = prepareIntentForEditing(thing);
            addEditActivityResultLauncher.launch(intent);
        });

        FloatingActionButton actionButton = findViewById(R.id.add_new_button);
        actionButton.setOnClickListener(view -> {
            Intent intent = new Intent(ListOfThingsActivity.this, AddEditThingActivity.class);
            intent.putExtra(I_WANT_TO, ADD_NEW_THING);
            addEditActivityResultLauncher.launch(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(ListOfThingsActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_item)
                        .addSwipeRightActionIcon(R.drawable.ic_edit)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeRightLabel("Edit")
                        .create().decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        Things thingToRemove = (Things) adapter.getThingAt(viewHolder.getAdapterPosition());
                        String objectType = thingToRemove.getObjectType();

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListOfThingsActivity.this, R.style.HaSZErrorDialogTheme);
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        deleteTheThing();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                        break;
                                }
                            }

                            private void deleteTheThing() {
                                thingsViewModel.deleteThing(ListOfThingsActivity.this, (Things) adapter.getThingAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(ListOfThingsActivity.this, "Item deleted (along with all the items that it contained)", Toast.LENGTH_LONG).show();
                            }
                        };

                        String aVeryImportamtQuestion = "This is cascade delete - all items under this item will be removed.\n\nDo you wish to continue?";
                        switch (objectType) {
                            case ART_PERIOD_STRING:
                                aVeryImportamtQuestion = "This is cascade delete. Do you want to delete this Art Period and all its Movements and Artwork flashcards?\n\nYou will not be able to undo this operation!";
                                break;
                            case MOVEMENT_STRING:
                                aVeryImportamtQuestion = "This is cascade delete. Do you want to delete this Movement and all its Artwork flashcards?\n\nYou will not be able to undo this operation!";
                                break;
                            case TYPE_STRING:
                                aVeryImportamtQuestion = "This is cascade delete. Do you want to delete this Type of Artwork and all its Artwork flashcards?\n\nYou will not be able to undo this operation!";
                                break;
                        }
                        alertBuilder
                                .setTitle("Cascade delete")
                                .setMessage(aVeryImportamtQuestion).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                        break;

                    case ItemTouchHelper.RIGHT:
                        Things things = (Things) adapter.getThingAt(viewHolder.getAdapterPosition());
                        Intent intent = prepareIntentForEditing(things);
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        ListOfThingsActivity.this.startActivity(intent);
                        break;
                }

            }


        }).attachToRecyclerView(recyclerView);
    }

    private Intent prepareIntentForEditing(Things thing) {
        Intent intent = new Intent(ListOfThingsActivity.this, AddEditThingActivity.class);
        intent.putExtra(THING_ID, thing.getId());
        intent.putExtra(THING_NAME, thing.getName());
        intent.putExtra(THING_TYPE, thing.getObjectType());
        intent.putExtra(THING_DATING, thing.getDating());
        intent.putExtra(THING_LOCATION, thing.getLocation());
        intent.putExtra(THING_PARENT_PERIOD_ID, thing.getParentPeriodId());
        intent.putExtra(TRIVIA, thing.getFunFact());
        intent.putExtra(I_WANT_TO, EDIT_A_THING);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }
}
