package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase{

  @Test
  public void testNewContact() {
    Contacts before = app.contact().all();
    File photo = new File("src/test/resources/stru.png");
    ContactData contact = new ContactData().withFirstname("Test").withMiddlename("Test1").withLastname("Test2").
            withNickname("Nick").withGroup(null).withPhoto(photo);
    app.goTo().newContactPage();
    app.contact().create(contact);
    Contacts after = app.contact().all();

    assertThat(after.size(), equalTo(before.size() + 1));
    assertThat(after, equalTo(before.withAdded(contact.
            withId(after.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

  }
}
