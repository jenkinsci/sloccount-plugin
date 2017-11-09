/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jenkins.plugins.sloccount.steps;

/**
 *
 * @author tzbjxk
 */
import org.apache.commons.lang.StringUtils; 
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition; 
import org.jenkinsci.plugins.workflow.job.WorkflowJob; 
import org.jenkinsci.plugins.workflow.job.WorkflowRun; 
import org.junit.Assert; 
import org.junit.Rule; 
import org.junit.Test; 
import org.jvnet.hudson.test.JenkinsRule; 
import hudson.model.Action;  
import hudson.plugins.sloccount.SloccountBuildAction;  

import java.util.ArrayList;  
import java.util.Arrays; 
import java.util.List; 

public class SloccountPublisherStepTest extends Assert { 
   @Rule public JenkinsRule j = new JenkinsRule(); 

    /** 
     * Test archiving of sloccount reports 
     */ 
    @Test 
    public void archive_1() throws Exception { 
        // job setup 
        WorkflowJob foo = j.jenkins.createProject(WorkflowJob.class, "foo"); 
        foo.setDefinition(new CpsFlowDefinition(StringUtils.join(Arrays.asList( 
                "node {", 
                "  writeFile file: 'sloc.xml', text: '<?xml version=\"1.0\" ?><results><files><file code=\"50\" comment=\"50\" language=\"C/C++\" name=\"file.h\"/><file code=\"300\" comment=\"100\" language=\"C/C++\" name=\"file.c\"/></files></results>'",
                "  sloccountPublish ( ",
                "     pattern: 'sloc.xml',",
                "     encoding: '',",
                "     commentIsCode: false,",
                "     numBuildsInGraph: 1,",
                "     ignoreBuildFailure: false )",
                "}"), "\n"),false)); 
 
        // get the build going, and wait until workflow pauses 
        WorkflowRun b = j.assertBuildStatusSuccess(foo.scheduleBuild2(0).get()); 

        List<SloccountBuildAction> sbas = new ArrayList<>();  
        for (Action a : b.getAllActions()) {  
            if (a instanceof SloccountBuildAction) {  
                sbas.add((SloccountBuildAction) a);  
            }  
        }  
        assertEquals("Should be exactly one SloccountBuildAction",  
                1, sbas.size());  
  
        SloccountBuildAction buildAction = sbas.get(0);  
        assertEquals("BuildAction should have exactly one ProjectAction",  
                 1, buildAction.getProjectActions().size());  
    } 
    
    /** 
     * Test archiving of sloccount reports 
     */ 
    @Test 
    public void archive_2() throws Exception { 
        // job setup 
        WorkflowJob foo = j.jenkins.createProject(WorkflowJob.class, "foo"); 
        foo.setDefinition(new CpsFlowDefinition(StringUtils.join(Arrays.asList( 
                "node {", 
                "  writeFile file: 'sloc.xml', text: '<?xml version=\"1.0\" ?><results><files><file code=\"50\" comment=\"50\" language=\"C/C++\" name=\"file.h\"/><file code=\"300\" comment=\"100\" language=\"C/C++\" name=\"file.c\"/></files></results>'",
                "  step([$class: 'SloccountPublisher',",
                "     pattern: 'sloc.xml',",
                "     encoding: '',",
                "     commentIsCode: false,",
                "     numBuildsInGraph: 1,",
                "     ignoreBuildFailure: false])",
                "}"), "\n"),false)); 
 
        // get the build going, and wait until workflow pauses 
        WorkflowRun b = j.assertBuildStatusSuccess(foo.scheduleBuild2(0).get()); 

        List<SloccountBuildAction> sbas = new ArrayList<>();  
        for (Action a : b.getAllActions()) {  
            if (a instanceof SloccountBuildAction) {  
                sbas.add((SloccountBuildAction) a);  
            }  
        }  
        assertEquals("Should be exactly one SloccountBuildAction",  
                1, sbas.size());  
  
        SloccountBuildAction buildAction = sbas.get(0);  
        assertEquals("BuildAction should have exactly one ProjectAction",  
                 1, buildAction.getProjectActions().size());  
    } 
} 