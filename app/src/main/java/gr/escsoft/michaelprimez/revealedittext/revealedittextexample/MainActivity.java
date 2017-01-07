package gr.escsoft.michaelprimez.revealedittext.revealedittextexample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import gr.escsoft.michaelprimez.revealedittext.RevealEditText;
import gr.escsoft.michaelprimez.revealedittext.tools.UITools;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private RevealEditText mRevealEditText1;
    private RevealEditText mRevealEditTextWithBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.content_main);
        linearLayout.setOnTouchListener(this);
        mRevealEditText1 = (RevealEditText) findViewById(R.id.RevealEditText1);

        mRevealEditTextWithBuilder = new RevealEditText.Builder(this).setTextIfEmpty("Thouch to insert ...").build();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UITools.dpToPx(this, 56));
        layoutParams.leftMargin = UITools.dpToPx(this, 24);
        layoutParams.rightMargin = UITools.dpToPx(this, 24);
        mRevealEditTextWithBuilder.setLayoutParams(layoutParams);
        linearLayout.addView(mRevealEditTextWithBuilder);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mRevealEditText1.setText("Text Changed!");
            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mRevealEditText1.hideEdit();
            mRevealEditTextWithBuilder.hideEdit();
        }
        return true;
    }
}
