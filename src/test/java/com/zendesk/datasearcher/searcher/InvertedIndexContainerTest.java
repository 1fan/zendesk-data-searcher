package com.zendesk.datasearcher.searcher;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.FieldUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InvertedIndexContainerTest {
    private InvertedIndexContainer invertedIndexContainer = new InvertedIndexContainer();

    private User user1;
    private User user2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Organization organization1;
    private Organization organization2;

    @BeforeMethod
    private void setUp() {
        invertedIndexContainer.setEnv(TestHelper.getMockEnv());
        invertedIndexContainer.setJsonReader(new JsonFileReader());
        invertedIndexContainer.setFieldUtil(new FieldUtil());

        user1 = TestHelper.getUser1();
        user2 = TestHelper.getUser2();
        ticket1 = TestHelper.getTicket1();
        ticket2 = TestHelper.getTicket2();
        organization1 = TestHelper.getOrganization1();
        organization2 = TestHelper.getOrganization2();
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
            List<Organization> searchResultById1 = invertedIndexContainer.lookUpOrganizations("id", "119");
            Assert.assertEquals(searchResultById1.size(), 1);
            Assert.assertEquals(searchResultById1.get(0), organization1);
            List<Organization> searchResultById2 = invertedIndexContainer.lookUpOrganizations("id", "120");
            Assert.assertEquals(searchResultById2.size(), 1);
            Assert.assertEquals(searchResultById2.get(0), organization2);

            List<Organization> searchResultByCreatedAt = invertedIndexContainer.lookUpOrganizations("createdAt", "2016-01-15T04:11:08 -11:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), organization2);

            //on boolean field
            List<Organization> searchBySharedTicketsTrue = invertedIndexContainer.lookUpOrganizations("sharedTickets", "true");
            Assert.assertEquals(searchBySharedTicketsTrue.size(), 0);

            List<Organization> searchBySharedTicketsFalse = invertedIndexContainer.lookUpOrganizations("sharedTickets", "false");
            Assert.assertEquals(searchBySharedTicketsFalse.size(), 2);
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization1));
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization2));

            //on List(Tag, domainNames)
            List<Organization> searchByTag = invertedIndexContainer.lookUpOrganizations("tags", "Erickson");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(organization1));

            List<Organization> searchByDomainNames = invertedIndexContainer.lookUpOrganizations("domainNames", "bleeko.com");
            Assert.assertEquals(searchByDomainNames.size(), 1);
            Assert.assertTrue(searchByDomainNames.contains(organization1));

            //on empty field - on tags
            List<Organization> searchByEmptyTag = invertedIndexContainer.lookUpOrganizations("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(organization2));

            //on null field - on domain_names
            List<Organization> searchByEmptyyDomainNames = invertedIndexContainer.lookUpOrganizations("domainNames", "");
            Assert.assertEquals(searchByEmptyyDomainNames.size(), 1);
            Assert.assertTrue(searchByEmptyyDomainNames.contains(organization2));

            //on null field - on details
            List<Organization> searchByEmptyDetails = invertedIndexContainer.lookUpOrganizations("details", "");
            Assert.assertEquals(searchByEmptyDetails.size(), 1);
            Assert.assertTrue(searchByEmptyDetails.contains(organization2));

            //on value not found
            List<User> searchOnNotFound = invertedIndexContainer.lookUpUser("name", "who am I?");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field not existed
            List<User> searchOnInvalidFieldName = invertedIndexContainer.lookUpUser("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);

        } catch (Exception e) {
            e.printStackTrace();
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
            List<User> searchResultById = invertedIndexContainer.lookUpUser("id", "1");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), user1);

            List<User> searchResultByCreatedAt = invertedIndexContainer.lookUpUser("createdAt", "2016-04-15T05:19:46 -10:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), user1);

            List<User> searchResultByOrgId = invertedIndexContainer.lookUpUser("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), user1);

            //on boolean field
            List<User> searchByActive = invertedIndexContainer.lookUpUser("active", "true");
            Assert.assertEquals(searchByActive.size(), 2);
            Assert.assertTrue(searchByActive.contains(user1));
            Assert.assertTrue(searchByActive.contains(user2));

            List<User> searchBySuspended = invertedIndexContainer.lookUpUser("suspended", "false");
            Assert.assertEquals(searchBySuspended.size(), 1);
            Assert.assertTrue(searchBySuspended.contains(user2));

            //on tags
            List<User> searchByTag = invertedIndexContainer.lookUpUser("tags", "Sutton");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(user1));

            List<User> searchByCommonTag = invertedIndexContainer.lookUpUser("tags", "common-tag");
            Assert.assertEquals(searchByCommonTag.size(), 2);
            Assert.assertTrue(searchByCommonTag.contains(user1));
            Assert.assertTrue(searchByCommonTag.contains(user2));

            //on empty field
            List<User> searchOnEmpty = invertedIndexContainer.lookUpUser("signature", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(user2));

            //on null field
            List<User> searchOnNull = invertedIndexContainer.lookUpUser("phone", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(user2));

            //on value not found
            List<User> searchOnNotFound = invertedIndexContainer.lookUpUser("alias", "aaaaalias");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<User> searchOnInvalidFieldName = invertedIndexContainer.lookUpUser("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);

        } catch (Exception e) {
            e.printStackTrace();
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
            List<Ticket> searchResultById = invertedIndexContainer.lookUpTickets("id", "436bf9b0-1147-4c0a-8439-6f79833bff5b");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), ticket1);

            List<Ticket> searchResultByCreatedAt = invertedIndexContainer.lookUpTickets("externalId", "9210cdc9-4bee-485f-a078-35396cd74063");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), ticket1);

            List<Ticket> searchResultByOrgId = invertedIndexContainer.lookUpTickets("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), ticket1);

            List<Ticket> searchResultByType = invertedIndexContainer.lookUpTickets("type", "incident");
            Assert.assertEquals(searchResultByType.size(), 2);
            Assert.assertTrue(searchResultByType.contains(ticket1));
            Assert.assertTrue(searchResultByType.contains(ticket2));

            //on boolean field
            List<Ticket> searchByHasIncidentTrue = invertedIndexContainer.lookUpTickets("hasIncidents", "true");
            Assert.assertEquals(searchByHasIncidentTrue.size(), 0);

            List<Ticket> searchByHasIncidentFalse = invertedIndexContainer.lookUpTickets("hasIncidents", "false");
            Assert.assertEquals(searchByHasIncidentFalse.size(), 2);
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket1));
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket2));

            //on tags
            List<Ticket> searchByTag = invertedIndexContainer.lookUpTickets("tags", "Ohio");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(ticket1));

            //on empty tags
            List<Ticket> searchByEmptyTag = invertedIndexContainer.lookUpTickets("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(ticket2));

            //on tags value not existed
            List<Ticket> searchByNotExistedTagValue = invertedIndexContainer.lookUpTickets("tags", "not-existed-tag");
            Assert.assertEquals(searchByNotExistedTagValue.size(), 0);

            //on empty field
            List<Ticket> searchOnEmpty = invertedIndexContainer.lookUpTickets("externalId", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(ticket2));

            //on null field
            List<Ticket> searchOnNull = invertedIndexContainer.lookUpTickets("dueAt", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(ticket2));

            //on value not found
            List<Ticket> searchOnNotFound = invertedIndexContainer.lookUpTickets("type", "what-type??");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<Ticket> searchOnInvalidFieldName = invertedIndexContainer.lookUpTickets("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
