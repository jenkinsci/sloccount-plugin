package hudson.plugins.sloccount.model.cloc;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * Cloc XML, files element.
 *
 * @author Michal Turek
 * @since 1.20
 */
public class ClocFiles implements Serializable {
    private static final long serialVersionUID = 1;

    @XmlElement(name = "file", type = ClocFile.class)
    private final List<ClocFile> files;

    @XmlElement(name = "total", type = ClocTotal.class)
    private final ClocTotal total;

    /**
     * Constructor.
     */
    public ClocFiles(List<ClocFile> files, ClocTotal total) {
        this.files = files;
        this.total = total;
    }

    /**
     * This constructor is required by JAXB.
     */
    public ClocFiles() {
        this(null, null);
    }

    public List<ClocFile> getFiles() {
        return files;
    }

    public ClocTotal getTotal() {
        return total;
    }
}
