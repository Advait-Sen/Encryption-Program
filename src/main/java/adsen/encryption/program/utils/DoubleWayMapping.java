package adsen.encryption.program.utils;


import java.util.HashMap;

/**
 * This file is purely academic, because I felt that it was a cool idea to have it and that it was an interesting challenge.
 * It basically maps one set to another, with no duplicate keys on any side, allowing for quick 2-way lookups.
 */
public interface DoubleWayMapping<A, B> {
    /**
     * The number of value-value mappings in this Mapping
     * @return the number of value-value entries in this map
     */
    int size();


}