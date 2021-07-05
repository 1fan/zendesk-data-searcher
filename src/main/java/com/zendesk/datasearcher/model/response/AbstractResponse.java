package com.zendesk.datasearcher.model.response;

import java.util.List;

import com.zendesk.datasearcher.model.entity.AbstractEntity;

public abstract class AbstractResponse {

    //enforce all children classes to implement this function
    public abstract String toString();

    protected <T extends AbstractEntity> String getSummaryOfListOfEntities(List<T> entities) {
        StringBuilder result = new StringBuilder();
        if (entities == null || entities.size() == 0) {
            result.append("NA");
        } else {
            for (int i = 0; i < entities.size(); i++) {
                result.append(String.format("-%d-", i + 1)).append("\n")
                        .append(entities.get(i).getSummary()).append("\n");
            }
        }
        return result.toString();
    }
}
