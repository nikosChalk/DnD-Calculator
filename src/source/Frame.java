/*
 * Copyright 2014 Nikos Chalkiadakis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package source;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author Nikos Chalkiadakis
 */
public class Frame extends javax.swing.JFrame {
    public static BufferedImage FRAME_ICON_IMAGE;   //initialized at main. Used as Frame Icon
    public static BufferedImage SPLASH_SCREEN_IMG;  //initialized at main. Used at splash screen.
    public static BufferedImage ABOUT_ICON_IMAGE;   //initialized at main. Used at About Panel.
    public static BufferedImage ADD_MONSTER_ICON_IMAGE; //initialized at main. Used at AddMonsterDialog.
    public static ImageIcon CLEAR_ALL_DIALOG_IMG;          //initialized at main. Used at Clear all screen.
    private static final int FRAME_BOTTOM_OFFSET = 100;
    private static final int FRAME_RIGHT_OFFSET = 50;

    public Frame() {
        setLookAndFeel();
        initComponents();
        prepareClasses();
        this.setIconImage(FRAME_ICON_IMAGE);
    }
    
    private void prepareClasses() {
        MyListener listener = new MyListener(this);
        
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);   //gia ta mousEvents pou ta pianei apo to eventDispatch thread.
        menuActionsAddMonster.addActionListener(listener);
        menuActionsClearAll.addActionListener(listener);
        menuActionsAbout.addActionListener(listener);
    }
    
    private class MyListener implements ActionListener, AWTEventListener {
        private Frame frame;
        private MonsterPanel focusedPanel;
        private ArrayList<ArrayList <MonsterPanel>> mPanelsArrayList;
        
        private MyListener(Frame frame) {
            this.frame = frame;
            mPanelsArrayList = new ArrayList<>();
            mPanelsArrayList.add(new ArrayList<>());
            focusedPanel = null;
        }
        
        @Override
        public void eventDispatched(AWTEvent event) {   //gia ta mousEvents pou ta pianei apo to eventDispatch thread.
            if(event.getID() == MouseEvent.MOUSE_CLICKED) {
                MouseEvent mouseEvent = (MouseEvent)event;
                Component clickedComponent = mouseEvent.getComponent();

                if(clickedComponent != null) {
                    if(clickedComponent.equals(monstersScrollContainer)) {  //o monadikos top parent pou borei na kanei click.
                        for(int i=0; i<3; i++)
                            clickedComponent = clickedComponent.getComponentAt(mouseEvent.getPoint());   //monstersScrollPanel --> JviewPoint --> monstersContainer --> MonsterPanel calss
                        if(clickedComponent instanceof MonsterPanel)    //an den exei stamathsei se kapoies apo tis parapanw metavivaseis epeidh den vrike componentAt kai epestrepse ton eauto tou.
                            setMonsterPanelFocus((MonsterPanel)clickedComponent);
                    } else {    //twra eimaste se components katw apo to monsterPanel h sto JMenu.
                        while(!(clickedComponent instanceof MonsterPanel) && clickedComponent != null) {
                            clickedComponent = clickedComponent.getParent();
                        }
                        if(clickedComponent instanceof MonsterPanel)
                            setMonsterPanelFocus((MonsterPanel)clickedComponent);
                    }
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource() instanceof JMenuItem) {
                JMenuItem source = (JMenuItem)event.getSource();
                
                if(source.equals(menuActionsAddMonster)) {
                    AddMonsterDialog dialog = new AddMonsterDialog(frame, true);
                    MonsterPanel createdMPanels[] = dialog.display();
                    int screenWidth = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
                    
                    for(MonsterPanel mPanel : createdMPanels) {
                        int adjustedWidth = (mPanelsArrayList.get(mPanelsArrayList.size()-1).size() + 1)*mPanel.getPreferredSize().width; //to neo width an minei sthn idia seira kai valei mPanel.

                        if(adjustedWidth > screenWidth) { //an currentRow's mPanels are greater than screenWidth.
                            mPanelsArrayList.add(new ArrayList<>());    //epomeno row.
                        }
                        mPanel.getDeleteButton().addActionListener(this);
                        monstersContainer.add(mPanel);
                        mPanelsArrayList.get(mPanelsArrayList.size()-1).add(mPanel);
                        setFixedSize();
                        frame.pack();
                        setMonsterPanelFocus(mPanel);
                    }
                } else if(source.equals(menuActionsClearAll)) {
                    if(!(mPanelsArrayList.get(0).isEmpty())) {    //ean einai empty, den exei nohma tipota apo ta parakatw.
                        int confirm = JOptionPane.showConfirmDialog(frame
                                ,"This will remove all Monster Calculators. Are you sure you want to continue?\n   (*Players Shouting* - Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes!)"
                                ,"Holy Mother Potato! We gonna Live!"
                                ,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, CLEAR_ALL_DIALOG_IMG);
                        if(confirm != JOptionPane.YES_OPTION)   //He should be sure.
                            return;
                        monstersContainer.removeAll();
                        mPanelsArrayList.clear();
                        mPanelsArrayList.add(new ArrayList<>());
                        setSizeEmpty();
                        frame.pack();
                        frame.repaint();
                    }
                } else if(source.equals(menuActionsAbout)) {
                    AboutPanel aboutPanel = new AboutPanel(frame, true);
                    aboutPanel.setVisible(true);
                }
            } else if(event.getSource() instanceof JButton) {   //delete button of monsterPanels.
                JButton source = (JButton)event.getSource();
                
                if(source.getActionCommand().equals("deleteMonsterPanel")) {
                    for(int i=0; i<mPanelsArrayList.size(); i++) {
                        for(int j=0; j<mPanelsArrayList.get(i).size(); j++) {   //pianoume kathe panel
                            MonsterPanel mPanel = mPanelsArrayList.get(i).get(j);
                            
                            for(Component mPanelComp : mPanel.getComponents()) {    //eksetazoume poio panel exei auto to button.
                                if(source.equals(mPanelComp)) { //found the correct monsterPanel.
                                    System.out.println("Deleting panel.");
                                    monstersContainer.remove(mPanel);       //removing from GUI.
                                    mPanelsArrayList.get(i).remove(j--);    //removing from arrayList. j is shifting. Now j could have an INVALID value (j<0).

                                    for(int k=i; k<mPanelsArrayList.size(); k++) {    //ena yparxei kiallh seira meta apo ekei pou evgala, tote ginetai shifting.
                                        if(mPanelsArrayList.get(k).isEmpty()) {
                                            System.out.println("removing row due to shifting.");
                                            mPanelsArrayList.remove(k--);   //ean to k==i kai kanei remove row, tote prepei kai to i na kanei shift. (i--).
                                            break;   //den exei nohma to ypoloipo an kanei remove row, giati panda oi megistes seires pou borei na vgalei einai 1. Epishs den ekteleitai oute h parakatw if.
                                        }
                                        if(k+1<mPanelsArrayList.size()) {
                                            System.out.println("Shifting panel.");
                                            MonsterPanel shiftingPanel = mPanelsArrayList.get(k+1).remove(0);
                                            mPanelsArrayList.get(k).add(shiftingPanel);
                                        }
                                    }
                                    
                                    if(mPanelsArrayList.isEmpty()) {
                                        System.out.println("It is empty.");
                                        mPanelsArrayList.add(new ArrayList<>());
                                        setSizeEmpty();
                                    } else {
                                        setFixedSize();
                                    }
                                    
                                    frame.pack();
                                    frame.repaint();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        private void setMonsterPanelFocus(MonsterPanel panel) {
            if(!(panel.equals(focusedPanel))) {
                if(focusedPanel != null) //giati sto init einai null.
                    focusedPanel.setFocused(false);
                focusedPanel = panel;
                focusedPanel.setFocused(true);
                frame.repaint();
            }
        }
        
        private void setSizeEmpty() {
            monstersContainer.setPreferredSize(new Dimension(350, 475));
            frame.setPreferredSize(new Dimension(monstersContainer.getPreferredSize().width + FRAME_RIGHT_OFFSET, monstersContainer.getPreferredSize().height + FRAME_BOTTOM_OFFSET));
        }
        private void setFixedSize() {
            MonsterPanel samplePanel = new MonsterPanel();
            int screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
            int mPanelsHeight = mPanelsArrayList.size() * samplePanel.getPreferredSize().height;
            int frameHeight;
            
            if(mPanelsHeight > screenHeight - 75) {
                frameHeight = screenHeight - FRAME_BOTTOM_OFFSET;
            } else if(mPanelsHeight + FRAME_BOTTOM_OFFSET > screenHeight - 75) {
                frameHeight = screenHeight - FRAME_BOTTOM_OFFSET;
            } else {
                frameHeight = mPanelsHeight + FRAME_BOTTOM_OFFSET;
            }
            monstersContainer.setPreferredSize(new Dimension(mPanelsArrayList.get(0).size()*samplePanel.getPreferredSize().width, mPanelsHeight));
            frame.setPreferredSize(new Dimension(monstersContainer.getPreferredSize().width + FRAME_RIGHT_OFFSET, frameHeight));
        }
    }
    
    private void setLookAndFeel() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            javax.swing.SwingUtilities.updateComponentTreeUI(this);
        } catch(Exception ex) {
            System.err.println("Unsupported LookAndFeel.");
        }
    }
    
    public static void main(String args[]) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");  //Logo bug sto JDK, otan ekana remove MonsterPanel apo to GUI petouse error.
        FRAME_ICON_IMAGE = readImg("logo.png");
        SPLASH_SCREEN_IMG = readImg("welcome.png");
        ABOUT_ICON_IMAGE = readImg("aboutIcon.png");
        ADD_MONSTER_ICON_IMAGE = readImg("monsterIcon.png");
        CLEAR_ALL_DIALOG_IMG = new ImageIcon(readImg("clear_all.png"));
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }
    
    public static BufferedImage readImg(String mediaFileName) {
        try {
            return ImageIO.read(Frame.class.getResource("/media/" + mediaFileName));
        } catch (IOException io) {
            System.err.println("Coud not read image: " + mediaFileName);
            return null;
        }
    }
    
    
//  <editor-fold desc="Generated Code." defaultstate="collapsed">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        monstersScrollContainer = new javax.swing.JScrollPane();
        monstersContainer = new javax.swing.JPanel() {
            @Override
            public void paintComponent(Graphics comp) {
                super.paintComponent(comp);
                if(this.getComponents().length == 0 ) {
                    Graphics2D comp2D = (Graphics2D)comp;

                    comp2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    comp2D.drawImage(SPLASH_SCREEN_IMG, this.getWidth()/2 - SPLASH_SCREEN_IMG.getWidth()/2, this.getHeight()/2 - SPLASH_SCREEN_IMG.getHeight()/2, null);
                }
            }
        }
        ;
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuActionsAddMonster = new javax.swing.JMenuItem();
        menuActionsClearAll = new javax.swing.JMenuItem();
        menuActionsAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("D&D HP Monster Calculator");
        setPreferredSize(new Dimension(350 + FRAME_RIGHT_OFFSET, 475 + FRAME_BOTTOM_OFFSET));

        monstersContainer.setPreferredSize(new java.awt.Dimension(337, 484));
        monstersContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        monstersScrollContainer.setViewportView(monstersContainer);

        jMenu1.setText("Actions");

        menuActionsAddMonster.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menuActionsAddMonster.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/monsterIcon.png"))); // NOI18N
        menuActionsAddMonster.setText("Add Monster");
        jMenu1.add(menuActionsAddMonster);

        menuActionsClearAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuActionsClearAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/clearAllIcon.png"))); // NOI18N
        menuActionsClearAll.setText("Clear All");
        jMenu1.add(menuActionsClearAll);

        menuActionsAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        menuActionsAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/aboutIcon.png"))); // NOI18N
        menuActionsAbout.setText("About/Info");
        jMenu1.add(menuActionsAbout);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(monstersScrollContainer)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(monstersScrollContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem menuActionsAbout;
    private javax.swing.JMenuItem menuActionsAddMonster;
    private javax.swing.JMenuItem menuActionsClearAll;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel monstersContainer;
    private javax.swing.JScrollPane monstersScrollContainer;
    // End of variables declaration//GEN-END:variables
//  </editor-fold>
}
