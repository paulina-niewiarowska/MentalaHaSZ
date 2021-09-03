package com.pniew.mentalahasz.model.database;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import java.util.Objects;

@DatabaseView("SELECT " +
        "m.movementId AS id, " +
        "m.movementName AS \"name\", " +
        "m.movementDating AS \"dating\", " +
        "m.movementLocation AS \"location\", " +
        "m.movementArtPeriod AS \"parent_art_period_id\"," +
        "a.artPeriodName AS \"in_period\", "+
        "\"Movement\" AS \"object_type\" " +
        "FROM Movement m JOIN ArtPeriod a ON artPeriodId = movementArtPeriod " +
        "UNION SELECT " +
        "artPeriodId, " +
        "artPeriodName, " +
        "artPeriodDating, " +
        "null, " +
        "null, " +
        "null, " +
        "\"Art Period\" " +
        "FROM ArtPeriod " +
        "UNION SELECT " +
        "typeId, " +
        "typeName, " +
        "null,  " +
        "null, " +
        "null, " +
        "null, " +
        "\"Type of Artwork\" " +
        "FROM Type " +
        "ORDER BY \"object_type\"")
public class Things {
    int id;
    String name;
    String dating;
    String location;
    @ColumnInfo(name = "in_period")
    String inPeriod;
    @ColumnInfo(name = "object_type")
    String objectType;
    @ColumnInfo(name = "parent_art_period_id")
    int parentPeriodId;

    public Things(int id, String name, String dating, String location, String inPeriod, String objectType, int parentPeriodId) {
        this.id = id;
        this.name = name;
        this.dating = dating;
        this.location = location;
        this.inPeriod = inPeriod;
        this.objectType = objectType;
        this.parentPeriodId = parentPeriodId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDating() {
        return dating;
    }

    public String getLocation() {
        return location;
    }

    public String getInPeriod() {
        return inPeriod;
    }

    public String getObjectType() {
        return objectType;
    }

    public int getParentPeriodId() {
        return parentPeriodId;
    }

    public boolean equals(Things things) {
        if (this == things) return true;
        if (things == null || getClass() != things.getClass()) return false;
        return getId() == things.getId() &&
                getParentPeriodId() == things.getParentPeriodId() &&
                getName().equals(things.getName()) &&
                getDating().equals(things.getDating()) &&
                getLocation().equals(things.getLocation()) &&
                getInPeriod().equals(things.getInPeriod()) &&
                getObjectType().equals(things.getObjectType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDating(), getLocation(), getInPeriod(), getObjectType(), getParentPeriodId());
    }
}
