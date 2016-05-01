package se.lantz.thisismycase.model;
/*
Valde att fortsatt hantera dessa såhär istället för som enum.
I praktiken blir det ju samma sak. Hanterade dessa genom separata
metoder förut, men i och med denna labb, så måste jag kunna använda
setStatus() och genom att införa en validering (ToolBox.class) så ska det funka. Sen
är det kanske inte optimalt.
 */

public class Status {

    public static final String Inactive = "inactive";
    public static final String Active = "active";
    public static final String Unstarted = "unstarted";
    public static final String Started = "started";
    public static final String Done = "done";
    public static final String Open = "open";
    public static final String Closed = "closed";
}
