package e2b.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.e2b.R;

/**
 * Created by gaurav on 6/3/17.
 */

public class MerchantDetailActivity extends ConsumerBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_merchant_detail);
    }
}
