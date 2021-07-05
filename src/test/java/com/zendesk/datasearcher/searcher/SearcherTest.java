package com.zendesk.datasearcher.searcher;

import java.util.List;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.model.response.OrganizationResponse;
import com.zendesk.datasearcher.model.response.TicketResponse;
import com.zendesk.datasearcher.model.response.UserResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SearcherTest {
    private Searcher searcher;
    private User user1;
    private User user2;
    private Ticket ticket1;
    private Ticket ticket2;
    private Organization organization1;
    private Organization organization2;

    @BeforeMethod
    void setUp() {
        searcher = new Searcher();
        searcher.setInvertedIndex(TestHelper.getInvertedIndex(TestHelper.getMockEnv()));

        user1 = TestHelper.getUser1();
        user2 = TestHelper.getUser2();
        ticket1 = TestHelper.getTicket1();
        ticket2 = TestHelper.getTicket2();
        organization1 = TestHelper.getOrganization1();
        organization2 = TestHelper.getOrganization2();
    }

    /**
     * We have tested that the correct User can be searched by field in {@link InvertedIndexTest}, now we want to test if related Tickets and Organization can be fetched.
     * For User1:
     * - should return with related organization id="119"
     * - should return with related submitted ticket id="436bf9b0-1147-4c0a-8439-6f79833bff5b"
     * - should return with related assigned ticket id="1a227508-9f39-427c-8f57-1b72f3fab87c"
     */
    @Test
    void searchByUser1ShouldReturnAssociatedTicketsAndOrgs() {
        try {
            List<UserResponse> userResponses = searcher.searchByUsers("id", "1");
            Assert.assertEquals(userResponses.size(), 1);
            Assert.assertEquals(userResponses.get(0).getUser(), user1);
            Assert.assertEquals(userResponses.get(0).getAssignedTickets().size(), 1);
            Assert.assertEquals(userResponses.get(0).getAssignedTickets().get(0), ticket2);
            Assert.assertEquals(userResponses.get(0).getSubmittedTickets().size(), 1);
            Assert.assertEquals(userResponses.get(0).getSubmittedTickets().get(0), ticket1);
            Assert.assertEquals(userResponses.get(0).getUserOrganization(), organization1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * We have tested that the correct User can be searched by field in {@link InvertedIndexTest}, now we want to test if related Tickets and Organization can be fetched.
     * For User2:
     * - should return with related organization id="120"
     * - should return with related submitted ticket id="1a227508-9f39-427c-8f57-1b72f3fab87c"
     * - should return with related assigned ticket id="436bf9b0-1147-4c0a-8439-6f79833bff5b"
     */
    @Test
    void searchByUser2ShouldReturnAssociatedTicketsAndOrgs() {
        try {
            List<UserResponse> userResponses = searcher.searchByUsers("id", "2");
            Assert.assertEquals(userResponses.size(), 1);
            Assert.assertEquals(userResponses.get(0).getUser(), user2);
            Assert.assertEquals(userResponses.get(0).getAssignedTickets().size(), 1);
            Assert.assertEquals(userResponses.get(0).getAssignedTickets().get(0), ticket1);
            Assert.assertEquals(userResponses.get(0).getSubmittedTickets().size(), 1);
            Assert.assertEquals(userResponses.get(0).getSubmittedTickets().get(0), ticket2);
            Assert.assertEquals(userResponses.get(0).getUserOrganization(), organization2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * We have tested that the correct User can be searched by field in {@link InvertedIndexTest}, now we want to test if related Users and Organization can be fetched.
     * For Ticket1:
     * - should return with related organization id="119"
     * - should return with related submitted user id="1"
     * - should return with related assigned user id="2"
     */
    @Test
    void searchByTicket1ShouldReturnAssociatedUsersAndOrgs() {
        try {
            List<TicketResponse> ticketResponses = searcher.searchByTickets("id", "436bf9b0-1147-4c0a-8439-6f79833bff5b");
            Assert.assertEquals(ticketResponses.size(), 1);
            Assert.assertEquals(ticketResponses.get(0).getTicket(), ticket1);
            Assert.assertEquals(ticketResponses.get(0).getTicketOrganization(), organization1);
            Assert.assertEquals(ticketResponses.get(0).getAssignee(), user2);
            Assert.assertEquals(ticketResponses.get(0).getSubmitter(), user1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * We have tested that the correct User can be searched by field in {@link InvertedIndexTest}, now we want to test if related Users and Organization can be fetched.
     * For Ticket2:
     * - should return with related organization id="120"
     * - should return with related submitted user id="2"
     * - should return with related assigned user id="1"
     */
    @Test
    void searchByTicket2ShouldReturnAssociatedUsersAndOrgs() {
        try {
            List<TicketResponse> ticketResponses = searcher.searchByTickets("id", "1a227508-9f39-427c-8f57-1b72f3fab87c");
            Assert.assertEquals(ticketResponses.size(), 1);
            Assert.assertEquals(ticketResponses.get(0).getTicket(), ticket2);
            Assert.assertEquals(ticketResponses.get(0).getTicketOrganization(), organization2);
            Assert.assertEquals(ticketResponses.get(0).getAssignee(), user1);
            Assert.assertEquals(ticketResponses.get(0).getSubmitter(), user2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * We have tested that the correct Organization can be searched by field in {@link InvertedIndexTest}, now we want to test if related Users and Tickets can be fetched.
     * For Organization1:
     * - should return with related ticket id="436bf9b0-1147-4c0a-8439-6f79833bff5b"
     * - should return with related user id="1"
     */
    @Test
    void searchByOrganization1ShouldReturnAssociatedUsersAndTickets() {
        try {
            List<OrganizationResponse> organizationResponses = searcher.searchByOrganizations("id", "119");
            Assert.assertEquals(organizationResponses.size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrganization(), organization1);
            Assert.assertEquals(organizationResponses.get(0).getOrgTickets().size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrgTickets().get(0), ticket1);
            Assert.assertEquals(organizationResponses.get(0).getOrgUsers().size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrgUsers().get(0), user1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * We have tested that the correct Organization can be searched by field in {@link InvertedIndexTest}, now we want to test if related Users and Tickets can be fetched.
     * For Organization2:
     * - should return with related ticket id="1a227508-9f39-427c-8f57-1b72f3fab87c"
     * - should return with related user id="2"
     */
    @Test
    void searchByOrganization2ShouldReturnAssociatedUsersAndTickets() {
        try {
            List<OrganizationResponse> organizationResponses = searcher.searchByOrganizations("id", "120");
            Assert.assertEquals(organizationResponses.size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrganization(), organization2);
            Assert.assertEquals(organizationResponses.get(0).getOrgTickets().size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrgTickets().get(0), ticket2);
            Assert.assertEquals(organizationResponses.get(0).getOrgUsers().size(), 1);
            Assert.assertEquals(organizationResponses.get(0).getOrgUsers().get(0), user2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
