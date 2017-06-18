package e2b.activity;

import android.os.Bundle;

import com.e2b.R;
import com.e2b.fragments.BaseFragment;

import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;

/**
 * Created by gaurav on 4/4/17.
 */

public class NotificationActivity extends ConsumerBaseActivity{

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setHeaderText("Notifications");
        EScreenType eScreenType = EScreenType.values()[EScreenType.NOTIFICATION_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, false);
        managebackIconVisiblity(true);
//        setFooterState(AppConstant.FOOTER_INDEX.ORDER);
    }
}
