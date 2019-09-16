//Brick-ball game - created by S.P.

package mygame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class MyGame {
    public static MyFrame frame;
    public static void main(String[] args) {
        frame=new MyFrame();
    }
}
class MyFrame extends Frame implements ActionListener {
    public static Thread t;
    public static MyPanel panel;
    Button button;
    Label instruct;
    long difference;
    public static long score,cut;
    public final static Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    public MyFrame() {
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setBackground(Color.gray);
        setSize(d);
        setVisible(true);
        setTitle("My Game");
        Panel panel1=new Panel();
        panel1.setLayout(null);
        panel1.setBounds(0,d.height-100,d.width,100);
        add(panel1);
        button=new Button("Start");
        button.setBounds(panel1.getWidth()/2-50,20,100,20);
        panel1.add(button);
        button.addActionListener(this);
        instruct=new Label("Prove you aren't a robot. Press 'Tab' key while the game starts or resumes.");
        instruct.setBounds(10,getHeight()-180,getWidth(),30);
        instruct.setAlignment(Label.CENTER);
        instruct.setFont(new Font("Monospaced",Font.PLAIN,18));
        add(instruct);
        panel=new MyPanel();
        addKeyListener(panel);
        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(button.getLabel().equals("Start")) {
            button.setLabel("Pause");
            button.setBackground(Color.orange);
            remove(instruct);
            t=new Thread(panel);
            add(panel);
            t.start();
            cut=System.currentTimeMillis();
        }
        else if(button.getLabel().equals("Resume")) {
            button.setLabel("Pause");
            button.setBackground(Color.orange);
            t.resume(); //deprecated method
            panel.resumeAction();
            difference=System.currentTimeMillis()-difference;
            cut=cut+difference;
        }
        else {
            button.setLabel("Resume");
            button.setBackground(Color.green);
            t.suspend(); //deprecated method
            panel.pauseAction();
            difference=System.currentTimeMillis();
        }
    }
}
class MyPanel extends Panel implements Runnable, KeyListener {
    Thread t1;
    static int x,y,dx;
    static char direct;
    public MyPanel() {
        setBounds(0,25,MyFrame.d.width,MyFrame.d.height-125);
        setBackground(new Color(0,191,255)); //Deep Sky Blue
        x=this.getWidth()/2-50;
        y=this.getHeight()-100;
        dx=10;
    }
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(new Color(245,64,64)); //Sunset Orange
        g.fillRect(x, y, 100, 10);
        g.setColor(new Color(76,135,191));
        g.fillOval(Ball.X, Ball.Y, 20, 20);
        g.setColor(Color.red);
        if(Ball.flag==true)
            g.fill3DRect(500,200,100,10,true);
    }
    public void action() {
        //while(true) {
            if(direct=='d' && x+dx+100<=this.getWidth()) {
                x=x+dx;
            }
            else if(direct=='a' && x-dx>=0) {
                x=x-dx;
            }
            /*if(x+160>=this.getWidth()) {
                direct='a';
                continue;
            }
            if(x<=0) {
                direct='d';
                continue;
            }*/
            paint(this.getGraphics());
            /*try {
                Thread.sleep(5);
            }
            catch(Exception e) {
                
            }
        }*/
    }
    public void resumeAction() {
        t1.resume(); //deprecated method
    }
    public void pauseAction() {
        t1.suspend(); //deprecated method
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==37 || e.getKeyChar()=='a') //Left arrow
            direct='a';
        if(e.getKeyCode()==39 || e.getKeyChar()=='d') //Right arrow
            direct='d';
        if(e.getKeyCode()==38 || e.getKeyChar()=='w') //Up arrow
            direct='w';
        if(e.getKeyCode()==40 || e.getKeyChar()=='s') //Down arrow
            direct='s';
        action();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    

    @Override
    public void run() {
        t1=new Ball();
        t1.start();
    }
}
class Ball extends Thread {
        public boolean Xsign,Ysign;
        public static int X,Y,dX;
        public static boolean flag;
        public Ball() {
            X=200;
            Y=100;
            dX=1;
            flag=true;
        }
        public void run() {
            Xsign=true;
            Ysign=true;
            while(true) {
                if(Xsign)
                    X=X+dX;
                else if(!Xsign)
                    X=X-dX;
                if(Ysign)
                    Y=Y+dX;
                else if(!Ysign)
                    Y=Y-dX;
                if(X+20>=MyFrame.panel.getWidth()) {
                    Xsign=false;
                    continue;
                }
                if(X<=0) {
                    Xsign=true;
                    continue;
                }
                if(Y>=MyFrame.panel.getHeight()) {
                    MyFrame.t.suspend(); //deprecated method
                    MyFrame.score=(System.currentTimeMillis()-MyFrame.cut)/1000*10;
                    JOptionPane.showMessageDialog(MyFrame.panel, "Game over! Your score: "+MyFrame.score);
                    System.exit(0);
                }
                if(Y<=0) {
                    Ysign=true;
                    continue;
                }
                if(Math.abs(Y+20-MyPanel.y)<=1) {
                    if(X+10>=MyPanel.x && X+10<=MyPanel.x+100) {
                        Ysign=false;
                        continue;
                    }
                }
                if(Y+20>MyPanel.y && Y+10<MyPanel.y) {
                    if(X+10<MyPanel.x && X+20>MyPanel.x) {
                        if(((X+10-MyPanel.x)*(X+10-MyPanel.x)+(Y+10-MyPanel.y)*(Y+10-MyPanel.y))<=10*10) {
                            Xsign=false;
                            Ysign=false;
                            continue;
                        }
                    }
                    if(X<MyPanel.x+100 && X+10>MyPanel.x+100) {
                        if(((X+10-MyPanel.x-100)*(X+10-MyPanel.x-100)+(Y+10-MyPanel.y)*(Y+10-MyPanel.y))<=10*10) {
                            Xsign=true;
                            Ysign=false;
                            continue;
                        }
                    }
                }
                if(Y+10>=MyPanel.y && Y+10<=MyPanel.y+10) {
                    if(Math.abs(X+10-MyPanel.x)<=1) {
                        Xsign=false;
                        continue;
                    }
                    if(Math.abs(X-MyPanel.x-100)<=1) {
                        Xsign=true;
                        continue;
                    }
                }
                if(Y+10>MyPanel.y+10 && Y<MyPanel.y+10) {
                    if(X+10<MyPanel.x && X+20>MyPanel.x) {
                        if(((X+10-MyPanel.x)*(X+10-MyPanel.x)+(Y+10-MyPanel.y-10)*(Y+10-MyPanel.y-10))<=10*10) {
                            Xsign=false;
                            Ysign=true;
                            continue;
                        }
                    }
                    if(X<MyPanel.x+100 && X+10>MyPanel.x+100) {
                        if(((X+10-MyPanel.x-100)*(X+10-MyPanel.x-100)+(Y+10-MyPanel.y-10)*(Y+10-MyPanel.y-10))<=10*10) {
                            Xsign=true;
                            Ysign=true;
                            continue;
                        }
                    }
                }
                if(Math.abs(Y-MyPanel.y-10)<=1) {
                    if(X+10>=MyPanel.x && X+10<=MyPanel.x+100) {
                        Ysign=true;
                        continue;
                    }
                }
                //Brick conditions
                if(flag==true) {
                if(Math.abs(Y+20-200)<=1) {
                    if(X+10>=500 && X+10<=500+100) {
                        flag=false;
                    }
                }
                if(Y+20>200 && Y+10<200) {
                    if(X+10<500 && X+20>500) {
                        if(((X+10-500)*(X+10-500)+(Y+10-200)*(Y+10-200))<=10*10) {
                            flag=false;
                        }
                    }
                    if(X<500+100 && X+10>500+100) {
                        if(((X+10-500-100)*(X+10-500-100)+(Y+10-200)*(Y+10-200))<=10*10) {
                            flag=false;
                        }
                    }
                }
                if(Y+10>=200 && Y+10<=200+10) {
                    if(Math.abs(X+10-500)<=1) {
                        flag=false;
                    }
                    if(Math.abs(X-500-100)<=1) {
                        flag=false;
                    }
                }
                if(Y+10>200+10 && Y<200+10) {
                    if(X+10<500 && X+20>500) {
                        if(((X+10-500)*(X+10-500)+(Y+10-500-10)*(Y+10-200-10))<=10*10) {
                            flag=false;
                        }
                    }
                    if(X<500+100 && X+10>500+100) {
                        if(((X+10-500-100)*(X+10-500-100)+(Y+10-200-10)*(Y+10-200-10))<=10*10) {
                            flag=false;
                        }
                    }
                }
                if(Math.abs(Y-200-10)<=1) {
                    if(X+10>=500 && X+10<=500+100) {
                        flag=false;
                    }
                }
                }
                //
                MyFrame.panel.paint(MyFrame.panel.getGraphics());
                try {
                    Thread.sleep(5);
                }
                catch(Exception e) {
                
                }
            }
        }
    }
