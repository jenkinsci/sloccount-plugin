# Change Log

### Version 1.24 (Dec 22, 2017)

-   Fixed: Concurrent builds are serialized when publishing
    ([JENKINS-43658](https://issues.jenkins-ci.org/browse/JENKINS-43658))

### Version 1.23 (Nov 26, 2017)

-   Fixed: Lines and comments value are inversed in folders tab
    ([JENKINS-37056](https://issues.jenkins-ci.org/browse/JENKINS-37056))

### Version 1.22 (Nov 12, 2017)

-   Implemented: Support the new workflow/pipeline model
    ([JENKINS-35234](https://issues.jenkins-ci.org/browse/JENKINS-35234))

### Version 1.21 (Nov 3, 2015)

-   Implemented: Get total comment count in SloccountReportStatistics
    ([JENKINS-31221](https://issues.jenkins-ci.org/browse/JENKINS-31221)).
    -   Only in cloc based reports, pull request by Jim SERRA.

### Version 1.20 (Sep 6, 2014)

-   Implemented: Native support for cloc tool
    ([JENKINS-24602](https://issues.jenkins-ci.org/browse/JENKINS-24602)).
-   Implemented: Folding help in configuration
    ([JENKINS-24094](https://issues.jenkins-ci.org/browse/JENKINS-24094)).
-   Implemented: Clean jelly code
    ([JENKINS-24093](https://issues.jenkins-ci.org/browse/JENKINS-24093)).
-   Implemented: Show statistics from last build on main job page
    ([JENKINS-24021](https://issues.jenkins-ci.org/browse/JENKINS-24021)).

### Version 1.19 (Apr 15, 2014)

-   Fixed: Error 404 when trying to access language details for "C/C++
    Header"
    ([JENKINS-22597](https://issues.jenkins-ci.org/browse/JENKINS-22597)).

### Version 1.18 (Apr 5, 2014)

-   Implemented: Should have option to not care about build failure
    status
    ([JENKINS-22303](https://issues.jenkins-ci.org/browse/JENKINS-22303)).

### Version 1.17 (Apr 2, 2014)

-   Unsuccessful.

### Version 1.16 (Mar 14, 2014)

-   Fixed: Lazy loaded report details are never released
    ([JENKINS-21921](https://issues.jenkins-ci.org/browse/JENKINS-21921)).
-   Fixed: Fix findings from FindBugs static analysis
    ([JENKINS-22160](https://issues.jenkins-ci.org/browse/JENKINS-22160)).
-   Implemented: Remote access API (REST API)
    ([JENKINS-21922](https://issues.jenkins-ci.org/browse/JENKINS-21922)).

### Version 1.15 (Feb 8, 2014)

-   Fixed: Possibly too wide table on summary page
    ([JENKINS-21557](https://issues.jenkins-ci.org/browse/JENKINS-21557)).
-   Fixed: Sorting using Distribution column/graph doesn't work
    ([JENKINS-21700](https://issues.jenkins-ci.org/browse/JENKINS-21700)).
-   Implemented: Configurable Graph
    ([JENKINS-21552](https://issues.jenkins-ci.org/browse/JENKINS-21552)).
-   Implemented: New Modules panel on report page
    ([JENKINS-21697](https://issues.jenkins-ci.org/browse/JENKINS-21697)).

### Version 1.14 (Jan 26, 2014)

-   Implemented: sloccount report formatting on build summary
    ([JENKINS-13235](https://issues.jenkins-ci.org/browse/JENKINS-13235)).
-   Implemented: Create a dash-board view portlet that shows the SLOC of
    the selected projectes
    ([JENKINS-6876](https://issues.jenkins-ci.org/browse/JENKINS-6876)).
-   Implemented: add a sloccount portlet to dashboard
    ([JENKINS-12166](https://issues.jenkins-ci.org/browse/JENKINS-12166)).
-   Rejected: Support multimodule sloccount report
    ([JENKINS-13382](https://issues.jenkins-ci.org/browse/JENKINS-13382)).

### Version 1.13 (Jan 24, 2014)

-   Fixed: Unable to copy the sloccount summary when the job has been
    executed on a slave with a different OS
    ([JENKINS-21467](https://issues.jenkins-ci.org/browse/JENKINS-21467)).
-   Fixed: Link to workspace root directory is not accessible in results
    ([JENKINS-21500](https://issues.jenkins-ci.org/browse/JENKINS-21500)).
-   Implemented: Can this plugin support CLOC for even more languages
    and better Windows support
    ([JENKINS-10274](https://issues.jenkins-ci.org/browse/JENKINS-10274)).
    -   Existing information in the task tested, a howto about
        [cloc](http://cloc.sourceforge.net/) support written and added
        to wiki.

### Version 1.12 (Jan 18, 2014)

-   Fixed: Can't open sloccount table portlet
    ([JENKINS-21419](https://issues.jenkins-ci.org/browse/JENKINS-21419)).
    -   Release for MS Windows, all long-term issues with Windows
        backslashes '\\' in paths should be fixed now.
-   Outdated information about plugin version \<= 1.6 removed from wiki
    to clean it up.

### Version 1.11 (Jan 15, 2014)

-   Fixed: Memory consumption is huge
    ([JENKINS-4769](https://issues.jenkins-ci.org/browse/JENKINS-4769)).
    -   Changes in data storage that heavily reduces memory consumption
        and increases performance during a common Jenkins use.
    -   Stored data now contains statistics per language (just several
        numbers), full report is lazy-loaded only while it is needed in
        the results page. Compare with parsing of giant XML that
        contains information about each source file present in your
        sandbox multiplied by number of builds just to show a simple
        trend graph.
    -   Data format is very different but the code should satisfy a
        backward compatibility, migration tested with plugin versions
        1.8 and 1.10.
    -   The plugin update will have full impact after builds from a
        previous version will completely role out and only new data are
        present.
-   Fixed: NoClassDefFoundError exception on job configuration page
    ([JENKINS-19629](https://issues.jenkins-ci.org/browse/JENKINS-19629)).
-   Fixed: Support for matrix projects
    ([JENKINS-12816](https://issues.jenkins-ci.org/browse/JENKINS-12816))
    and IVY projects
    ([JENKINS-17024](https://issues.jenkins-ci.org/browse/JENKINS-17024)).
    -   All project types should be supported now.
    -   Unnecessary dependencies to Maven and FindBugs removed.
-   Fixed: Summary is missing for the first build
    ([JENKINS-20646](https://issues.jenkins-ci.org/browse/JENKINS-20646)).
-   Fixed: Broken link in menu on the job page before the first build
    ([JENKINS-21174](https://issues.jenkins-ci.org/browse/JENKINS-21174)).
-   Fixed: Broken differences in build summary for failed builds
    ([JENKINS-21223](https://issues.jenkins-ci.org/browse/JENKINS-21223)).
-   Fixed: Possible resource leak in parser
    ([JENKINS-21229](https://issues.jenkins-ci.org/browse/JENKINS-21229)).
-   Fixed: Broken HTML code on results page
    ([JENKINS-21230](https://issues.jenkins-ci.org/browse/JENKINS-21230)).
-   Fixed: Graphical artifacts in the trend graphs
    ([JENKINS-21258](https://issues.jenkins-ci.org/browse/JENKINS-21258)).
-   Implemented: SLOCCount churn graph
    ([JENKINS-14504](https://issues.jenkins-ci.org/browse/JENKINS-14504)).
    -   New trend graph with differences in lines count between current
        and previous build.
-   Wiki updates - information, screenshots.

### Version 1.10 (Apr 16, 2013)

-   fixed java.io.InvalidClassException
    ([JENKINS-14255](https://issues.jenkins-ci.org/browse/JENKINS-14255)).
-   Made the plugin applicable for Job Generator jobs.

### Version 1.8 (Jun 16, 2012)

-   sloccount trend report only works up to last failed build
    ([JENKINS-9309](https://issues.jenkins-ci.org/browse/JENKINS-9309)).
-   SLOCCount plugin fails with Windows/Cygwin SLOCCount execution
    ([JENKINS-4836](https://issues.jenkins-ci.org/browse/JENKINS-4836)).

### Version 1.7 (May 19, 2012)

-   String index out of range
    ([JENKINS-13775](https://issues.jenkins-ci.org/browse/JENKINS-13775)).
-   added Japanese localization.
-   use commas in numbers for readability
    ([JENKINS-13235](https://issues.jenkins-ci.org/browse/JENKINS-13235)).
-   sort lines and files numerically.
-   fixed breadcrumb move down.

### Version 1.6 (Oct 19, 2011)

-   Allow Maven support

### Version 1.5 (Feb 14, 2011)

-   Update link in help
-   Translation updates (French, Dutch)

### Version 1.4 (Feb 10, 2010)

-   Update code for more recent Hudson

### Version 1.3 (Jul 7, 2009)

-   Made to work with distributed builds
    ([patch](http://www.nabble.com/sloccount-plugin-and-distributed-builds--%3E-exception-td22791316.html))

### Version 1.2 (Feb 28, 2009)

-   Clicking on the SLOCCount icon in the left sidebar will now take you
    directly to the last build's detailed sloccount report instead of
    that build's general main page

### Version 1.1 (Feb 24, 2009)

-   Added breadcrumbs to help in the navigation of the result report

### Version 1.0 (Feb 20, 2009)

-   Initial release
