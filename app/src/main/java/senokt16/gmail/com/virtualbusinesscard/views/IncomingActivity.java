package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

/**
 * Created by Ian_Tai on 2018-01-20.
 */

public class IncomingActivity extends AppCompatActivity {
    final String cardKey = "card";
    TextView sampleText;

    //savedInstanceState contains key:value pair of
    //"card":InformationCard
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);

        Intent intent = getIntent();
        InformationCard iC = new InformationCard(intent.getStringExtra(cardKey));
        PackageManager pm = this.getPackageManager();

        

    }

}
