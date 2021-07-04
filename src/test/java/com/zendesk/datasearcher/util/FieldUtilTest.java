package com.zendesk.datasearcher.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zendesk.datasearcher.model.entity.User;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

//The User, Ticket and Organization are equivalent classes - thus it's okay to take User as a sample.
public class FieldUtilTest {
    FieldUtil fieldUtil = new FieldUtil();
    Field idField = FieldUtils.getField(User.class, "id", true);
    Field createdAtField = FieldUtils.getField(User.class, "createdAt", true);
    Field tagsField = FieldUtils.getField(User.class, "tags", true);
    Field externalIdField = FieldUtils.getField(User.class, "externalId", true);
    Field urlField = FieldUtils.getField(User.class, "url", true);
    Field nameField = FieldUtils.getField(User.class, "name", true);
    Field aliasField = FieldUtils.getField(User.class, "alias", true);
    Field activeField = FieldUtils.getField(User.class, "active", true);
    Field verifiedField = FieldUtils.getField(User.class, "verified", true);
    Field sharedField = FieldUtils.getField(User.class, "shared", true);
    Field localeField = FieldUtils.getField(User.class, "locale", true);
    Field timezoneField = FieldUtils.getField(User.class, "timezone", true);
    Field lastLoginAtField = FieldUtils.getField(User.class, "lastLoginAt", true);
    Field emailField = FieldUtils.getField(User.class, "email", true);
    Field phoneField = FieldUtils.getField(User.class, "phone", true);
    Field signatureField = FieldUtils.getField(User.class, "signature", true);
    Field organizationIdField = FieldUtils.getField(User.class, "organizationId", true);
    Field suspendedField = FieldUtils.getField(User.class, "suspended", true);
    Field roleField = FieldUtils.getField(User.class, "role", true);

    @Test
    void shouldConvertTextFromSnakeToCamel() {
        Assert.assertEquals(fieldUtil.converSnaketToCamelNamingConvention("_id"), "id");
        Assert.assertEquals(fieldUtil.converSnaketToCamelNamingConvention("submitter_id"), "submitterId");
        Assert.assertEquals(fieldUtil.converSnaketToCamelNamingConvention("assignee_id"), "assigneeId");
        Assert.assertEquals(fieldUtil.converSnaketToCamelNamingConvention("description"), "description");
        Assert.assertEquals(fieldUtil.converSnaketToCamelNamingConvention(""), "");
    }

    @Test
    void shouldConvertTextFromCamelToSnake() {
        Assert.assertEquals(fieldUtil.convertCamelToSnakeNamingConvention("id"), "_id");
        Assert.assertEquals(fieldUtil.convertCamelToSnakeNamingConvention("submitterId"), "submitter_id");
        Assert.assertEquals(fieldUtil.convertCamelToSnakeNamingConvention("assigneeId"), "assignee_id");
        Assert.assertEquals(fieldUtil.convertCamelToSnakeNamingConvention(""), "");
    }

    @Test
    void shouldGetAllFieldsOfClassIncludeParentFields() throws NoSuchFieldException {
        List<Field> fields = fieldUtil.getFieldsOfClass(User.class);
        Assert.assertEquals(fields.size(), 19);
        //The field id,createdAt, tags, externalId and url are in the AbstractEntity class.
        Assert.assertTrue(fields.contains(idField));
        Assert.assertTrue(fields.contains(createdAtField));
        Assert.assertTrue(fields.contains(tagsField));
        Assert.assertTrue(fields.contains(externalIdField));
        Assert.assertTrue(fields.contains(urlField));
        //The re st fields are in the User class.
        Assert.assertTrue(fields.contains(nameField));
        Assert.assertTrue(fields.contains(aliasField));
        Assert.assertTrue(fields.contains(activeField));
        Assert.assertTrue(fields.contains(verifiedField));
        Assert.assertTrue(fields.contains(sharedField));
        Assert.assertTrue(fields.contains(localeField));
        Assert.assertTrue(fields.contains(timezoneField));
        Assert.assertTrue(fields.contains(lastLoginAtField));
        Assert.assertTrue(fields.contains(emailField));
        Assert.assertTrue(fields.contains(phoneField));
        Assert.assertTrue(fields.contains(signatureField));
        Assert.assertTrue(fields.contains(organizationIdField));
        Assert.assertTrue(fields.contains(suspendedField));
        Assert.assertTrue(fields.contains(roleField));
    }

    @Test
    void shouldGetAllFieldsOfClassInStringIncludeParentFields() throws NoSuchFieldException {
        List<String> fields = fieldUtil.getFieldNamesInStringOfClass(User.class);
        Assert.assertEquals(fields.size(), 19);
        //The field id,createdAt, tags, externalId and url are in the AbstractEntity class.
        Assert.assertTrue(fields.contains("_id"));
        Assert.assertTrue(fields.contains("created_at"));
        Assert.assertTrue(fields.contains("tags"));
        Assert.assertTrue(fields.contains("external_id"));
        Assert.assertTrue(fields.contains("url"));
        //The re st fields are in the User class.
        Assert.assertTrue(fields.contains("name"));
        Assert.assertTrue(fields.contains("alias"));
        Assert.assertTrue(fields.contains("active"));
        Assert.assertTrue(fields.contains("verified"));
        Assert.assertTrue(fields.contains("shared"));
        Assert.assertTrue(fields.contains("locale"));
        Assert.assertTrue(fields.contains("timezone"));
        Assert.assertTrue(fields.contains("last_login_at"));
        Assert.assertTrue(fields.contains("email"));
        Assert.assertTrue(fields.contains("phone"));
        Assert.assertTrue(fields.contains("signature"));
        Assert.assertTrue(fields.contains("organization_id"));
        Assert.assertTrue(fields.contains("suspended"));
        Assert.assertTrue(fields.contains("role"));
    }

    @Test
    void shouldGetAllFieldsValueOfClassProperly() {
        User user = new User();
        try {
            //id
            user.setId("user_id");
            Assert.assertEquals(fieldUtil.readFiledValue(idField, user), "user_id");
            //boolean
            user.setActive(true);
            Assert.assertEquals(fieldUtil.readFiledValue(activeField, user), true);
            //string
            user.setAlias("alias");
            Assert.assertEquals(fieldUtil.readFiledValue(aliasField, user), "alias");
            //Normal list
            user.setTags(Arrays.asList("tag1", "tag2", "tag3"));
            Assert.assertEquals(fieldUtil.readFiledValue(tagsField, user), Arrays.asList("tag1", "tag2", "tag3"));
            //Null list
            user.setTags(null);
            Assert.assertEquals(fieldUtil.readFiledValue(tagsField, user), null);
            //empty list
            user.setTags(new ArrayList<>());
            Assert.assertEquals(fieldUtil.readFiledValue(tagsField, user), new ArrayList<>());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
