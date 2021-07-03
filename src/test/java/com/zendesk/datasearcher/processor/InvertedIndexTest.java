package com.zendesk.datasearcher.processor;

import java.util.Arrays;
import java.util.List;

import com.zendesk.datasearcher.model.entity.User;
import com.zendesk.datasearcher.util.FieldUtil;
import com.zendesk.datasearcher.util.JsonFileReader;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InvertedIndexTest{
    private InvertedIndex invertedIndex = new InvertedIndex();

    private User u1;
    private User u2;

    @BeforeMethod
    private void setUp() {
        Environment mockEnv = Mockito.mock(Environment.class);
        Mockito.when(mockEnv.getProperty(Mockito.eq("users.filepath"), Mockito.eq("users.json"))).thenReturn("users.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("users.filepath"), Mockito.eq("tickets.json"))).thenReturn("tickets.json");
        Mockito.when(mockEnv.getProperty(Mockito.eq("users.filepath"), Mockito.eq("organizations.json"))).thenReturn("organizations.json");
        invertedIndex.setEnv(mockEnv);
        invertedIndex.setJsonReader(new JsonFileReader());
        invertedIndex.setFieldUtil(new FieldUtil());

        u1 = TestData.getUser1();
        u2 = TestData.getUser1();
    }

    /**
     * Take User as example, the index should be built properly, and we should be able to search value by field name.
     * The User, Ticket and Organization are equivalent classes - thus it's okay to take User as a sample.
     */
    @Test
    void shouldBuildUserIndexProperly() {
        try {
            //on string fields
            List<User> searchResultById = invertedIndex.lookUpUser("id", "1");
            Assert.assertEquals(searchResultById.size(), 1);
            Assert.assertEquals(searchResultById.get(0), u1);

            List<User> searchResultByCreatedAt = invertedIndex.lookUpUser("createdAt", "2016-04-15T05:19:46 -10:00");
            Assert.assertEquals(searchResultByCreatedAt.size(), 1);
            Assert.assertEquals(searchResultByCreatedAt.get(0), u1);

            List<User> searchResultByOrgId = invertedIndex.lookUpUser("organizationId", "119");
            Assert.assertEquals(searchResultByOrgId.size(), 1);
            Assert.assertEquals(searchResultByOrgId.get(0), u1);

            //on boolean field
            List<User> searchByActive = invertedIndex.lookUpUser("active", "true");
            Assert.assertEquals(searchByActive.size(), 2);
            Assert.assertTrue(searchByActive.contains(u1));
            Assert.assertTrue(searchByActive.contains(u2));

            List<User> searchBySuspended = invertedIndex.lookUpUser("suspended", "false");
            Assert.assertEquals(searchBySuspended.size(), 1);
            Assert.assertTrue(searchBySuspended.contains(u2));

            //on tags
            List<User> searchByTag = invertedIndex.lookUpUser("tags", "Sutton");
            Assert.assertEquals(searchByTag.size(), 1);
            Assert.assertTrue(searchByTag.contains(u1));

            List<User> searchByCommonTag = invertedIndex.lookUpUser("tags", "common-tag");
            Assert.assertEquals(searchByCommonTag.size(), 2);
            Assert.assertTrue(searchByCommonTag.contains(u1));
            Assert.assertTrue(searchByCommonTag.contains(u2));

            //on empty field
            List<User> searchOnEmpty = invertedIndex.lookUpUser("signature", "");
            Assert.assertEquals(searchOnEmpty.size(), 1);
            Assert.assertTrue(searchOnEmpty.contains(u2));

            //on null field
            List<User> searchOnNull = invertedIndex.lookUpUser("phone", "");
            Assert.assertEquals(searchOnNull.size(), 1);
            Assert.assertTrue(searchOnNull.contains(u2));

            //on not found
            List<User> searchOnNotFound = invertedIndex.lookUpUser("alias", "aaaaalias");
            Assert.assertEquals(searchOnNotFound.size(), 0);

            //on not existed field name
            List<User> searchOnInvalidFieldName = invertedIndex.lookUpUser("invalidField", "whateverValue");
            Assert.assertEquals(searchOnInvalidFieldName.size(), 0);

        } catch (Exception e) {
            Assert.fail();
        }
    }
}
