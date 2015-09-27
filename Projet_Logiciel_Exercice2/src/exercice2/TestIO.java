package exercice2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestIO {

	public static void main(String[] args) throws IOException {


		BufferedReader bf = new BufferedReader(
				new InputStreamReader (
						new FileInputStream ("/home/gciotto/workspace/exercice2/test.io")));

		FileWriter writer = new FileWriter ("/home/gciotto/workspace/exercice2/test.out");
		
		String line = bf.readLine(); 
		while (line != null) {
			
			String parts[] = line.split(" ");
			
			for (int i = 0; i < parts.length; i++)
				writer.write(parts[i]+'\n');
			
			line = bf.readLine();


		}
		
		bf.close();
		writer.close();
	}

	}
