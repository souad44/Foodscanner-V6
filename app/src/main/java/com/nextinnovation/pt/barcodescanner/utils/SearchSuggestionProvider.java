package com.nextinnovation.pt.barcodescanner.utils;

import android.content.SearchRecentSuggestionsProvider;

import com.nextinnovation.pt.barcodescanner.BuildConfig;


public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID+".utils.SearchSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
