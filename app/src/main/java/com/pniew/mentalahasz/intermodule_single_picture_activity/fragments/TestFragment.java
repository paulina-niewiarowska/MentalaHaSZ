package com.pniew.mentalahasz.intermodule_single_picture_activity.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.intermodule_single_picture_activity.LearnShowAddActivity;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class TestFragment extends Fragment {

    private TestViewModel viewModel;

    ImageButton reloadButton;
    ImageButton doneButton;

    EditText titleEditText;
    EditText authorEditText;
    EditText locationEditText;
    EditText datingEditText;
    EditText movementEditText;

    TextView titleEditTextRevealed;
    TextView authorEditTextRevealed;
    TextView locationEditTextRevealed;
    TextView datingEditTextRevealed;
    TextView movementEditTextRevealed;

    LiveData<Movement> movementLiveData;

    boolean result;


    boolean finishedAnimating;
    boolean animatingTitle;
    boolean animatingAuthor;
    boolean animatingLocation;
    boolean animatingDating;
    boolean animatingMovement;
    boolean answersShown;



    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_test_fragment, container, false);

        viewModel = new ViewModelProvider(this).get(TestViewModel.class);

        reloadButton = v.findViewById(R.id.button_reload);
        doneButton = v.findViewById(R.id.button_confirm);

        titleEditText = v.findViewById(R.id.test_picture_name);
        authorEditText = v.findViewById(R.id.test_picture_author);
        locationEditText = v.findViewById(R.id.test_picture_location);
        datingEditText = v.findViewById(R.id.test_picture_dating);
        movementEditText = v.findViewById(R.id.test_movement);

        titleEditTextRevealed = v.findViewById(R.id.test_picture_name_revealed);
        authorEditTextRevealed = v.findViewById(R.id.test_picture_author_revealed);
        locationEditTextRevealed = v.findViewById(R.id.test_picture_location_revealed);
        datingEditTextRevealed = v.findViewById(R.id.test_picture_dating_revealed);
        movementEditTextRevealed = v.findViewById(R.id.test_movement_revealed);


        answersShown = false;

        animatingTitle = false;
        animatingAuthor = false;
        animatingLocation = false;
        animatingDating = false;
        animatingMovement = false;
        finishedAnimating = true;

        Activity act = this.getActivity();

        // take data from parent: current picture id ====================================================
        viewModel.setCurrentPictureId(getArguments().getInt(PICTURE_ID));

        // set everything in the viemodel so we can compare it later ====================================


        // todo: if there is no picture with this id, show message
        viewModel.getPictureById().observe(getViewLifecycleOwner(), new Observer<Picture>() {
            @Override
            public void onChanged(Picture picture) {
                viewModel.setNameString(picture.getPictureName());
                titleEditTextRevealed.setText(picture.getPictureName());
                viewModel.setAuthorString(picture.getPictureAuthor());
                authorEditTextRevealed.setText(picture.getPictureAuthor());
                viewModel.setDatingString(picture.getPictureDating());
                datingEditTextRevealed.setText(picture.getPictureDating());
                viewModel.setLocationString(picture.getPictureLocation());
                locationEditTextRevealed.setText(picture.getPictureLocation());
                if(picture.getPictureMovement() == null) {
                    viewModel.setHasMovement(false);
                }
                if(picture.getPictureMovement() != null) {
                    viewModel.setHasMovement(true);
                    viewModel.setMovementId(picture.getPictureMovement());
                    viewModel.getMovementById().observe(getViewLifecycleOwner(), new Observer<Movement>() {
                        @Override
                        public void onChanged(Movement movement) {
                            viewModel.setMovementString(movement.getMovementName());
                            movementEditTextRevealed.setText(movement.getMovementName());
                            movementEditText.setEnabled(true);
                        }
                    });
                }

            }
        });

        // reload answers / clear all fields ============================================================

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEditText.setText(null);
                authorEditText.setText(null);
                locationEditText.setText(null);
                datingEditText.setText(null);
                movementEditText.setText(null);
            }
        });

        // save answers and remove the picture from the test list =======================================
        // also go to the next picture

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answersShown) {
                    result = viewModel.isThatCorrect(titleEditText.getText().toString(),
                            authorEditText.getText().toString(),
                            locationEditText.getText().toString(),
                            datingEditText.getText().toString(),
                            movementEditText.getText().toString());

                    reloadButton.setClickable(false);
                    reloadButton.setEnabled(false);

                    if (titleEditText.getText().toString().equals(viewModel.getNameString())) {
                        titleEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.good_text_view_background));
                    } else {
                        titleEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.error_text_view_background));
                    }
                    titleEditText.setFocusable(false);

                    if (authorEditText.getText().toString().equals(viewModel.getAuthorString())) {
                        authorEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.good_text_view_background));
                    } else {
                        authorEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.error_text_view_background));
                    }
                    authorEditText.setFocusable(false);

                    if (locationEditText.getText().toString().equals(viewModel.getLocationString())) {
                        locationEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.good_text_view_background));
                    } else {
                        locationEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.error_text_view_background));
                    }
                    locationEditText.setFocusable(false);

                    if (datingEditText.getText().toString().equals(viewModel.getDatingString())) {
                        datingEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.good_text_view_background));
                    } else {
                        datingEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.error_text_view_background));
                    }
                    datingEditText.setFocusable(false);

                    if(viewModel.hasMovement) {
                        if (movementEditText.getText().toString().equals(viewModel.getMovementString())) {
                            movementEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.good_text_view_background));
                        } else {
                            movementEditText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.error_text_view_background));
                        }
                        movementEditText.setFocusable(false);
                    }

                    answersShown = true;

                    // swipe the card listeners =====================================================================
                    setListeners(v);
                } else {
                    if (act instanceof LearnShowAddActivity) {
                        ((LearnShowAddActivity) act).testFinishedGetNextOne(result);
                    }
                }
            }

        });


        return  v;
    } // end of onCreate method =============================================================================

    private void setListeners(View v) {
        titleEditText.setOnClickListener(anonymousMethod -> {
            titleEditTextRevealed.setText(viewModel.getNameString());
            flipTheCard(v.getContext(), titleEditText, titleEditTextRevealed);
        });
        titleEditTextRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), titleEditTextRevealed, titleEditText);
        });

        authorEditText.setOnClickListener(anonymousMethod -> {
            authorEditTextRevealed.setText(viewModel.getAuthorString());
            flipTheCard(v.getContext(), authorEditText, authorEditTextRevealed);
        });
        authorEditTextRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), authorEditTextRevealed, authorEditText);
        });

        datingEditText.setOnClickListener(anonymousMethod -> {
            datingEditTextRevealed.setText(viewModel.getDatingString());
            flipTheCard(v.getContext(), datingEditText, datingEditTextRevealed);
        });
        datingEditTextRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), datingEditTextRevealed, datingEditText);
        });

        locationEditText.setOnClickListener(anonymousMethod -> {
            locationEditTextRevealed.setText(viewModel.getLocationString());
            flipTheCard(v.getContext(), locationEditText, locationEditTextRevealed);
        });
        locationEditTextRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), locationEditTextRevealed, locationEditText);
        });

        movementEditText.setOnClickListener(anonymousMethod -> {
            movementEditTextRevealed.setText(viewModel.getMovementString());
            flipTheCard(v.getContext(), movementEditText, movementEditTextRevealed);
        });
        movementEditTextRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), movementEditTextRevealed, movementEditText);
        });
    }


    private boolean canIFlip(View toHide) {
        if ((toHide == titleEditText|| toHide ==  titleEditTextRevealed) && !animatingTitle) {
            animatingTitle = true;
            return true;
        } else if ((toHide == authorEditText || toHide == authorEditTextRevealed) && !animatingAuthor) {
            animatingAuthor = true;
            return true;
        } else if ((toHide == locationEditText || toHide == locationEditTextRevealed) && !animatingLocation) {
            animatingLocation = true;
            return true;
        } else if ((toHide == datingEditText || toHide == datingEditTextRevealed) && !animatingDating) {
            animatingDating = true;
            return true;
        } else if ((toHide == movementEditText || toHide == movementEditTextRevealed) && !animatingMovement) {
            animatingMovement = true;
            return true;
        }
        return false;
    }
    private void flipTheCard(Context context, View toHide, View toShow) {
        if(canIFlip(toHide)) {
            float scale = (context.getResources().getDisplayMetrics().density) / 2;
            float cameraDist = 8000 * scale;
            toHide.setCameraDistance(cameraDist);
            toShow.setCameraDistance(cameraDist);
            toShow.setVisibility(View.VISIBLE);
            AnimatorSet animatorFlipIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_in);
            AnimatorSet animatorFlipOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
            animatorFlipOut.setTarget(toHide);
            animatorFlipIn.setTarget(toShow);

            animatorFlipOut.start();
            animatorFlipIn.start();


            animatorFlipIn.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toHide.setVisibility(View.GONE);
                    if (toHide == titleEditText|| toHide ==  titleEditTextRevealed) {
                        animatingTitle = false;
                    } else if (toHide == authorEditText || toHide == authorEditTextRevealed) {
                        animatingAuthor = false;
                    } else if (toHide == locationEditText || toHide == locationEditTextRevealed) {
                        animatingLocation = false;
                    } else if (toHide == datingEditText || toHide == datingEditTextRevealed) {
                        animatingDating = false;
                    } else if (toHide == movementEditText || toHide == movementEditTextRevealed) {
                        animatingMovement = false;
                    }
                    super.onAnimationEnd(animation);
                }
            });
        }
    }
}