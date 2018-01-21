package senokt16.gmail.com.virtualbusinesscard.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

@Database(entities = {InformationCard.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CardsDB extends RoomDatabase {
    private static CardsDB instance;

    public abstract CardsDAO cardsDAO();

    public static CardsDB getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CardsDB.class, "CardsDB").build();
        return instance;
    }
}
