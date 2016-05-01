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
import se.lantz.thisismycase.model.WorkItem;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class WorkItemListProvider implements MessageBodyWriter<ArrayList<WorkItem>>
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new WorkItemListAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		if (type.isAssignableFrom(ArrayList.class) && genericType instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();

			if (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(WorkItem.class))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public long getSize(ArrayList<WorkItem> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public void writeTo(ArrayList<WorkItem> workItems, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(workItems, ArrayList.class, writer);
		}
	}

	private static final class WorkItemListAdapter implements JsonSerializer<ArrayList<WorkItem>>
	{
		@Override
		public JsonElement serialize(ArrayList<WorkItem> workItems, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonArray jsonWorkItems = new JsonArray();

			workItems.forEach(workItem -> {
				JsonObject jsonWorkItem = new JsonObject();
				/*JsonArray jsonIssues = new JsonArray();
				JsonObject jsonUser = new JsonObject();

				workItem.getIssues().forEach(issue -> {
					JsonObject jsonIssue = new JsonObject();
					jsonIssue.add("entityId", new JsonPrimitive(issue.getEntityId()));
					jsonIssue.add("issueDescription", new JsonPrimitive(issue.getIssueDescription()));
					jsonIssues.add(jsonIssue);
				});*/

				jsonWorkItem.add("workItemId", new JsonPrimitive(workItem.getWorkItemId()));
				jsonWorkItem.add("description", new JsonPrimitive(workItem.getDescription()));
				jsonWorkItem.add("status", new JsonPrimitive(String.valueOf(workItem.getStatus())));
				//jsonWorkItem.add("issues", jsonIssues);
				
				/*if(workItem.getUser() != null)
				{
					User user = workItem.getUser();
					jsonUser.add("entityId", new JsonPrimitive(user.getEntityId()));
					jsonUser.add("userName", new JsonPrimitive(user.getUsername()));
					jsonUser.add("firstName", new JsonPrimitive(user.getFirstName()));
					jsonUser.add("lastName", new JsonPrimitive(user.getLastName()));
					jsonWorkItem.add("user", jsonUser);
				}*/
				
				jsonWorkItems.add(jsonWorkItem);
			});

			return jsonWorkItems;
		}

	}

}
