package senokt16.gmail.com.virtualbusinesscard.util;

import android.content.Intent;
import android.provider.ContactsContract;

/**
 * Created by mjhutchinson on 20/01/18.
 */

public class ContactUtils {

    public static Intent newContactIntent(){

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);


        intent.putExtra(ContactsContract.Intents.Insert.NAME, "Mr some Contact Name");
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "04673");
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, "11119919");

        return intent;

    }
}
