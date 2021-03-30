package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase{

  @Test(enabled = false)
  public void testContactDeletion() {
    app.goTo().gotoHomePage();
    if (! app.getContactHelper().isThereAContact()) {
      app.goTo().gotoNewContactPage();
      app.getContactHelper().createNewContact(new ContactData("Test", null, "Test2", null, null));
    }
    List<ContactData> before = app.getContactHelper().getContactList();
    app.getContactHelper().selectContacts(before.size() - 1);
    app.getContactHelper().deleteSelectedContacts();
    app.goTo().gotoHomePage();

    List<ContactData> after = app.getContactHelper().getContactList();
    Assert.assertEquals(after.size(), before.size() - 1);

    before.remove(before.size() - 1);
    Assert.assertEquals(before, after);
  }
}
