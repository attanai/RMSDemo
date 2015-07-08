import java.awt.*;

import javax.swing.*;

import java.text.NumberFormat;
import java.util.*;
import java.awt.event.*;

public class RMS extends JApplet
{
	private JPanel primary;
	private JPanel center;
	private JPanel bottom;
	private JFormattedTextField xAxis;
	private JFormattedTextField yAxis;
	private JButton setAxes;
	private JButton test;
	private JLabel Result;
	private int xAxisCount = 4;
	private int yAxisCount = 4;
	
	
	
	public RMS()
	{
		primary = new JPanel(new BorderLayout(2,2));

		bottom = new JPanel(new BorderLayout(2,2));
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMaximumIntegerDigits(1);
		xAxis = new JFormattedTextField(f);
		xAxis.setValue(4);
		yAxis = new JFormattedTextField(f);
		yAxis.setValue(4);
		setAxes = new JButton("Set Axes");
		setAxes.addActionListener(new setAxesClick());
		test = new JButton("Run Test");
		test.addActionListener(new testClick());
		Result = new JLabel("0");
		
		JPanel bottomLeftLeft = new JPanel(new BorderLayout(2,2));
		bottomLeftLeft.add(new JLabel("X-Axis"), BorderLayout.WEST);
		bottomLeftLeft.add(xAxis, BorderLayout.EAST);
		JPanel bottomLeftCenter = new JPanel(new BorderLayout(2,2));
		bottomLeftCenter.add(new JLabel("Y-Axis"), BorderLayout.WEST);
		bottomLeftCenter.add(yAxis, BorderLayout.EAST);
		JPanel topCenter = new JPanel(new BorderLayout(2,2));
		topCenter.add(bottomLeftLeft, BorderLayout.WEST);
		topCenter.add(bottomLeftCenter, BorderLayout.CENTER);
		JPanel top = new JPanel(new BorderLayout(2,2));
		top.add(topCenter, BorderLayout.WEST);
		top.add(setAxes, BorderLayout.CENTER);
		
		JPanel bottomRight = new JPanel(new BorderLayout(2,2));
		bottomRight.add(new JLabel("Result:"), BorderLayout.WEST);
		bottomRight.add(Result, BorderLayout.EAST);
		
		bottom.add(test, BorderLayout.CENTER);
		bottom.add(bottomRight, BorderLayout.EAST);
		
		buildCenter();
		
		primary.add(bottom, BorderLayout.SOUTH);
		primary.add(top, BorderLayout.NORTH);
		//primary.setPreferredSize(new Dimension(640,480));
		
		add(primary);
		
	}
	
	
	private static int getNumOns(boolean[][] points)
	{
		int total = 0;
		java.util.List<Dimension> ons = new ArrayList<Dimension>();
		java.util.List<java.util.List<Integer>> groups = new ArrayList<java.util.List<Integer>>();
		
		//Iterate through all of the points to find the ons. Put them into a list
		for(int a = 0; a<points.length; a++)
		{
			for(int b = 0; b<points[a].length; b++)
			{
				if(points[a][b] && !ons.contains(new Dimension(a,b)))
				{
					ons.add(new Dimension(a,b));
				}
			}
		}
		
		//dig through the list to find connected pairs, put the indexes of the pairs into a separate list.
		for(Dimension d : ons)
		{
			int x = d.width;
			int y = d.height;
			Dimension z1 = new Dimension(x + 1, y);
			Dimension z2 = new Dimension(x, y + 1);
			if(ons.contains(z1))
			{
				java.util.List<Integer> group = new ArrayList<Integer>();
				group.add(ons.indexOf(d));
				group.add(ons.indexOf(z1));
				groups.add(group);
			}
			if(ons.contains(z2))
			{
				java.util.List<Integer> group = new ArrayList<Integer>();
				group.add(ons.indexOf(d));
				group.add(ons.indexOf(z2));
				groups.add(group);
			}
		}
		
		//Now connect all of the pairs
		int s = groups.size();
		for(int i = 0; i<s; i++)
		{
			boolean flag = true;
			while(flag)
			{
				flag = false;
				for(int j = i+1; j<s; j++)
				{
					try  //concurrent modification exception
					{
						for(int k : groups.get(i))
						{
							for(int l : groups.get(j))
							{
								if(k==l)
								{
									groups.get(i).addAll(groups.get(j));
									groups.remove(j);
									s-=1;
									flag = true;
								}
							}
						}
					}
					catch(Exception ex)
					{
					}
				}
			}
		}
		
		//Now find out which group is the largest
		for(int i = 0; i<groups.size(); i++)
		{
			java.util.List<Integer> group = new ArrayList<Integer>(new LinkedHashSet<Integer>(groups.get(i)));
			total = total > group.size() ? total : group.size();
		}
		
		
		return total;
	}

	private void buildCenter()
	{
		center = new JPanel(new GridLayout(xAxisCount, yAxisCount,2,2));
		for(int i = 0; i<xAxisCount; i++)
		{
			for(int j = 0; j<yAxisCount; j++)
			{
				point x = new point(i,j);
				x.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						x.setOn(!x.getOn());
					}
				});
				center.add(x);
			}
		}
		
		primary.add(center, BorderLayout.CENTER);
	}
	
	///Create two-dimensional array of booleans and calculate
	class testClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int x = xAxisCount;
			int y = yAxisCount;
			
			boolean[][] ons = new boolean[x][y]; 
			int n = 0;
			for(int i = 0; i<x; i++)
			{
				for(int j = 0; j<y; j++)
				{
					point g = (point)center.getComponent(n);
					ons[i][j] = g.getOn();
					n++;
				}
			}
			
			int t = getNumOns(ons);
			Result.setText(String.valueOf(t));
			System.out.println(t);
			
		}
	}
	
	///Change size of layout
	class setAxesClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int x = Integer.parseInt(xAxis.getText());
			int y = Integer.parseInt(yAxis.getText());
			
			if(x>0 && y > 0)
			{
				xAxisCount = x;
				yAxisCount = y;
				
				primary.remove(((BorderLayout)primary.getLayout()).getLayoutComponent(BorderLayout.CENTER));
				buildCenter();
				primary.revalidate();
			}
		}
	}





}


