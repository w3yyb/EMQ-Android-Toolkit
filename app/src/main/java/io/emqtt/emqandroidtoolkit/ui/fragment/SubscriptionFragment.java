package io.emqtt.emqandroidtoolkit.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import io.emqtt.emqandroidtoolkit.R;
import io.emqtt.emqandroidtoolkit.ui.widget.QoSChooseLayout;

/**
 * ClassName: SubscriptionFragment
 * Desc:
 * Created by zhiw on 2017/3/24.
 */

public class SubscriptionFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_suscription, null);
        EditText topic = (EditText) view.findViewById(R.id.topic);
        QoSChooseLayout mQoSLayout = (QoSChooseLayout) view.findViewById(R.id.qos);
        builder.setView(view)
                .setPositiveButton(getString(R.string.subscribe),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).setNegativeButton("Cancel", null);
        return builder.create();
    }


}
