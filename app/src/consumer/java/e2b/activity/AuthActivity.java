package e2b.activity;

import android.content.Intent;
import android.os.Bundle;

import com.e2b.R;
import com.e2b.activity.BaseActivity;

import e2b.enums.EScreenType;
import e2b.fragments.FragmentFactory;
import e2b.intrface.ISaveUserInfo;
import e2b.model.response.UserResponse;

public class AuthActivity extends BaseActivity implements ISaveUserInfo {

    public UserResponse userResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        replaceFragment(R.id.container_auth, FragmentFactory.getInstance().getFragment(EScreenType.MOBILE_VERIFICATION));
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentFactory.getInstance().getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void saveUserInfo(UserResponse userResponse) {
        // TODO :: Save user data is prefrence for future use.
    }

    @Override
    public void saveUserInfo() {
        if(userResponse != null){
            saveUserInfo(userResponse);
        }
    }
}
