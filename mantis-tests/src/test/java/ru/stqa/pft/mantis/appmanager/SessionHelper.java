package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class SessionHelper extends HelperBase {

  public SessionHelper(ApplicationManager app) {
    super(app);
  }

  public void loginAsAdmin() {
    type(By.id("username"), app.getProperty("web.adminLogin"));
    click(By.xpath("//input[@value='Login']"));
    type(By.id("password"), app.getProperty("web.adminPassword"));
    click(By.xpath("//input[@value='Login']"));
  }

}
