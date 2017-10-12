package fi.foyt.foursquare.api.entities;

import fi.foyt.foursquare.api.FoursquareEntity;

/**
 * Created by Pavel Asadchiy
 * on 22:33 11.09.17.
 */
public class Icon implements FoursquareEntity {

    /**
     *
     */
    private static final long serialVersionUID = -6661365522571579781L;

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }


    private String prefix;
    private String suffix;
}
