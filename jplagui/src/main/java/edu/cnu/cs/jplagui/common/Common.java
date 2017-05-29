package edu.cnu.cs.jplagui.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Common {
	private static void delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete( c );
		}
		if (f.exists() && !f.delete()) {
			throw new RuntimeException( "Failed to delete file: " + f );
		}
	}

	public static String runJPlag(String path, String language, boolean isRecursive) {
		List<String> list   = new ArrayList<String>();
		String       result = path + "/jplag";
		
		// remove results folder (if existing)
		Path toDelete = Paths.get( result );
		delete( toDelete.toFile() );
		
		// no verbose (quiet)
		list.add( "-vq" );
		
		// recursing sub-directories
		if (isRecursive) { 
			list.add( "-s" );
		}
		// parsing language
		list.add( "-l" );
		list.add( language );

		// results written to "./jplag"
		list.add( "-r" );
		list.add( result );

		// code location
		list.add( path );

		String[] array = new String[ list.size() ];
		for (int i = 0; i < array.length; i++) {
			array[ i ] = list.get( i );
		}
		
		// capture console
		PrintStream           console = System.out;
		ByteArrayOutputStream out     = new ByteArrayOutputStream();
		System.setOut( new PrintStream( out ));
		
		// debug
		for (String a : array) {
			System.out.print( a+" " );
		}
		System.out.println();
		// invoke JPlag
		jplag.JPlag.main( array );

		// display results
		System.setOut( console  );
		return out.toString();
	}

}
