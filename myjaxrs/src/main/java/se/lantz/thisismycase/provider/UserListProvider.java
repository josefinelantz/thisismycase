package se.lantz.thisismycase.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
import se.lantz.thisismycase.model.User;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public final class UserListProvider implements MessageBodyWriter<ArrayList<User>>
{
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new UserListAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

			if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(User.class))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public long getSize(ArrayList<User> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(ArrayList<User> users, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(users, ArrayList.class, writer);
		}
	}

	private static final class UserListAdapter implements JsonSerializer<ArrayList<User>>
	{
		@Override
		public JsonElement serialize(ArrayList<User> users, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonUsers = new JsonArray();
			users.forEach(user -> {
				JsonObject jsonUser = new JsonObject();
				/*JsonArray jsonWorkItems = new JsonArray();
				user.getWorkItems().forEach(workItem -> {
					JsonObject jsonWorkItem = new JsonObject();
                    jsonWorkItem.add("workItemId", new JsonPrimitive(workItem.getEntityId()));
					jsonWorkItem.add("description", new JsonPrimitive(workItem.getDescription()));
					jsonWorkItem.add("status", new JsonPrimitive(workItem.getStatus()));
					jsonWorkItems.add(jsonWorkItem);
				});*/
				jsonUser.add("id", new JsonPrimitive(user.getId()));
				jsonUser.add("userId", new JsonPrimitive(user.getUserId()));
				jsonUser.add("username", new JsonPrimitive(user.getUsername()));
				jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
				jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));
				//jsonUser.add("workItems", jsonWorkItems);
				jsonUsers.add(jsonUser);
			});

			return jsonUsers;
		}
	}

}
