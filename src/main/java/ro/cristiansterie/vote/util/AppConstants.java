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

    public static final String EVENT_CANDIDATES_GET_ALL                         = "Get all candidates";
    public static final String EVENT_CANDIDATES_GET_FILTERED                    = "Get filtered candidates";
    public static final String EVENT_CANDIDATES_COUNT_FILTERED                  = "Count filtered candidates";
    public static final String EVENT_CANDIDATES_GET_ONE                         = "Get candidate with ID: ";
    public static final String EVENT_CANDIDATES_SAVE                            = "Save candidate with ID: ";
    public static final String EVENT_CANDIDATES_DELETE                          = "Delete candidate with ID: ";

    public static final String EVENT_DASHBOARD_GET_TOTALS                       = "Get dashboard totals";
    public static final String EVENT_DASHBOARD_GENERATE_FAKE_USERS              = "Generate fake users";
    public static final String EVENT_DASHBOARD_GENERATE_FAKE_CANDIDATES         = "Generate fake candidates";
    public static final String EVENT_DASHBOARD_GENERATE_FAKE_VOTES              = "Generate fake votes";

    public static final String EVENT_ELECTIONS_GET_ALL                          = "Get all elections";
    public static final String EVENT_ELECTIONS_GET_FILTERED                     = "Get filtered elections";
    public static final String EVENT_ELECTIONS_COUNT_FILTERED                   = "Count filtered elections";
    public static final String EVENT_ELECTIONS_GET_LAST                         = "Get last election";
    public static final String EVENT_ELECTIONS_GET_ONE                          = "Get election with ID: ";
    public static final String EVENT_ELECTIONS_SAVE                             = "Save election with ID: ";
    public static final String EVENT_ELECTIONS_DELETE                           = "Delete election with ID: ";

    public static final String EVENT_ELECTIONS_HELPER_GET_CAMPAIGN_STATUS       = "Get election campaign status";
    public static final String EVENT_ELECTIONS_HELPER_SWITCH_CAMPAIGN_STATUS    = "Switch election campaign status";
    public static final String EVENT_ELECTIONS_HELPER_GET_ELECTION_RESULT       = "Get election result";
    public static final String EVENT_ELECTIONS_HELPER_VOTE                      = "Vote for candidate with ID: ";
    public static final String EVENT_ELECTIONS_HELPER_GET_PARSED_VOTES          = "Get parsed votes for current election"; 
    public static final String EVENT_ELECTIONS_HELPER_CLEAN_ALL_VOTES           = "Clean all votes for current election";

    public static final String EVENT_LOGIN_AUTHENTICATED                        = "User authenticated with username: ";

    public static final String EVENT_USERS_GET_ALL                              = "Get all users";
    public static final String EVENT_USERS_GET_FILTERED                         = "Get filtered users";
    public static final String EVENT_USERS_COUNT_FILTERED                       = "Count filtered users";
    public static final String EVENT_USERS_GET_ONE                              = "Get user with ID: ";
    public static final String EVENT_USERS_SAVE                                 = "Save user with ID: ";
    public static final String EVENT_USERS_DELETE                               = "Delete user with ID: ";
}
