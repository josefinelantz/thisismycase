package se.lantz.thisismycase.service.Utils;

import se.lantz.thisismycase.model.Status;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

import java.util.Collection;

import static se.lantz.thisismycase.model.Status.*;

/*
Denna klass ska ha hand om alla hjälpmetoder typ.
 */

public class ToolBox {

    public static String checkStatus(String status) {
        String checkedStatus = null;
        switch (status) {
            case Open:
                checkedStatus = Open;
                break;
            case Unstarted:
                checkedStatus = Unstarted;
                break;
            case Started:
                checkedStatus = Started;
                break;
            case Status.Closed:
                checkedStatus = Closed;
                break;
        }
        return checkedStatus;
    }

    public static boolean validUserName(String userName) {
        //return (userName.trim().length() >= 10);
        return true;
	}

    public static boolean userHasCapacity(Collection<WorkItem> workItems) {
        //return (workItems.size() < 5);
        return true;
    }

    public static boolean okToAddUser(Team team, User user) {
        //return (team.getUsers().size() < 5 && user.getTeam() == null);
        return true;
    }
}
