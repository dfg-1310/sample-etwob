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
    private int img[] = {R.color.color_6e6e6e, R.color.color_080808, R.color.color_33929292,
            R.color.color_CC42B757, R.color.color_B3142029, R.color.color_fb523f};

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
        launchActivityMain(HomeActivity.class);
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

            helpImageView.setBackgroundColor(img[position]);

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

