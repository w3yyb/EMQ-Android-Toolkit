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
import io.emqtt.emqandroidtoolkit.model.EmqMessage;

/**
 * ClassName: MessageAdapter
 * Desc:
 * Created by zhiw on 2017/3/28.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<EmqMessage> mMessageList;

    public MessageAdapter(List<EmqMessage> messageList) {
        mMessageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        EmqMessage message = mMessageList.get(position);
        holder.payloadText.setText("Payload:\n" + message.getPayload());
        holder.timeText.setText(message.getUpdateTime());
        holder.qosText.setText("QoS:" + message.getQos());
        holder.retainedText.setText("Retained:" + message.isRetained());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void insertData(EmqMessage message) {
        notifyItemInserted(getItemCount());

    }

    public void deleteAll() {
        notifyDataSetChanged();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time) TextView timeText;
        @BindView(R.id.payload) TextView payloadText;
        @BindView(R.id.qos) TextView qosText;
        @BindView(R.id.retained) TextView retainedText;

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
