package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.zxing.WriterException;
import static senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import senokt16.gmail.com.virtualbusinesscard.R;

import senokt16.gmail.com.virtualbusinesscard.card.CommunicationProtocol;
import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import senokt16.gmail.com.virtualbusinesscard.database.CardsDB;

import senokt16.gmail.com.virtualbusinesscard.util.QRUtils;
import senokt16.gmail.com.virtualbusinesscard.util.Unit;

public class ProfileActivity extends AppCompatActivity {

    private static final int MAX_IMAGE_HEIGHT_DP = 384;
    private static final int MID_IMAGE_HEIGHT_DP = 144;
    private static int MIN_IMAGE_HEIGHT_PX;
    private static final String CARDKEY = "card";
    private static final String ID = "UUID";
    public static final String CREATED = "CREATED";

    private boolean editMode = false;

    private static String NEWCARD = "NEW";
    ImageView profileImage, qrImage;
    RelativeLayout imageWrapper;
    ViewGroup bottomSheet;
    BottomSheetBehavior behavior;
    Toolbar toolbar;
    AppBarLayout appBar;
    ViewGroup upButton;
    private RecyclerView contactDetails;
    FloatingActionButton fab;
    private InformationCard infoCard;
    private static CardsDB cardsDB;
    private boolean fromCardsAdapter;

    ImageView nameEditButton;
    TextView name;
    ViewGroup wrapper;

