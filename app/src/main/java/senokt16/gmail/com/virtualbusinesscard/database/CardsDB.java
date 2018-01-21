package senokt16.gmail.com.virtualbusinesscard.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

/**
 * Created by mjhutchinson on 20/01/18.
 */

@Database(entities = {InformationCard.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CardsDB extends RoomDatabase {
    public abstract CardsDAO cardsDAO();
}
