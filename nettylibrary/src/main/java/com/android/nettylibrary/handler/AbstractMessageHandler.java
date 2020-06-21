package com.android.nettylibrary.handler;


import com.android.nettylibrary.bean.IMMessageBean;

public abstract class AbstractMessageHandler implements IMessageHandler {

    @Override
    public void execute(IMMessageBean message) {
        action(message);
    }

    protected abstract void action(IMMessageBean message);
}
