package com.fintrack.model;

/**
 * Anything that has an identifier assigned when it is saved.
 *
 * @param <ID> the type of the identifier
 */
public interface Identifiable<ID> {

    ID getId();

    void setId(ID id);
}