package hudson.plugins.sloccount.model.cloc;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Cloc XML, header element.
 *
 * @author Michal Turek
 * @since 1.20
 */
public class ClocHeader implements Serializable {
    private static final long serialVersionUID = 1;

    @XmlElement(name = "cloc_url", type = ClocParameter.class)
    private final ClocParameter clocUrl;

    @XmlElement(name = "cloc_version", type = ClocParameter.class)
    private final ClocParameter clocVersion;

    @XmlElement(name = "elapsed_seconds", type = ClocParameter.class)
    private final ClocParameter elapsedSeconds;

    @XmlElement(name = "n_files", type = ClocParameter.class)
    private final ClocParameter filesCount;

    @XmlElement(name = "n_lines", type = ClocParameter.class)
    private final ClocParameter linesCount;

    @XmlElement(name = "files_per_second", type = ClocParameter.class)
    private final ClocParameter filesPerSecond;

    @XmlElement(name = "lines_per_second", type = ClocParameter.class)
    private final ClocParameter linesPerSecond;

    @XmlElement(name = "report_file", type = ClocParameter.class)
    private final ClocParameter reportFile;

    /**
     * Constructor.
     */
    public ClocHeader(ClocParameter clocUrl, ClocParameter clocVersion,
                      ClocParameter elapsedSeconds, ClocParameter filesCount,
                      ClocParameter linesCount, ClocParameter filesPerSecond,
                      ClocParameter linesPerSecond, ClocParameter reportFile) {
        this.clocUrl = clocUrl;
        this.clocVersion = clocVersion;
        this.elapsedSeconds = elapsedSeconds;
        this.filesCount = filesCount;
        this.linesCount = linesCount;
        this.filesPerSecond = filesPerSecond;
        this.linesPerSecond = linesPerSecond;
        this.reportFile = reportFile;
    }

    /**
     * This constructor is required by JAXB.
     */
    public ClocHeader() {
        this(null, null, null, null, null, null, null, null);
    }

    public ClocParameter getClocUrl() {
        return clocUrl;
    }

    public ClocParameter getClocVersion() {
        return clocVersion;
    }

    public ClocParameter getElapsedSeconds() {
        return elapsedSeconds;
    }

    public ClocParameter getFilesCount() {
        return filesCount;
    }

    public ClocParameter getLinesCount() {
        return linesCount;
    }

    public ClocParameter getFilesPerSecond() {
        return filesPerSecond;
    }

    public ClocParameter getLinesPerSecond() {
        return linesPerSecond;
    }

    public ClocParameter getReportFile() {
        return reportFile;
    }
}
