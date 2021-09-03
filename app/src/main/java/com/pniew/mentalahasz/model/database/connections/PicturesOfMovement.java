package com.pniew.mentalahasz.model.database.connections;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;

import java.util.List;

public class PicturesOfMovement {
    @Embedded public Movement movement;
    @Relation(
            parentColumn = "movementId",
            entityColumn = "pictureMovement",
            entity = Picture.class
    )
    public List<Picture> picturesOfMovement;
}
