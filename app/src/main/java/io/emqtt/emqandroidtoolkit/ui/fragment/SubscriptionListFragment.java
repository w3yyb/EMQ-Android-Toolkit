package io.emqtt.emqandroidtoolkit.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.EmqMessage;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.activity.DashboardActivity;
import io.emqtt.emqandroidtoolkit.ui.adapter.SubscriptionRecyclerViewAdapter;
import io.emqtt.emqandroidtoolkit.ui.base.BaseFragment;

/**
 *
 */
public class SubscriptionListFragment extends BaseFragment {

    public static final String TAG = "Subscription";

    @BindView(R.id.subscription_list) RecyclerView mSubscriptionRecyclerView;

    private OnListFragmentInteractionListener mListener;
    private SubscriptionRecyclerViewAdapter mAdapter;

    private List<String> mTopicList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubscriptionListFragment() {
    }

    public static SubscriptionListFragment newInstance() {
        SubscriptionListFragment fragment = new SubscriptionListFragment();
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_subscription_list;
    }

    @Override
    protected void setUpView() {
        mSubscriptionRecyclerView.setLayoutManager(new LinearLayoutManager(fragmentActivity));
    }

    @Override
    protected void setUpData() {
        mAdapter = new SubscriptionRecyclerViewAdapter(((DashboardActivity) fragmentActivity).getSubscriptionList(), mListener);
        mSubscriptionRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addData(Subscription subscription){
        mAdapter.addData(subscription);
        mTopicList.add(subscription.getTopic());
    }

    public void updateData(EmqMessage emqMessage){
        if (mTopicList.contains(emqMessage.getTopic())) {
            mAdapter.updateData(emqMessage);
        }

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Subscription item);
    }
}
