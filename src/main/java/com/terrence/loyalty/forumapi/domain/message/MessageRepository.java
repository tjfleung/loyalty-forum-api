package com.terrence.loyalty.forumapi.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m where m.username = :username order by m.postedDate desc")
    Set<Message> findMessagesByUsername(String username);
}
