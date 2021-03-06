package com.terrence.loyalty.forumapi.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUsernameOrderByPostedDateDesc(String username);

    List<Message> findAllByUsernameAndParentIdOrderByPostedDateDesc(String username, long parentId);

    List<Message> findAllByParentIdEqualsOrderByPostedDateDesc(long parentId);
}
