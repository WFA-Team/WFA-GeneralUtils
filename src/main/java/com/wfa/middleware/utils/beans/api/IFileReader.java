package com.wfa.middleware.utils.beans.api;

import com.wfa.middleware.utils.visitors.api.ILineVisitor;

public interface IFileReader {
	void readFile(String filePath, boolean ignoreErr, ILineVisitor visitor);
}
