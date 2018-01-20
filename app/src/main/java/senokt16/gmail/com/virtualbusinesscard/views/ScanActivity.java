package senokt16.gmail.com.virtualbusinesscard.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import senokt16.gmail.com.virtualbusinesscard.R;

public class ScanActivity extends AppCompatActivity {

    TextView tvCardText;
    Button btStartScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        tvCardText = (TextView)findViewById(R.id.tv_code_text);
        btStartScan = (Button)findViewById(R.id.btn_scan);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }

}