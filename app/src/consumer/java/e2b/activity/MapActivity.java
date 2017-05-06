package e2b.activity;

import android.os.Bundle;

import com.e2b.R;
import com.e2b.fragments.BaseFragment;
import com.e2b.utils.AppConstant;

import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;
import e2b.model.response.Merchant;

public class MapActivity extends ConsumerBaseActivity {

    private BaseFragment currentFragment;
    private Merchant selectedMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setHeaderText("General Store");
        EScreenType eScreenType = EScreenType.values()[EScreenType.HOME_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, false);
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
    }

    public Merchant getSelectedMerchant() {
        return selectedMerchant;
    }

    public void markerClick(Merchant merchant) {
        this.selectedMerchant = merchant;
        setHeaderText("Store Information");
        EScreenType eScreenType = EScreenType.values()[EScreenType.STORE_INFO_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, false);
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
    }
}
