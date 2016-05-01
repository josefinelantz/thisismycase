package se.lantz.thisismycase.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TeamListProvider implements MessageBodyWriter<ArrayList<Team>> {

    private Gson gson;

    public TeamListProvider() {
        gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new TeamListAdapter()).create();
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

            if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Team.class)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public long getSize(ArrayList<Team> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(ArrayList<Team> teams, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream))) {
            gson.toJson(teams, ArrayList.class, writer);
        }
    }

    private static final class TeamListAdapter implements JsonSerializer<ArrayList<Team>> {
        @Override
        public JsonElement serialize(ArrayList<Team> teams, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray jsonTeams = new JsonArray();
            teams.forEach(team -> {
                JsonObject jsonTeam = new JsonObject();
                jsonTeam.add("teamId", new JsonPrimitive(team.getTeamId()));
                jsonTeam.add("teamName", new JsonPrimitive(team.getTeamName()));
                JsonArray jsonUsers = new JsonArray();

                team.getUsers().forEach(user -> {
                    JsonObject jsonUser = new JsonObject();
                    jsonUser.add("userId", new JsonPrimitive(user.getUserId()));
                    jsonUser.add("username", new JsonPrimitive(user.getUsername()));
                    jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
                    jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));
                    jsonUsers.add(jsonUser);
                });
                jsonTeam.add("users", jsonUsers);
                jsonTeams.add(jsonTeam);
            });

            return jsonTeams;
        }
    }
}


