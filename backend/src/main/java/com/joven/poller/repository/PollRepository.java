package com.joven.poller.repository;

import com.joven.poller.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PollRepository extends JpaRepository<Poll,Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Poll p SET p.hasEnded = true WHERE p.pollId = :pollId")
    public void endPoll(@Param("pollId") Long pollId);

}
