package com.wfa.middleware.utils.beans.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.beans.api.IFileReader;
import com.wfa.middleware.utils.beans.api.IUserConfigExtractor;
import com.wfa.middleware.utils.visitors.api.ILineVisitor;

/**
 * Configuration reader and provider, can read config lines in format
 * key = value and can parse value in required datatype as needed
 * 
 * author = tortoiseDev
 */
@Component
public class UserConfigExtractor implements IUserConfigExtractor {

	private Map<String, Object> configs;
	private static final String delimiter = "=";
	private static final char COMMENT_PREFIX = '#';
	private static final String DEFAULT_CONFIG = "mkv.jinit";
	private final IFileReader fileReader;
	
	UserConfigExtractor(IFileReader fileReader) {
		configs = new HashMap<String, Object>();
		this.fileReader = fileReader;
		doParseConfigFile(DEFAULT_CONFIG, true);
	}
	
	@Override
	public void parseConfigFile(String configPath) {
		doParseConfigFile(configPath, false);
	}
	
	private void doParseConfigFile(String configPath, boolean ignoreErr) {
		fileReader.readFile(configPath, ignoreErr, new ILineVisitor() {
			@Override
			public void visitLine(String line) {
				
				if (line.strip().toCharArray()[0] == COMMENT_PREFIX)
						return; // Don't touch commented line
                
				String[] kv = Arrays.stream(line.split(delimiter))
                        .map(String::trim)
                        .filter(element -> !element.isEmpty())
                        .toArray(String[]::new);
                
                if (kv.length != 2 && !ignoreErr) {
                	System.err.println("Invalid Config line, key value length = " + kv.length);
                	return;
                }
                
                configs.put(kv[0], kv[1]);				
			}
		});		
	}

	@Override
	public String getStringConfig(String configName) {
		Object val = getConfig(configName);
		
		if (val == null)
			return null;
		
		return val.toString();
	}

	@Override
	public Integer getIntConfig(String configName) {
		return Integer.parseInt(getStringConfig(configName));
	}

	@Override
	public Double getDoubleConfig(String configName) {
		return Double.parseDouble(getStringConfig(configName));
	}

	@Override
	public Integer getDateConfig(String configName) {
		
		// TODO Implement Separate Date DataType
		return getIntConfig(configName);
	}
	
	private Object getConfig(String configName) {
		if (!configs.containsKey(configName))
		{
			System.err.println("No such config : " + configName);
		}

		return configs.get(configName);
	}

	@Override
	public void parseConfigFile() {
		parseConfigFile(DEFAULT_CONFIG);	
	}
}
