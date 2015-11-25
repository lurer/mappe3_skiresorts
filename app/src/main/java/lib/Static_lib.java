package lib;

import java.text.DecimalFormat;

/**
 * Created by espen on 11/1/15.
 */
public class Static_lib {

    public static DecimalFormat DECIMAL_FORMAT_EN = new DecimalFormat(".#");
    public static DecimalFormat DECIMAL_FORMAT_NO = new DecimalFormat(",#");

    public enum USE_API {FNUGG_INIT, FNUGG_DETAIL}

    public final static String prefNordNorge = "Nord-Norge";
    public final static String prefMidtNorge = "Midt-Norge";
    public final static String prefNordVestlandet = "Nord-Vestlandet";
    public final static String prefSorVestlandet = "Sør-Vestlandet";
    public final static String prefSorlandet = "Sørlandet";
    public final static String prefOstlandet = "Østlandet";

}
