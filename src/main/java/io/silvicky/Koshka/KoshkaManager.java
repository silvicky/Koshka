package io.silvicky.Koshka;

import javax.swing.*;
import javax.swing.GroupLayout.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;


public class KoshkaManager extends JFrame {
    JComboBox classList;
    JButton addButton,delButton;
    Queue<Koshka> queue;
    public static final String ver="0.2";
    public KoshkaManager()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Koshka Manager");
        classList=new JComboBox<>(findAllClasses("io.silvicky.Koshka").toArray(new Class[0]));
        addButton=new JButton("+");
        delButton=new JButton("-");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class requiredClass= (Class) classList.getSelectedItem();
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
    public List<Class> findAllClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .filter(Objects::nonNull)
                .filter(Koshka.class::isAssignableFrom)
                .collect(Collectors.toList());
    }
    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException ignored) {

        }
        return null;
    }
    public static void main(String[] args)
    {
        KoshkaManager koshkaManager=new KoshkaManager();
    }
}
