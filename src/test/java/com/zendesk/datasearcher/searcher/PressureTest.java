package com.zendesk.datasearcher.searcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zendesk.datasearcher.model.entity.Organization;
import com.zendesk.datasearcher.model.entity.Ticket;
import com.zendesk.datasearcher.model.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PressureTest {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Searcher searcher = new Searcher();

    @BeforeMethod
    void init() throws IOException, InterruptedException {
        String basePath = "./src/test/resources/";
        String userFileName = "pressure-test-users.json";
        String ticketFileName = "pressure-test-tickets.json";
        String organizationFileName = "pressure-test-organizations.json";

        generateUserFile(basePath + "pressure-test-users.json", 100000);//user file contains 100k users
        generateOrganizationFile(basePath + "pressure-test-organizations.json", 50000);// organization file contains 50k organizations
        generateTicketFile(basePath + "pressure-test-tickets.json", 1000000);//ticket file contains 1 million tickets

        Environment env = Mockito.mock(Environment.class);
        Mockito.when(env.getProperty(Mockito.eq("users.filepath"), Mockito.eq("users.json"))).thenReturn(userFileName);
        Mockito.when(env.getProperty(Mockito.eq("tickets.filepath"), Mockito.eq("tickets.json"))).thenReturn(ticketFileName);
        Mockito.when(env.getProperty(Mockito.eq("organizations.filepath"), Mockito.eq("organizations.json"))).thenReturn(organizationFileName);

        InvertedIndex index = TestHelper.getInvertedIndex(env);
        searcher.setInvertedIndex(index);
    }

    private List<String> generateListOfRandomString(int len) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            result.add(RandomStringUtils.randomAlphabetic(10));
        }
        return result;
    }

    private void generateUserFile(String fileName, int num) throws IOException {
        if (new File(fileName).exists()) {
            System.out.println(String.format("File %s existed, will skip.", fileName));
            return;
        }
        List<User> fakeUsers = new ArrayList<>();
        List<String> roles = generateListOfRandomString(10);//assume there are 10 different roles

        for (int i = 0; i < num; i++) {
            User user = new User();
            user.setRole(roles.get(RandomUtils.nextInt(0, 10)));
            user.setSuspended(RandomUtils.nextBoolean());
            user.setSignature(RandomStringUtils.randomAlphabetic(20));
            user.setTags(generateListOfRandomString(3));//assume there are 3 tags in the 'tags' field
            user.setPhone(RandomStringUtils.randomNumeric(10));
            user.setEmail(RandomStringUtils.randomAlphanumeric(15));
            user.setLocale(RandomStringUtils.randomAlphanumeric(6));
            user.setOrganizationId(RandomStringUtils.randomNumeric(5));
            user.setTimezone(RandomStringUtils.randomAlphanumeric(6));
            user.setShared(RandomUtils.nextBoolean());
            user.setVerified(RandomUtils.nextBoolean());
            user.setLastLoginAt(RandomStringUtils.randomAlphanumeric(26));
            user.setCreatedAt(RandomStringUtils.randomAlphanumeric(26));
            user.setActive(RandomUtils.nextBoolean());
            user.setName(RandomStringUtils.randomAlphabetic(15));
            user.setAlias(RandomStringUtils.randomAlphabetic(15));
            user.setUrl(RandomStringUtils.randomAlphabetic(30));
            user.setExternalId(RandomStringUtils.randomAlphanumeric(36));
            user.setId((i + 1) + "");

            fakeUsers.add(user);
            if (i % 1000 == 0) {
                System.out.println(i);
            }
        }
        Files.write(Paths.get(fileName), gson.toJson(fakeUsers).getBytes());
    }

    private void generateTicketFile(String fileName, int num) throws IOException {
        if (new File(fileName).exists()) {
            System.out.println(String.format("File %s existed, will skip.", fileName));
            return;
        }
        List<Ticket> fakeTickets = new ArrayList<>();
        List<String> priorities = generateListOfRandomString(5);//assume there are 5 priorities
        List<String> types = generateListOfRandomString(10);//assume there are 10 types
        for (int i = 0; i < num; i++) {
            Ticket ticket = new Ticket();
            ticket.setVia(RandomStringUtils.randomAlphabetic(5));
            ticket.setDueAt(RandomStringUtils.randomAlphanumeric(26));
            ticket.setHasIncidents(RandomUtils.nextBoolean());
            ticket.setTags(generateListOfRandomString(3));
            ticket.setSubject(RandomStringUtils.randomAlphabetic(15));
            ticket.setStatus(RandomStringUtils.randomAlphabetic(5));
            ticket.setPriority(priorities.get(RandomUtils.nextInt(0, 5)));
            ticket.setAssigneeId(RandomStringUtils.randomNumeric(5));
            ticket.setSubmitterId(RandomStringUtils.randomNumeric(5));
            ticket.setOrganizationId(RandomStringUtils.randomNumeric(5));
            ticket.setType(types.get(RandomUtils.nextInt(0, 10)));
            ticket.setDescription(RandomStringUtils.randomAlphanumeric(20));
            ticket.setExternalId(RandomStringUtils.randomAlphanumeric(36));
            ticket.setUrl(RandomStringUtils.randomAlphanumeric(20));
            ticket.setId((i + 1) + "");
            fakeTickets.add(ticket);
        }
        Files.write(Paths.get(fileName), gson.toJson(fakeTickets).getBytes());
    }

    private void generateOrganizationFile(String fileName, int num) throws IOException {
        if (new File(fileName).exists()) {
            System.out.println(String.format("File %s existed, will skip.", fileName));
            return;
        }
        List<Organization> fakeOrganizations = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Organization org = new Organization();
            org.setTags(generateListOfRandomString(3));
            org.setSharedTickets(RandomUtils.nextBoolean());
            org.setDetails(RandomStringUtils.randomAlphanumeric(10));
            org.setCreatedAt(RandomStringUtils.randomAlphanumeric(26));
            org.setName(RandomStringUtils.randomAlphabetic(10));
            org.setExternalId(RandomStringUtils.randomAlphanumeric(36));
            org.setUrl(RandomStringUtils.randomAlphanumeric(20));
            org.setDomainNames(generateListOfRandomString(3));
            org.setId((i + 1) + "");

            fakeOrganizations.add(org);
        }
        Files.write(Paths.get(fileName), gson.toJson(fakeOrganizations).getBytes());
    }

    @Test(enabled = false)
    void pressureTest() {
        try {
            searcher.searchByUsers("_id", "100");
            searcher.searchByUsers("external_id", "c9995ea4-ff72-46e0-ab77-dfe0ae1ef6c2");
            searcher.searchByUsers("active", "true");
            searcher.searchByUsers("last_login_at", "2012-04-12T04:03:28 -10:00");
            searcher.searchByUsers("tags", "Foxworth");
            searcher.searchByUsers("tags", "");
            searcher.searchByUsers("invalidField", "whatever");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
