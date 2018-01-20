package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.util.Unit;

public class ProfileActivity extends AppCompatActivity {

    private static final int MAX_IMAGE_HEIGHT_DP = 192;
    private static final int MID_IMAGE_HEIGHT_DP = 128;
    private static int MIN_IMAGE_HEIGHT_PX;

    ImageView profileImage, qrImage;
    RelativeLayout imageWrapper;
    ViewGroup bottomSheet;
    BottomSheetBehavior behavior;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        profileImage = findViewById(R.id.profile_image);
        imageWrapper = findViewById(R.id.image_wrapper);
        bottomSheet = findViewById(R.id.foreground);

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        MIN_IMAGE_HEIGHT_PX = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        Log.v("IMAGE_HEIGHT:", ""+MIN_IMAGE_HEIGHT_PX);

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
    }

    private void setImageOpacity(float offset) {
        profileImage.setAlpha(offset < 0 ? 1+offset : 1);
        toolbar.setAlpha(offset < 0 ? 0 : offset);
    }

    private void setImageHeight(float offset) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) imageWrapper.getLayoutParams();
        Log.v("setImageHeight", "MIN: " + MIN_IMAGE_HEIGHT_PX + ", MID: " + Unit.dp(this, MID_IMAGE_HEIGHT_DP));
        if (offset < 0)
            params.height = (int) (Unit.dp(this, MID_IMAGE_HEIGHT_DP) + (Unit.dp(this, MAX_IMAGE_HEIGHT_DP) - Unit.dp(this, MID_IMAGE_HEIGHT_DP)) * (-offset));
        else
            params.height = (int) (MIN_IMAGE_HEIGHT_PX + (Unit.dp(this, MID_IMAGE_HEIGHT_DP) - MIN_IMAGE_HEIGHT_PX) * (1-offset));
        imageWrapper.setLayoutParams(params);
    }

    private void setImageTranslation(float offset) {
        int maxY = getResources().getDisplayMetrics().heightPixels/4 - MID_IMAGE_HEIGHT_DP /2;

        imageWrapper.setTranslationY(maxY * (1-offset));

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
}
