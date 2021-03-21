package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase{

  @Test
  public void testContactModification() {
    app.getNavigationHelper().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      app.getNavigationHelper().gotoNewContactPage();
      app.getContactHelper().createNewContact(new ContactData("Test", "Test1", "Test2", "Nick", null));
    }
    app.getContactHelper().selectContacts();
    app.getContactHelper().initContactModification();
    app.getContactHelper().fillContactForm(new ContactData("Test", "Test1", "Test2", "Nick", null), false);
    app.getContactHelper().submitContactModification();
    app.getContactHelper().returnToHomePage();
  }
}
