package hudson.plugins.sloccount;

import hudson.maven.AbstractMavenProject;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.Publisher;

/**
 *
 * @author lordofthepigs
 */
public class SloccountDescriptor extends Descriptor<Publisher> {

    public SloccountDescriptor(){
        super(SloccountPublisher.class);
    }

    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return !AbstractMavenProject.class.isAssignableFrom(jobType);
    }

    @Override
    public String getDisplayName() {
        return Messages.Sloccount_Publisher_Name();
    }
}
