package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactLinkToGroupTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().contacts().size() == 0) {
      app.goTo().newContactPage();
      app.contact().create(new ContactData().withFirstname("Test").withLastname("Test2").withNickname("krakrakra"));
    }

    if (app.db().groups().size() == 0) {
      app.goTo().groupPage();
      app.group().create((new GroupData().withName("TestGroup")));
    }
  }

  @Test
  public void testContactAddToGroup() {
    //получение списка всех существующих групп
    Groups allGroups = app.db().groups();
    //получение всех контактов
    Contacts allContactsBefore = app.db().contacts();
    boolean needToCreateGroup = false;
    for (ContactData contact : allContactsBefore) {
      //список групп в которых контакт уже состоит
      Groups contactInGroups = contact.getGroups();
      //разница групп вхождения контакта из общего количества групп
      allGroups.removeAll(contactInGroups);
      if (allGroups.size() > 0) {
        //получение группы для добавления в неё контакта
        GroupData groupToAdd = allGroups.iterator().next();
        //получение идентификатора этого контакта
        int ContactId = contact.getId();
        //добавление контакта в группу
        app.goTo().homePage();
        app.contact().addToGroup(contact, groupToAdd);
        //получение контакта из БД после добавления его в группу
        ContactData addedContactAfter = app.db().contactWithId(ContactId);
        //проверка количества групп вхождения для контакта
        assertThat(addedContactAfter.getGroups().size(), equalTo(contact.getGroups().size() + 1));
        //проверка списка групп вхождения контакта до и после добавления в группу
        assertThat(addedContactAfter.getGroups(), equalTo(contactInGroups.withAdded(groupToAdd)));
        needToCreateGroup = false;
        break;
      } else {
        needToCreateGroup = true;
      }
    }

    if (needToCreateGroup) {
      //создание новой группы
      GroupData newTestGroup = new GroupData().withName("NewTestGroup");
      app.goTo().groupPage();
      app.group().create(newTestGroup);
      //повторное получение списка всех групп
      Groups newAllGroups = app.db().groups();
      //получение группы для добавления в неё контакта
      int id = newAllGroups.stream().mapToInt((g) -> g.getId()).max().getAsInt();
      GroupData groupToAdd = app.db().groupWithId(id);
      //выбор контакта для добавления в группу
      ContactData addedContactBefore = allContactsBefore.iterator().next();
      //получение идентификатора этого контакта
      int ContactId = addedContactBefore.getId();
      //список групп в которых контакт уже состоит
      Groups contactInGroups = addedContactBefore.getGroups();
      //добавление контакта в группу
      app.goTo().homePage();
      app.contact().addToGroup(addedContactBefore, groupToAdd);
      //получение контакта из БД после добавления его в группу
      ContactData addedContactAfter = app.db().contactWithId(ContactId);
      //проверка количества групп вхождения для контакта
      assertThat(addedContactAfter.getGroups().size(), equalTo(addedContactBefore.getGroups().size() + 1));
      //проверка множества групп вхождения контакта до и после добавления в группу
      assertThat(addedContactAfter.getGroups(), equalTo(contactInGroups.withAdded(groupToAdd)));
    }
  }

  @Test
  public void testContactDeleteFromGroup() {
    boolean needToCreateLink = false;
    //получение списка всех существующих групп
    Groups allGroupsBefore = app.db().groups();
    //получение всех контактов
    Contacts allContacts = app.db().contacts();
    for (GroupData group : allGroupsBefore) {
      //список контактов, которые состоят в группе
      Contacts allContactsInGroupBefore = group.getContacts();
      if (allContactsInGroupBefore.size() > 0) {
        //получение идентификатора группы
        int idGroup = group.getId();
        //получение контакта для удаления из группы
        ContactData contactToDeleteFromGroup = allContactsInGroupBefore.iterator().next();
        //удаление контакта из группы
        app.goTo().homePage();
        app.contact().deleteSelectedContactFromGroup(contactToDeleteFromGroup, group);
        //получение группы по id из БД после удаления контакта
        GroupData groupAfterDeleteContact = app.db().groupWithId(idGroup);
        //проверка количества контактов в группе после удаления
        assertThat(groupAfterDeleteContact.getContacts().size(), equalTo(allContactsInGroupBefore.size() - 1));
        //проверка множества контактов вхождения в группу до и после удаления контакта
        assertThat(groupAfterDeleteContact.getContacts(), equalTo(allContactsInGroupBefore.without(contactToDeleteFromGroup)));
        needToCreateLink = false;
        break;
      } else {
        needToCreateLink = true;
      }
    }

    if (needToCreateLink) {
      //выбор контакта и группы для связки
      ContactData contactToAddBeforeLink = allContacts.iterator().next();
      GroupData groupToAddBeforeLink = allGroupsBefore.iterator().next();
      int idContactToAdd = contactToAddBeforeLink.getId();
      int idGroupToAdd = groupToAddBeforeLink.getId();
      //список групп в которых контакт уже состоит
      Groups contactToAddBeforeInGroups = contactToAddBeforeLink.getGroups();
      //связка контакта и группы
      app.goTo().homePage();
      app.contact().addToGroup(contactToAddBeforeLink, groupToAddBeforeLink);
      //проверка добавления контакта в группу
      ContactData contactToAddAfter = app.db().contactWithId(idContactToAdd);
      GroupData groupToAddAfter = app.db().groupWithId(idGroupToAdd);
      assertThat(contactToAddAfter.getGroups(), equalTo(contactToAddBeforeInGroups.withAdded(groupToAddBeforeLink)));
      //контакты, входящие в группу
      Contacts allContactsInGroupBefore = groupToAddAfter.getContacts();
      //удаление добавленного контакта из группы
      app.goTo().homePage();
      app.contact().deleteSelectedContactFromGroup(contactToAddAfter, groupToAddAfter);

      //получение группы по id из БД после удаления контакта
      GroupData groupAfterDeleteContact = app.db().groupWithId(idGroupToAdd);
      //проверка количества контактов в группе после удаления
      assertThat(groupAfterDeleteContact.getContacts().size(), equalTo(allContactsInGroupBefore.size() - 1));
      //проверка множества контактов вхождения в группу до и после удаления контакта
      assertThat(groupAfterDeleteContact.getContacts(), equalTo(allContactsInGroupBefore.without(contactToAddAfter)));
    }
  }
}
