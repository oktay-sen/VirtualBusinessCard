package senokt16.gmail.com.virtualbusinesscard.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;

import senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol;

/**
 * Created by mjhutchinson on 20/01/18.
 */

public class Converters {
    @TypeConverter
    public static ArrayList<Pair<String,String>> arraylistPairFromString(String value) {
        ArrayList<Pair<String,String>> information = new ArrayList<>();
        String[] strings = value.split(CommunicationProtocol.NEW_LINE);
        for(String s : strings){
            String[] parts = s.split(CommunicationProtocol.DELIMITER);
            if(parts.length == 2){
                information.add(new Pair<>(parts[0], parts[1]));
            }
        }
        return information;
    }

    @TypeConverter
    public static String arraylistPairToString(ArrayList<Pair<String,String>> list) {
        StringBuilder out = new StringBuilder();
        for(Pair<String, String> pair : list){
            out.append(pair.first).append(CommunicationProtocol.DELIMITER).append(pair.second).append(CommunicationProtocol.NEW_LINE);
        }
        return out.toString();
    }
}
