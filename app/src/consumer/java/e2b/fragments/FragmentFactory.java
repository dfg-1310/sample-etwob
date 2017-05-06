package e2b.fragments;


import android.util.Log;

import com.e2b.fragments.BaseFragment;

import e2b.enums.EScreenType;

public class FragmentFactory {

    private String TAG = FragmentFactory.class.getSimpleName();
    private static FragmentFactory fragmentFactory;

    //fragments to be launched on authActivity
    private SignInFragment signinFragment;
    private ProfileSetupFragment signUpFragment;

    //fragments to be launched on MapActivity
    private GoogleMapFragment mapFragment;
    private OrdersFragment orderFragment;
    private ProfileFragment profileFragment;
    private MobileVerificationFragment mobileVerificationFragment;
    private OTPConfirmFragment otpConfirmFragment;
    private StoreInformationFragment storeInformationFragment;
    private NotificationsFragment notificationFragment;
    private BaseFragment baseFragment;

    public static FragmentFactory getInstance() {
        if (fragmentFactory == null) {
            fragmentFactory = new FragmentFactory();
        }
        return fragmentFactory;
    }

    /**
     * method to get fragment
     *
     * @param eScreenType
     * @return
     */
    public BaseFragment getFragment(EScreenType eScreenType) {
        switch (eScreenType) {
            case LOGIN_SCREEN:
                if (signinFragment == null) {
                    Log.i(TAG, SignInFragment.class.getSimpleName());
                    signinFragment = new SignInFragment();
                }
                baseFragment = signinFragment;
                break;

            case PROFILE_SETUP_SCREEN:
                if (signUpFragment == null) {
                    Log.i(TAG, ProfileSetupFragment.class.getSimpleName());
                    signUpFragment = new ProfileSetupFragment();
                }
                baseFragment = signUpFragment;
                break;

            case HOME_SCREEN:
                if (mapFragment == null)
                    mapFragment = new GoogleMapFragment();
                baseFragment = mapFragment;
                break;

            case ORDERS_SCREEN:
                if (orderFragment == null)
                    orderFragment = new OrdersFragment();
                baseFragment = orderFragment;
                break;

            case PROFILE_SCREEN:
                if (profileFragment == null)
                    profileFragment = new ProfileFragment();
                baseFragment = profileFragment;
                break;

            case MOBILE_VERIFICATION:
                mobileVerificationFragment = new MobileVerificationFragment();
                baseFragment = mobileVerificationFragment;
                break;

            case OTP_CONFRM:
                otpConfirmFragment = new OTPConfirmFragment();
                baseFragment = otpConfirmFragment;
                break;

            case STORE_INFO_SCREEN:
                if (storeInformationFragment == null)
                    storeInformationFragment = new StoreInformationFragment();
                baseFragment = storeInformationFragment;
                break;

            case NOTIFICATION_SCREEN:
                if (notificationFragment == null)
                    notificationFragment = new NotificationsFragment();
                baseFragment = notificationFragment;
                break;
        }

        setCurrentFragment(baseFragment);
        return baseFragment;
    }


    public BaseFragment getCurrentFragment() {
        return baseFragment;
    }

    public void setCurrentFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

}
