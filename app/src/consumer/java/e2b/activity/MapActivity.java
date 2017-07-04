package e2b.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.e2b.R;
import com.e2b.activity.CameraActivity;
import com.e2b.fragments.BaseFragment;
import com.e2b.utils.AppConstant;
import com.e2b.utils.BitmapUtils;

import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;
import e2b.fragments.GoogleMapFragment;
import e2b.model.response.Merchant;

public class MapActivity extends ConsumerBaseActivity {

    private BaseFragment currentFragment;
    private Merchant selectedMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EScreenType eScreenType = EScreenType.values()[EScreenType.HOME_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, true);
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
    }

    public Merchant getSelectedMerchant() {
        return selectedMerchant;
    }

    public void markerClick(Merchant merchant) {
        this.selectedMerchant = merchant;
        EScreenType eScreenType = EScreenType.values()[EScreenType.STORE_INFO_SCREEN.ordinal()];
        this.currentFragment = FragmentFactory.getInstance().getFragment(eScreenType);
        replaceFragment(R.id.container_home, currentFragment, true);
        setFooterState(AppConstant.FOOTER_INDEX.HOME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppConstant.REQUEST_CODE.MAP_LOCATION_PERMISSION:
                if(currentFragment instanceof GoogleMapFragment){
                    ((GoogleMapFragment)currentFragment).currentLocation();
                }
        }
    }
}
