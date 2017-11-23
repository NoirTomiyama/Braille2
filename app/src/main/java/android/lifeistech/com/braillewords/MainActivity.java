package android.lifeistech.com.braillewords;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView2;
        textView2 = (TextView)findViewById(R.id.textView2);
    }



    public void toJ(View v){
        Intent intent = new Intent(this, toJActivity.class);
        startActivity(intent);
    }

    public void toB(View v){
        Intent intent = new Intent(this, toBActivity.class);
        startActivity(intent);
    }

    public void help(View v){
        Intent intent = new Intent(this, helpActivity.class);
        startActivity(intent);
    }
}


