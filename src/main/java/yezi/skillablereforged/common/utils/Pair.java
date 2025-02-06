package yezi.skillablereforged.common.utils;

public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getLeft() {
        return key;
    }

    public V getRight() {
        return value;
    }
}
