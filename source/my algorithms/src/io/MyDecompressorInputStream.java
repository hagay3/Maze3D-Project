package io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decompress the data it gets from inputStream
 *
 */
public class MyDecompressorInputStream extends InputStream {
	protected InputStream in;
	protected int currByte;
	protected int count;
	

	
	/**
	 * constructor with parameters
	 * @param in input stream source
	 * @return MyDecompressorInputStream Object
	 */
	public MyDecompressorInputStream(InputStream in) {
		super();
		this.in = in;

	}
	/**
	 * returning the input stream source
	 * @return input stream source
	 */
	public InputStream getIn() {
		return in;
	}
	/**
	 * setting the input stream source
	 * @param in input stream source
	 */
	public void setIn(InputStream in) {
		this.in = in;
	}
	/**
	 * reading the compressed data from input stream source,and decompress it
	 */
	@Override
	public int read() throws IOException 
	{
		
		if(count<=0)
		{
			if((currByte=in.read())==-1)//if the data is ended
			{
				return -1;
			}
			if((count=in.read())==-1)//if there is problem with the sequence of bytes
			{
				throw (new IOException("Expected counter,invalid byte array!"));
			}
			if(count<=0)//counter is negative
			{
				throw (new IOException("Invalid Counter"));
			}
		}
		//returning currByte count times
		count--;
		return currByte;
	}
	
	
	
	
}
