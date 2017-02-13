package e2b.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.views.CirclePageIndicator;


public class HelpActivity extends BaseActivity {

    LayoutInflater layoutInflater;
    private int img[] = {R.drawable.screen_1, R.drawable.screen_2, R.drawable.screen_3,
            R.drawable.screen_4, R.drawable.screen_5, R.drawable.screen_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewPager);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator_help);
        TextView skip = (TextView) findViewById(R.id.skip);
        viewpager.setAdapter(new HelpAdapter());
        indicator.setViewPager(viewpager);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFromHelpScreens();

            }
        });
    }

    private void exitFromHelpScreens() {
            launchActivityMain(AuthActivity.class);
            finish();
    }


    private class HelpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflatedView = layoutInflater.inflate(R.layout.layout_help_item, null);
            ImageView helpImageView = (ImageView) inflatedView.findViewById(R.id.help_imageview);

            View view = inflatedView.findViewById(R.id.view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitFromHelpScreens();
                }
            });

            helpImageView.setImageResource(img[position]);

            if(position == img.length-1){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }

            ((ViewPager) container).addView(inflatedView);
            return inflatedView;
        }

        @Override
        public void destroyItem(View container, int position, Object obj) {
            ((ViewPager) container).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View container, Object obj) {
            return container == obj;
        }

    }

    @Override
    public void onBackPressed() {
        exitFromHelpScreens();
    }
}

