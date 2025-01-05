package com.wfa.middleware.utils.beans.api;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * Directory Traversor to perform a Depth first traversal on the directory tree
 * and perform actions at each step while visiting files
 * 
 * @author = tortoiseDev
 */
public interface IDirectoryTraverser {
	void traverseDirectory(String rootNode, FileVisitor<Path> visitor);
}
