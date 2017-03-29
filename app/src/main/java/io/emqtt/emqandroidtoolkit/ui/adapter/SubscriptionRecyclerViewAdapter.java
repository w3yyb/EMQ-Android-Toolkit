package io.emqtt.emqandroidtoolkit.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.model.EmqMessage;
import io.emqtt.emqandroidtoolkit.model.Subscription;
import io.emqtt.emqandroidtoolkit.ui.fragment.SubscriptionListFragment.OnListFragmentInteractionListener;


public class SubscriptionRecyclerViewAdapter extends RecyclerView.Adapter<SubscriptionRecyclerViewAdapter.ViewHolder> {

    private final List<Subscription> mSubscriptionList;
    private ArrayMap<String ,Integer> mArrayMap;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;


    public SubscriptionRecyclerViewAdapter(List<Subscription> items, OnListFragmentInteractionListener listener) {
        mSubscriptionList = items;
        mArrayMap=new ArrayMap<>();
        for (Subscription subscription : mSubscriptionList) {
            mArrayMap.put(subscription.getTopic(),mSubscriptionList.indexOf(subscription));
        }
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Subscription subscription = mSubscriptionList.get(position);

        if (subscription.getDisplayName() == null) {
            holder.displayNameText.setText(subscription.getTopic());
        } else {
            holder.displayNameText.setText(subscription.getDisplayName());
        }
        holder.topicText.setText("Topic:" + subscription.getTopic());
        holder.QoSText.setText("QoSText:" + subscription.getQoS());
        if (null != subscription.getMessage()) {
            holder.payloadText.setText(new String(subscription.getMessage().getMqttMessage().getPayload()));
            holder.timeText.setText(subscription.getMessage().getUpdateTime());
        }

        holder.moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v, position, subscription);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(position,subscription);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubscriptionList.size();
    }

    private void showMenu(View v, final int position, final Subscription subscription) {
        final PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.inflate(R.menu.menu_subscription);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        if (mListener != null) {
                            mListener.onItemUnsubcribe(position,subscription);
                        }

                        return true;

                    case R.id.action_subscribe:
                        if (mListener != null) {
                            mListener.onItemSubscribe(position, subscription);
                        }
                        return true;

                    case R.id.action_unsubscribe:
                        if (mListener != null) {
                            mListener.onItemUnsubcribe(position,subscription);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public void addData(Subscription subscription){
        if (mArrayMap.containsKey(subscription.getTopic())) {
            mSubscriptionList.set(mArrayMap.get(subscription.getTopic()), subscription);
        } else {
            mSubscriptionList.add(subscription);
            mArrayMap.put(subscription.getTopic(), mSubscriptionList.size() - 1);
        }
        notifyDataSetChanged();
    }

    public void updateData(EmqMessage emqMessage) {
        String topic = emqMessage.getTopic();
        int position = mArrayMap.get(topic);
        Subscription subscription = mSubscriptionList.get(position);
        subscription.setMessage(emqMessage);
        notifyItemChanged(position, subscription);


    }

    public void removeData(int position){
        mSubscriptionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());

    }



    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.display_name) TextView displayNameText;
        @BindView(R.id.topic) TextView topicText;
        @BindView(R.id.QoS) TextView QoSText;
        @BindView(R.id.payload) TextView payloadText;
        @BindView(R.id.time) TextView timeText;
        @BindView(R.id.more) ImageView moreImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

        }


    }
}
