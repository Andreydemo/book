package com.epam.cdp.dao.parameterSource;

import com.epam.cdp.entityHolder.EntityHolder;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;

import java.util.HashMap;
import java.util.Map;

public class DefaultEntitySqlParameterSource extends AbstractSqlParameterSource {
    private static final String EVENT_ID = "eventId";
    private static final String USER_ID = "userId";
    private EntityHolder entityHolder;
    private Map<String, Object> map = new HashMap<>();

    public DefaultEntitySqlParameterSource(EntityHolder entityHolder) {
        this.entityHolder = entityHolder;
    }

    @Override
    public boolean hasValue(String paramName) {
        if (getDefaultValue(paramName) != null) return true;
        return map.get(paramName) != null;
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object value = getDefaultValue(paramName);
        if (value != null) return value;
        return map.get(paramName);
    }

    public DefaultEntitySqlParameterSource addValue(String name, Object param) {
        map.put(name, param);
        return this;
    }

    private Object getDefaultValue(String paramName) {
        if (paramName.equals(EVENT_ID) && entityHolder.getEvent() != null)
            return entityHolder.getEvent().getId();
        if (paramName.equals(USER_ID) && entityHolder.getUser() != null)
            return entityHolder.getUser().getId();
        return null;
    }
}