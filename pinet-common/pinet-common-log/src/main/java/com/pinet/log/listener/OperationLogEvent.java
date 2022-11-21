package com.pinet.log.listener;

import com.pinet.log.dto.OperationLog;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 */
public class OperationLogEvent extends ApplicationEvent {
    public OperationLogEvent(OperationLog source) {
        super(source);
    }
}
