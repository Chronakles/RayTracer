package a11911340;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ObjParser {
	
	private boolean hasTC;
	private boolean hasVN;

	public ObjParser(String fileName) {
		this.hasTC = false;
		this.hasVN = false;
		
		BufferedReader mash = null;
		
		try {
			mash = new BufferedReader(new FileReader("./" + fileName));
			String line;
			
			while((line = mash.readLine()) != null) {
			
				String[] fragment = line.split(" ");
				if( fragment.length == 0) {
					continue;
				}
				
				if(line.startsWith("#")) {
					continue;
				}

				
				if (fragment[0].equals("v")) {
			
					
					Output.meshVertex.add(new MyVector(
							Double.valueOf(fragment[1]), 
							Double.valueOf(fragment[2]),
							Double.valueOf(fragment[3])
							));
					
	            } else if (fragment[0].equals("vt")) {

	            	Output.meshTexCo.add(new MyVector(
							Double.valueOf(fragment[1]), 
							Double.valueOf(fragment[2]),
							0.0
							));
	            	
	            } else if (fragment[0].equals("vn")) {
	            	
	            	Output.meshNormals.add(new MyVector(
							Double.valueOf(fragment[1]), 
							Double.valueOf(fragment[2]),
							Double.valueOf(fragment[3])
							));
	            	
	            } else if (fragment[0].equals("f")) {
	                
	            	for(int i = 0; i < fragment.length - 3; i++) {
	            		Output.meshIndices.add(parseOBJIndex(fragment[1]));
	            		Output.meshIndices.add(parseOBJIndex(fragment[2 + i]));
	            		Output.meshIndices.add(parseOBJIndex(fragment[3 + i]));
	            	}
	            	
	            	
	            	
	            }
				
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	    
	
	private OBJIndex parseOBJIndex(String token) {
		
		String[] values = token.split("/");
		
		OBJIndex result = new OBJIndex();
		result.vertexIndex = Integer.parseInt(values[0]) - 1;
		
		if(values.length > 1) {
			hasTC = true;
			result.texCoIndex = Integer.parseInt(values[1]) - 1;
			
			if(values.length > 2) {
				hasVN = true;
				result.normalIndex = Integer.parseInt(values[2]) - 1;
			}
		}
		
		return result;
		
	}
}
