package com.github.ruediste.jreact;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class OptionalN<T> {

    private static final OptionalN<Object> EMPTY = new OptionalN<>(null, false);

    private final T value;
    private final boolean present;

    private OptionalN(T value, boolean present) {
        this.value = value;
        this.present = present;
    }

    public static <T> OptionalN<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalN<T> t = (OptionalN<T>) EMPTY;
        return t;
    }

    public static <T> OptionalN<T> of(T value) {
        return new OptionalN<>(value, true);
    }

    public static <T> OptionalN<T> ofNullable(T value) {
        return value == null ? empty() : new OptionalN<>(value, true);
    }

    public T get() {
        if (!present)
            throw new NoSuchElementException("No value present");
        return value;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isEmpty() {
        return !present;
    }

    public void ifPresent(Consumer<? super T> action) {
        if (present)
            action.accept(value);
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (present)
            action.accept(value);
        else
            emptyAction.run();
    }

    public OptionalN<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!present)
            return this;
        return predicate.test(value) ? this : empty();
    }

    public <U> OptionalN<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!present)
            return empty();
        U result = mapper.apply(value);
        return result == null ? empty() : new OptionalN<>(result, true);
    }

    public <U> OptionalN<U> flatMap(Function<? super T, OptionalN<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!present)
            return empty();
        @SuppressWarnings("unchecked")
        OptionalN<U> result = (OptionalN<U>) mapper.apply(value);
        return Objects.requireNonNull(result);
    }

    public T orElse(T other) {
        return present ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return present ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (present)
            return value;
        throw Objects.requireNonNull(exceptionSupplier).get();
    }

    public Stream<T> stream() {
        if (!present)
            return Stream.empty();
        return Stream.of(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OptionalN<?>))
            return false;
        OptionalN<?> other = (OptionalN<?>) obj;
        if (present != other.present)
            return false;
        if (!present)
            return true;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return present ? Objects.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return present ? String.format("OptionalN[%s]", value) : "OptionalN.empty";
    }
}
