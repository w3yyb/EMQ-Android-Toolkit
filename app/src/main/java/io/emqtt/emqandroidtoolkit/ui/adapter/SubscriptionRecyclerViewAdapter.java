package io.emqtt.emqandroidtoolkit.ui.adapter;

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


    public SubscriptionRecyclerViewAdapter(List<Subscription> items, OnListFragmentInteractionListener listener) {
        mSubscriptionList = items;
        mArrayMap = new ArrayMap<>();
        for (Subscription subscription : mSubscriptionList) {
            mArrayMap.put(subscription.getTopic(), mSubscriptionList.indexOf(subscription));
        }
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

        final int pos = holder.getAdapterPosition();
        final Subscription subscription = mSubscriptionList.get(position);

        holder.topicText.setText("Topic:" + subscription.getTopic());
        holder.QoSText.setText("QoSText:" + subscription.getQoS());
        if (null != subscription.getMessage()) {
            holder.payloadText.setText(subscription.getMessage().getPayload());
            holder.timeText.setText(subscription.getMessage().getUpdateTime());
        }else {
            holder.payloadText.setText("");
            holder.timeText.setText("");
        }

        holder.moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(holder, pos, subscription);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(pos,subscription);
                }
            }
        });

        holder.popMenu.getMenu().findItem(R.id.action_subscribe).setEnabled(!subscription.isSubscribed());
        holder.popMenu.getMenu().findItem(R.id.action_unsubscribe).setEnabled(subscription.isSubscribed());
    }

    @Override
    public int getItemCount() {
        return mSubscriptionList.size();
    }

    private void showMenu(ViewHolder holder, final int position, final Subscription subscription) {
        holder.popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        if (mListener != null) {
                            mListener.onItemDelete(position, subscription);
                        }
                        removeData(position);

                        return true;

                    case R.id.action_subscribe:
                        if (mListener != null) {
                            mListener.onItemSubscribe(position, subscription);
                        }
                        return true;

                    case R.id.action_unsubscribe:
                        if (mListener != null) {
                            mListener.onItemUnsubscribe(position,subscription);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
        holder.popMenu.show();
    }

    public void addData(Subscription subscription) {
        if (mArrayMap.containsKey(subscription.getTopic())) {
            mSubscriptionList.set(mArrayMap.get(subscription.getTopic()), subscription);
            notifyItemChanged(mArrayMap.get(subscription.getTopic()));
        } else {
            mSubscriptionList.add(subscription);
            mArrayMap.put(subscription.getTopic(), mSubscriptionList.size() - 1);
            notifyItemInserted(mSubscriptionList.size() - 1);
        }
    }

    public void updateSubscription(Subscription subscription) {
        int pos = mArrayMap.get(subscription.getTopic());
        notifyItemChanged(pos);

    }

    public void updateData(EmqMessage emqMessage) {
        String topic = emqMessage.getTopic();
        int position = mArrayMap.get(topic);
        Subscription subscription = mSubscriptionList.get(position);
        if (emqMessage.getMqttMessage() == null) {
            emqMessage.setPayload("");
            emqMessage.setUpdateTime("");
        }
        subscription.setMessage(emqMessage);
        notifyItemChanged(position, subscription);

    }

    private void removeData(int position){
        mSubscriptionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());

    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.topic) TextView topicText;
        @BindView(R.id.QoS) TextView QoSText;
        @BindView(R.id.payload) TextView payloadText;
        @BindView(R.id.time) TextView timeText;
        @BindView(R.id.more) ImageView moreImage;

        PopupMenu popMenu;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            popMenu = new PopupMenu(itemView.getContext(), moreImage);
            popMenu.inflate(R.menu.menu_subscription);


        }


    }
}
