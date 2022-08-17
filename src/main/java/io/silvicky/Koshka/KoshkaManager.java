package io.silvicky.Koshka;


import com.google.common.reflect.ClassPath;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;


public class KoshkaManager extends JFrame {
    JComboBox classList;
    JTextField genderInput;
    JTextArea debugOutput;
    JScrollPane scrollPane;
    JButton addButton,delButton;
    Queue<Object> queue;
    Queue<Class<? extends KoshkaTemplate>> classQueue;
    public KoshkaManager() throws IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Koshka Manager");
        classList=new JComboBox<>(findAllClasses("io.silvicky.Koshka").toArray(new Class[0]));
        genderInput=new JTextField("",16);
        debugOutput=new JTextArea("Welcome to Koshka!\n",10,40);
        scrollPane=new JScrollPane(debugOutput);
        addButton=new JButton("+");
        delButton=new JButton("-");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class<? extends KoshkaTemplate> requiredClass= (Class) classList.getSelectedItem();
                    Constructor<? extends KoshkaTemplate> constructor;
                    Object object;
                    if(genderInput.getText().length()==0) {
                        constructor = requiredClass.getConstructor(GraphicsConfiguration.class);
                        object = constructor.newInstance(getGraphicsConfiguration());
                    }
                    else {
                        constructor = requiredClass.getConstructor(GraphicsConfiguration.class,String.class);
                        object = constructor.newInstance(getGraphicsConfiguration(),genderInput.getText());
                    }
                    queue.add(object);
                    classQueue.add(requiredClass);
                } catch (Exception ex) {
                    StringWriter sw=new StringWriter();
                    PrintWriter pw=new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    debugOutput.append(sw.toString());
                }
            }
        });
        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!queue.isEmpty()) {
                    classQueue.remove().cast(queue.peek()).exit();
                    queue.remove();
                }
            }
        });
        queue=new ArrayDeque<>();
        classQueue=new ArrayDeque<>();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup h1 = layout.createSequentialGroup();
        SequentialGroup h2 = layout.createSequentialGroup();
        h1.addContainerGap();
        h1.addComponent(addButton);
        h1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h1.addComponent(classList, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE);
        h1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h1.addComponent(genderInput, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE);
        h1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h1.addComponent(delButton);
        h1.addContainerGap();
        h2.addContainerGap();
        h2.addComponent(scrollPane);
        h2.addContainerGap();
        hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
        hGroup.addGroup(GroupLayout.Alignment.TRAILING, h2);
        layout.setHorizontalGroup(hGroup);
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup v1 = layout.createSequentialGroup();
        v1.addContainerGap();
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        ParallelGroup v3=layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        v2.addComponent(addButton);
        v2.addComponent(classList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        v2.addComponent(genderInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        v2.addComponent(delButton);
        v3.addComponent(scrollPane);
        v1.addGroup(v2);
        v1.addContainerGap();
        v1.addGroup(v3);
        v1.addContainerGap();
        vGroup.addGroup(v1);
        layout.setVerticalGroup(vGroup);
        pack();
        setVisible(true);
    }
    public List<Class> findAllClasses(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .map(c->c.load())
                .filter(c->KoshkaTemplate.class.isAssignableFrom(c))
                .filter(c-> !Modifier.isAbstract(c.getModifiers()))
                .collect(Collectors.toList());
    }
    public static void main(String[] args) throws IOException {
        KoshkaManager koshkaManager=new KoshkaManager();
    }
}
