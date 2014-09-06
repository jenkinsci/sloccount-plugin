package hudson.plugins.sloccount.model.cloc;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Cloc XML, total element.
 *
 * @author Michal Turek
 * @since 1.20
 */
public class ClocTotal implements Serializable {
    private static final long serialVersionUID = 1;

    @XmlAttribute
    private final int blank;

    @XmlAttribute
    private final int comment;

    @XmlAttribute
    private final int code;

    /**
     * Constructor.
     */
    public ClocTotal(int blank, int comment, int code) {
        this.blank = blank;
        this.comment = comment;
        this.code = code;
    }

    /**
     * This constructor is required by JAXB.
     */
    public ClocTotal() {
        this(0, 0, 0);
    }

    public int getBlank() {
        return blank;
    }

    public int getComment() {
        return comment;
    }

    public int getCode() {
        return code;
    }
}
