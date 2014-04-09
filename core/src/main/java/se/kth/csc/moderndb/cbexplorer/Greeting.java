package se.kth.csc.moderndb.cbexplorer;

/**
 * This is a simple POJO class that represents a simple greeting class.  It is POJO because
 * its members are either other POJO objects, primitive types, or Strings.  Notice getters
 * and setters are of a specific format.
 *
 * Created by mhotan on 4/8/14.
 */
public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
