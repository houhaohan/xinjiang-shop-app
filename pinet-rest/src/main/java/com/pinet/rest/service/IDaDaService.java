package com.pinet.rest.service;

import com.imdada.open.platform.callback.internal.CallbackParam;

public interface IDaDaService {

    void callback(CallbackParam callbackParam);

    /**
     * 同步订单状态
     * @param callbackParam
     */
    void syncOrderStatus(CallbackParam callbackParam);
}
