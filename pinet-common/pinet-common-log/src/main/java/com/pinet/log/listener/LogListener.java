package com.pinet.log.listener;

import com.pinet.log.dto.OperationLog;
import com.pinet.log.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class LogListener {

    @Autowired
    private OperateLogService operateLogService;

    @Async
    @EventListener(OperationLogEvent.class)
    public void saveOperateLog(OperationLogEvent logEvent) {
        System.err.println("日志事件测试 1111111111111111111111111111111111111122");
//        Object obj = logEvent.getSource();
//        if (obj == null) return;
//        if (obj instanceof OperationLog) {
//            OperationLog operationLog = (OperationLog) obj;
//            operateLogService.save(operationLog);
//        }

    }
}
