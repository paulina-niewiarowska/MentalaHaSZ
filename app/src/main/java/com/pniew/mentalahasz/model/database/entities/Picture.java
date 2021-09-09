package com.pniew.mentalahasz.model.database.entities;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.pniew.mentalahasz.repository.AThing;

import java.util.Objects;

@Entity( foreignKeys =
        {@ForeignKey(entity = ArtPeriod.class, parentColumns = "artPeriodId", childColumns = "pictureArtPeriod", onDelete = ForeignKey.CASCADE),
         @ForeignKey(entity = Movement.class, parentColumns = "movementId", childColumns = "pictureMovement", onDelete = ForeignKey.CASCADE),
         @ForeignKey(entity = Type.class, parentColumns = "typeId", childColumns = "pictureType", onDelete = ForeignKey.CASCADE)},
        indices = {@Index("pictureArtPeriod"), @Index("pictureMovement"), @Index("pictureType")})

public class Picture implements AThing {

    @PrimaryKey(autoGenerate = true)
    private int pictureId;

    private String picturePath;
    private String pictureName;
    private String pictureAuthor;
    private String pictureDating;
    private String pictureLocation;
    private int pictureArtPeriod;
    private Integer pictureMovement;
    private int pictureType;
    @Nullable
    private String pictureFunFact;
    @Nullable
    private int pictureKnowledgeDegree;

    public Picture(String picturePath, String pictureName, String pictureAuthor, String pictureDating, String pictureLocation) {
        this.picturePath = picturePath;
        this.pictureName = pictureName;
        this.pictureAuthor = pictureAuthor;
        this.pictureDating = pictureDating;
        this.pictureLocation = pictureLocation;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureAuthor() {
        return pictureAuthor;
    }

    public void setPictureAuthor(String pictureAuthor) {
        this.pictureAuthor = pictureAuthor;
    }

    public String getPictureDating() {
        return pictureDating;
    }

    public void setPictureDating(String pictureDating) {
        this.pictureDating = pictureDating;
    }

    @org.jetbrains.annotations.Nullable
    public String getPictureFunFact() { return pictureFunFact; }

    public void setPictureFunFact(String pictureFunFact) { this.pictureFunFact = pictureFunFact; }

    public int getPictureKnowledgeDegree() {
        return pictureKnowledgeDegree;
    }

    public void setPictureKnowledgeDegree(int pictureKnowledgeDegree) {
        this.pictureKnowledgeDegree = pictureKnowledgeDegree;
    }

    public String getPictureLocation() {
        return pictureLocation;
    }

    public void setPictureLocation(String pictureLocation) {
        this.pictureLocation = pictureLocation;
    }

    public int getPictureArtPeriod() {
        return pictureArtPeriod;
    }

    public void setPictureArtPeriod(int pictureArtPeriod) {
        this.pictureArtPeriod = pictureArtPeriod;
    }

    public Integer getPictureMovement() {
        return pictureMovement;
    }

    public void setPictureMovement(Integer pictureMovement) {
        this.pictureMovement = pictureMovement;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    @Override
    public int getId() {
        return getPictureId();
    }

    @Override
    public boolean equals(AThing t) {
        if(t == null) return false;
        if (this == t) return true;
        if (t == null || getClass() != t.getClass()) return false;
        Picture picture = (Picture) t;
        if(getPictureMovement() != null && picture.getPictureMovement() != null) {
            return (getPictureId() == picture.getPictureId() &&
                    getPictureArtPeriod() == picture.getPictureArtPeriod() &&
                    getPictureMovement().equals(picture.getPictureMovement()) &&
                    getPictureType() == picture.getPictureType() &&
                    getPicturePath().equals(picture.getPicturePath()) &&
                    getPictureName().equals(picture.getPictureName()) &&
                    getPictureAuthor().equals(picture.getPictureAuthor()) &&
                    getPictureDating().equals(picture.getPictureDating()) &&
                    getPictureLocation().equals(picture.getPictureLocation()) &&
                    getPictureKnowledgeDegree() == picture.getPictureKnowledgeDegree() &&
                    getPictureFunFact() != null) && (picture.getPictureFunFact() != null && getPictureFunFact().equals(picture.getPictureFunFact()));
        } else {
            return (getPictureId() == picture.getPictureId() &&
                    getPictureArtPeriod() == picture.getPictureArtPeriod() &&
                    getPictureType() == picture.getPictureType() &&
                    getPicturePath().equals(picture.getPicturePath()) &&
                    getPictureName().equals(picture.getPictureName()) &&
                    getPictureAuthor().equals(picture.getPictureAuthor()) &&
                    getPictureDating().equals(picture.getPictureDating()) &&
                    getPictureLocation().equals(picture.getPictureLocation()) &&
                    getPictureKnowledgeDegree() == picture.getPictureKnowledgeDegree() &&
                    getPictureFunFact() != null) && (picture.getPictureFunFact() != null && getPictureFunFact().equals(picture.getPictureFunFact()));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPictureId(), getPicturePath(), getPictureName(), getPictureAuthor(), getPictureDating(), getPictureLocation(), getPictureArtPeriod(), getPictureMovement(), getPictureType());
    }
}
