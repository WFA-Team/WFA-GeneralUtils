package com.wfa.middleware.utils.beans.impl;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.beans.api.IDirectoryTraverser;

/**
 * A traverser for directory tree, implementing visitor pattern, at every stage
 * exposing a stub of functionality segregating the logic of traversal and the action
 * to be taken at each leaf.
 * 
 * @author -> tortoiseDev
 */
@Component
public class DirectoryTraverser implements IDirectoryTraverser{
	@Override
	public void traverseDirectory(String rootNode, FileVisitor<Path> visitor) {
		Path root = Paths.get(rootNode);
		try {
			Files.walkFileTree(root,visitor);	
		} catch (IOException e) {
			System.err.println("Exception while traversing directory tree. Stack -> " + e.getStackTrace().toString());
		}
	}	
}
