package hudson.plugins.sloccount;


import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

/**
 *
 * @author lordofthepigs
 */
@Extension
public class SloccountDescriptor extends BuildStepDescriptor<Publisher> {

    public SloccountDescriptor(){
        super(SloccountPublisher.class);
    }

	@SuppressWarnings("rawtypes")
	@Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        // The plugin is applicable for all job types
        return true;
    }

    @Override
    public String getDisplayName() {
        return Messages.Sloccount_Publisher_Name();
    }
}
