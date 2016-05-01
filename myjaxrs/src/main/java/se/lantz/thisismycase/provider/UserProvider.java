package se.lantz.thisismycase.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;
import se.lantz.thisismycase.model.Team;
import se.lantz.thisismycase.model.User;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public final class UserProvider implements MessageBodyReader<User>, MessageBodyWriter<User>
{
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserAdapter()).create();

	//============= MessageBodyWriter

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(User.class);
	}

	@Override
	public long getSize(User t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(User user, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(user, User.class, writer);
		}
	}

	//============= MessageBodyReader

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(User.class);
	}

	@Override
	public User readFrom(Class<User> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		User user = gson.fromJson(new InputStreamReader(entityStream), User.class);

		return user;
	}

	// GSON

	private static final class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User>
	{
		@Override
		public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject jsonUser = json.getAsJsonObject();
			//Long id = jsonUser.get("id").getAsLong();
			//String userId = jsonUser.get("userId").getAsString();
			String userName = jsonUser.get("username").getAsString();
			String firstName = jsonUser.get("firstName").getAsString();
			String lastName = jsonUser.get("lastName").getAsString();

			return new User(userName, firstName, lastName);
		}

		@Override
		public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonUser = new JsonObject();
            jsonUser.add("id", new JsonPrimitive(user.getId()));
			jsonUser.add("userId", new JsonPrimitive(user.getUserId()));
            jsonUser.add("username", new JsonPrimitive(user.getUsername()));
			jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
			jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));

			/*Team team = user.getTeam();
			if (team != null) {
				JsonElement jsonTeam = context.serialize(team);
				jsonUser.add("team", jsonTeam);
			}*/

			return jsonUser;
		}
	}
}