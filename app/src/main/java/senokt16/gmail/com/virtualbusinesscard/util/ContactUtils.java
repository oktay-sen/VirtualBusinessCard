package senokt16.gmail.com.virtualbusinesscard.util;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Pair;

import senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

/**
 * Created by mjhutchinson on 20/01/18.
 */

public class ContactUtils {

    public static Intent newContactIntent(InformationCard informationCard){

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        int addresses = 0;
        int emails = 0;
        int phones = 0;

        for(Pair<String, String> pair : informationCard.getAll()){
            switch (pair.first){
                case(CommunicationProtocol.NAME_PREFIX):
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, pair.second);
                    break;
                case(CommunicationProtocol.ADDRESS_PREFIX):
                    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, pair.second);
                    break;
                case(CommunicationProtocol.EMAIL_PREFIX):
                    switch (emails){
                        case(0):
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, pair.second);
                            break;
                        case(1):
                            intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, pair.second);
                            break;
                        case(2):
                            intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, pair.second);
                            break;
                    }
                    emails++;
                    break;
                case(CommunicationProtocol.PHONE_PREFIX):
                    switch (phones){
                        case(0):
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, pair.second);
                            break;
                        case(1):
                            intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, pair.second);
                            break;
                        case(2):
                            intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, pair.second);
                            break;
                    }
                    emails++;
                    break;
            }
        }

        return intent;

    }
}
