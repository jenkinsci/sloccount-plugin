package hudson.plugins.sloccount.model.cloc;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * Cloc XML, file element.
 *
 * @author Michal Turek
 * @since 1.20
 */
public class ClocFile implements Serializable {
    private static final long serialVersionUID = 1;

    @XmlAttribute
    private final String name;

    @XmlAttribute
    private final int blank;

    @XmlAttribute
    private final int comment;

    @XmlAttribute
    private final int code;

    @XmlAttribute
    private final String language;

    /**
     * Constructor.
     */
    public ClocFile(String name, int blank, int comment, int code, String language) {
        this.name = name;
        this.blank = blank;
        this.comment = comment;
        this.code = code;
        this.language = language;
    }

    /**
     * This constructor is required by JAXB.
     */
    public ClocFile() {
        this(null, 0, 0, 0, null);
    }

    public String getName() {
        return name;
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

    public String getLanguage() {
        return language;
    }
}
