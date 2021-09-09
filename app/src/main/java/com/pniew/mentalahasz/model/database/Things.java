package com.pniew.mentalahasz.model.database;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

import java.util.Objects;

@DatabaseView("SELECT " +
        "m.movementId AS id, " + //=================== id
        "m.movementName AS \"name\", " + //--------------- name
        "m.movementDating AS \"dating\", " + //=================== dating
        "m.movementLocation AS \"location\", " +  //--------------- location
        "m.movementArtPeriod AS \"parent_art_period_id\"," + //=================== parent id
        "a.artPeriodName AS \"in_period\", "+ //--------------- parent name
        "m.movementFunFact AS \"fun_fact\", " + //=================== funfact
        "\"Movement\" AS \"object_type\" " + //--------------- object type
        "FROM Movement m JOIN ArtPeriod a ON artPeriodId = movementArtPeriod " +
        "UNION SELECT " +
        "artPeriodId, " + //=================== id
        "artPeriodName, " + //--------------- name
        "artPeriodDating, " + //=================== dating
        "null, " + //--------------- location
        "null, " + //=================== parent id
        "null, " + //--------------- parent name
        "artPeriodFunFact, " + //=================== funfact
        "\"Art Period\" " + //--------------- object type
        "FROM ArtPeriod " +
        "UNION SELECT " +
        "typeId, " + //=================== id
        "typeName, " + //--------------- name
        "null,  " + //=================== dating
        "null, " + //--------------- location
        "null, " + //=================== parent id
        "null, " + //--------------- parent name
        "typeFunFact, " + //=================== funfact
        "\"Type of Artwork\" " + //--------------- object type
        "FROM Type " +
        "ORDER BY \"object_type\"")

public class Things {
    int id;
    String name;
    String dating;
    String location;
    @ColumnInfo(name = "parent_art_period_id")
    int parentPeriodId;
    @ColumnInfo(name = "in_period")
    String inPeriod;
    @ColumnInfo(name = "fun_fact")
    String funFact;
    @ColumnInfo(name = "object_type")
    String objectType;


    public Things(int id, String name, String dating, String location, int parentPeriodId, String inPeriod, String funFact, String objectType) {
        this.id = id;
        this.name = name;
        this.dating = dating;
        this.location = location;
        this.parentPeriodId = parentPeriodId;
        this.inPeriod = inPeriod;
        this.funFact = funFact;
        this.objectType = objectType;
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

    public String getFunFact() { return funFact; }


    public boolean equals(Things things) {
        if (this == things) return true;
        if (things == null || getClass() != things.getClass()) return false;
        return getId() == things.getId() &&
                getParentPeriodId() == things.getParentPeriodId() &&
                getName().equals(things.getName()) &&
                getDating().equals(things.getDating()) &&
                getLocation().equals(things.getLocation()) &&
                getInPeriod().equals(things.getInPeriod()) &&
                getObjectType().equals(things.getObjectType()) &&
                getFunFact().equals(things.getFunFact());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDating(), getLocation(), getInPeriod(), getObjectType(), getParentPeriodId(), getFunFact());
    }
}
