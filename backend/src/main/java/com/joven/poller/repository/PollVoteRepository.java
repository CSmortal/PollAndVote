package com.joven.poller.repository;

import com.joven.poller.entity.Poll;
import com.joven.poller.entity.PollOption;
import com.joven.poller.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote,Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT count(poll_vote_id) FROM poll_vote pV WHERE pV.poll_option_id = :pollOptionId"
    )
    public Integer countPollVotesForPollOption(@Param("pollOptionId") Long pollOptionId);

}
