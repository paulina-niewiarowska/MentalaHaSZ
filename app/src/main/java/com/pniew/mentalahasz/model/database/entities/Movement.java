package com.pniew.mentalahasz.model.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.pniew.mentalahasz.repository.AThing;

import java.util.Objects;

@Entity (foreignKeys = {@ForeignKey(entity = ArtPeriod.class, parentColumns = "artPeriodId", childColumns = "movementArtPeriod", onDelete = ForeignKey.CASCADE)},
        indices = {@Index("movementId"), @Index("movementArtPeriod")})
public class Movement implements AThing {
    @PrimaryKey(autoGenerate = true)
    private int movementId;
    private String movementName;
    private String movementDating;
    private String movementLocation;
    private int movementArtPeriod;
    private String movementFunFact;

    public Movement(String movementName, String movementDating, String movementLocation) {
        this.movementName = movementName;
        this.movementDating = movementDating;
        this.movementLocation = movementLocation;
    }

    @Override
    public String toString() {
        return movementName;
    }

    public int getMovementId() {
        return movementId;
    }

    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }

    public String getMovementName() {
        return movementName;
    }

    public void setMovementName(String movementName) {
        this.movementName = movementName;
    }

    public String getMovementDating() {
        return movementDating;
    }

    public void setMovementDating(String movementDating) {
        this.movementDating = movementDating;
    }

    public String getMovementLocation() {
        return movementLocation;
    }

    public void setMovementLocation(String movementLocation) {
        this.movementLocation = movementLocation;
    }

    public int getMovementArtPeriod() {
        return movementArtPeriod;
    }

    public void setMovementArtPeriod(int movementArtPeriod) {
        this.movementArtPeriod = movementArtPeriod;
    }

    public String getMovementFunFact() {
        return movementFunFact;
    }

    public void setMovementFunFact(String movementFunFact) {
        this.movementFunFact = movementFunFact;
    }

    @Override
    public boolean equals(AThing t) {
        if (this == t) return true;
        if (t == null || getClass() != t.getClass()) return false;
        Movement movement = (Movement) t;
        return getMovementId() == movement.getMovementId() &&
                getMovementArtPeriod() == movement.getMovementArtPeriod() &&
                getMovementName().equals(movement.getMovementName()) &&
                getMovementDating().equals(movement.getMovementDating()) &&
                getMovementLocation().equals(movement.getMovementLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMovementId(), getMovementName(), getMovementDating(), getMovementLocation(), getMovementArtPeriod());
    }

    @Override
    public int getId() {
        return getMovementId();
    }
}
