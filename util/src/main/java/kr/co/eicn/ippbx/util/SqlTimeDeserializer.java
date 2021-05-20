package kr.co.eicn.ippbx.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Time;

public class SqlTimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isEmpty(jp.getValueAsString()))
            return null;

        return Time.valueOf(jp.getValueAsString() + ":00");
    }
}
