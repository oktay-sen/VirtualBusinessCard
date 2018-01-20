package senokt16.gmail.com.virtualbusinesscard.card;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mjhutchinson on 20/01/18.
 */

public class InformationCard implements Serializable {

    private ArrayList<Pair<String, String>> information;

    public InformationCard(){
        information = new ArrayList<>();
    }

    public InformationCard(String data){
        information = new ArrayList<>();
        String[] strings = data.split(CommunicationProtocol.NEW_LINE);
        for(String s : strings){
            String[] parts = s.split(CommunicationProtocol.DELIMITER);
            if(parts.length == 2){
                add(parts[0], parts[1]);
            }
        }
    }

    public InformationCard(ArrayList<Pair<String, String>> data){
        information = data;
    }

    public void add(String key, String value){
        information.add(new Pair<String, String>(key, value));
    }

    public ArrayList<Pair<String, String>> getAll(){
        return information;
    }

    public String toString(){
        StringBuilder out = new StringBuilder();
        for(Pair<String, String> pair : information){
            out.append(pair.first).append(CommunicationProtocol.DELIMITER).append(pair.second).append(CommunicationProtocol.NEW_LINE);
        }
        return out.toString();
    }

}
