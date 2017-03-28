package io.emqtt.emqandroidtoolkit.event;

import io.emqtt.emqandroidtoolkit.model.EmqMessage;

/**
 * ClassName: MessageEvent
 * Desc:
 * Created by zhiw on 2017/3/28.
 */

public class MessageEvent {

    private EmqMessage message;

    public MessageEvent(EmqMessage message) {
        this.message = message;
    }

    public EmqMessage getMessage() {
        return message;
    }
}
