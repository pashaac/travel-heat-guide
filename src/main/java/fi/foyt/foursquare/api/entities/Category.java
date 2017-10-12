package fi.foyt.foursquare.api.entities;

import fi.foyt.foursquare.api.FoursquareEntity;

/**
 * Created by Pavel Asadchiy
 * on 22:35 11.09.17.
 */
public class Category implements FoursquareEntity {

    private static final long serialVersionUID = -4573082152802069375L;

    /**
     * Returns category id
     *
     * @return category id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns category name
     *
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Return plural name
     *
     * @return plural name
     */
    public String getPluralName() {
        return pluralName;
    }

    /**
     * Returns icon
     *
     * @return icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Returns parents as array of Strings
     *
     * @return parents as array of Strings
     */
    public String[] getParents() {
        return parents;
    }

    /**
     * Returns if this is a primary category
     *
     * @return is this a primary category
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     * Returns sub categories
     *
     * @return sub categories
     */
    public fi.foyt.foursquare.api.entities.Category[] getCategories() {
        return categories;
    }

    private String id;
    private String name;
    private String pluralName;
    private Icon icon;
    private String[] parents;
    private Boolean primary;
    private fi.foyt.foursquare.api.entities.Category[] categories;

    // TODO
    private String shortName;
}
