package hudson.plugins.sloccount;

import hudson.util.StackedAreaRenderer2;
import hudson.util.ChartUtil.NumberOnlyBuildLabel;

import org.jfree.data.category.CategoryDataset;

/**
 * Renderer that provides direct access to the individual results of a build via
 * links. This renderer does not render tooltips, these need to be defined in
 * sub-classes.
 */
public class SloccountAreaRenderer extends StackedAreaRenderer2 {
    /** Unique identifier of this class. */
    private static final long serialVersionUID = 1440842055316682192L;
    /** Base URL of the graph links. */
    private final String url;

    /**
     * Creates a new instance of <code>AbstractAreaRenderer</code>.
     *
     * @param url
     *            base URL of the graph links
     */
    public SloccountAreaRenderer(final String url) {
        super();
        this.url = "/" + url + "/";
    }

    /** {@inheritDoc} */
    @Override
    public final String generateURL(final CategoryDataset dataset, final int row, final int column) {
        return getLabel(dataset, column).getRun().getNumber() + url;
    }

    /**
     * Returns the build label at the specified column.
     *
     * @param dataset
     *            data set of values
     * @param column
     *            the column
     * @return the label of the column
     */
    private NumberOnlyBuildLabel getLabel(final CategoryDataset dataset, final int column) {
        return (NumberOnlyBuildLabel)dataset.getColumnKey(column);
    }

    /**
     * Checks this instance for equality with an arbitrary object.
     *
     * @param obj the object
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SloccountAreaRenderer)) {
            return false;
        }
        SloccountAreaRenderer that = (SloccountAreaRenderer) obj;
        return url.equals(that.url) && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this.url);
    }
}
