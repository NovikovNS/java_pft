package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.Users;
import ru.stqa.pft.mantis.model.UsersData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ChangePasswordTests extends TestBase{

  @BeforeMethod
  public void startMailServer() {
    app.mail().start();
  }

  @Test
  public void testChangePassword () throws MessagingException, IOException {
    long now = System.currentTimeMillis();
    String passwordToChange = String.format("%s", now);
    Users before = app.db().users();
    before.removeIf(q -> q.getId() == 1);
    UsersData userForChange = before.iterator().next();
    String email = userForChange.getEmail();
    String user = userForChange.getUsername();
    app.login().loginAsAdmin();
    app.goTo().manageUserPage(userForChange);
    app.user().StartChangePasswordForUser();
    List<MailMessage> mailMessages = app.mail().waitForMail(1, 20000);
    String confirmationLink = findConfirmationLink(mailMessages, email);
    app.user().finishChangePasswordForUser(confirmationLink, passwordToChange);
    assertTrue(app.newSession().login(user, passwordToChange));

  }

  private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }

  @AfterMethod(alwaysRun = true)
  public void stopMailServer() {
    app.mail().stop();
  }
}
