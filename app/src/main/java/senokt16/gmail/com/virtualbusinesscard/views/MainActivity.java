package senokt16.gmail.com.virtualbusinesscard.views;

import android.app.ActivityOptions;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;
import senokt16.gmail.com.virtualbusinesscard.database.CardsDB;
import senokt16.gmail.com.virtualbusinesscard.util.RecyclerItemClickListener;
import senokt16.gmail.com.virtualbusinesscard.util.Unit;

public class MainActivity extends AppCompatActivity {

    private static final String CARDKEY = "card";
    private static final String ID = "UUID";
    private static final String CREATED = "CREATED";
    RecyclerView cardsView;
    private AppBarLayout appBar;
    ProgressBar loading;
    InformationCard queryCard;
    // DataBase creation
    final CardsDB cardsDB = CardsDB.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);
        cardsView = findViewById(R.id.cards_view);
        cardsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));cardsView.addOnItemTouchListener(new RecyclerItemClickListener(this, cardsView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final int pos = position;
                //TODO: Animated transition
                //TODO: Attach contact info.
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);

                InformationCard getInfo = ((CardsAdapter)cardsView.getAdapter()).getSingleCard(pos);

                i.putExtra(CARDKEY, getInfo.toString());
                i.putExtra(ID, getInfo.getUUID());
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.thumbnail), "image");

                startActivity(i, options.toBundle());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long pressed item " + position, Toast.LENGTH_SHORT).show();
            }
        }));


        final InformationCard card = new InformationCard("N:Michael Hutchinson\nD:A Card!\nEM:mjh252@cam.ac.uk\nPH:07446880103\nFB:mike.hutch.56\n");
        final InformationCard card1 = new InformationCard();
        card1.add(CommunicationProtocol.NAME_PREFIX, "Michael Hutchinson");
        card1.add(CommunicationProtocol.DESCRIPTION_PREFIX, "A nifty business card to transmit social media info!");
        card1.add(CommunicationProtocol.ADDRESS_PREFIX, "60 High Street, Ringstead, PE36 5JU");
        card1.add(CommunicationProtocol.EMAIL_PREFIX, "mjh252@cam.ac.uk");
        card1.add(CommunicationProtocol.PHONE_PREFIX, "0744880103");
        card1.add(CommunicationProtocol.FACEBOOK_PREFIX, "mike.hutch.56");
        card1.add(CommunicationProtocol.SNAPCHAT_PREFIX, "michaelhutchinson");
        card1.add(CommunicationProtocol.LINK_PREFIX, "www.github.com/mjhutchinson");
        card1.add(CommunicationProtocol.INSTAGRAM_PREFIX, "abc123");

//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                cardsDB.cardsDAO().deleteAll();
//                cardsDB.cardsDAO().insertCard(card);
//                cardsDB.cardsDAO().insertCard(card1);
//                Log.v("INFO", cardsDB.cardsDAO().getAllCards().get(1).toString());
//            }
//        });




        Toolbar toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startQRScanner();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<InformationCard> cardback = cardsDB.cardsDAO().getAllCards();
                if(cardback.size() > 0) {
                    Log.v("Data",cardback.get(0).getUUID());
//                  List<InformationCard> getByID = cardsDB.cardsDAO().getCardById(cardback.get(0).getUUID());
//                  Log.v("Data",getByID.get(0).toString());
                }

                Handler h = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        loading.setVisibility(View.GONE);
                        cardsView.setAdapter(new CardsAdapter(cardback));
                    }
                };
                h.sendEmptyMessage(0);
            }
        });
        appBar.setElevation(Unit.dp(this, 8));
        appBar.getBackground().setAlpha(255);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled",Toast.LENGTH_SHORT).show();
            } else {
                Log.d("QR Result", result.getContents());
                showNew(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void showNew(String iC){
        Intent goToNextActivity = new Intent(getApplicationContext(), ProfileActivity.class);
        goToNextActivity.putExtra(CARDKEY, iC);
        startActivity(goToNextActivity);
    }
}
