package com.pniew.mentalahasz.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pniew.mentalahasz.model.database.daos.ArtPeriodDao;
import com.pniew.mentalahasz.model.database.daos.HaSZDao;
import com.pniew.mentalahasz.model.database.daos.MovementDao;
import com.pniew.mentalahasz.model.database.daos.PictureDao;
import com.pniew.mentalahasz.model.database.daos.ThingsDao;
import com.pniew.mentalahasz.model.database.daos.TypeDao;
import com.pniew.mentalahasz.model.database.entities.ArtPeriod;
import com.pniew.mentalahasz.model.database.entities.Movement;
import com.pniew.mentalahasz.model.database.entities.Picture;
import com.pniew.mentalahasz.model.database.entities.Type;

@Database(entities = {Picture.class, ArtPeriod.class, Movement.class, Type.class},
        views = {Things.class},
        version = 6,
        exportSchema = true
//        ,autoMigrations = {
//                @AutoMigration(from = 4, to = 5)
//        }
        )
public abstract class HaszDatabase extends RoomDatabase {
    private static HaszDatabase instance;
    public abstract HaSZDao haSZDao();
    public abstract MovementDao movementDao();
    public abstract ArtPeriodDao artPeriodDao();
    public abstract PictureDao pictureDao();
    public abstract TypeDao typeDao();
    public abstract ThingsDao thingsDao();

    public static synchronized HaszDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    HaszDatabase.class, "hasz_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
