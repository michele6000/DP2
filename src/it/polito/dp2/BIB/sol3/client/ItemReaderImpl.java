package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.sol3.client.Items.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemReaderImpl implements it.polito.dp2.BIB.ass3.ItemReader {
    private final Integer itemID;
    private final String itemTitle;
    private String itemSubtitle = null;
    private final List<String> itemAuthors;
    private Set<it.polito.dp2.BIB.ass3.ItemReader> citingOfItems;

    public ItemReaderImpl(Item i) {
        this.itemAuthors = new ArrayList<>();
        if (i.getAuthor() != null) {
            if (!i.getAuthor().isEmpty())
                this.itemAuthors.addAll(i.getAuthor());
        }
        citingOfItems = new HashSet<>();
        this.itemTitle = i.getTitle();
        this.itemID = Integer.parseInt(i.getSelf().split("/")[i.getSelf().split("/").length - 1]);

        if (i.getSubtitle() != null)
            this.itemSubtitle = i.getSubtitle();
    }

    @Override
    public String[] getAuthors() {
        String[] a = new String[itemAuthors.size()];
        {
            int i = 0;
            for (String autor : itemAuthors) {
                a[i] = autor;
                i++;
            }
        }
        return a;
    }

    @Override
    public String toString() {

        StringBuilder ret = new StringBuilder("Item id=" + itemID + " Title=" + itemTitle);

        if (itemSubtitle != null)
            ret.append(" Subtitle=").append(itemSubtitle);

        ret.append("\nAuthors=").append(itemAuthors);
        ret.append("\nCiting Items=");


        for (it.polito.dp2.BIB.ass3.ItemReader i : citingOfItems) {
            ret.append(i.getTitle()).append(" ");
        }

        return ret.substring(0, ret.length() - 1);
    }

    @Override
    public Set<it.polito.dp2.BIB.ass3.ItemReader> getCitingItems() {
        if (citingOfItems == null)
            citingOfItems = new HashSet<>();
        return citingOfItems;
    }

    @Override
    public String getSubtitle() {
        return itemSubtitle;
    }

    @Override
    public String getTitle() {
        return itemTitle;
    }

    public Integer getId() {
        return itemID;
    }


}
