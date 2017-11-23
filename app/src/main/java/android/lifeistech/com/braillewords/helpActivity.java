package android.lifeistech.com.braillewords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class helpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
    public void BtoJ(View v){
        Intent intent = new Intent(this, toJActivity.class);
        startActivity(intent);
    }

    public void JtoB(View v){
        Intent intent = new Intent(this, toBActivity.class);
        startActivity(intent);
    }
}
