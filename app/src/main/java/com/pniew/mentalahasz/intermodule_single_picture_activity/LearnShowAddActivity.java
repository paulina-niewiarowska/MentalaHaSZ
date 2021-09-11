package com.pniew.mentalahasz.intermodule_single_picture_activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.intermodule_single_picture_activity.fragments.AddEditFragment;
import com.pniew.mentalahasz.intermodule_single_picture_activity.fragments.AddEditViewModel;
import com.pniew.mentalahasz.intermodule_single_picture_activity.fragments.LearnFragment;
import com.pniew.mentalahasz.intermodule_single_picture_activity.fragments.TestFragment;
import com.pniew.mentalahasz.utils.OnSwipeListener;
import com.pniew.mentalahasz.utils.Permissions;
import com.pniew.mentalahasz.utils.RealPathUtil;
import com.pniew.mentalahasz.model.database.entities.Picture;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;


public class LearnShowAddActivity extends AppCompatActivity {


    // member variables ============================================================================
    LifecycleOwner thisActivity;

    Fragment currentFragment;
    RelativeLayout fragmentHolder;
    View cover;

    private LearnShowAddViewModel viewModel;
    private AddEditViewModel viewModelAddEdit;
    private ImageView imageView;
    private EditText triviaEditText;
    private RelativeLayout viewForImageview;
    Button buttonAddPictureFile;
    Button buttonChangePictureFile;
    Bundle bundleAddNew;
    Bundle bundleEdit;

    AlertDialog.Builder alert;

    boolean animateLeft;
    boolean animateRight;
    boolean animateFragmentLeft;
    boolean animateFragmentRight;

    private boolean alertShown;

    private boolean triviaEdited;

    private int toRemove;

    public OnSwipeListener swipeListener;

    Intent picture;
    int width;

    // activity for result - result code =======================================================================
    /* this activity is meant to load pictures from user's disk. In result we get intent with selected
    * picture's data. So, what we have to do is to put the picture on the screen */

