package se.lantz.thisismycase.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class TeamProvider implements MessageBodyReader<Team>, MessageBodyWriter<Team> {
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Team.class, new TeamAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAssignableFrom(Team.class);
	}

	@Override
	public long getSize(Team team, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return 0;
	}

	@Override
	public void writeTo(Team team, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
						OutputStream entityStream) throws IOException, WebApplicationException {
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream))) {
			gson.toJson(team, Team.class, writer);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isAssignableFrom(Team.class);
	}

	@Override
	public Team readFrom(Class<Team> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
						 InputStream entityStream) throws IOException, WebApplicationException {
		Team team = gson.fromJson(new InputStreamReader(entityStream), Team.class);
		return team;
	}

	private static final class TeamAdapter implements JsonSerializer<Team>, JsonDeserializer<Team> {
		@Override
		public Team deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonTeam = json.getAsJsonObject();
			String teamName = jsonTeam.get("teamName").getAsString();

		return new Team(teamName);
		}

		@Override
		public JsonElement serialize(Team team, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonTeam = new JsonObject();
			jsonTeam.addProperty("id", team.getId());
			jsonTeam.addProperty("teamId", team.getTeamId());
			jsonTeam.addProperty("teamName", team.getTeamName());

			return jsonTeam;
		}
	}
}