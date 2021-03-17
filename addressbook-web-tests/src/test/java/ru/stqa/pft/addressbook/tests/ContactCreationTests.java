package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase{

  @Test
  public void testNewContact() {
    app.getNavigationHelper().gotoNewContactPage();
    app.getContactHelper().createNewContact(new ContactData("Test", "Test1", "Test2", "Nick", null));
  }
}
