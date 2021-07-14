package com.zendesk.datasearcher.searcher;

import java.util.List;

import com.zendesk.datasearcher.model.InvertedIndex;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InvertedIndexTest {

    InvertedIndex<Organization> organizationIndex;
    InvertedIndex<User> userIndex;
    InvertedIndex<Ticket> ticketIndex;

    private User user1;
    private User user2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Organization organization1;
    private Organization organization2;

    private final Environment env = TestHelper.getMockEnv();
    private final JsonFileReader jsonReader = new JsonFileReader();

    @BeforeMethod
    private void setUp() throws Exception {
        user1 = TestHelper.getUser1();
        user2 = TestHelper.getUser2();
        ticket1 = TestHelper.getTicket1();
        ticket2 = TestHelper.getTicket2();
        organization1 = TestHelper.getOrganization1();
        organization2 = TestHelper.getOrganization2();

        String usersFilePath = env.getProperty("users.filepath", "users.json");
        List<User> users = jsonReader.parseJson(usersFilePath, User.class);
        this.userIndex = new InvertedIndex(users);

        String organizationsFilePath = env.getProperty("organizations.filepath", "organizations.json");
        List<Organization> organizations = jsonReader.parseJson(organizationsFilePath, Organization.class);
        this.organizationIndex = new InvertedIndex<>(organizations);

        String ticketFilePath = env.getProperty("tickets.filepath", "tickets.json");
        List<Ticket> tickets = jsonReader.parseJson(ticketFilePath, Ticket.class);
        this.ticketIndex = new InvertedIndex(tickets);

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
            List<Organization> searchResultById1 = organizationIndex.lookUp("id", "119");
            Assert.assertEquals(searchResultById1.size(), 1);
            Assert.assertEquals(searchResultById1.get(0), organization1);
            List<Organization> searchResultById2 = organizationIndex.lookUp("id", "120");
            Assert.assertEquals(searchResultById2.size(), 1);
            Assert.assertEquals(searchResultById2.get(0), organization2);

            List<Organization> searchResultByCreatedAt = organizationIndex.lookUp("createdAt", "2016-01-15T04:11:08 -11:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), organization2);

            //on boolean field
            List<Organization> searchBySharedTicketsTrue = organizationIndex.lookUp("sharedTickets", "true");
            Assert.assertEquals(searchBySharedTicketsTrue.size(), 0);

            List<Organization> searchBySharedTicketsFalse = organizationIndex.lookUp("sharedTickets", "false");
            Assert.assertEquals(searchBySharedTicketsFalse.size(), 2);
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization1));
            Assert.assertTrue(searchBySharedTicketsFalse.contains(organization2));

            //on List(Tag, domainNames)
            List<Organization> searchByTag = organizationIndex.lookUp("tags", "Erickson");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(organization1));

            List<Organization> searchByDomainNames = organizationIndex.lookUp("domainNames", "bleeko.com");
            Assert.assertEquals(searchByDomainNames.size(), 1);
            Assert.assertTrue(searchByDomainNames.contains(organization1));

            //on empty field - on tags
            List<Organization> searchByEmptyTag = organizationIndex.lookUp("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(organization2));

            //on null field - on domain_names
            List<Organization> searchByEmptyyDomainNames = organizationIndex.lookUp("domainNames", "");
            Assert.assertEquals(searchByEmptyyDomainNames.size(), 1);
            Assert.assertTrue(searchByEmptyyDomainNames.contains(organization2));

            //on null field - on details
            List<Organization> searchByEmptyDetails = organizationIndex.lookUp("details", "");
            Assert.assertEquals(searchByEmptyDetails.size(), 1);
            Assert.assertTrue(searchByEmptyDetails.contains(organization2));

            //on value not found
            List<Organization> searchOnNotFound = organizationIndex.lookUp("name", "who am I?");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field not existed
            List<Organization> searchOnInvalidFieldName = organizationIndex.lookUp("invalidField", "whateverValue");
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
            List<User> searchResultById = userIndex.lookUp("id", "1");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), user1);

            List<User> searchResultByCreatedAt = userIndex.lookUp("createdAt", "2016-04-15T05:19:46 -10:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), user1);

            List<User> searchResultByOrgId = userIndex.lookUp("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), user1);

            //on boolean field
            List<User> searchByActive = userIndex.lookUp("active", "true");
            Assert.assertEquals(searchByActive.size(), 2);
            Assert.assertTrue(searchByActive.contains(user1));
            Assert.assertTrue(searchByActive.contains(user2));

            List<User> searchBySuspended = userIndex.lookUp("suspended", "false");
            Assert.assertEquals(searchBySuspended.size(), 1);
            Assert.assertTrue(searchBySuspended.contains(user2));

            //on tags
            List<User> searchByTag = userIndex.lookUp("tags", "Sutton");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(user1));

            List<User> searchByCommonTag = userIndex.lookUp("tags", "common-tag");
            Assert.assertEquals(searchByCommonTag.size(), 2);
            Assert.assertTrue(searchByCommonTag.contains(user1));
            Assert.assertTrue(searchByCommonTag.contains(user2));

            //on empty field
            List<User> searchOnEmpty = userIndex.lookUp("signature", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(user2));

            //on null field
            List<User> searchOnNull = userIndex.lookUp("phone", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(user2));

            //on value not found
            List<User> searchOnNotFound = userIndex.lookUp("alias", "aaaaalias");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<User> searchOnInvalidFieldName = userIndex.lookUp("invalidField", "whateverValue");
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
            List<Ticket> searchResultById = ticketIndex.lookUp("id", "436bf9b0-1147-4c0a-8439-6f79833bff5b");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), ticket1);

            List<Ticket> searchResultByCreatedAt = ticketIndex.lookUp("externalId", "9210cdc9-4bee-485f-a078-35396cd74063");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), ticket1);

            List<Ticket> searchResultByOrgId = ticketIndex.lookUp("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), ticket1);

            List<Ticket> searchResultByType = ticketIndex.lookUp("type", "incident");
            Assert.assertEquals(searchResultByType.size(), 2);
            Assert.assertTrue(searchResultByType.contains(ticket1));
            Assert.assertTrue(searchResultByType.contains(ticket2));

            //on boolean field
            List<Ticket> searchByHasIncidentTrue = ticketIndex.lookUp("hasIncidents", "true");
            Assert.assertEquals(searchByHasIncidentTrue.size(), 0);

            List<Ticket> searchByHasIncidentFalse = ticketIndex.lookUp("hasIncidents", "false");
            Assert.assertEquals(searchByHasIncidentFalse.size(), 2);
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket1));
            Assert.assertTrue(searchByHasIncidentFalse.contains(ticket2));

            //on tags
            List<Ticket> searchByTag = ticketIndex.lookUp("tags", "Ohio");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(ticket1));

            //on empty tags
            List<Ticket> searchByEmptyTag = ticketIndex.lookUp("tags", "");
            Assert.assertEquals(searchByEmptyTag.size(), 1);
            Assert.assertTrue(searchByEmptyTag.contains(ticket2));

            //on tags value not existed
            List<Ticket> searchByNotExistedTagValue = ticketIndex.lookUp("tags", "not-existed-tag");
            Assert.assertEquals(searchByNotExistedTagValue.size(), 0);

            //on empty field
            List<Ticket> searchOnEmpty = ticketIndex.lookUp("externalId", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(ticket2));

            //on null field
            List<Ticket> searchOnNull = ticketIndex.lookUp("dueAt", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(ticket2));

            //on value not found
            List<Ticket> searchOnNotFound = ticketIndex.lookUp("type", "what-type??");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on field name not existed
            List<Ticket> searchOnInvalidFieldName = ticketIndex.lookUp("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
