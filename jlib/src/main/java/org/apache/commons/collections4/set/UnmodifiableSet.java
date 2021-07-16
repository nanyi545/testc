package org.apache.commons.collections4.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableSet<E>
        extends AbstractSerializableSetDecorator<E>
        implements Unmodifiable {

    /** Serialization version */
    private static final long serialVersionUID = 6499119872185240161L;

    /**
     * Factory method to create an unmodifiable set.
     *
     * @param <E> the element type
     * @param set  the set to decorate, must not be null
     * @return a new unmodifiable set
     * @throws IllegalArgumentException if set is null
     * @since 4.0
     */
    public static <E> Set<E> unmodifiableSet(final Set<? extends E> set) {
        if (set instanceof Unmodifiable) {
            @SuppressWarnings("unchecked") // safe to upcast
            final Set<E> tmpSet = (Set<E>) set;
            return tmpSet;
        }
        return new UnmodifiableSet<E>(set);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     *
     * @param set  the set to decorate, must not be null
     * @throws IllegalArgumentException if set is null
     */
    @SuppressWarnings("unchecked") // safe to upcast
    private UnmodifiableSet(final Set<? extends E> set) {
        super((Set<E>) set);
        System.out.println("-----hehe:it is a hack----");
    }

    //-----------------------------------------------------------------------
    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

}
