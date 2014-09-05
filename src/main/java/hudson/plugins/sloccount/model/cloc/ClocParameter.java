package hudson.plugins.sloccount.model.cloc;

import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

/**
 * Cloc XML, parameter in header element.
 *
 * @author Michal Turek
 * @since 1.20
 */
public class ClocParameter implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * Value of the parameter.
     */
    @XmlValue
    private final String value;

    /**
     * Constructor.
     *
     * @param value the value
     */
    public ClocParameter(String value) {
        this.value = value;
    }

    /**
     * This constructor is required by JAXB.
     */
    @SuppressWarnings("unused")
    public ClocParameter() {
        this(null);
    }

    public String getValue() {
        return value;
    }
}
