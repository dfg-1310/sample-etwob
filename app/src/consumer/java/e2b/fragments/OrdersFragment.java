package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e2b.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 6/2/17.
 */

public class OrdersFragment extends BaseFragment {

    private static final String TAG = OrdersFragment.class.getSimpleName();

    @Bind(R.id.order_listview)
    ListView orderListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOrders();
    }

    private void getOrders() {
        // TODO :: make api call and show on ui
    }
}
