package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.zxing.WriterException;

import java.util.concurrent.Executors;

import senokt16.gmail.com.virtualbusinesscard.R;

import senokt16.gmail.com.virtualbusinesscard.card.InformationCard;

import senokt16.gmail.com.virtualbusinesscard.database.CardsDB;

import senokt16.gmail.com.virtualbusinesscard.util.QRUtils;
import senokt16.gmail.com.virtualbusinesscard.util.Unit;

public class ProfileActivity extends AppCompatActivity {

    private static final int MAX_IMAGE_HEIGHT_DP = 384;
    private static final int MID_IMAGE_HEIGHT_DP = 128;
    private static int MIN_IMAGE_HEIGHT_PX;
    private static final String CARDKEY = "card";
    private static final String ID = "UUID";

    ImageView profileImage, qrImage;
    RelativeLayout imageWrapper;
    ViewGroup bottomSheet;
    BottomSheetBehavior behavior;
    Toolbar toolbar;
    AppBarLayout appBar;
    ViewGroup upButton;
    private RecyclerView contactDetails;
    FloatingActionButton fab;
    private static CardsDB cardsDB;
    private boolean fromCardsAdapter;
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

        final InformationCard card = new InformationCard(getIntent().getStringExtra(CARDKEY));

        contactDetails = findViewById(R.id.contact_details);
        contactDetails.setLayoutManager(new LinearLayoutManager(this));
        contactDetails.setAdapter(new ProfileAdapter(card, this));
        fromCardsAdapter = getIntent().hasExtra(ID);
        Log.v("Has UUID", Boolean.toString(fromCardsAdapter));

        fab = findViewById(R.id.fab);
        if(fromCardsAdapter) {
            fab.setVisibility(View.GONE);
        }

        //TODO:Edits to card
        if(!fromCardsAdapter) {
            cardsDB = CardsDB.getInstance(this);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                Snackbar.make(view, "Saving Card...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("DBSave", "Saving...");
                        cardsDB.cardsDAO().insertCard(card);
                        Log.v("DBSave", "Saved to DB");
                    }
                });
            }
        });

        ColorGenerator generator = ColorGenerator.MATERIAL;

        String[] names = card.getAll().get(0).second.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String s:names) {
            initials.append(s.charAt(0));
        }

        int color = generator.getColor(card.getAll().get(0).second);

        profileImage.setImageDrawable(TextDrawable.builder().beginConfig().height(Unit.dp(this,128)).width(Unit.dp(this,128)).endConfig().buildRound(initials.toString(), color));


        try {
            qrImage.setImageBitmap(QRUtils.TextToImageEncode(this, card.toString(), 100));
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
        int midY = totalHeight/4 - Unit.dp(this, MID_IMAGE_HEIGHT_DP) /2 + MIN_IMAGE_HEIGHT_PX;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
