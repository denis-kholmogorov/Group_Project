package project.models.util.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseDataMap extends Response {

    private final Map<String, ? super Object> data;

    public ResponseDataMap() {
        this.data = new LinkedHashMap<>();
    }

    public <T> Object put(String key, T value) {
        return this.data.put(key, value);
    }

    public <T> void put(T object) {
        JsonMapper mapper = new JsonMapper();
        JsonNode nodeUser = mapper.valueToTree(object);
        Iterator<String> iterator = nodeUser.fieldNames();
        while(iterator.hasNext()) {
            String fieldName = iterator.next();
            this.data.put(fieldName, nodeUser.get(fieldName));
        }
    }
}
