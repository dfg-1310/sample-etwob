package e2b.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.fragments.BaseFragment;
import com.e2b.utils.AppConstant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import e2b.model.response.UserResponse;
import e2b.utils.ConsumerPreferenceKeeper;

/**
 * Created by gaurav on 23/3/17.
 */

public class ConsumerBaseActivity extends BaseActivity {

    private FrameLayout bodyContainerLayout;

    @Bind(R.id.tv_home)
    TextView tv_home;

    @Bind(R.id.tv_orders)
    TextView tv_orders;

    @Bind(R.id.tv_profile)
    TextView tv_profile;

    @Bind(R.id.tv_header_title)
    TextView tv_header_title;

    @Bind(R.id.cb_home)
    CheckBox cb_home;

    @Bind(R.id.cb_orders)
    CheckBox cb_orders;

    @Bind(R.id.cb_profile)
    CheckBox cb_profile;

    @Bind(R.id.ll_home)
    RelativeLayout ll_home;

    @Bind(R.id.ll_order)
    RelativeLayout ll_orders;

    @Bind(R.id.ll_profile)
    RelativeLayout ll_profile;

    @Bind(R.id.iv_notify)
    ImageView iv_notify;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    private int previous = -1;

    private BaseFragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_consumer_base);
        ButterKnife.bind(this);
        bodyContainerLayout = (FrameLayout) findViewById(R.id.base_body);
        bodyContainerLayout.addView(getLayoutInflater().inflate(layoutResID, null), 0, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * This method changes footer selected tab to selected colour
     *
     * @param index
     */

    public void setFooterState(int index) {
        if (index != previous) {
            resetSelectedFooterOption(previous);
            previous = index;
            switch (previous) {
                case AppConstant.FOOTER_INDEX.HOME:
                    manageFooterOptionSelection(cb_home, tv_home, true);
                    break;
                case AppConstant.FOOTER_INDEX.PROFILE:
                    manageFooterOptionSelection(cb_profile, tv_profile, true);
                    break;
                case AppConstant.FOOTER_INDEX.ORDER:
                    manageFooterOptionSelection(cb_orders, tv_orders, true);
                    break;

            }
        }
    }

    /**
     * This method changes footer unselected tab to default colour
     *
     * @param previous
     */

    private void resetSelectedFooterOption(int previous) {
        if (previous >= 0) {
            switch (previous) {
                case AppConstant.FOOTER_INDEX.HOME:
                    manageFooterOptionSelection(cb_home, tv_home, false);
                    break;
                case AppConstant.FOOTER_INDEX.PROFILE:
                    manageFooterOptionSelection(cb_profile, tv_profile, false);
                    break;
                case AppConstant.FOOTER_INDEX.ORDER:
                    manageFooterOptionSelection(cb_orders, tv_orders, false);
                    break;
            }
        }
    }

    public void manageFooterOptionSelection(CheckBox cb, TextView tv, boolean isSelected) {
        cb.setChecked(isSelected);
        if (isSelected) {
            tv.setTextColor(getColor(this, R.color.color_929292));
        } else {
            tv.setTextColor(getColor(this, R.color.black));
        }
    }

    @OnClick(R.id.ll_home)
    public void homeClick() {
        if (previous != AppConstant.FOOTER_INDEX.HOME) {
            setFooterState(AppConstant.FOOTER_INDEX.HOME);
            launchActivityMain(MapActivity.class);
        }
    }

    @OnClick(R.id.ll_order)
    public void ordersClick() {
        if (previous != AppConstant.FOOTER_INDEX.ORDER) {
            setFooterState(AppConstant.FOOTER_INDEX.ORDER);
            launchActivityMain(OrdersActivity.class);
        }
    }

    @OnClick(R.id.ll_profile)
    public void profileClick() {
        if (previous != AppConstant.FOOTER_INDEX.PROFILE) {
            setFooterState(AppConstant.FOOTER_INDEX.PROFILE);
            launchActivityMain(ProfileActivity.class);
        }
    }

    @OnClick(R.id.iv_notify)
    public void notifyClick() {
        launchActivity(NotificationActivity.class);
    }

    public void setCurrentFragment(int fragmentVal) {
        launchActivityMain(MapActivity.class);
    }

    public void setHeaderText(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_header_title.setText(title);
        }
    }

    public void managebackIconVisiblity(boolean toBeVisible) {
        if (iv_back != null) {
            if (toBeVisible) {
                iv_back.setVisibility(View.VISIBLE);
            } else {
                iv_back.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void backClick() {
        //this.finish();
onBackPressed();

    }

    @Override
    public void saveUserInfo(final UserResponse userResponse) {
        final ConsumerPreferenceKeeper keeper = ConsumerPreferenceKeeper.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (keeper != null) {
                    keeper.saveUser(userResponse);
                }
            }
        }).start();
    }

}
