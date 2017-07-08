package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import e2b.adapter.NotificationAdapter;
import e2b.utils.DummyData;

/**
 * Created by gaurav on 6/2/17.
 */

public class NotificationsFragment extends BaseFragment {

    private static final String TAG = NotificationsFragment.class.getSimpleName();

    @Bind(R.id.order_listview)
    RecyclerView orderListView;
    NotificationAdapter notificationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        ButterKnife.bind(this, view);

        orderListView.setLayoutManager(new LinearLayoutManager(activity));
        orderListView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getNotifications() {
        // TODO :: make api call and show on ui

        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        Call<BaseResponse<Orders>> call = request.getOrders();
        call.enqueue(new ApiCallback<Orders>(activity) {
            @Override
            public void onSucess(Orders orders) {
                activity.hideProgressBar();
                showData(orders);
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }
        });



        notificationAdapter = new NotificationAdapter(getActivity(), DummyData.getNotifications());
        orderListView.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        getOrders();

    }

}
