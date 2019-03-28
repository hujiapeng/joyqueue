package com.jd.journalq.handler.message;

import com.jd.laf.web.vertx.MessageHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 审计日志消息处理器
 * Created by yangyang115 on 18-7-30.
 */
public class AuditLogMessageHandler implements MessageHandler<AuditLogMessage> {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogMessageHandler.class);
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String type() {
        return "auditLogMessage";
    }

    @Override
    public void handle(final Message<AuditLogMessage> message) {

        if (message != null) {
            AuditLogMessage auditLog = message.body();
            Instant instant = auditLog.getTime().toInstant();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            if (auditLog.getTarget() != null) {
                logger.info(
                        String.format("%s在时间点%s为%s%s%s", auditLog.getUser(), localDateTime.format(FORMAT),
                                auditLog.getTarget(), auditLog.getType().description(), auditLog.message));
            } else {
                logger.info(String.format("%s在时间点%s%s%s", auditLog.getUser(), localDateTime.format(FORMAT),
                        auditLog.getType().description(), auditLog.message));
            }
        }
    }

}