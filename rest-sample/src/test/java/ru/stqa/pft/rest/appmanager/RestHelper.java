package ru.stqa.pft.rest.appmanager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import ru.stqa.pft.rest.model.Issue;

import java.io.IOException;
import java.util.Set;

public class RestHelper{

  private final ApplicationManager app;

  public RestHelper(ApplicationManager app) {
    this.app = app;
  }


  private Executor getExecutor() {
    return Executor.newInstance().auth("288f44776e7bec4bf44fdfeb1e646490", "");
  }

  public int createIssue(Issue newIssue) throws IOException {
    String json = getExecutor().execute(Request.Post(String.format("%s/api/issues.json", app.getProperty("bugify.url")))
            .bodyForm(new BasicNameValuePair("subject", newIssue.getSubject()),
                    new BasicNameValuePair("description", newIssue.getDescription())))
            .returnContent().asString();
    JsonElement parsed = JsonParser.parseString(json);
    return parsed.getAsJsonObject().get("issue_id").getAsInt();
  }

  public String statusIssue(int issueId) throws IOException {
    String json = getExecutor().execute(Request.Get(String.format("%s/api/issues/%s.json", app.getProperty("bugify.url"), issueId))).returnContent().asString();
    JsonElement parsed = JsonParser.parseString(json);
    JsonElement issuesArray = parsed.getAsJsonObject().get("issues");
    JsonElement issue = issuesArray.getAsJsonArray().get(0);
    return issue.getAsJsonObject().get("state_name").getAsString();
  }

  public Set<Issue> getIssues() throws IOException {
    String json = getExecutor().execute(Request.Get("https://bugify.stqa.ru/api/issues.json"))
            .returnContent().asString();
    JsonElement parsed = JsonParser.parseString(json);
    JsonElement issues = parsed.getAsJsonObject().get("issues");
    return new Gson().fromJson(issues, new TypeToken<Set<Issue>>() {}.getType());
  }
}
