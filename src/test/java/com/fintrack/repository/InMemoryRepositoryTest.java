package com.fintrack.repository;

import com.fintrack.exceptions.DataAccessException;
import com.fintrack.model.Identifiable;
import com.fintrack.model.MonthlyTransaction;
import com.fintrack.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryRepositoryTest {

    private static final LocalDate DATE = LocalDate.of(2026, 10, 29);

    private final InMemoryRepository<Transaction> repository = new InMemoryRepository<>();

    @Test
    void save_newEntities_getSequentialIds() {
        Transaction first = repository.save(expense("Rent"));
        Transaction second = repository.save(expense("Internet"));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void findById_returnsSavedEntity() {
        Transaction saved = repository.save(expense("Rent"));

        assertEquals("Rent", repository.findById(saved.getId()).orElseThrow().getDescription());
    }

    @Test
    void findById_unknownId_returnsEmpty() {
        assertTrue(repository.findById(99L).isEmpty());
    }

    @Test
    void save_unknownId_throwsDataAccessException() {
        Transaction ghost = expense("Ghost");
        ghost.setId(99L);

        assertThrows(DataAccessException.class, () -> repository.save(ghost));
    }

    @Test
    void deleteById_removesEntityAndReportsResult() {
        Transaction saved = repository.save(expense("Rent"));

        assertTrue(repository.deleteById(saved.getId()));
        assertFalse(repository.deleteById(saved.getId()));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void saveAll_acceptsCollectionOfSubtypes() {
        List<MonthlyTransaction> monthlies = List.of(
                new MonthlyTransaction("Salary", BigDecimal.TEN, true, DATE, 5),
                new MonthlyTransaction("Rent", BigDecimal.TEN, false, DATE, 10));

        repository.saveAll(monthlies);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void findAllInto_acceptsCollectionOfSupertypes() {
        repository.save(expense("Rent"));
        List<Object> target = new ArrayList<>();

        repository.findAllInto(target);

        assertEquals(1, target.size());
    }

    @Test
    void worksWithAnyIdentifiableType() {
        InMemoryRepository<Note> notes = new InMemoryRepository<>();

        Note saved = notes.save(new Note());

        assertEquals(1L, saved.getId());
    }

    private Transaction expense(String description) {
        return new Transaction(description, BigDecimal.TEN, false, DATE);
    }

    private static class Note implements Identifiable<Long> {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }
    }
}