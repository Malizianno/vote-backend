package ro.cristiansterie.vote.util;

public class AppConstants {

    /* DB */
    
    public static final String TABLE_USER                   = "users";
    public static final String TABLE_CANDIDATE              = "candidates";
    public static final String TABLE_ELECTION               = "elections";
    public static final String TABLE_VOTE                   = "votes";
    public static final String TABLE_EVENT                  = "events";

    /* AUTH */

    public static final String AUTH_APIKEY                  = "apikey";

    /* EVENTS MESSAGES */
    public static final String EVENT_CANDIDATES_GET_ALL             = "Get all candidates";
    public static final String EVENT_CANDIDATES_GET_FILTERED        = "Get filtered candidates";
}
