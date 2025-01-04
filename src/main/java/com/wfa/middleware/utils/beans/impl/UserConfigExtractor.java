package com.wfa.middleware.utils.beans.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.beans.api.IUserConfigExtractor;

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
	private static final String DEFAULT_CONFIG = "mkv.jinit";
	
	UserConfigExtractor() {
		configs = new HashMap<String, Object>();
	}
	
	@Override
	public void parseConfigFile(String configPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] kv = Arrays.stream(line.split(delimiter))
                        .map(String::trim)
                        .filter(element -> !element.isEmpty())
                        .toArray(String[]::new);
                
                if (kv.length != 2)
                {
                	System.err.println("Invalid Config line, key value length = " + kv.length);
                	continue;
                }
                
                configs.put(kv[0], kv[1]);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }			
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
