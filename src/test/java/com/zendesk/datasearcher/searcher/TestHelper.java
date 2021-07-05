package com.zendesk.datasearcher.searcher;

import java.util.Arrays;
import java.util.Collections;

import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.FieldUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class TestHelper {
    protected static Environment getMockEnv() {
        Environment mockEnv = Mockito.mock(Environment.class);
        Mockito.when(mockEnv.getProperty(Mockito.eq("users.filepath"), Mockito.eq("users.json"))).thenReturn("users.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("tickets.filepath"), Mockito.eq("tickets.json"))).thenReturn("tickets.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("organizations.filepath"), Mockito.eq("organizations.json"))).thenReturn("organizations.json");
        return mockEnv;
    }

    protected static InvertedIndex getInvertedIndex() {
        InvertedIndex invertedIndex = new InvertedIndex();
        invertedIndex.setEnv(getMockEnv());
        invertedIndex.setJsonReader(new JsonFileReader());
        invertedIndex.setFieldUtil(new FieldUtil());

        return invertedIndex;
    }

    protected static User getUser1() {
        User u1 = new User();
        u1.setId("1");
        u1.setUrl("http://initech.zendesk.com/api/v2/users/1.json");
        u1.setExternalId("74341f74-9c79-49d5-9611-87ef9b6eb75f");
        u1.setName("Francisca Rasmussen");
        u1.setAlias("Miss Coffey");
        u1.setCreatedAt("2016-04-15T05:19:46 -10:00");
        u1.setActive(true);
        u1.setVerified(true);
        u1.setShared(false);
        u1.setLocale("en-AU");
        u1.setTimezone("Sri Lanka");
        u1.setLastLoginAt("2013-08-04T01:03:27 -10:00");
        u1.setEmail("coffeyrasmussen@flotonic.com");
        u1.setPhone("8335-422-718");
        u1.setSignature("Don't Worry Be Happy!");
        u1.setOrganizationId("119");
        u1.setTags(Arrays.asList("Springville", "Sutton", "Hartsville/Hartley", "Diaperville", "common-tag"));
        u1.setSuspended(true);
        u1.setRole("admin");
        return u1;
    }

    protected static User getUser2() {
        User u2 = new User();
        u2.setId("2");
        u2.setUrl("http://initech.zendesk.com/api/v2/users/2.json");
        u2.setExternalId("c9995ea4-ff72-46e0-ab77-dfe0ae1ef6c2");
        u2.setName("Cross Barlow");
        u2.setAlias("Miss Joni");
        u2.setCreatedAt("2016-06-23T10:31:39 -10:00");
        u2.setActive(true);
        u2.setVerified(true);
        u2.setShared(false);
        u2.setLocale("zh-CN");
        u2.setTimezone("Armenia");
        u2.setLastLoginAt("2012-04-12T04:03:28 -10:00");
        u2.setEmail("jonibarlow@flotonic.com");
        u2.setSignature("");
        u2.setOrganizationId("120");
        u2.setTags(Arrays.asList("Foxworth", "Woodlands", "Herlong", "Henrietta", "common-tag"));
        u2.setSuspended(false);
        u2.setRole("admin");
        return u2;
    }

    protected static Ticket getTicket1() {
        Ticket ticket = new Ticket();
        ticket.setId("436bf9b0-1147-4c0a-8439-6f79833bff5b");
        ticket.setUrl("http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json");
        ticket.setExternalId("9210cdc9-4bee-485f-a078-35396cd74063");
        ticket.setCreatedAt("2016-04-28T11:19:34 -10:00");
        ticket.setType("incident");
        ticket.setSubject("A Catastrophe in Korea (North)");
        ticket.setDescription("Nostrud ad sit velit cupidatat laboris ipsum nisi amet laboris ex exercitation amet et proident. Ipsum fugiat aute dolore tempor nostrud velit ipsum.");
        ticket.setPriority("high");
        ticket.setStatus("pending");
        ticket.setSubmitterId("1");
        ticket.setAssigneeId("2");
        ticket.setOrganizationId("119");
        ticket.setTags(Arrays.asList("Ohio", "Pennsylvania", "American Samoa", "Northern Mariana Islands"));
        ticket.setHasIncidents(false);
        ticket.setDueAt("2016-07-31T02:37:50 -10:00");
        ticket.setVia("web");
        return ticket;
    }

    protected static Ticket getTicket2() {
        Ticket ticket = new Ticket();
        ticket.setId("1a227508-9f39-427c-8f57-1b72f3fab87c");
        ticket.setUrl("http://initech.zendesk.com/api/v2/tickets/1a227508-9f39-427c-8f57-1b72f3fab87c.json");
        ticket.setExternalId("");
        ticket.setCreatedAt("2016-04-14T08:32:31 -10:00");
        ticket.setType("incident");
        ticket.setSubject("A Catastrophe in Micronesia");
        ticket.setDescription("Aliquip excepteur fugiat ex minim ea aute eu labore. Sunt eiusmod esse eu non commodo est veniam consequat.");
        ticket.setPriority("low");
        ticket.setStatus("hold");
        ticket.setSubmitterId("2");
        ticket.setAssigneeId("1");
        ticket.setOrganizationId("120");
        ticket.setTags(Collections.emptyList());
        ticket.setHasIncidents(false);
        ticket.setVia("chat");
        return ticket;
    }

    protected static Organization getOrganization1() {
        Organization org = new Organization();
        org.setId("119");
        org.setUrl("http://initech.zendesk.com/api/v2/organizations/119.json");
        org.setExternalId("2386db7c-5056-49c9-8dc4-46775e464cb7");
        org.setName("Multron");
        org.setDomainNames(Arrays.asList("bleeko.com", "pulze.com", "xoggle.com", "sultraxin.com"));
        org.setCreatedAt("2016-02-29T03:45:12 -11:00");
        org.setDetails("Non profit");
        org.setSharedTickets(false);
        org.setTags(Arrays.asList("Erickson", "Mccoy", "Wiggins", "Brooks"));
        return org;
    }

    protected static Organization getOrganization2() {
        Organization org = new Organization();
        org.setId("120");
        org.setUrl("http://initech.zendesk.com/api/v2/organizations/120.json");
        org.setExternalId("82da5daf-d6ad-484d-a831-05cd3e2baea5");
        org.setName("Andershun");
        org.setDomainNames(Collections.emptyList());//empty
        org.setCreatedAt("2016-01-15T04:11:08 -11:00");
        org.setDetails(null);//null
        org.setSharedTickets(false);
        org.setTags(null);//null
        return org;
    }
}
