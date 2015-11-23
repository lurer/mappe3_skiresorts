package lib;

import java.text.DecimalFormat;

/**
 * Created by espen on 11/1/15.
 */
public class Static_lib {

    public static DecimalFormat DECIMAL_FORMAT1 = new DecimalFormat(".#");

    public enum USE_API {FNUGG_INIT, FNUGG_DETAIL}
    public enum IMAGE_SCALE {SCALE1, SCALE2, SCALE3}
    public enum IMAGE_TYPE {SMALL_THUMB, BIG_THUMB, COVER}
    public enum WEEKDAY {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;};
    public enum SOCIALMEDIA{TWITTER, FACEBOOK, INSTRAGRAM};
}
