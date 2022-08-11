package io.silvicky.Koshka;


import com.google.common.reflect.ClassPath;

import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;


public class KoshkaManager extends JFrame {
    JComboBox classList;
    JButton addButton,delButton;
    Queue<KoshkaTemplate> queue;
    public static final String ver="0.2";
    public KoshkaManager() throws IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Koshka Manager");
        classList=new JComboBox<>(findAllClasses("io.silvicky.Koshka").toArray(new Class[0]));
        addButton=new JButton("+");
        delButton=new JButton("-");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class requiredClass= (Class) classList.getSelectedItem();
                    Constructor<? extends KoshkaTemplate> constructor=requiredClass.getConstructor();
                    KoshkaTemplate object=constructor.newInstance();
                    queue.add(object);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!queue.isEmpty()) {
                    queue.peek().exit();
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
        h1.addComponent(classList, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);
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
        v2.addComponent(classList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        v2.addComponent(delButton);
        v1.addGroup(v2);
        v1.addContainerGap();
        vGroup.addGroup(v1);
        layout.setVerticalGroup(vGroup);
        pack();
        setVisible(true);
    }
    public List<Class> findAllClasses(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getTopLevelClasses(packageName)
                .stream()
                .map(ClassPath.ClassInfo::load)
                .filter(KoshkaTemplate.class::isAssignableFrom)
                .filter(aClass->aClass!=KoshkaTemplate.class)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) throws IOException {
        KoshkaManager koshkaManager=new KoshkaManager();
    }
}
