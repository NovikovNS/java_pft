package ru.stqa.pft.github;

import com.jcabi.github.*;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    Github github = new RtGithub("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");
    RepoCommits commits = github.repos().get(new Coordinates.Simple("NovikovNS", "java_pft")).commits();
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }

}
