package com.sun.midp.appmanager;

import java.util.Vector;

/**
 *  Class contains set of pairs (ID, label)
 *  and one entity market as selected.
 *  It can be used as a data for exclusive
 *  option buttons where each option has an ID.
 */
public class ValueChoiceImpl implements ValueChoice {

    /** Choice title. */
    private String title;

    /** Keeps track of the choice IDs. */
    private Vector ids;

    /** Choice lables. */
    private Vector labels;

    /** Id of selected item. */
    int selectedID;

    /**
     * Creates empty ValueChoice
     * @param title of the choice
     */
    ValueChoiceImpl(String title) {
        this.title = title;
        ids = new Vector(5);
        labels = new Vector(5);
    }

    /**
     * Appends choice to the set.
     *
     * @param label the lable of the element to be added
     * @param id ID for the item
     */
    void append(String label, int id) {
        ids.addElement(id);
        labels.addElement(label);
    }

    /**
     * Set the selected item.
     *
     * @param id ID of selected item
     */
    void setSelectedID(int id) {
        selectedID = id;
    }

    /**
     * Returns the ID of the selected item.
     *
     * @return ID of selected element
     */
    int getSelectedID() {
        return selectedID;
    }

    /**
     * Returns ID of specified item.
     * @param index item index
     * @return item ID
     */
    int getID(int index) {
        return (Integer)ids.elementAt(index);
    }

    /**
     * Returns label of cpecified choice items.
     * @param index item index
     * @return label
     */
    String getLabel(int index) {
        return (String)labels.elementAt(index);
    }

    /**
     * Returns count of items
     * @return count
     */
    int getCount() {
        return ids.size();
    }

    /**
     * Returns choice title.
     * @return title
     */
    String getTitle() {
        return title;
    }

}
