package e2b.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.activity.BaseActivity;
import com.e2b.utils.AppConstant;

import butterknife.Bind;
import butterknife.OnClick;
import e2b.enums.EScreenType;
import e2b.fragments.BaseFragment;
import e2b.fragments.FragmentFactory;

public class HomeActivity extends BaseActivity {

    private int previous;

    @Bind(R.id.tv_home)
    TextView tv_home;

    @Bind(R.id.tv_orders)
    TextView tv_orders;

    @Bind(R.id.tv_profile)
    TextView tv_profile;

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



    private BaseFragment currentFragment;

    @OnClick(R.id.ll_home)
    public void homeClick() {
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
        setCurrentFragment(EScreenType.HOME_SCREEN.ordinal());
    }

    @OnClick(R.id.ll_order)
    public void ordersClick() {
        setFooterState(AppConstant.FOOTER_INDEX.ORDER);
        setCurrentFragment(EScreenType.ORDERS_SCREEN.ordinal());
    }

    @OnClick(R.id.ll_profile)
    public void profileClick() {
        setFooterState(AppConstant.FOOTER_INDEX.PROFILE);
        setCurrentFragment(EScreenType.PROFILE_SCREEN.ordinal());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setFooterState(AppConstant.FOOTER_INDEX.HOME);
        setCurrentFragment(EScreenType.HOME_SCREEN.ordinal());

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("home button click");
            }
        });
    }

   /**
     * This method changes footer selected tab to selected colour
     *
     * @param index
     */

    private void setFooterState(int index) {
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

    private void manageFooterOptionSelection(CheckBox cb, TextView tv, boolean isSelected) {
        cb.setChecked(isSelected);
        if (isSelected) {
            tv.setTextColor(getColor(this, R.color.color_CC42B757));
        } else {
            tv.setTextColor(getColor(this, R.color.color_929292));
        }
    }

    private void setCurrentFragment(int fragmentVal) {
        EScreenType eScreenType = EScreenType.values()[fragmentVal];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, false);
    }
}
