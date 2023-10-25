package kr.co.eicn.ippbx.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapToLinkedHashMap {
    public <KEY extends Comparable<? super KEY>, VALUE> LinkedHashMap<KEY, VALUE> toLinkedHashMapByKey(Map<KEY, VALUE> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public <KEY, VALUE extends Comparable<? super VALUE>> LinkedHashMap<KEY, VALUE> toLinkedHashMapByValue(Map<KEY, VALUE> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }

    public <KEY, VALUE extends Comparable<? super VALUE>> LinkedHashMap<KEY, VALUE> toLinkedHashMapByValueReverse(Map<KEY, VALUE> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
    }
}
