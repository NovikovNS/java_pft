package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class ManageUsers extends HelperBase{

  public ManageUsers(ApplicationManager app) {
    super(app);
  }

  public void StartChangePasswordForUser() {
    click(By.cssSelector("input[value = 'Reset Password']"));
  }

  public void finishChangePasswordForUser(String confirmationLink, String password) {
    wd.get(confirmationLink);
    type(By.name("password"), password);
    type(By.name("password_confirm"), password);
    click(By.cssSelector("button[type = 'submit']"));
  }
}
