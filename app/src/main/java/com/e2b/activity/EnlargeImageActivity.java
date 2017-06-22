package com.e2b.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.e2b.R;
import com.e2b.model.Media;
import com.e2b.views.CirclePageIndicator;

import java.util.List;

/**
 * Class is used to show image in large view.
 */
public class EnlargeImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_enlargeimage);
        ViewPager pager = (ViewPager) findViewById(R.id.enlarge_view);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        if (getIntent() != null) {
            int selectedPosition = getIntent().getIntExtra("position", 0);
            List<Media> medias = (List<Media>) getIntent().getSerializableExtra("medias");
            if (medias != null) {
                if (medias.size() == 1) {
                    circlePageIndicator.setVisibility(View.GONE);
                }
                pager.setAdapter(new MyViewPager(medias));
                circlePageIndicator.setViewPager(pager);
                pager.setCurrentItem(selectedPosition);

            }
        }
    }


    public class MyViewPager extends PagerAdapter {

        private final List<Media> medias;

        private LayoutInflater mLayoutInflater;

        public MyViewPager(List<Media> medias) {
            super();
            this.medias = medias;
        }

        @Override
        public int getCount() {
            return medias.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = mLayoutInflater.inflate(R.layout.detail_feed_pager, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            if (medias.get(position).getImgUrl() == null) {
                imageView.setVisibility(View.GONE);
            }
            container.addView(itemView);
            loadImageGlide(medias.get(position).getImgUrl(), imageView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
