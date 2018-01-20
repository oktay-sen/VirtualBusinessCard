package senokt16.gmail.com.virtualbusinesscard.views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import senokt16.gmail.com.virtualbusinesscard.R;
import senokt16.gmail.com.virtualbusinesscard.util.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {


    RecyclerView cardsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        cardsView = findViewById(R.id.cards_view);
        cardsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cardsView.setAdapter(new CardsAdapter());
        cardsView.addOnItemTouchListener(new RecyclerItemClickListener(this, cardsView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: Animated transition
                //TODO: Attach contact info.
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.thumbnail), "image");
                startActivity(i, options.toBundle());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long pressed item " + position, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
