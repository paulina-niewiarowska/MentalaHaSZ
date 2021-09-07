package com.pniew.mentalahasz.picture_showeditaddtest.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pniew.mentalahasz.R;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.picture_showeditaddtest.LearnShowAddActivity;

import static com.pniew.mentalahasz.utils.CallsStringsIntents.*;

public class LearnFragment extends Fragment {

    LearnViewModel viewModel;

    TextView name;
    TextView nameRevealed;
    TextView author;
    TextView authorRevealed;
    TextView dating;
    TextView datingRevealed;
    TextView location;
    TextView locationRevealed;
    TextView movement;
    TextView movementRevealed;
    LiveData<Movement> movementLiveData;
    ConstraintLayout thisFragment;

    Button buttonIKnewThat;
    Button buttonDidntKnow;
    Button buttonUpdatePicture;

    boolean finishedAnimating;
    boolean animatingTitle;
    boolean animatingAuthor;
    boolean animatingLocation;
    boolean animatingDating;
    boolean animatingMovement;


    public static LearnFragment newInstance() {
        return new LearnFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_learn_fragment, container, false);

        viewModel = new ViewModelProvider(this).get(LearnViewModel.class);

        viewModel.setPictureId(getArguments().getInt(PICTURE_ID));
        thisFragment = v.findViewById(R.id.learn_fragment);

        animatingTitle = false;
        animatingAuthor = false;
        animatingLocation = false;
        animatingDating = false;
        animatingMovement = false;

        movement = v.findViewById(R.id.learn_text_movement);
        movementRevealed = v.findViewById(R.id.learn_text_movement_revealed);


        LiveData<Picture> picture = viewModel.getPictureById();
        // todo: if there is no picture with this id, show message
        picture.observe(getViewLifecycleOwner(), new Observer<Picture>() {
            @Override
            public void onChanged(Picture picture) {
                viewModel.setNameString(picture.getPictureName());
                viewModel.setAuthorString(picture.getPictureAuthor());
                viewModel.setDatingString(picture.getPictureDating());
                viewModel.setLocationString(picture.getPictureLocation());
                if(picture.getPictureMovement() != null) {
                    viewModel.setMovementId(picture.getPictureMovement());
                } else {
                    viewModel.setMovementString(null);
                    movement.setText("-");
                }
                viewModel.setCurrentKnowledgeDegree(picture.getPictureKnowledgeDegree());
                if(viewModel.getMovementId() > 0) {
                    movementLiveData = viewModel.getMovementById(); //we already have id in viewModel so we don't have to pass it
                    movementLiveData.observe(getViewLifecycleOwner(), new Observer<Movement>() {
                        @Override
                        public void onChanged(Movement movement1) {
                            viewModel.setMovementString(movement1.getMovementName());
                            movement.setOnClickListener(anonymousMethod -> {
                                movementRevealed.setText(viewModel.getMovementString());
                                flipTheCard(v.getContext(), movement, movementRevealed);
                            });
                            movementRevealed.setOnClickListener(anonymousMethod -> {
                                flipTheCard(v.getContext(), movementRevealed, movement);
                            });
                        }
                    });
                }
            }
        });

        finishedAnimating = true;



        name = v.findViewById(R.id.learn_text_name);
        nameRevealed = v.findViewById(R.id.learn_text_name_revealed);
        author = v.findViewById(R.id.learn_text_author);
        authorRevealed = v.findViewById(R.id.learn_text_author_revealed);
        dating = v.findViewById(R.id.learn_text_dating);
        datingRevealed = v.findViewById(R.id.learn_text_dating_revealed);
        location = v.findViewById(R.id.learn_text_location);
        locationRevealed = v.findViewById(R.id.learn_text_location_revealed);

        buttonIKnewThat = v.findViewById(R.id.button_i_knew_that);
        buttonDidntKnow = v.findViewById(R.id.button_didnt_know);
        buttonUpdatePicture = v.findViewById(R.id.button_i_want_to_update_picture);

        Activity act = this.getActivity();

        buttonDidntKnow.setOnClickListener(v12 -> {
            viewModel.decreaseKnowledgeDegree();
            if (act instanceof LearnShowAddActivity) {
                ((LearnShowAddActivity) act).swipeLeft();
            }

        });

        buttonIKnewThat.setOnClickListener(v13 -> {
            viewModel.increaseKnowledgeDegree();
            if (act instanceof LearnShowAddActivity) {
                ((LearnShowAddActivity) act).swipeLeft();
            }
        });

//        v1 -> AddEditTestIntentMethods
//                .startEditPictureActivity(getContext(), viewModel.getPictureId())

        buttonUpdatePicture.setOnClickListener( v111 -> {
            if (act instanceof LearnShowAddActivity) {
                ((LearnShowAddActivity) act).editPictureCall();
            }
        });


        name.setOnClickListener(anonymousMethod -> {
            nameRevealed.setText(viewModel.getNameString());
            flipTheCard(v.getContext(), name, nameRevealed);
        });
        nameRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), nameRevealed, name);
        });

        author.setOnClickListener(anonymousMethod -> {
            authorRevealed.setText(viewModel.getAuthorString());
            flipTheCard(v.getContext(), author, authorRevealed);
        });
        authorRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), authorRevealed, author);
        });

        dating.setOnClickListener(anonymousMethod -> {
            datingRevealed.setText(viewModel.getDatingString());
            flipTheCard(v.getContext(), dating, datingRevealed);
        });
        datingRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), datingRevealed, dating);
        });

        location.setOnClickListener(anonymousMethod -> {
            locationRevealed.setText(viewModel.getLocationString());
            flipTheCard(v.getContext(), location, locationRevealed);
        });
        locationRevealed.setOnClickListener(anonymousMethod -> {
            flipTheCard(v.getContext(), locationRevealed, location);
        });



        return v;
    }

    private boolean canIFlip(View toHide) {
        if ((toHide == name|| toHide ==  nameRevealed) && !animatingTitle) {
            animatingTitle = true;
            return true;
        } else if ((toHide == author || toHide == authorRevealed) && !animatingAuthor) {
            animatingAuthor = true;
            return true;
        } else if ((toHide == location || toHide == locationRevealed) && !animatingLocation) {
            animatingLocation = true;
            return true;
        } else if ((toHide == dating || toHide == datingRevealed) && !animatingDating) {
            animatingDating = true;
            return true;
        } else if ((toHide == movement || toHide == movementRevealed) && !animatingMovement) {
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
                    if (toHide == name|| toHide ==  nameRevealed) {
                        animatingTitle = false;
                    } else if (toHide == author || toHide == authorRevealed) {
                        animatingAuthor = false;
                    } else if (toHide == location || toHide == locationRevealed) {
                        animatingLocation = false;
                    } else if (toHide == dating || toHide == datingRevealed) {
                        animatingDating = false;
                    } else if (toHide == movement || toHide == movementRevealed) {
                        animatingMovement = false;
                    }
                    super.onAnimationEnd(animation);
                }
            });
        }
    }
}

