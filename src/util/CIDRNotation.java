package util;

import java.util.ArrayList;

public class CIDRNotation
{
	private long startIP,endIp;
	private ArrayList<String> notations;

	public CIDRNotation()
	{
		startIP = 0;
		endIp = 4294967295L;
		notations = new ArrayList<>();
		notations.add("0.0.0.0/0");
	}
	public CIDRNotation(String startIP, String endIP)
	{
		this.startIP = convertFromIPAdrress(startIP);
		this.endIp = convertFromIPAdrress(endIP);
		this.notations = new ArrayList<>();
		calculateCIDRNotations();
	}
	public CIDRNotation(long startIP, long endIP)
	{
		this.startIP = startIP;
		this.endIp = endIP;
		this.notations = new ArrayList<>();
		calculateCIDRNotations();
	}
	public CIDRNotation(String notation)
	{
		String addr[] = notation.split("/");
		long ip = convertFromIPAdrress(addr[0]);
		int mb = Integer.parseInt(addr[1]);
		long size = (long)Math.pow(2,(32-mb));
		this.startIP = ip - (ip % size);
		this.endIp = this.startIP + size - 1L;
		this.notations = new ArrayList<>();
		notations.add(convertToIPAddress(this.startIP) + "/" + mb);
	}

	private String convertToIPAddress(long ip)
	{
		return String.format("%d.%d.%d.%d",(ip/16777216L),(ip % 16777216L)/65536L,(ip % 65536L)/256L,(ip % 256L));
	}
	public static long convertFromIPAdrress(String ip)
	{
		String[] octet = ip.split("\\.");
		return ((Long.parseLong(octet[0])*16777216L)+(Long.parseLong(octet[1])*65536L)+(Long.parseLong(octet[2])*256L)+(Long.parseLong(octet[3])));
	}
	private void calculateCIDRNotations()
	{
		long start = startIP;
		long end = endIp;
		long maxSize = end-start;
		int mb = 32;
		int lowest = 32;
		while(true)
		{
			long size = (long)Math.pow(2,(32-mb));
			long ip = (end-(size-1L));
			if(end < start) break;
			if((ip % size) == 0L && (size-1L <= maxSize) && (ip >= start))
			{
				lowest = mb;
				mb--;
			}
			else
			{
				size = (long)Math.pow(2,(32-lowest));
				ip = (end-(size-1L));
				notations.add(convertToIPAddress(ip) + "/" + lowest);
				end = ip-1L;
				maxSize = end-start;
				mb = lowest = 32;
			}
		}
	}

	public String getFirstIPAddress()
	{
		return convertToIPAddress(startIP);
	}
	public String getLasIPAddress()
	{
		return convertToIPAddress(endIp);
	}
	public long getFirstIPLong()
	{
		return startIP;
	}
	public long getLastIPLong()
	{
		return endIp;
	}
	public String printCIDRNotations()
	{
		String output = null;
		for(int i = notations.size()-1; i >= 0; i--)
		{
			String notation = notations.get(i);
			if(output == null) output = notation;
			else output = output.concat("\n" + notation);
		}
		return output;
	}
}
