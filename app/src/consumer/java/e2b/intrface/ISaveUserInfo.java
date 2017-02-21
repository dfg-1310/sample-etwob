package e2b.intrface;

import e2b.model.response.UserResponse;

/**
 * Created by gaurav on 21/2/17.
 */

public interface ISaveUserInfo {

    void saveUserInfo(UserResponse userResponse);
    void saveUserInfo();
}
