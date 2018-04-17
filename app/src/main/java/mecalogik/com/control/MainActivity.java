package mecalogik.com.control;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView mProfilelabel;

    private TextView mNotificacionLabel;

    private ViewPager mMainPager;

    private  PagerViewAdapter mPagerViewAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){

            sendToLogin();

        }
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mProfilelabel = (TextView) findViewById(R.id.tvprofiles);
        mNotificacionLabel = (TextView) findViewById(R.id.tvnotoficaciones);

        mMainPager = (ViewPager) findViewById(R.id.viewp);
        mMainPager.setOffscreenPageLimit(2);

        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);

        mProfilelabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(0);

            }
        });



        mNotificacionLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(1);
            }
        });

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTabs(int position) {

        if(position == 0){
            mProfilelabel.setTextColor(getColor(R.color.textTabBright));
            mProfilelabel.setTextSize(22);



            mNotificacionLabel.setTextColor(getColor(R.color.textTabLight));
            mNotificacionLabel.setTextSize(16);

        }


        if (position == 1){
            mProfilelabel.setTextColor(getColor(R.color.textTabLight));
            mProfilelabel.setTextSize(16);


            mNotificacionLabel.setTextColor(getColor(R.color.textTabBright));
            mNotificacionLabel.setTextSize(22);
        }
    }

}
