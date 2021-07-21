package adsen.encryption.program.utils;

import java.util.Map;

public class Pair<A, B> implements Map.Entry<A, B> {
    private final A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static Pair<Object, Object> of(Object a, Object b) {
        return new Pair<>(a, b);
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public A getLeft() {
        return a;
    }

    public B getRight() {
        return b;
    }

    @Override
    public A getKey() {
        return a;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B value) {
        return b = value;
    }

    public int hashCode() {
        int hashCodeMix = a.hashCode() ^ b.hashCode();

        return (hashCodeMix << 16) ^ hashCodeMix ^ (hashCodeMix >> 16);
    }
}
