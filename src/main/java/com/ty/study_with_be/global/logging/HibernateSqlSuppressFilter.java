package com.ty.study_with_be.global.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.MDC;
import org.slf4j.Marker;

public class HibernateSqlSuppressFilter extends TurboFilter {

    private static final String MDC_KEY = "suppressHibernateSql";

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (!isSuppressed()) return FilterReply.NEUTRAL;

        String name = logger.getName();
        if (name == null) return FilterReply.NEUTRAL;

        if (name.equals("org.hibernate.SQL") || name.equals("org.hibernate.orm.jdbc.bind")) {
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;
    }

    private boolean isSuppressed() {
        return "true".equals(MDC.get(MDC_KEY));
    }
}
