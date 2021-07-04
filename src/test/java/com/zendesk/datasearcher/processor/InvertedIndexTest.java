package com.zendesk.datasearcher.processor;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.FieldUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InvertedIndexTest {
    private InvertedIndex invertedIndex = new InvertedIndex();

    private User user1;
    private User user2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Organization organization1;
    private Organization organization2;

    @BeforeMethod
    private void setUp() {
        Environment mockEnv = Mockito.mock(Environment.class);
        Mockito.when(mockEnv.getProperty(Mockito.eq("users.filepath"), Mockito.eq("users.json"))).thenReturn("users.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("tickets.filepath"), Mockito.eq("tickets.json"))).thenReturn("tickets.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("organizations.filepath"), Mockito.eq("organizations.json"))).thenReturn("organizations.json");
        invertedIndex.setEnv(mockEnv);
        invertedIndex.setJsonReader(new JsonFileReader());
        invertedIndex.setFieldUtil(new FieldUtil());

        user1 = TestData.getUser1();
        user2 = TestData.getUser2();
        ticket1 = TestData.getTicket1();
        ticket2 = TestData.getTicket2();
        organization1 = TestData.getOrganization1();
        organization2 = TestData.getOrganization2();
    }

    /**
     * The Organization InvertedIndex should be built properly, and we should be able to search value by field name.
     * Specifically, we should be able to search on fields whose value is:
     * 1) String - id, createdAt, etc
     * 2) boolean - sharedTickets
     * 3) list - tags, domainNames
     * 4) null(field not set) or empty(field value is empty). The null/empty field could be either on primitive field, or List field.
     * If the field name is invalid, 0 result should be returned;
     * If the field value is not found, 0 result should be returned;
     */
    @Test
    void shouldBuildOrganizationIndexProperly() {
        try {
            //on string fields
            List<Organization> searchResultById1 = invertedIndex.lookUpOrganizations("id", "119");
            Assert.assertEquals(searchResultById1.size(), 1);
            Assert.assertEquals(searchResultById1.get(0), organization1);
            List<Organization> searchResultById2 = invertedIndex.lookUpOrganizations("id", "120");
            Assert.assertEquals(searchResultById2.size(), 1);
            Assert.assertEquals(searchResultById2.get(0), organization2);

            List<Organization> searchResultByCreatedAt = invertedIndex.lookUpOrganizations("createdAt", "2016-01-15T04:11:08 -11:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), organization2);

            //on boolean field
            List<Organization> searchBySharedTicketsTrue = invertedIndex.lookUpOrganizations("sharedTickets", "true");
            Assert.assertEquals(searchBySharedTicketsTrue.size(), 0);

            List<Organization> searchBySharedTicketsFalse = invertedIndex.lookUpOrganizations("sharedTickets", "false");
            Assert.assertEquals(searchBySharedTicketsFalse.size(), 2);
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization1));
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization2));

            //on List(Tag, domainNames)
            List<Organization> searchByTag = invertedIndex.lookUpOrganizations("tags", "Erickson");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(organization1));

            List<Organization> searchByDomainNames = invertedIndex.lookUpOrganizations("domainNames", "bleeko.com");
            Assert.assertEquals(searchByDomainNames.size(), 1);
            Assert.assertTrue(searchByDomainNames.contains(organization1));

            //on empty field - on tags
            List<Organization> searchByEmptyTag = invertedIndex.lookUpOrganizations("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(organization2));

            //on null field - on domain_names
            List<Organization> searchByEmptyyDomainNames = invertedIndex.lookUpOrganizations("domainNames", "");
            Assert.assertEquals(searchByEmptyyDomainNames.size(), 1);
            Assert.assertTrue(searchByEmptyyDomainNames.contains(organization2));

            //on null field - on details
            List<Organization> searchByEmptyDetails = invertedIndex.lookUpOrganizations("details", "");
            Assert.assertEquals(searchByEmptyDetails.size(), 1);
            Assert.assertTrue(searchByEmptyDetails.contains(organization2));

            //on value not found
            List<User> searchOnNotFound = invertedIndex.lookUpUser("name", "who am I?");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field not existed
            List<User> searchOnInvalidFieldName = invertedIndex.lookUpUser("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);

        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * The User InvertedIndex should be built properly, and we should be able to search value by field name.
     * Specifically, we should be able to search on fields whose value is:
     * 1) String - id, createdAt, etc
     * 2) boolean - active, suspended, etc
     * 3) list - tags.
     * 4) null(field not set) or empty(field value is empty)
     * If the field name is invalid, 0 result should be returned;
     * If the field value is not found, 0 result should be returned;
     */
    @Test
    void shouldBuildUserIndexProperly() {
        try {
            //on string fields
            List<User> searchResultById = invertedIndex.lookUpUser("id", "1");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), user1);

            List<User> searchResultByCreatedAt = invertedIndex.lookUpUser("createdAt", "2016-04-15T05:19:46 -10:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), user1);

            List<User> searchResultByOrgId = invertedIndex.lookUpUser("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), user1);

            //on boolean field
            List<User> searchByActive = invertedIndex.lookUpUser("active", "true");
            Assert.assertEquals(searchByActive.size(), 2);
            Assert.assertTrue(searchByActive.contains(user1));
            Assert.assertTrue(searchByActive.contains(user2));

            List<User> searchBySuspended = invertedIndex.lookUpUser("suspended", "false");
            Assert.assertEquals(searchBySuspended.size(), 1);
            Assert.assertTrue(searchBySuspended.contains(user2));

            //on tags
            List<User> searchByTag = invertedIndex.lookUpUser("tags", "Sutton");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(user1));

            List<User> searchByCommonTag = invertedIndex.lookUpUser("tags", "common-tag");
            Assert.assertEquals(searchByCommonTag.size(), 2);
            Assert.assertTrue(searchByCommonTag.contains(user1));
            Assert.assertTrue(searchByCommonTag.contains(user2));

            //on empty field
            List<User> searchOnEmpty = invertedIndex.lookUpUser("signature", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(user2));

            //on null field
            List<User> searchOnNull = invertedIndex.lookUpUser("phone", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(user2));

            //on value not found
            List<User> searchOnNotFound = invertedIndex.lookUpUser("alias", "aaaaalias");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<User> searchOnInvalidFieldName = invertedIndex.lookUpUser("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);

        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * The Ticket InvertedIndex should be built properly, and we should be able to search value by field name.
     * Specifically, we should be able to search on fields whose value is:
     * 1) String - id, externalId, organizationId, etc
     * 2) boolean - hasIncidents, etc
     * 3) list - tags.
     * 4) null(field not set) or empty(field value is empty)
     * If the field name is invalid, 0 result should be returned;
     * If the field value is not found, 0 result should be returned;
     */
    @Test
    void shouldBuildTicketIndexProperly() {
        try {
            //on string fields
            List<Ticket> searchResultById = invertedIndex.lookUpTickets("id", "436bf9b0-1147-4c0a-8439-6f79833bff5b");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), ticket1);

            List<Ticket> searchResultByCreatedAt = invertedIndex.lookUpTickets("externalId", "9210cdc9-4bee-485f-a078-35396cd74063");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), ticket1);

            List<Ticket> searchResultByOrgId = invertedIndex.lookUpTickets("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), ticket1);

            List<Ticket> searchResultByType = invertedIndex.lookUpTickets("type", "incident");
            Assert.assertEquals(searchResultByType.size(), 2);
            Assert.assertTrue(searchResultByType.contains(ticket1));
            Assert.assertTrue(searchResultByType.contains(ticket2));

            //on boolean field
            List<Ticket> searchByHasIncidentTrue = invertedIndex.lookUpTickets("hasIncidents", "true");
            Assert.assertEquals(searchByHasIncidentTrue.size(), 0);

            List<Ticket> searchByHasIncidentFalse = invertedIndex.lookUpTickets("hasIncidents", "false");
            Assert.assertEquals(searchByHasIncidentFalse.size(), 2);
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket1));
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket2));

            //on tags
            List<Ticket> searchByTag = invertedIndex.lookUpTickets("tags", "Ohio");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(ticket1));

            //on empty tags
            List<Ticket> searchByEmptyTag = invertedIndex.lookUpTickets("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(ticket2));

            //on tags value not existed
            List<Ticket> searchByNotExistedTagValue = invertedIndex.lookUpTickets("tags", "not-existed-tag");
            Assert.assertEquals(searchByNotExistedTagValue.size(), 0);

            //on empty field
            List<Ticket> searchOnEmpty = invertedIndex.lookUpTickets("externalId", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(ticket2));

            //on null field
            List<Ticket> searchOnNull = invertedIndex.lookUpTickets("dueAt", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(ticket2));

            //on value not found
            List<Ticket> searchOnNotFound = invertedIndex.lookUpTickets("type", "what-type??");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<Ticket> searchOnInvalidFieldName = invertedIndex.lookUpTickets("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
