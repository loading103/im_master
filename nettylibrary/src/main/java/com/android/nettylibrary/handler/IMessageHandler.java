package com.android.nettylibrary.handler;


import com.android.nettylibrary.bean.IMMessageBean;

public interface IMessageHandler {

    void execute(IMMessageBean message);
}
