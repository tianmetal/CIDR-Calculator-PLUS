package gui;

import util.CIDRNotation;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class CalculatorGUI extends JFrame
{
	private JTextField fieldStartIP;
	private JTextField fieldEndIP;
	private JTextArea fieldNotations;
	private JButton buttonCalculate;
	private JPanel calculatorPanel;
	private JTextArea fieldBlacklist;

	public CalculatorGUI()
	{
		setTitle("CIDR Calculator");
		setContentPane(calculatorPanel);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fieldBlacklist.setText(load());
		pack();
		setVisible(true);
		buttonCalculate.addActionListener(e -> {
			calculate();
		});
	}

	private void calculate()
	{
		String output = null;
		long startIP, endIP;
		ArrayList<CIDRNotation> notations = new ArrayList<>();
		save(fieldBlacklist.getText());
		String blacklistField = fieldBlacklist.getText().replace("\n", ",");
		String[] blacklistIP = blacklistField.split(",");
		if(fieldStartIP.getText().isEmpty())
		{
			fieldStartIP.setText("0.0.0.0");
		}
		if(fieldEndIP.getText().isEmpty())
		{
			fieldEndIP.setText("255.255.255.255");
		}
		startIP = CIDRNotation.convertFromIPAdrress(fieldStartIP.getText().trim());
		endIP = CIDRNotation.convertFromIPAdrress(fieldEndIP.getText().trim());
		if(blacklistIP.length > 0)
		{
			//Arrays.sort(blacklistIP);
			for(String ip : blacklistIP)
			{
				ip = ip.trim();
				if(!ip.isEmpty())
				{
					CIDRNotation blacklist = new CIDRNotation(ip);
					notations.add(new CIDRNotation(startIP, blacklist.getFirstIPLong() - 1L));
					startIP = blacklist.getLastIPLong() + 1L;
				}
			}
		}
		notations.add(new CIDRNotation(startIP, endIP));

		for(CIDRNotation notation : notations)
		{
			if(output == null) output = notation.printCIDRNotations();
			else output = output.concat("\n" + notation.printCIDRNotations());
		}
		fieldNotations.setText(output);
		pack();
	}

	private void save(String blacklist)
	{
		try
		{
			PrintWriter writer = new PrintWriter("blacklist.txt", "UTF-8");
			writer.print(blacklist);
			writer.close();
		}
		catch(FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	private String load()
	{
		String output = null;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("blacklist.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while(line != null)
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			output = sb.toString();
			br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return ((output == null) ? ("") : (output));
	}
}
