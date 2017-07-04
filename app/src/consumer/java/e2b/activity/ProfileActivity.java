package e2b.activity;

import android.os.Bundle;

import com.e2b.R;
import com.e2b.fragments.BaseFragment;
import com.e2b.utils.AppConstant;

import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;

public class ProfileActivity extends ConsumerBaseActivity {

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EScreenType eScreenType = EScreenType.values()[EScreenType.PROFILE_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, true);
        setFooterState(AppConstant.FOOTER_INDEX.PROFILE);
    }

}
