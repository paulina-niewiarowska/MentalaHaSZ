package com.pniew.mentalahasz.thetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.utils.CallsStringsIntents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TestViewModel viewModel;

    private ExpandableListView list;
    private ArrayList<TestArtPeriod> artPeriods;
    private MyExpandableListAdapter adapter;
    ArrayList<Integer> movementsIds;
    ArrayList<Integer> artPeriodsIds;
    ArrayList<Integer> picturesIds;

    private Button buttonConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        LifecycleOwner owner = TestActivity.this;
        artPeriods = new ArrayList<>();
        list = findViewById(R.id.test_expandable_list);
        adapter = new MyExpandableListAdapter(this, list, artPeriods);

        buttonConfirm = findViewById(R.id.test_choose_button_confirm);

        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(TestViewModel.class);
        viewModel.getArtPeriods().observe(owner, new Observer<List<ArtPeriod>>() {
            @Override
            public void onChanged(List<ArtPeriod> artPeriodsList) {
                for (int i = 0; i < artPeriodsList.size(); i++) {
                    TestArtPeriod artPeriod = new TestArtPeriod();
                    artPeriod.id = artPeriodsList.get(i).getArtPeriodId();
                    artPeriod.artPeriodName = artPeriodsList.get(i).getArtPeriodName();
                    ArrayList<TestMovement> testMovementArrayList = new ArrayList<>();
                    int id = artPeriodsList.get(i).getArtPeriodId();

                    viewModel.getMovements(id).observe(owner, new Observer<List<Movement>>() {
                        @Override
                        public void onChanged(List<Movement> movements) {
                            for (int i = 0; i < movements.size(); i++) {
                                TestMovement movement = new TestMovement();
                                movement.id = movements.get(i).getMovementId();
                                movement.name = movements.get(i).getMovementName();
                                testMovementArrayList.add(movement);
                            }
                            artPeriod.subList = testMovementArrayList;
                            artPeriods.add(artPeriod);
                            adapter.notifyDataSetChanged();

                            viewModel.getMovements(id).removeObservers(owner);
                        }
                    });

                }
                viewModel.getArtPeriods().removeObservers(owner);
            }
        });

        doTheRest();

        // on click button "confirm" listener =======================================================================


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementsIds = new ArrayList<>();
                artPeriodsIds = new ArrayList<>();
                ArrayList<TestArtPeriod> testList = adapter.getArtPeriodsArrayList();


                for (int i = 0; i < artPeriods.size(); i++) {

                    TestArtPeriod testArtPeriod = testList.get(i);
                    if (testArtPeriod.selected && testArtPeriod.subList.isEmpty()) {
                        artPeriodsIds.add(testArtPeriod.id);
                    }

                    for (int j = 0; j < testArtPeriod.subList.size(); j++) {
                        if (testArtPeriod.subList.get(j).selected) {
                            movementsIds.add(testArtPeriod.subList.get(j).id);
                        }
                    }
                }

                viewModel.getBackgroundExecutor().execute(() -> {
                    List<Integer> pictureIdsList = viewModel.getPictureIdsByArtPeriodOrMovementIds(artPeriodsIds, movementsIds);
                    Collections.shuffle(pictureIdsList);
                    int[] picturesIdsArray = pictureIdsList.stream().mapToInt(i -> i).toArray();
                    CallsStringsIntents.startTestPictureActivity(TestActivity.this, picturesIdsArray);
                    TestActivity.this.finish();
                });
            }
        });

    } // <--- end of onCreate() method ======================================================================

    private void doTheRest() {


        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

//                TestArtPeriod artPeriod = artPeriods.get(groupPosition);
//                artPeriod.selected = !artPeriods.get(groupPosition).selected;
//                artPeriods.set(groupPosition, artPeriod);

//                v.setBackgroundColor(Color.parseColor("#FFA63126"));

                return false;
            }
        });

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                v.setBackgroundColor(Color.parseColor("#FFA63126"));

                TestArtPeriod artPeriod;
                TestMovement movement;

                movement = artPeriods.get(groupPosition).subList.get(childPosition);
                movement.selected = !artPeriods.get(groupPosition).subList.get(childPosition).selected;
                artPeriods.get(groupPosition).subList.set(childPosition, movement);

                boolean selected = false;
                for (int i = 0; i < artPeriods.get(groupPosition).subList.size() && !selected; i++) {
                    selected = artPeriods.get(groupPosition).subList.get(i).selected;
                }
                artPeriod = artPeriods.get(groupPosition);
                artPeriod.selected = selected;
                artPeriods.set(groupPosition, artPeriod);

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, movement.selected);

                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }
}