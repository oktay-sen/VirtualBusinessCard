package senokt16.gmail.com.virtualbusinesscard.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by mjhutchinson on 20/01/18.
 */

@Dao
public interface CardsDAO {

    @Insert(onConflict = REPLACE)
    void insertCard(InformationCard informationCard);

    @Query("SELECT * FROM InformationCard")
    List<InformationCard> getAllCards();

    @Query("SELECT * FROM InformationCard where UUID=:UUID")
    List<InformationCard> getCardById(String UUID);

    @Query("SELECT * FROM InformationCard where created=:created")
    List<InformationCard> getCardsByCreated(boolean created);

    @Query("DELETE FROM InformationCard")
    void deleteAll();
}
