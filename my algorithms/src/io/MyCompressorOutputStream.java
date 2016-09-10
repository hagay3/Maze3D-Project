package io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * compress given data and write it to the output stream source
 */
public class MyCompressorOutputStream extends OutputStream 
{
	protected OutputStream out;
	protected int previousByte;
	protected int count;
	

	
	/**
	 * constructor using fields
	 * @param out output stream source
	 * 
	 */
	public MyCompressorOutputStream(OutputStream out) {
		super();
		this.out = out;
		this.count=0;

	}
	/**
	 * returning output stream source
	 * @return output stream source
	 */
	public OutputStream getOut() {
		return out;
	}
	/**
	 * setting output stream source
	 * @param out output stream source
	 */
	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	
	
	
	/**
	 * Compressing data and then writing to data source
	 */
	@Override
	public void write(int num) throws IOException 
	{
		if(count==0)//if it is the first time we writing something to data source
		{
			this.previousByte=num;
			this.count=1;
			return;
		}
		
		
		if(num==this.previousByte)//if we read the same byte,count it
		{
			
			count++;
			//if there are more than 255 bytes from the same type,write the byte than 255 and than starting count again
			if(count==256)
			{
				out.write(previousByte);
				out.write(255);
				count=1;
			}
		}
		else//new byte,lets write the previus ones
		{
			System.out.println("count: "+(int)count+" previousByte: "+(int)previousByte);
			out.write(previousByte);
			out.write(count);
			this.previousByte=num;
			this.count=1;
		}

	}
	/**
	 * writing byte array to data source
	 */
	@Override
	public void write(byte[] byteArr) throws IOException 
	{
		//because the write method that get only integer didn't write the last byte,
		//we have to override this method,and writing the last byte the data source
		super.write(byteArr);//first calling super's method
		if(count>0)//writing the last byte
		{
			
			out.write((byte)previousByte);
			out.write((byte)count);
		}
		
		//In case we continue writing after something else
		count=0;
		previousByte=0;
	}
	
}
