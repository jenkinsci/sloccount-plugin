package hudson.plugins.sloccount;


import hudson.Extension;
import hudson.maven.AbstractMavenProject;
import hudson.model.AbstractProject;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
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

    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return AbstractMavenProject.class.isAssignableFrom(jobType) ||
               FreeStyleProject.class.isAssignableFrom(jobType) ||
               jobType.getSimpleName().equals("JobGenerator");
    }

    @Override
    public String getDisplayName() {
        return Messages.Sloccount_Publisher_Name();
    }
}
