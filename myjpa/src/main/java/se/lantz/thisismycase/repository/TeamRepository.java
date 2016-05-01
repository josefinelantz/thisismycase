package se.lantz.thisismycase.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.lantz.thisismycase.model.Team;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long>
{
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.users")
    List<Team> findAllTeams();
}
