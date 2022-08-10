package io.silvicky.Koshka;

import javax.swing.*;
import javax.swing.GroupLayout.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.Queue;


public class KoshkaManager extends JFrame {
    JTextField jTextField;
    JButton addButton,delButton;
    Queue<Koshka> queue;
    public static final String ver="0.2";
    public KoshkaManager()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Koshka Manager");
        jTextField=new JTextField("io.silvicky.Koshka.Koshka",10);
        addButton=new JButton("+");
        delButton=new JButton("-");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class<? extends io.silvicky.Koshka.Koshka> requiredClass=Class.forName(jTextField.getText()).asSubclass(io.silvicky.Koshka.Koshka.class);
                    Constructor<? extends io.silvicky.Koshka.Koshka> constructor=requiredClass.getConstructor();
                    Koshka object=constructor.newInstance();
                    queue.add(object);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!queue.isEmpty()) {
                    queue.peek().dispose();
                    queue.remove();
                }
            }
        });
        queue=new ArrayDeque<>();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup h1 = layout.createSequentialGroup();
        h1.addContainerGap();
        h1.addComponent(addButton);
        h1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h1.addComponent(jTextField, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);
        h1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h1.addComponent(delButton);
        h1.addContainerGap();
        hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
        layout.setHorizontalGroup(hGroup);
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup v1 = layout.createSequentialGroup();
        v1.addContainerGap();
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        v2.addComponent(addButton);
        v2.addComponent(jTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        v2.addComponent(delButton);
        v1.addGroup(v2);
        v1.addContainerGap();
        vGroup.addGroup(v1);
        layout.setVerticalGroup(vGroup);
        pack();
        setVisible(true);
    }
    public static void main(String[] args)
    {
        KoshkaManager koshkaManager=new KoshkaManager();
    }
}
