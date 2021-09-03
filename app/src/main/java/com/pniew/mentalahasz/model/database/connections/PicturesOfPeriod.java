package com.pniew.mentalahasz.model.database.connections;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Picture;

import java.util.List;

public class PicturesOfPeriod {
    @Embedded public ArtPeriod artPeriod;
    @Relation(
            parentColumn = "artPeriodId",
            entityColumn = "pictureArtPeriod",
            entity = Picture.class
    )
    public List<Picture> picturesOfPeriod;
}
