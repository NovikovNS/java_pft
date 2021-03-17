package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactDeletionTests extends TestBase{

  @Test
  public void testContactDeletion() {
    app.getNavigationHelper().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      app.getNavigationHelper().gotoNewContactPage();
      app.getContactHelper().createNewContact(new ContactData("Test", "Test1", "Test2", "Nick", null));
    }
    app.getContactHelper().selectContacts();
    app.getContactHelper().deleteSelectedContacts();
  }
}
