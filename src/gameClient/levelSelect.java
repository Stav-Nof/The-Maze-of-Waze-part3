/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameClient;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class levelSelect extends JFrame implements ActionListener, Runnable{

    static final long serialVersionUID = 1L;
    private JComboBox<String> _combo;
    private MyGameGUI mg;

    public levelSelect(MyGameGUI mg) {
        super("Select level");
        this.setBounds(100, 100, 250, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mg = mg;
    }

    public void createGui() {
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        _combo = new JComboBox<>();
        _combo.setFont(new Font("arial", Font.PLAIN, 20));
        _combo.addItem("random level");
        for (int i = 1; i <= 24; i++) {
        	_combo.addItem("level " + i);
		}
        _combo.setBackground(Color.white);
        _combo.addActionListener(this);
        this.add(_combo);
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
        int level = _combo.getSelectedIndex();
        if (level == 0) {
        	level = (int)((Math.random()*24)+1);
        }
		this.mg.setLevel(level);
		
		
	}

	@Override
	public void run() {
		this.createGui();
	}

}
