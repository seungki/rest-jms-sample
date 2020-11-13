package com.github.seungki.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * MAP <-> JSONObject 변환
 * 필요에 따라 Serialize 가능한 Object Type 사용.
 */
public class ObjectConverter {

    public static JSONObject convertToAMQ(Map<String, Object> dataMap) throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(dataMap);
        JSONObject jsonObject = mapper.readValue(json, JSONObject.class);

        return jsonObject;
    }

    public static Map<String, Object> convertFromAMQ(Object object) throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = (JSONObject)object;
        Map<String, Object> dataMap = mapper.readValue(jsonObject.toJSONString(), Map.class);

        return dataMap;
    }

}
