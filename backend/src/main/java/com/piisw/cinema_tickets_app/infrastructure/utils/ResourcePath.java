package com.piisw.cinema_tickets_app.infrastructure.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourcePath {

    public final String ID = "id";
    public final String ID_PATH = "/{" + ID + "}";
    public final String IDS = "ids";
    public final String IDS_PATH = "/{" + IDS + "}";
    public final String OBJECT_STATE = "objectState";
    public final String SEARCH_TEXT = "searchText";
}
