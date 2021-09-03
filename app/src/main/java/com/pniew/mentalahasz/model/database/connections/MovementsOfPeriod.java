package com.pniew.mentalahasz.model.database.connections;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;

import java.util.List;

public class MovementsOfPeriod {
    @Embedded public ArtPeriod artPeriod;
    @Relation(
            parentColumn = "artPeriodId",
            entityColumn = "movementArtPeriod",
            entity = Movement.class
    )
    public List<Movement> movementsOfPeriod;
}
