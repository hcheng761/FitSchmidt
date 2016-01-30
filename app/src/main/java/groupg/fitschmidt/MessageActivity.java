package groupg.fitschmidt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

public class MessageActivity extends AppCompatActivity {
    AnimationDrawable logoAnimation;
    String quitter = "You give up? Too bad.";
    String finish = "Nice work!";
    TextView txtview;
    Intent intent = getIntent();
    //Bundle bundle = intent.getExtras();
    boolean result = true;// intent.getExtras().getBoolean("RESULT");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        txtview = (TextView)findViewById(R.id.txtview);

        if (MessageBoolean.getInstance().getValue()) {
            txtview.setText(finish);
            ImageView logoImage = (ImageView) findViewById(R.id.iv1);
            logoImage.setBackgroundResource(R.drawable.complete);
            logoAnimation = (AnimationDrawable) logoImage.getBackground();
            logoAnimation.start();
        }
        else
        {
            txtview.setText(quitter);
            ImageView logoImage = (ImageView) findViewById(R.id.iv1);
            logoImage.setBackgroundResource(R.drawable.given_up);
            logoAnimation = (AnimationDrawable) logoImage.getBackground();
            logoAnimation.start();
        }
    }

    public void onBackPressed()
    {
        // super.onBackPressed();
    }
    public void ButtonOnClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
