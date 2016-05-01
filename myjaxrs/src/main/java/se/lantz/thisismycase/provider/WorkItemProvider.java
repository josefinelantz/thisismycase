package se.lantz.thisismycase.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
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
import se.lantz.thisismycase.model.Issue;
import se.lantz.thisismycase.model.User;
import se.lantz.thisismycase.model.WorkItem;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class WorkItemProvider implements MessageBodyWriter<WorkItem>, MessageBodyReader<WorkItem>
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(WorkItem.class, new WorkItemAdapter()).create();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(WorkItem.class);
	}

	@Override
	public long getSize(WorkItem workItem, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return 0;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		return type.isAssignableFrom(WorkItem.class);
	}

	@Override
	public WorkItem readFrom(Class<WorkItem> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException
	{
		WorkItem workItem = gson.fromJson(new InputStreamReader(entityStream), WorkItem.class);

		return workItem;
	}

	@Override
	public void writeTo(WorkItem workItem, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException, WebApplicationException
	{
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream)))
		{
			gson.toJson(workItem, WorkItem.class, writer);
		}
	}

	private static final class WorkItemAdapter implements JsonSerializer<WorkItem>, JsonDeserializer<WorkItem>

	{
		@Override
		public JsonElement serialize(WorkItem workItem, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonWorkItem = new JsonObject();
			jsonWorkItem.add("workItemId", new JsonPrimitive(workItem.getWorkItemId()));
			jsonWorkItem.add("description", new JsonPrimitive(workItem.getDescription()));
			jsonWorkItem.add("status", new JsonPrimitive(workItem.getStatus()));

			/*List<Issue> issues = workItem.getIssues();
			JsonArray jsonIssues = new JsonArray();

			if (issues != null) {
				issues.forEach(issue -> {
					JsonObject jsonIssue = new JsonObject();
					jsonIssue.add("entityId", new JsonPrimitive(issue.getEntityId()));
					jsonIssue.add("issueDescription", new JsonPrimitive(issue.getIssueDescription()));
					jsonIssues.add(jsonIssue);
				});
			}
			jsonWorkItem.add("issues", jsonIssues);*/
			return jsonWorkItem;
		}

		@Override
		public WorkItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject jsonWorkItem = json.getAsJsonObject();
            String workItemId = jsonWorkItem.get("workItemId").getAsString();
			String description = jsonWorkItem.get("description").getAsString();
			String status = jsonWorkItem.get("status").getAsString();

			return new WorkItem(description);
		}
	}
}
