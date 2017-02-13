package e2b.fragments;


import android.util.Log;

import e2b.enums.EScreenType;

public class FragmentFactory {

    private String TAG = FragmentFactory.class.getSimpleName();
    private static FragmentFactory fragmentFactory;

    //fragments to be launched on authActivity
    private BaseFragment baseFragment;
    private SignInFragment signinFragment;
    private SignUpFragment signUpFragment;

    //fragments to be launched on HomeActivity
    private GoogleMapFragment mapFragment;
    private OrdersFragment orderFragment;
    private ProfileFragment profileFragment;
    private MobileVerificationFragment mobileVerificationFragment;
    private OTPConfirmFragment otpConfirmFragment;

//    private NotificationFragment notificationFragment;
//    private HotTopicsFragment hotTopicsFragment;
//    private InboxTabFragment inboxTabFragment;
//    private GroupDetailFragment groupDetailFragment;
//
//    //fragments to be launched on SubActivity
//    private ProfileFragment profileFragment;
//    private StatusFragment statusFragment;
//    private EmojiFragment activityGridFragment;
//    private PrefrenceTabFragment prefrenceTabFragment;
//    private OtherProfileFragment otherProfileFragment;
//    private GroupRightDrawerFragment groupRightDrawerFragment;
//    private GroupMemberListFragment groupMemberListFragment;

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

            case SIGN_UP_SCREEN:
                if (signUpFragment == null) {
                    Log.i(TAG, SignUpFragment.class.getSimpleName());
                    signUpFragment = new SignUpFragment();
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

//            case PROFILE_SCREEN:
//                if (profileFragment == null)
//                    profileFragment = new ProfileFragment();
//                baseFragment = profileFragment;
//                break;
//
//            case ORDERS_SCREEN:
//                if (profileFragment == null)
//                    profileFragment = new GroupsFragment();
//                baseFragment = profileFragment;
//                break;
//
//            case GROUP_DETAIL_SCREEN:
//                if (groupDetailFragment == null)
//                    groupDetailFragment = new GroupDetailFragment();
//                baseFragment = groupDetailFragment;
//                break;
//            case NOTIFICATION_SCREEN:
//                if (notificationFragment == null)
//                    notificationFragment = new NotificationFragment();
//                baseFragment = notificationFragment;
//                break;
//
//            case Activity_SCREEN:
//                if (activityGridFragment == null)
//                    activityGridFragment = new EmojiFragment();
//                baseFragment = activityGridFragment;
//                break;
//
//            case PREFRENCE_SCREEN:
//                if (prefrenceTabFragment == null)
//                    prefrenceTabFragment = new PrefrenceTabFragment();
//                baseFragment = prefrenceTabFragment;
//                break;
//
//            case OTHER_PROFILE_SCREEN:
//                if (otherProfileFragment == null)
//                    otherProfileFragment = new OtherProfileFragment();
//                baseFragment = otherProfileFragment;
//                break;
//
//            case GROUP_RIGHT_DRAWER_SCREEN:
//                if (groupRightDrawerFragment == null)
//                    groupRightDrawerFragment = new GroupRightDrawerFragment();
//                baseFragment = groupRightDrawerFragment;
//                break;
//            case MEMBER_LIST_SCREEN:
//                if (groupMemberListFragment == null)
//                    groupMemberListFragment = new GroupMemberListFragment();
//                baseFragment = groupMemberListFragment;
//                break;
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
