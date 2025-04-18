package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/*
JOIN FETCH을 사용해서 N+1 문제 방지
Todo 엔티티와 연관된 User 엔티티를 fetch join해서 두 엔티티를 동시에 가져오는 쿼리 작성

EntityGraph를 사용해서 N+1 문제 방지
Todo 엔티티에서 가지고 있는 User 엔티티의 이름을 @EntityGraph 내부에 설정해서, 해당 쿼리를 실행할 때 User 엔티티를 함께 조회하도록 변경
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT t FROM Todo t ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN FETCH t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    int countById(Long todoId);
}
