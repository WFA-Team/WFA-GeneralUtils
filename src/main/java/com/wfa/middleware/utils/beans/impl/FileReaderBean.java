package com.wfa.middleware.utils.beans.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.wfa.middleware.utils.beans.api.IFileReader;
import com.wfa.middleware.utils.visitors.api.ILineVisitor;

/**
 * Basic bean to open a file on a certain path and iterate over its lines;
 * It expects a visitor which will perform logic on each line
 * 
 * @author -> tortoiseDev
 */
@Component
public class FileReaderBean implements IFileReader {

	@Override
	public void readFile(String filePath, boolean ignoreErr, ILineVisitor visitor) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	visitor.visitLine(line);
            }
        } catch (IOException e) {
        	if (!ignoreErr)
            	System.err.println("Error reading file: " + e.getMessage());
        }		
	}
}
