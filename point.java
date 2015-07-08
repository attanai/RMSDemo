import javax.swing.JLabel;

	public class point extends JLabel
	{
		private char offDot = 0x2022;
		private char onDot = 0x2E0B;
		private int xAxis;
		private int yAxis;
		private boolean on = false;
		
		public point(int xAxis, int yAxis)
		{
			setOn(false);
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.setVerticalAlignment(CENTER);
			this.setHorizontalAlignment(CENTER);
		}
		
		public point()
		{
			setOn(false);
			this.setVerticalAlignment(CENTER);
			this.setHorizontalAlignment(CENTER);
		}
		
		public void setOn(boolean on)
		{
			this.on = on;
			this.setText(on ? String.valueOf(onDot) : String.valueOf(offDot));
		}
		
		public boolean getOn()
		{
			return on;
		}
		
		public int getXAxis()
		{
			return xAxis;
		}
		
		public int getYAxis()
		{
			return yAxis;
		}
		
			
	}
