package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase{

  @BeforeMethod
  public void ensurePreconditions(){
    if (app.db().contacts().size() == 0) {
      app.goTo().newContactPage();
      app.contact().create(new ContactData().withFirstname("Test").withLastname("Test2").withNickname("krakrakra"));
    }
  }


  @Test
  public void testContactModification() {
    Contacts before = app.db().contacts();
    ContactData modifiedContact = before.iterator().next();
    ContactData contact = new ContactData().withId(modifiedContact.getId())
            .withFirstname("Test23").withMiddlename("blabla").withLastname("Test223")
            .withNickname("blablabla").withHomePhone("555").withEmail("bla@bla.ru");
    app.contact().modify(contact);
    Contacts after = app.db().contacts();

    assertThat(after.size(), equalTo(before.size()));
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
  }
}
