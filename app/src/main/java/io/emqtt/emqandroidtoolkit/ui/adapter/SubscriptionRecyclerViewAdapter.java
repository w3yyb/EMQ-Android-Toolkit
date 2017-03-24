package io.emqtt.emqandroidtoolkit.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.fragment.SubscriptionListFragment.OnListFragmentInteractionListener;


public class SubscriptionRecyclerViewAdapter extends RecyclerView.Adapter<SubscriptionRecyclerViewAdapter.ViewHolder> {

    private final List<Subscription> mSubscriptionList;
    private final OnListFragmentInteractionListener mListener;


    public SubscriptionRecyclerViewAdapter(List<Subscription> items, OnListFragmentInteractionListener listener) {
        mSubscriptionList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Subscription subscription = mSubscriptionList.get(position);

        holder.displayName.setText(subscription.getDisplayName());
        holder.topic.setText("Topic:" + subscription.getTopic());
        holder.QoS.setText("QoS:" + subscription.getQoS());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(subscription);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubscriptionList.size();
    }

    public void addData(Subscription subscription){
        mSubscriptionList.add(subscription);
        notifyItemInserted(getItemCount());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.display_name) TextView displayName;
        @BindView(R.id.topic) TextView topic;
        @BindView(R.id.QoS) TextView QoS;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }


    }
}
