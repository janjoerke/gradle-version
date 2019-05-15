package com.github.janjoerke.gradle.version;

import static java.nio.file.Files.isDirectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.Version;

public class VersionSeeker {

	private static final Logger LOGGER = Logging.getLogger(VersionSeeker.class);

	public Version seek(Path path) {
		Optional<Path> repositoryPath = findRepository(path);
		if (!repositoryPath.isPresent()) {
			LOGGER.warn("No git repository could be found, version will be set to 0.0.0.0.");
			return Version.forIntegers(0);
		}
		return null;
	}

	private Optional<Path> findRepository(Path path) {
		Path _path = path;
		while (_path != null) {
			if (!isDirectory(_path)) {
				try {
					Optional<Path> git = Files.list(_path).filter(p -> p.endsWith(".git")).findFirst();
					if (git.isPresent()) {
						return Optional.of(_path.resolve(git.get()));
					}
				} catch (IOException e) {
					LOGGER.warn("An IOException occured wihle trying to list childrens of path {}.", _path);
					// Do nothing and continue with parent.
				}
			}
			_path = _path.getParent();
		}
		return Optional.empty();
	}
}
