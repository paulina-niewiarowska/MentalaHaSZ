package com.pniew.mentalahasz.model.database.connections;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.database.entities.Type;

import java.util.List;

public class PicturesOfType {
    @Embedded public Type type;
    @Relation(
            parentColumn = "typeId",
            entityColumn = "pictureType",
            entity = Picture.class
    )
    public List<Picture> picturesOfType;
}
