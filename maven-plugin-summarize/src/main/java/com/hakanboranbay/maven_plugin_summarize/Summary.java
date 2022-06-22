package com.hakanboranbay.maven_plugin_summarize;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "summarize", defaultPhase = LifecyclePhase.INSTALL)
public class Summary extends AbstractMojo {
	
	@Parameter(defaultValue = "${project}", required = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		String version = project.getVersion();
		String groupId = project.getGroupId();
		String artifactId = project.getArtifactId();
		List<Dependency> dependencies = project.getDependencies();
		List<Plugin> plugins = project.getBuildPlugins();
		List<Developer> developers = project.getDevelopers();
		String releaseDate = project.getProperties().getProperty("releaseDate");
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter("outputFile.txt");
			
			writer.write("Project Info:\n");
			writer.write(groupId + "." + artifactId + "." + version); 
			
			writer.write("\n");
			writer.write("Developers: \n");
			
			int counter = 1;
			for (Developer dev: developers) {
				writer.write("Developer " + counter + ": " + dev.getName());
				counter++;
			}
			
			writer.write("\n");
			writer.write("Plugins: \n");
			
			int counter2 = 1;
			for (Plugin p: plugins) {
				writer.write("Plugin " + counter2 + ": " + p.getArtifactId());
				counter2++;
			}
			
			writer.write("\n");
			writer.write("Dependencies: \n");
			
			int counter3 = 1;
			for (Dependency dep: dependencies) {
				writer.write("Dependency " + counter3 + ": " + dep.getArtifactId());
				counter3++;
			}
			
			writer.write("\n");
			writer.write(releaseDate);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
