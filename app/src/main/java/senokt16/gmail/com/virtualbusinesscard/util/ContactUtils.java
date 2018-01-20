package senokt16.gmail.com.virtualbusinesscard.util;

import android.content.Intent;
import android.provider.ContactsContract;

/**
 * Created by mjhutchinson on 20/01/18.
 */

public class ContactUtils {

    public static Intent newContactIntent(String name, String phone, String email, String address){

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);


        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
        return intent;

    }
}