    ActivityResultLauncher<Intent> addPictureActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bitmap bitmap = null;
                    Uri selectedImage = result.getData().getData();
                    try {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                    selectedImage);
                        } else {
                            ImageDecoder.Source source = ImageDecoder.createSource(this.
                                    getContentResolver(), selectedImage);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }
                        String path = RealPathUtil.getRealPath(this, selectedImage);
                        getContentResolver().takePersistableUriPermission(selectedImage,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);

                        bundleAddNew.putString(PICTURE_PATH, path);
                        bundleEdit.putBoolean(PICTURE_CHANGED, true);
                        bundleEdit.putString(PICTURE_PATH, path);

                        buttonAddPictureFile.setVisibility(View.GONE);
                        buttonChangePictureFile.setVisibility(View.VISIBLE);
                        buttonChangePictureFile.getBackground().setAlpha(128);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    //ONCREATE starts here ============================================================================================================== on Create() =======
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_show_add_picture);

        // set variables ---------------------------------------------------------------------------
        thisActivity = LearnShowAddActivity.this;

        animateLeft = false;
        animateRight = false;
        animateFragmentLeft = false;
        animateFragmentRight = false;
        toRemove = 0;

        alertShown = false;
        triviaEdited = false;

        viewModel = new ViewModelProvider(this).get(LearnShowAddViewModel.class);
        viewModelAddEdit = new ViewModelProvider(this).get(AddEditViewModel.class);
        viewForImageview = findViewById(R.id.view_for_imageview);
        imageView = findViewById(R.id.imageView_picture);
        fragmentHolder = findViewById(R.id.fragment_holder_learnaddedit);
        cover = findViewById(R.id.add_edit_cover);

        // get calling thing from intent, set everything in the vmodel -----------------------------
        Intent dataFromCaller = getIntent();
        viewModel.setCalledBy(dataFromCaller.getIntExtra(CALLED_BY, 0));
        viewModel.setIdOfCallingThing(dataFromCaller.getIntExtra(CHILD_OF, 0));
        viewModel.setParentPeriodId(dataFromCaller.getIntExtra(PARENT_PERIOD_ID, 0));
        viewModel.setIWantTo(dataFromCaller.getIntExtra(I_WANT_TO, -1));
        viewModel.setPictureId(dataFromCaller.getIntExtra(PICTURE_ID, 0));
        viewModel.setPictureById();
        if(dataFromCaller.getIntArrayExtra(PICTURES_IDS_ARRAY) != null) {
            viewModel.setPicturesIdsArray(dataFromCaller.getIntArrayExtra(PICTURES_IDS_ARRAY));
        }


        // prepare bundles for fragments -----------------------------------------------------------
        bundleAddNew = new Bundle();
        bundleAddNew.putInt(PARENT_PERIOD_ID, viewModel.getParentPeriodId());
        bundleAddNew.putInt(CALLED_BY, viewModel.getCalledBy());
        bundleAddNew.putInt(CHILD_OF, viewModel.getIdOfCallingThing());

        bundleEdit = new Bundle();
        bundleEdit.putInt(CALLED_BY, viewModel.getCalledBy());
        bundleEdit.putInt(CHILD_OF, viewModel.getIdOfCallingThing());


        // width for animation ---------------------------------------------------------------------
        width = findViewById(R.id.layout_learn_show_add).getWidth();
        width = (int) (width * 0.7);


        AddEditFragment addEditFragment;

        // which fragment to load ------------------------------------------------------------------

        switch (viewModel.getIWantTo()) {
            case ADD_NEW_PICTURE: {
                addNewPictureCall();
                break;
            }
            case EDIT_A_PICTURE: {
                editPictureCall();
                break;
            }
            case LEARN:{
                learnPictureCall();
                break;
            }
            case TEST_ME: {
                viewModel.setFirstToTest();
                testPictureCall();
                break;
            }
        }

        // we swipe current image ------------------------------------------------------------------
        if(viewModel.getIWantTo() == LEARN) {

            swipeListener = new OnSwipeListener(this) {
                @Override
                public void onSwipeRight() {
                    swipeRight();
                }

                @Override
                public void onSwipeLeft() {
                    swipeLeft();
                }
            };
            imageView.setOnTouchListener(swipeListener);
        }


    } // end of ON CREATE ================================================================================================================================

    private void prepareAlertDialog() {
        alert = new AlertDialog.Builder(LearnShowAddActivity.this, R.style.TriviaDialogTheme);
        triviaEditText = new EditText(LearnShowAddActivity.this);
        triviaEditText.setText(viewModel.getTriviaText());
        alert.setView(triviaEditText);
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                triviaEditText = null;
                alertShown = false;
            }
        });

        if(viewModel.getIWantTo() == LEARN || viewModel.getIWantTo() == TEST_ME) {
            triviaEditText.setClickable(false);
            triviaEditText.setFocusable(false);
            alert.setTitle("Fun Fact:");
        } else {
            triviaEditText.setClickable(true);
            triviaEditText.setFocusable(true);
            if(viewModel.getIWantTo() == EDIT_A_PICTURE && !(viewModel.getTriviaText() == null) && !(viewModel.getTriviaText().isEmpty())) {
                alert.setTitle("Edit Fun Fact");
            } else {
                alert.setTitle("Insert new Fun Fact");
            }
            alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     String trivia = triviaEditText.getText().toString();
                     viewModel.setTriviaText(trivia);
                     if(currentFragment instanceof AddEditFragment) {
                         ((AddEditFragment) currentFragment).setTrivia(trivia);
                     }
                     triviaEdited = true;
                     alertShown = false;
                }
            });
            alert.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertShown = false;
                }
            });
        }
    }

    //calls that create bundles and put them to fragments ==========================================

    public void testPictureCall() {
        setTitle("Fill the Fields:");

        viewModel.setIWantTo(TEST_ME);
        pictureShow();
        Bundle bundle = new Bundle();
        bundle.putInt(I_WANT_TO, TEST_ME);
        bundle.putInt(PICTURE_ID, viewModel.getPictureId());

        replaceFragmentWithAnimation(TestFragment.newInstance(), bundle);
    }

    public void learnPictureCall() {
        setTitle("Do you know this picture?");

        viewModel.setIWantTo(LEARN);
        pictureShow();
        Bundle bundle = new Bundle();
        bundle.putInt(I_WANT_TO, LEARN);
        bundle.putInt(PICTURE_ID, viewModel.getPictureId());

        replaceFragmentWithAnimation(LearnFragment.newInstance(), bundle);
    }

    public void editPictureCall() {
        setTitle("Edit a Picture");

        viewModel.setIWantTo(EDIT_A_PICTURE);
        pictureEditable();
        bundleEdit.putInt(I_WANT_TO, EDIT_A_PICTURE);
        bundleEdit.putInt(PICTURE_ID, viewModel.getPictureId());
        bundleEdit.putBoolean(PICTURE_CHANGED, false);

        runAddEditFragment(bundleEdit);
    }

    private void addNewPictureCall() {

        setTitle("Add New Picture");

        viewModel.setIWantTo(ADD_NEW_PICTURE);
        pictureAddable();
        bundleAddNew.putInt(I_WANT_TO, ADD_NEW_PICTURE);

        runAddEditFragment(bundleAddNew);
    }

    //methods to create instances of fragments and to put the instances into the fragment-container field

    private void runAddEditFragment(Bundle bundle) {

        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).detach(currentFragment).commit();
        }
        currentFragment = null;
        currentFragment = AddEditFragment.newInstance();
        currentFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_add_edit_learn_test, currentFragment).commit(); //addEditFragment

    }

    private void replaceFragmentWithAnimation(Fragment newFragment, Bundle bundle) {

        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).detach(currentFragment).commit();
        }

        currentFragment = null;
        currentFragment = newFragment;
        currentFragment.setArguments(bundle);

        if (animateFragmentRight) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).add(R.id.fragment_container_add_edit_learn_test, currentFragment).commit();
            animateFragmentRight = false;
        } else if (animateFragmentLeft) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).add(R.id.fragment_container_add_edit_learn_test, currentFragment).commit();
            animateFragmentLeft = false;
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_add_edit_learn_test, currentFragment).commit();
        }
    }

    //prepare buttons that allow to add ir change the picture
    private void pictureEditable() {

        buttonAddPictureFile = findViewById(R.id.button_choose_picture_file);
        buttonChangePictureFile = findViewById(R.id.button_change_picture_file);

        buttonChangePictureFile.setVisibility(View.VISIBLE);
        buttonAddPictureFile.setVisibility(View.GONE);
    }

    private void pictureAddable() {

        buttonAddPictureFile = findViewById(R.id.button_choose_picture_file);
        buttonChangePictureFile = findViewById(R.id.button_change_picture_file);

        buttonChangePictureFile.setVisibility(View.GONE);
        buttonAddPictureFile.setVisibility(View.VISIBLE);
    }


    //start activity for result: take picture from user. Method to be applied after this is to be
    //found at the top of the class, where we have to register the activity for result.
    public void prepareStartChoosePictureIntent(View v) {
        Permissions.showPhoneStatePermission(LearnShowAddActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Permissions.PERMISSION_READ_EXTERNAL_STORAGE,
                getResources().getString(R.string.add_picture_needs_picture));
    }
    private void continueWithLoadingAPicture(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        addPictureActivityLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    //show picture in the imageView. Id is taken from viewModel. ===============================================================
    private void pictureShow(){
        viewModel.getPictureById().observe(thisActivity, new Observer<Picture>() {
            @Override
            public void onChanged(Picture picture) {
                viewModel.setPath(picture.getPicturePath());
                viewModel.setTriviaText(picture.getPictureFunFact());

                Glide.with(LearnShowAddActivity.this)
                        .load(viewModel.getPath())
                        .into(imageView);

                if (animateRight) {
                    viewForImageview.setTranslationX(-width);
                    viewForImageview.animate().translationX(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> animateRight = false);
                }

                if (animateLeft) {
                    viewForImageview.setTranslationX(width);
                    viewForImageview.animate().translationX(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> animateLeft = false);
                }
                viewModel.getPictureById().removeObservers(thisActivity);
            }
        });

    }

    //methods to change viewModel.pictureId so we can work on it =========================================================
    public void nextPictureLearn(){
        viewModel.getOneAfter(this);
        if (toRemove != 0) {
            viewModel.updateArray(toRemove);
            toRemove = 0;
        }
        learnPictureCall();
    }

    public void previousPictureLearn(){
        viewModel.getOneBefore(this);
        learnPictureCall();
    }

    public void nextPictureTest(){
        viewModel.getOneAfter(this);
        if (toRemove != 0) {
            viewModel.updateArray(toRemove);
            toRemove = 0;
        }
        testPictureCall();
    }

    // animations and swiping ===============================================================================================

    public void swipeRight() {
        if (viewModel.getIWantTo() == LEARN && (!animateLeft || !animateRight)) {

            animateRight = true;
            animateLeft = false;
            animateFragmentRight = true;
            animateFragmentLeft = false;

            width = findViewById(R.id.layout_learn_show_add).getWidth();
            viewForImageview.animate().translationX(width).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(this::doWhatYouGottaDoRight);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).detach(currentFragment).commit(); //learnFragment
        }
    }

    public void swipeLeft() {
        if ((viewModel.getIWantTo() == LEARN || viewModel.getIWantTo() == TEST_ME)&& (!animateLeft || !animateRight)) {
            animateLeft = true;
            animateRight = false;
            animateFragmentLeft = true;
            animateFragmentRight = false;

            width = findViewById(R.id.layout_learn_show_add).getWidth();
            viewForImageview.animate().translationX(-width).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(this::doWhatYouGottaDoLeft);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).detach(currentFragment).commit(); //learnFragment
        }
    }

    private void doWhatYouGottaDoRight(){
        if(viewModel.getIWantTo() == LEARN) {
            previousPictureLearn();
        }
    }

    private void doWhatYouGottaDoLeft(){
        if(viewModel.getIWantTo() == LEARN) {
            nextPictureLearn();
        }
        if (viewModel.getIWantTo() == TEST_ME){
            nextPictureTest();
        }
    }

    // picture added, we want to add new one =========================================================
    public void refreshAddingPicture(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).detach(currentFragment).commit();
        cover.setAlpha(1);
        cover.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(null);
        addNewPictureCall();
        cover.animate().alpha(0).setDuration(1500);
        cover.setVisibility(View.GONE);
    }

    // picture edited, go back to the learn fragment =========================================================

    public void editFinished() {
        buttonChangePictureFile.setVisibility(View.GONE);
        triviaEdited = false;
        Bundle bundle = new Bundle();
        bundle.putInt(I_WANT_TO, LEARN);
        bundle.putInt(PICTURE_ID, viewModel.getPictureId());

        learnPictureCall();
    }

    public void editFinishedChangedParticipation() { //edited but goes away to another art period / movement
        viewModel.setIWantTo(LEARN);
        buttonChangePictureFile.setVisibility(View.GONE);
        toRemove = viewModel.getPictureId();
        swipeLeft();
    }

    // test for this picture finished, next picture ==========================================================

    public void testFinishedGetNextOne(boolean gotPoint){
        viewModel.addPoint(gotPoint);
        if (viewModel.isArrayFinished()) {
            //show result of the test
            new AlertDialog.Builder(this, R.style.HaSZDialogTheme)
                    .setTitle("Test finished.")
                    .setMessage("You've gained " + viewModel.getPoints() + " points out of " + viewModel.getMaxPoints() + " possible.\nDo you want to retake this test?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            retakeTheTest();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            closeTheTest();
                        }
                    })
                    .setIcon(R.drawable.ic_done_button)
                    .show();
        } else {
            toRemove = viewModel.getPictureId();
            swipeLeft();
        }
    }

    private void closeTheTest() {
        LearnShowAddActivity.this.finish();
    }

    private void retakeTheTest() {
        viewModel.reload();
        viewModel.setFirstToTest();
        viewModel.setPictureById();
        toRemove = 0;
        testPictureCall();
    }

    // other ==================================================================================================================

    public void closeThisActivity(){
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the default resource ID of the actionBar's back button
            finish();
        }
        return true;
    }

    public void setUpTriviaView(View view) {
        if (!alertShown && !triviaEdited && !(viewModel.getIWantTo()==ADD_NEW_PICTURE)) {
            viewModel.getPictureById().observe(thisActivity, new Observer<Picture>() {
                @Override
                public void onChanged(Picture picture) {
                    if (!(viewModel.getIWantTo() == ADD_NEW_PICTURE)) {
                        viewModel.setTriviaText(picture.getPictureFunFact());
                    }
                    prepareAlertDialog();
                    triviaEdited = false;

                    alertShown = true;
                    alert.show();

                    viewModel.getPictureById().removeObservers(thisActivity);
                }
            });
        } else if (!alertShown){
            prepareAlertDialog();
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions.PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted!
                continueWithLoadingAPicture();
            } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LearnShowAddActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}