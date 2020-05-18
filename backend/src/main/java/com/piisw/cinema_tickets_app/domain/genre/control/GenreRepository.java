package com.piisw.cinema_tickets_app.domain.genre.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
    List<Genre> findAllByNameInAndObjectState(Collection<String> name, ObjectState objectState);
    List<Genre> findAllByNameLikeAndObjectStateIn(String searchText, Collection<ObjectState> objectState);
}