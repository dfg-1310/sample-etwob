package e2b.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e2b.R;
import com.e2b.api.ApiCallback;
import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
import com.e2b.fragments.BaseFragment;
import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.Error;

import butterknife.Bind;
import butterknife.ButterKnife;
import e2b.adapter.NotificationAdapter;
import e2b.model.response.Notifications;
import e2b.utils.DummyData;
import retrofit2.Call;

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

    private void getOrders() {
        activity.showProgressBar();
        IApiRequest request = ApiClient.getRequest();

        Call<BaseResponse<Notifications>> call = request.getNotifications();
        call.enqueue(new ApiCallback<Notifications>(activity) {
            @Override
            public void onSucess(Notifications notifications) {
                activity.hideProgressBar();
                notificationAdapter = new NotificationAdapter(getActivity(), notifications.getNotifications());
                orderListView.setAdapter(notificationAdapter);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Error error) {
                activity.hideProgressBar();
                activity.showToast(error.getMsg());
                Log.d(TAG, error.getMsg());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        getOrders();

    }

}
