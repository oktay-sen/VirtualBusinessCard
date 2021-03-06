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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private static final String NEWCARD = "NEW";
    RecyclerView cardsView;
    private AppBarLayout appBar;
    ProgressBar loading;
    InformationCard queryCard;
    // DataBase creation
    final CardsDB cardsDB = CardsDB.getInstance(this);
    private boolean createdToggle = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


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


        final InformationCard card = new InformationCard("N:Ian Tai\nD:University of Cambridge\nEM:ihbt2@cam.ac.uk\nPH:07662554026\nFB:ian.tai.58\nSC:ian.tai\nLN:www.github.com/ianhtai\nIG:ianhtai");
        final InformationCard card2 = new InformationCard();
        card2.add(CommunicationProtocol.NAME_PREFIX, "Oktay Sen");
        card2.add(CommunicationProtocol.DESCRIPTION_PREFIX, "University Of Edinburgh");
        card2.add(CommunicationProtocol.EMAIL_PREFIX, "senokt.16@gmail.com");
        card2.add(CommunicationProtocol.PHONE_PREFIX, "07599994408");
        card2.add(CommunicationProtocol.FACEBOOK_PREFIX, "elektroktay");
        card2.add(CommunicationProtocol.SNAPCHAT_PREFIX, "elektroktay");
        card2.add(CommunicationProtocol.LINK_PREFIX, "www.github.com/oktay-sen");
        card2.add(CommunicationProtocol.INSTAGRAM_PREFIX, "senokt16");
        final InformationCard card1 = new InformationCard();
        card1.add(CommunicationProtocol.NAME_PREFIX, "Michael Hutchinson");
        card1.add(CommunicationProtocol.DESCRIPTION_PREFIX, "Christs College Cambridge");
        card1.add(CommunicationProtocol.ADDRESS_PREFIX, "60 High Street, Ringstead, PE36 5JU");
        card1.add(CommunicationProtocol.EMAIL_PREFIX, "mjh252@cam.ac.uk");
        card1.add(CommunicationProtocol.PHONE_PREFIX, "0744880103");
        card1.add(CommunicationProtocol.FACEBOOK_PREFIX, "mike.hutch.56");
        card1.add(CommunicationProtocol.SNAPCHAT_PREFIX, "mikehutchinson");
        card1.add(CommunicationProtocol.LINK_PREFIX, "www.github.com/mjhutchinson");
        card1.add(CommunicationProtocol.INSTAGRAM_PREFIX, "abc123");
        card1.add(CommunicationProtocol.LINK_PREFIX, "www.linkedin.com/in/michael-hutchinson");

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                cardsDB.cardsDAO().deleteAll();
//                cardsDB.cardsDAO().insertCard(card);
                //cardsDB.cardsDAO().insertCard(card1);
                cardsDB.cardsDAO().insertCard(card2);
            }
        });




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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.new_card){
            Intent goToNextActivity = new Intent(getApplicationContext(), ProfileActivity.class);
            InformationCard tempCreate = new InformationCard();
            Log.v("iC", tempCreate.toString());
            goToNextActivity.putExtra(CARDKEY, tempCreate.toString());
            goToNextActivity.putExtra(CREATED, true);
            goToNextActivity.putExtra(NEWCARD, true);
            startActivity(goToNextActivity);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.profile_filter) {

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    final List<InformationCard> cardback;
                    if(createdToggle){
                        cardback  = cardsDB.cardsDAO().getCardsByCreated(true);
                    }
                    else {
                        cardback = cardsDB.cardsDAO().getAllCards();
                    }
                    createdToggle = !createdToggle;
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
            (cardsView.getAdapter()).notifyDataSetChanged();
        }


        return super.onOptionsItemSelected(item);

    }

}
