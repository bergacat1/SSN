package General;

import java.text.SimpleDateFormat;

/**
 * Created by Lluís on 18/11/2015.
 */
public class Globals {

    public static final String TAG = "---------> SSN::";

    public static SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    public static SimpleDateFormat sdfNoHour = new SimpleDateFormat("EEE, d MMM yyyy");
    public static SimpleDateFormat sdfNoDay = new SimpleDateFormat("d MMM yyyy HH:mm");
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_USER = "user";
    public static final String PROPERTY_USER_ID = "userId";
    public static final String PROPERTY_USER_NAME = "userName";

    public static final String PROPERTY_REMEMBER_NO_MANAGED_SELECTION = "remember_no_managed_selection";

    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;

    public static final String SENDER_ID = "1016170460617";
}
