package e2b.intrface;

import android.app.Dialog;


public interface ICustomCallback {

    void onOkClicked(Dialog dialog, float rating, String comment);

}
