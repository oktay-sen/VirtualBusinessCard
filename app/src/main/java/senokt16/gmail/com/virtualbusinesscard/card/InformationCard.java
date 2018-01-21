package senokt16.gmail.com.virtualbusinesscard.card;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by mjhutchinson on 20/01/18.
 */

@Entity
public class InformationCard {

    @PrimaryKey
    @NonNull
    private String UUID;
    private ArrayList<Pair<String, String>> information;
    private boolean created;

    public InformationCard(){
        UUID = java.util.UUID.randomUUID().toString();
        information = new ArrayList<>();
        created = true;
    }

    public InformationCard(String data){
        UUID = java.util.UUID.randomUUID().toString();
        information = new ArrayList<>();
        String[] strings = data.split(CommunicationProtocol.NEW_LINE);
        for(String s : strings){
            String[] parts = s.split(CommunicationProtocol.DELIMITER);
            if(parts.length == 2){
                add(parts[0], parts[1]);
            }
        }
        created = false;
    }

    public InformationCard(ArrayList<Pair<String, String>> data){
        UUID = java.util.UUID.randomUUID().toString();
        information = data;
        created = false;
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

    @NonNull
    public String getUUID() {
        return UUID;
    }

    public void setUUID(@NonNull String UUID) {
        this.UUID = UUID;
    }

    public ArrayList<Pair<String, String>> getInformation() {
        return information;
    }

    public void setInformation(ArrayList<Pair<String, String>> information) {
        this.information = information;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
