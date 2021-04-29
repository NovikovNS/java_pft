package ru.stqa.pft.mantis.appmanager;

import ru.stqa.pft.mantis.model.UsersData;

public class NavigationHelper extends HelperBase {

  public NavigationHelper(ApplicationManager app) {
    super(app);
  }

  public void manageUserPage(UsersData user) {
    wd.get(app.getProperty("web.baseURL") + String.format("manage_user_edit_page.php?user_id=%s", user.getId()));
  }

}
