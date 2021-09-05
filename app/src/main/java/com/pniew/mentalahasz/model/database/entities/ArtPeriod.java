package com.pniew.mentalahasz.model.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.pniew.mentalahasz.repository.AThing;

import java.util.Objects;

@Entity(indices = {@Index("artPeriodId")})
public class ArtPeriod implements AThing {

    @PrimaryKey(autoGenerate = true)
    private int artPeriodId;
    private String artPeriodName;
    private String artPeriodDating;
    private String artPeriodFunFact;

    @Override
    public boolean equals(AThing t) {
        if (this == t) return true;
        if (t == null || getClass() != t.getClass()) return false;
        ArtPeriod artPeriod = (ArtPeriod) t;
        return getArtPeriodId() == artPeriod.getArtPeriodId() &&
                Objects.equals(getArtPeriodName(), artPeriod.getArtPeriodName()) &&
                Objects.equals(getArtPeriodDating(), artPeriod.getArtPeriodDating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArtPeriodId(), getArtPeriodName(), getArtPeriodDating());
    }


    public ArtPeriod(String artPeriodName, String artPeriodDating) {
        this.artPeriodName = artPeriodName;
        this.artPeriodDating = artPeriodDating;
    }

    public int getArtPeriodId() { return artPeriodId; }
    public void setArtPeriodId(int artPeriodId) { this.artPeriodId = artPeriodId; }

    public String getArtPeriodName() { return artPeriodName; }
    public void setArtPeriodName(String artPeriodName) { this.artPeriodName = artPeriodName; }

    public String getArtPeriodDating() { return artPeriodDating; }
    public void setArtPeriodDating(String artPeriodDating) { this.artPeriodDating = artPeriodDating; }

    public String getArtPeriodFunFact() { return artPeriodFunFact; }
    public void setArtPeriodFunFact(String artPeriodFunFact) {
        this.artPeriodFunFact = artPeriodFunFact;
    }

    @Override
    public String toString() {
        return artPeriodName;
    }

    @Override
    public int getId() {
        return getArtPeriodId();
    }
}
