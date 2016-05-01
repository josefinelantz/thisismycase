package se.lantz.thisismycase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import se.lantz.thisismycase.service.*;

/*
Denna klass kändes nödvändig för att kunna testa på ett bra sätt.
Kändes lite overkill att ha ytterligare en abstraktion, men nu
såhär i efterhand tycker jag faktiskt att det blev bra.
 */

@Configuration
@EnableJpaRepositories("se.lantz.thisismycase.repository")
public class ServiceConfig
{

    @Bean
    public UserService userService()
    {
        return new UserServiceImpl();
    }

    @Bean
    public TeamService teamService()
    {
        return new TeamServiceImpl();
    }

    @Bean
    public WorkItemService workItemService()
    {
        return new WorkItemServiceImpl();
    }

    @Bean
    public IssueService issueService()
    {
        return new IssueServiceImpl();
    }
}
