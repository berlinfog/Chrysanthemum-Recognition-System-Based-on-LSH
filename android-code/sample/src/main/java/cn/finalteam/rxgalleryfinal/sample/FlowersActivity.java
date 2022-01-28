package cn.finalteam.rxgalleryfinal.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FlowersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button button = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);
        TextView tv = (TextView)findViewById(R.id.text_type0);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        button = (Button)findViewById(R.id.btn_return);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FlowersActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
