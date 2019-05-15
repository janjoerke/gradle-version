/*******************************************************************************
 * The MIT License
 *
 * Copyright 2019 Jan Jörke <janjoerke@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package com.github.janjoerke.gradle.version;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.List;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class VersionPlugin implements Plugin<Project> {

	static final String GROUP_NAME = "version";
	
	static final String CANDIDATE_TASK_NAME = "candidate";
	static final String HOTFIX_TASK_NAME = "hotfix";
	static final String MAJOR_TASK_NAME = "major";
	static final String MINOR_TASK_NAME = "minor";
	static final String RELEASE_TASK_NAME = "release";

	@Override
	public void apply(Project project) {
		TaskContainer tasks = project.getTasks();
		TaskProvider<Release> releaseTask = registerReleaseTask(tasks);
		registerConvenientTasks(tasks, releaseTask);
	}

	private TaskProvider<Release> registerReleaseTask(TaskContainer tasks) {
		return tasks.register(RELEASE_TASK_NAME, Release.class, task -> {
			task.setGroup(GROUP_NAME);
			task.setDescription("Executes a release and tags the current commit with the current version.");
		});
	}

	private void registerConvenientTasks(TaskContainer tasks, TaskProvider<Release> releaseTask) {
		List<String> taskNames = Arrays.asList(CANDIDATE_TASK_NAME, HOTFIX_TASK_NAME, MAJOR_TASK_NAME, MINOR_TASK_NAME);
		for(String taskName : taskNames) {
			tasks.register(taskName, task -> {
				task.setGroup(GROUP_NAME);
				task.setDescription(format("Execute %s release.", taskName));
				task.dependsOn(releaseTask);
			});
		}
	}

}
