package org.ekstep.genieservices.commons.bean.telemetry;

import org.ekstep.genieservices.commons.utils.CollectionUtil;
import org.ekstep.genieservices.commons.utils.GsonUtil;
import org.ekstep.genieservices.commons.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 15/11/17.
 *
 * @author swayangjit
 */
public class Log extends Telemetry {

    private static final String EID = "LOG";

    private Log(String type, String level, String message, String pageid, List<Map<String, Object>> paramsList) {
        super(EID);
        setEData(createEData(type, level, message, pageid, paramsList));
    }

    protected Map<String, Object> createEData(String type, String level, String message, String pageid, List<Map<String, Object>> paramsList) {
        Map<String, Object> eData = new HashMap<>();
        eData.put("type", type);
        eData.put("level", level);
        eData.put("message", message);
        eData.put("pageid", !StringUtil.isNullOrEmpty(pageid) ? pageid : "");
        eData.put("params", !CollectionUtil.isNullOrEmpty(paramsList) ? paramsList : new ArrayList<>());
        return eData;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public interface Level {
        String TRACE = "TRACE";
        String DEBUG = "DEBUG";
        String INFO = "INFO";
        String WARN = "WARN";
        String ERROR = "ERROR";
        String FATAL = "FATAL";
    }

    public static class Builder {
        private String type;
        private String level;
        private String message;
        private String pageid;
        private String actorType;
        private List<Map<String, Object>> paramList;

        /**
         * Type of log (system, process, api_access, api_call, job, app_update etc)
         */
        public Builder type(String type) {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalArgumentException("type should not be null or empty.");
            }

            this.type = type;
            return this;
        }

        /**
         * Level of the log. TRACE, DEBUG, INFO, WARN, ERROR, FATAL
         */
        public Builder level(String level) {
            if (StringUtil.isNullOrEmpty(level)) {
                throw new IllegalArgumentException("level should not be null or empty.");
            }

            this.level = level;
            return this;
        }

        /**
         * Log message
         */
        public Builder message(String message) {
            if (StringUtil.isNullOrEmpty(message)) {
                throw new IllegalArgumentException("message should not be null or empty.");
            }

            this.message = message;
            return this;
        }

        /**
         * Page where the log event has happened
         */
        public Builder pageId(String pageid) {
            this.pageid = pageid;
            return this;
        }

        /**
         * Additional params in the log message
         */
        public Builder addParam(String key, Object value) {
            if (paramList == null) {
                paramList = new ArrayList<>();
            }
            if (key != null && value != null) {
                Map<String, Object> map = new HashMap<>();
                map.put(key, value);
                this.paramList.add(map);
            }

            return this;
        }

        /**
         * Type of actor who created the event
         */
        public Builder actorType(String actorType) {
            this.actorType = actorType;
            return this;
        }

        public Log build() {
            if (StringUtil.isNullOrEmpty(type)) {
                throw new IllegalStateException("type is required.");
            }

            if (StringUtil.isNullOrEmpty(level)) {
                throw new IllegalStateException("level is required.");
            }

            if (StringUtil.isNullOrEmpty(message)) {
                throw new IllegalStateException("message is required.");
            }

            Log event = new Log(type, level, message, pageid, paramList);
            event.setActor(new Actor(actorType));
            return event;
        }
    }
}