    private boolean edited;
    private boolean created;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        appBar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        profileImage = findViewById(R.id.profile_image);
        qrImage = findViewById(R.id.qr_image);
        imageWrapper = findViewById(R.id.image_wrapper);
        bottomSheet = findViewById(R.id.foreground);
        upButton = findViewById(R.id.button_up);
        nameEditButton = findViewById(R.id.name_edit);
        name = findViewById(R.id.name);

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        MIN_IMAGE_HEIGHT_PX = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        setImageOpacity(0);
        setImageHeight(0);
        setImageTranslation(0);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels/2);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setHideable(true);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Log.v("ProfileActivity", "Slide: " + slideOffset);
                setImageOpacity(slideOffset);
                setImageHeight(slideOffset);
                setImageTranslation(slideOffset);
            }
        });

        try {
            qrImage.setImageBitmap(QRUtils.TextToImageEncode(this, "Hello World", 100));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        created = getIntent().getBooleanExtra(CREATED, false);
        contactDetails = findViewById(R.id.contact_details);
        contactDetails.setLayoutManager(new LinearLayoutManager(this));

        infoCard = new InformationCard(getIntent().getStringExtra(CARDKEY));
        if(created){
            if(getIntent().hasExtra(NEWCARD)){
                if(getIntent().getBooleanExtra(NEWCARD, true)){
                    infoCard.add(CommunicationProtocol.NAME_PREFIX, "");
                    infoCard.add(CommunicationProtocol.DESCRIPTION_PREFIX, "");
                    infoCard.add(CommunicationProtocol.EMAIL_PREFIX, "");
                    infoCard.add(CommunicationProtocol.ADDRESS_PREFIX, "");
                    infoCard.add(CommunicationProtocol.PHONE_PREFIX, "");
                    infoCard.add(CommunicationProtocol.FACEBOOK_PREFIX, "");
                    infoCard.add(CommunicationProtocol.TWITTER_PREFIX, "");
                    infoCard.add(CommunicationProtocol.SNAPCHAT_PREFIX, "");
                    infoCard.add(CommunicationProtocol.INSTAGRAM_PREFIX, "");
                    infoCard.add(CommunicationProtocol.YOUTUBE_PREFIX, "");
                }
            }
        }
        Log.v("Infocard click ", infoCard.getAll().toString());
        infoCard.setCreated(created);
        contactDetails.setAdapter(new ProfileAdapter(infoCard, this, created));

        fromCardsAdapter = getIntent().hasExtra(ID);
        Log.v("Has UUID", Boolean.toString(fromCardsAdapter));

        fab = findViewById(R.id.fab);
        if(fromCardsAdapter) {
            fab.setVisibility(View.GONE);
        }


        if(!fromCardsAdapter || edited) {
            cardsDB = CardsDB.getInstance(this);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fab.setVisibility(View.GONE);
                //NOTE: not definitely saved yet
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("DBSave", "Saving...");
                        infoCard.setCreated(created);
                        cardsDB.cardsDAO().insertCard(infoCard);
                        Log.v("DBSave", "Saved to DB");
                        Handler h = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);

                            }
                        };
                    }
                });
            }
        });

        name.setText(infoCard.getAll().get(0).second);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        Log.v("card values", infoCard.toString());
        int color;
        if(infoCard.getAll().size() > 0 && !infoCard.getAll().get(0).second.equals("")) {
            String[] names = infoCard.getAll().get(0).second.split(" ");
            StringBuilder initials = new StringBuilder();
            for (String s : names) {
                initials.append(s.charAt(0));
            }

            color = generator.getColor(infoCard.getAll().get(0).second);

            TextDrawable imageDrawable = TextDrawable.builder().beginConfig().height(Unit.dp(this,128)).width(Unit.dp(this,128)).endConfig().buildRound(initials.toString(), color);
            profileImage.setImageDrawable(imageDrawable);
        }else{
            color = Color.LTGRAY;
        }


        try {
            qrImage.setImageBitmap(QRUtils.TextToImageEncode(this, infoCard.toString(), 200));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *=0.8;

        wrapper = findViewById(R.id.wrapper);
        wrapper.setBackgroundColor(Color.HSVToColor(hsv));
    }

    private void setImageOpacity(float offset) {
        profileImage.setAlpha(offset < 0 ? 1+offset : 1);
        appBar.getBackground().setAlpha(offset >= 0 ? (int)(offset*255) : 0);
        appBar.setElevation(offset < 0.5 ? 0 : Unit.dp(this, 8) * (offset-.5f) * 2);
        qrImage.setAlpha(offset < 0 ? 1f : 0f);
    }

    private void setImageHeight(float offset) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) imageWrapper.getLayoutParams();
        if (offset < 0)
            params.height = (int) (Unit.dp(this, MID_IMAGE_HEIGHT_DP) + (Unit.dp(this, MAX_IMAGE_HEIGHT_DP) - Unit.dp(this, MID_IMAGE_HEIGHT_DP)) * (-offset));
        else
            params.height = (int) (MIN_IMAGE_HEIGHT_PX + (Unit.dp(this, MID_IMAGE_HEIGHT_DP) - MIN_IMAGE_HEIGHT_PX) * (1-offset));
        imageWrapper.setLayoutParams(params);
    }

    private void setImageTranslation(float offset) {
        int totalHeight = getResources().getDisplayMetrics().heightPixels-Unit.dp(this,35);
        int midY = totalHeight/4 - Unit.dp(this, MID_IMAGE_HEIGHT_DP) /2 + MIN_IMAGE_HEIGHT_PX/2;
        int maxY = totalHeight/2 - Unit.dp(this, MAX_IMAGE_HEIGHT_DP) /2;
        if (offset >= 0)
            imageWrapper.setTranslationY(midY * (1-offset));
        else
            imageWrapper.setTranslationY(midY + (maxY-midY) * (-offset));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public void editField(String fieldKey, String newData) {
        infoCard.replace(fieldKey, newData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            //Do edit
            editMode = ((ProfileAdapter)contactDetails.getAdapter()).toggleEdit();
            if (editMode) {
                item.setIcon(R.drawable.ic_done_white_24dp);
                nameEditButton.setVisibility(View.VISIBLE);
            } else {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("DBSave", "Saving...");
                        infoCard.setCreated(created);
                        cardsDB.cardsDAO().insertCard(infoCard);
                        Log.v("DBSave", "Saved to DB");
                        Handler h = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);

                            }
                        };
                    }
                });
                item.setIcon(R.drawable.ic_edit_white_24dp);
                nameEditButton.setVisibility(View.GONE);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
