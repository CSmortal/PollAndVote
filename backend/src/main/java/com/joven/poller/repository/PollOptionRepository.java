package com.joven.poller.repository;

import com.joven.poller.entity.Poll;
import com.joven.poller.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption,Long> {

    public List<PollOption> findPollOptionsByPoll(Poll poll);
}
