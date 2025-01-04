package com.wfa.middleware.utils.beans.api;

/**
 * A reader for user configurations
 * author = tortoiseDev
 */
public interface IUserConfigExtractor {
	void parseConfigFile(String configPath);
	void parseConfigFile();
	
	String getStringConfig(String configName);
	Integer getIntConfig(String configName);
	Double getDoubleConfig(String configName);
	Integer getDateConfig(String configName);
}
