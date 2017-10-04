

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorFrame extends JFrame
{
	public ErrorFrame (String error)
	{
		this.getContentPane().setLayout(new FlowLayout());
		this.getContentPane().add(new JLabel(error));
		this.setVisible(true);
	}
}
