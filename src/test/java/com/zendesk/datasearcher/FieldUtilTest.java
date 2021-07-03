package com.zendesk.datasearcher;

import com.zendesk.datasearcher.util.FieldUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FieldUtilTest {
    FieldUtil fieldUtil = new FieldUtil();

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
}
