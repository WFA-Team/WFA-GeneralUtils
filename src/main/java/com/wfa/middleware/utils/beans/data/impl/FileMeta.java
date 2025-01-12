package com.wfa.middleware.utils.beans.data.impl;

import com.wfa.middleware.utils.beans.data.api.IFileMeta;

public class FileMeta implements IFileMeta{

	private String name;
	private String path;
	
	public FileMeta(String name, String path) {
		this.name = name;
		this.path = path;
	}
	@Override
	public String getFileName() {
		return name;
	}

	@Override
	public String getFilePath() {
		return path;
	}
	
	@Override
	public String getId() {
		return path;
	}
}
