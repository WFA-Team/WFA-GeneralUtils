package com.wfa.middleware.utils.visitors.api;

/**
 * contract for a line visitor, its implementation contains logic to actually read
 * lines of text file.
 * 
 * @author -> tortoiseDev
 */
public interface ILineVisitor {
	void visitLine(String line);
}
