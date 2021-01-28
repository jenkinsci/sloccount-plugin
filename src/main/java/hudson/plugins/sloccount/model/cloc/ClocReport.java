package hudson.plugins.sloccount.model.cloc;

import hudson.plugins.sloccount.model.SloccountReportInterface;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Cloc report parser and the parsed file.
 *
 * @author Michal Turek
 * @since 1.20
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "results")
public class ClocReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "header", type = ClocHeader.class)
    private final ClocHeader header;

    @XmlElement(name = "files", type = ClocFiles.class)
    private final ClocFiles files;

    /**
     * Constructor.
     * @param header header
     * @param files files
     */
    public ClocReport(ClocHeader header, ClocFiles files) {
        this.header = header;
        this.files = files;
    }

    /**
     * This constructor is required by JAXB.
     */
    public ClocReport() {
        this(null, null);
    }

    public ClocHeader getHeader() {
        return header;
    }

    public ClocFiles getFiles() {
        return files;
    }

    /**
     * Parse one input file.
     *
     * @param file the file to be parsed
     * @return the content of the parsed file in form of a report
     * @throws javax.xml.bind.JAXBException if a XML related error occurs
     */
    public static ClocReport parse(File file) throws JAXBException, java.io.IOException {
        JAXBContext context = JAXBContext.newInstance(ClocReport.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        // Jenkins replaces ASCII symbols like colons found in filenames to Unicode.
        // Using File() here causes converting Unicode symbols back to ASCII.
        // Such files could not be found of course.
        try (InputStream is = new FileInputStream(file)) {
          return (ClocReport) unmarshaller.unmarshal(is);
        }
    }

    /**
     * Convert cloc report to SLOCCount report representation used in the plugin.
     *
     * @param report        output report
     * @param commentIsCode include comments to the measured lines
     * @throws JAXBException if the report has unexpected structure
     */
    public void toSloccountReport(SloccountReportInterface report, boolean commentIsCode)
            throws JAXBException {
        try {
            for (ClocFile file : files.getFiles()) {
                // Get rid of Microsoft's incompatibility once and forever
                String filePath = file.getName().replace('\\', '/');

                int begin = filePath.indexOf('/');
                int end = filePath.indexOf('/', begin + 1);
                String moduleName = (begin != -1 && end != -1) ? filePath.substring(begin + 1, end) : "";

                int lineCount = file.getCode();
                if (commentIsCode) {
                    lineCount += file.getComment();
                }

                report.add(filePath, file.getLanguage(), moduleName, lineCount, file.getComment());
            }
        } catch (RuntimeException e) {
            throw new JAXBException("Broken cloc report file: " + e, e);
        }
    }
}
