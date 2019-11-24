package io.pivotal.demo.sko;


import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryOperation;
import org.apache.geode.cache.PartitionResolver;

import java.util.Map;
import java.util.Properties;


public class IdPartitionResolver implements PartitionResolver<Object, Object>, Declarable {
    private String field;

    public IdPartitionResolver() {
    }

    public void init(Properties props) {
        this.field = props.getProperty("field");
        if (null == this.field) {
            throw new IllegalArgumentException("Property 'field' must be set.");
        }
    }

    public void close() {
    }

    public String getName() {
        return IdPartitionResolver.class.getName();
    }

    public Object getRoutingObject(EntryOperation<Object, Object> opDetails) {
        Object key = opDetails.getKey();
        if (key instanceof Map) {
            Object partitionKey = ((Map)key).get(this.field);
            if (null != partitionKey) {
                return partitionKey;
            }
        }

        return key;
    }

    public String getField() {
        return this.field;
    }
}
