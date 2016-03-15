package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JKyte on 3/14/2016.
 */
public class RandomList {

    /**
     * This list will hold a list of responses to be randomly selected
     */
    private ArrayList<String> list;

    /**
     * This list will hold the last N responses and ensure the bot does
     * not send them again too soon.
     */
    private ArrayList<String> buffer;

    private int bufferLen;

    private Random rand;

    private Logger log = LogManager.getLogger(getClass());

    public RandomList(ArrayList<String> list) {
        copyToList(list);
        setBufferLen(list.size());
        rand = new Random();
    }

    /**
     * We trust you to set a reasonable bufferLen that is in bounds.
     *
     * @param list
     * @param bufferLen
     */
    public RandomList(ArrayList<String> list, int bufferLen) {
        copyToList(list);
        this.bufferLen = bufferLen;
        buffer = new ArrayList<>();
        rand = new Random();
    }

    public void copyToList(ArrayList<String> listToCopy) {
        this.list = new ArrayList<>();
        this.list.addAll(listToCopy);
        buffer = new ArrayList<>();
        rand = new Random();
    }

    public void setBufferLen(int listSize) {
        if (listSize > 2) {
            this.bufferLen = (listSize - 1);
        } else {
            this.bufferLen = 0;
        }
    }

    public String getListEntry() {

        if (buffer.size() == bufferLen) {
            log.info("Removing: " + buffer.remove(0));
        }

        String value = null;
        boolean matchFound = false;
        while (!matchFound) {

            int index = rand.nextInt(bufferLen + 1);
            value = list.get(index);

            if (!buffer.contains(value)) {
                buffer.add(value);
                matchFound = true;
            }
        }

        return value;
    }
}
