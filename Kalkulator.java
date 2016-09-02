import Grunt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by Feranodor on 2016-08-25.
 * PN-81/B-03020
 */
public class Kalkulator {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            CalcFrame mainFrame = new CalcFrame();
            mainFrame.setTitle("Kalkulator parametrów geotechnicznych");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });
    }
}

class CalcFrame extends JFrame {

    private NazwaNiespoistegoGruntu nazwaNiespoistegoGruntu;
    private NazwaSpoistegoGruntu nazwaSpoistegoGruntu;
    private StanWilg stanWilg;
    private GruntNieSpoisty gruntNieSpoisty;
    private int value;
    private TypSpoistego typSpoistego;
    private GruntSpoisty gruntSpoisty;

    private JPanel radio;
    private ButtonGroup soilChoser;
    private SomeString string2;
    private JComboBox<NazwaNiespoistegoGruntu> soilName1;
    private JComboBox<NazwaSpoistegoGruntu> soilName2;
    private JPanel soilNameContainer;
    private JComboBox<TypSpoistego> cohesiveSoilType;
    private JComboBox<StanWilg> humidity;
    private JPanel soilInputsContainer;
    private JComboBox<String> cohesiveInputType;
    private JComboBox<String> nonCohesiveInputType;
    private JPanel inputTypeContainer;
    private SomeString string4 = new SomeString("Stopień zagęszczenia:");
    private JPanel sliderContainer;
    private ChangeListener slidersListener;
    private JSlider slider1;
    private Color colorOfSetters;
    private boolean whatSoil = true;
    private String init = "";
    private Color changed;

    private SomeString display3a = new SomeString("Stopień zagęszczenia gruntu, Id [-]:");
    private SomeString display3b = new SomeString(init);
    private SomeString display4a = new SomeString("Kąt tarcia wewnętrznego, ø [º]:");
    private SomeString display4b = new SomeString(init);
    private SomeString display5a = new SomeString("Gęstość właściwa, ps [t/m3]:");
    private SomeString display5b = new SomeString(init);
    private SomeString display6a = new SomeString("Gęstość objętościowa, p [t/m3]:");
    private SomeString display6b = new SomeString(init);
    private SomeString display7a = new SomeString("Wilgotność naturalna, Wn [%]:");
    private SomeString display7b = new SomeString(init);
    private SomeString display8a = new SomeString("Moduł pierwotnego odkształcenia gruntu, Eo [kPa]:");
    private SomeString display8b = new SomeString(init);
    private SomeString display9a = new SomeString("Edometryczny moduł ściśliwości pierwotnej, Mo [kPa]:");
    private SomeString display9b = new SomeString(init);
    private SomeString display10a = new SomeString("Edometryczny moduł ściśliwości wtórnej, M [kPa]:");
    private SomeString display10b = new SomeString(init);
    private SomeString display11a = new SomeString("");
    private SomeString display11b = new SomeString(init);


    private static final int DEFAULT_WIDTH = 460;
    private static final int DEFAULT_HEIGHT = 500;

    private static final TypGruntu DEFAULT_SOIL_TYPE = TypGruntu.NIESPOISTY;

    CalcFrame() {
        JPanel allSetters = new JPanel();
        allSetters.setLayout(new GridLayout(5, 2));
        JPanel display = new JPanel();
        JPanel display2 = new JPanel();
        display.setLayout(new GridLayout(9, 1));
        display2.setLayout(new GridLayout(9, 1));
        Dimension sizeOfComboBox = new Dimension(205, 25);

        //radio spoisty niespoisty
        radio = new JPanel();
        soilChoser = new ButtonGroup();
        addRadioButton("Spoisty", TypGruntu.SPOISTY);
        addRadioButton("Nie spoisty", TypGruntu.NIESPOISTY);
        allSetters.add(radio);

        //My name
        SomeString companyName = new SomeString("Autor:   Piotr Kraska");
        allSetters.add(companyName);

        //string1
        SomeString string1 = new SomeString("Nazwa gruntu:");
        allSetters.add(string1);

        //nazwa gruntu
        soilName1 = new JComboBox<>();
        soilName1.setPreferredSize(sizeOfComboBox);
        for (NazwaNiespoistegoGruntu x : NazwaNiespoistegoGruntu.values()) {
            soilName1.addItem(x);
        }
        soilName1.addActionListener(e -> calculations());
        soilName2 = new JComboBox<>();
        soilName2.setPreferredSize(sizeOfComboBox);
        for (NazwaSpoistegoGruntu x : NazwaSpoistegoGruntu.values()) {
            soilName2.addItem(x);
        }
        soilName2.addActionListener(e -> calculations());
        soilNameContainer = new JPanel();
        soilNameContainer.add(soilName1);
        allSetters.add(soilNameContainer);

        //string2
        string2 = new SomeString("Wilgotność gruntu:");
        allSetters.add(string2);

        //typ i wilgotnosc
        cohesiveSoilType = new JComboBox<>();
        cohesiveSoilType.setPreferredSize(sizeOfComboBox);
        for (TypSpoistego x : TypSpoistego.values()) {
            cohesiveSoilType.addItem(x);
        }
        cohesiveSoilType.addActionListener(e -> calculations());
        humidity = new JComboBox<>();
        humidity.setPreferredSize(sizeOfComboBox);
        for (StanWilg x : StanWilg.values()) {
            humidity.addItem(x);
        }
        humidity.addActionListener(e -> calculations());
        soilInputsContainer = new JPanel();
        soilInputsContainer.add(humidity);
        allSetters.add(soilInputsContainer);

        //string3
        SomeString string3 = new SomeString("Parametr wiodący:");
        allSetters.add(string3);

        //parametr wiodacy
        cohesiveInputType = new JComboBox<>();
        cohesiveInputType.setPreferredSize(sizeOfComboBox);
        cohesiveInputType.addItem("stopień plastyczności");
        cohesiveInputType.addItem("kąt tarcia wewnętrznego");
        cohesiveInputType.addItem("spójność");
        cohesiveInputType.addActionListener(e -> {
            string4.setStr(cohesiveInputType.getItemAt(cohesiveInputType.getSelectedIndex()) + ":");
            calculations();
        });
        nonCohesiveInputType = new JComboBox<>();
        nonCohesiveInputType.setPreferredSize(sizeOfComboBox);
        nonCohesiveInputType.addItem("stopień zagęszczenia");
        nonCohesiveInputType.addItem("kąt tarcia wewnętrznego");
        nonCohesiveInputType.addActionListener(e -> {
            string4.setStr(nonCohesiveInputType.getItemAt(nonCohesiveInputType.getSelectedIndex()) + ":");
            calculations();
        });
        inputTypeContainer = new JPanel();
        inputTypeContainer.add(nonCohesiveInputType);
        allSetters.add(inputTypeContainer);

        //string4 inicjalizacja nastapila wyzej
        allSetters.add(string4);

        //regulacja parametru wiodacego
        slidersListener = e -> calculations();

        slider1 = new JSlider();
        slider1.addChangeListener(slidersListener);

        sliderContainer = new JPanel();
        sliderContainer.add(slider1);
        allSetters.add(sliderContainer);


/////////////////////////////////////////////////////////
        display.add(display3a);
        display2.add(display3b);
        display.add(display4a);
        display2.add(display4b);
        display.add(display5a);
        display2.add(display5b);
        display.add(display6a);
        display2.add(display6b);
        display.add(display7a);
        display2.add(display7b);
        display.add(display8a);
        display2.add(display8b);
        display.add(display9a);
        display2.add(display9b);
        display.add(display10a);
        display2.add(display10b);
        display.add(display11a);
        display2.add(display11b);

        ////////////////////////////////////////////////////////////////

        //Kolory
        colorOfSetters = new Color(211, 224, 255);
        allSetters.setBackground(colorOfSetters);
        radio.setBackground(colorOfSetters);
        soilNameContainer.setBackground(colorOfSetters);
        soilInputsContainer.setBackground(colorOfSetters);
        inputTypeContainer.setBackground(colorOfSetters);
        sliderContainer.setBackground(colorOfSetters);
        slider1.setBackground(colorOfSetters);
        display.setBackground(new Color(184, 203, 255));
        display2.setBackground(new Color(224, 235, 255));
        changed = new Color(170, 0, 255);

        //ikona
        URL iconURL = getClass().getResource("icon.gif");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationByPlatform(true);
        setResizable(false);
        add(allSetters, BorderLayout.NORTH);
        add(display, BorderLayout.CENTER);
        add(display2, BorderLayout.EAST);
        calculations();
    }

    private void addRadioButton(String name, final TypGruntu s) {
        boolean selected = s == DEFAULT_SOIL_TYPE;
        JRadioButton button = new JRadioButton(name, selected);
        button.setBackground(colorOfSetters);
        soilChoser.add(button);
        radio.add(button);

        ActionListener listener = e -> {
            //ustawienie typu gruntu spoisty czy nie spoisty i zmiana interfejsu
            if (s == TypGruntu.SPOISTY) {
                string2.setStr("Typ:");
                whatSoil = false;
                soilNameContainer.removeAll();
                soilNameContainer.revalidate();
                soilNameContainer.add(soilName2);
                soilInputsContainer.removeAll();
                soilInputsContainer.revalidate();
                soilInputsContainer.add(cohesiveSoilType);
                inputTypeContainer.removeAll();
                inputTypeContainer.revalidate();
                inputTypeContainer.add(cohesiveInputType);
                string4.setStr(cohesiveInputType.getItemAt(cohesiveInputType.getSelectedIndex()) + ":");
                display3a.setStr("Stopień plastyczności gruntu, IL [-]:");
                display11a.setStr("Spójność gruntu, Cu [kPa]:");
                calculations();
            } else {
                string2.setStr("Wilgotność gruntu:");
                whatSoil = true;
                soilNameContainer.removeAll();
                soilNameContainer.revalidate();
                soilNameContainer.add(soilName1);
                soilInputsContainer.removeAll();
                soilInputsContainer.revalidate();
                soilInputsContainer.add(humidity);
                inputTypeContainer.removeAll();
                inputTypeContainer.revalidate();
                inputTypeContainer.add(nonCohesiveInputType);
                string4.setStr(nonCohesiveInputType.getItemAt(nonCohesiveInputType.getSelectedIndex()) + ":");
                display3a.setStr("Stopień zagęszczenia gruntu, Id [-]:");
                display11a.setStr("");
                calculations();
            }
        };
        button.addActionListener(listener);
    }

    private void calculations() {
        value = slider1.getValue();

        if (whatSoil)//nie spoisty
        {
            nazwaNiespoistegoGruntu = soilName1.getItemAt(soilName1.getSelectedIndex());
            stanWilg = humidity.getItemAt(humidity.getSelectedIndex());

            if ("stopień zagęszczenia".equals(nonCohesiveInputType.getItemAt(nonCohesiveInputType.getSelectedIndex()))) {
                gruntNieSpoisty = new GruntNieSpoisty(nazwaNiespoistegoGruntu, stanWilg, (0.2f + 0.8f * ((float) value) / 100.0f));
                display3b.setForeground(changed);
                display11b.setForeground(Color.BLACK);
                display4b.setForeground(Color.BLACK);
            } else//kat tarcia wew
            {
                display3b.setForeground(Color.BLACK);
                display11b.setForeground(Color.BLACK);
                display4b.setForeground(changed);
                if (nazwaNiespoistegoGruntu == NazwaNiespoistegoGruntu.ZWIR || nazwaNiespoistegoGruntu == NazwaNiespoistegoGruntu.POSPOLKA) {
                    gruntNieSpoisty = new GruntNieSpoisty((36.5f + 5.5f * ((float) value) / 100f), nazwaNiespoistegoGruntu, stanWilg);
                } else if (nazwaNiespoistegoGruntu == NazwaNiespoistegoGruntu.PIASEK_GRUBY || nazwaNiespoistegoGruntu == NazwaNiespoistegoGruntu.PIASEK_SREDNI) {
                    gruntNieSpoisty = new GruntNieSpoisty((31f + 5f * ((float) value) / 100f), nazwaNiespoistegoGruntu, stanWilg);
                } else {
                    gruntNieSpoisty = new GruntNieSpoisty((29f + 4f * ((float) value) / 100f), nazwaNiespoistegoGruntu, stanWilg);
                }
            }
            display3b.setStr("" + gruntNieSpoisty.getStZagGruntu());
            display4b.setStr("" + gruntNieSpoisty.getKatTarciaWew());
            display5b.setStr("" + gruntNieSpoisty.getGestWlasc());
            display6b.setStr("" + gruntNieSpoisty.getGestObj());
            display7b.setStr("" + gruntNieSpoisty.getWilgNat());
            display8b.setStr("" + gruntNieSpoisty.getModulEo());
            display9b.setStr("" + gruntNieSpoisty.getModulMo());
            display10b.setStr("" + gruntNieSpoisty.getModulM());
            display11b.setStr("");
        } else//spoisty
        {
            nazwaSpoistegoGruntu = soilName2.getItemAt(soilName2.getSelectedIndex());
            typSpoistego = cohesiveSoilType.getItemAt(cohesiveSoilType.getSelectedIndex());

            if ("stopień plastyczności".equals(cohesiveInputType.getItemAt(cohesiveInputType.getSelectedIndex()))) {
                display3b.setForeground(changed);
                display11b.setForeground(Color.BLACK);
                display4b.setForeground(Color.BLACK);
                gruntSpoisty = new GruntSpoisty(nazwaSpoistegoGruntu,typSpoistego,(0.75f*((float)value)/100f));
            } else if ("spójność".equals(cohesiveInputType.getItemAt(cohesiveInputType.getSelectedIndex()))) {
                display3b.setForeground(Color.BLACK);
                display11b.setForeground(changed);
                display4b.setForeground(Color.BLACK);
                if (typSpoistego==TypSpoistego.A){
                    gruntSpoisty =new GruntSpoisty(nazwaSpoistegoGruntu,(20f+31f*((float)value)/100f),typSpoistego);
                }else if (typSpoistego==TypSpoistego.B){
                    gruntSpoisty =new GruntSpoisty(nazwaSpoistegoGruntu,(15f+25f*((float)value)/100f),typSpoistego);
                }else if (typSpoistego==TypSpoistego.C){
                    gruntSpoisty =new GruntSpoisty(nazwaSpoistegoGruntu,(5f+26f*((float)value)/100f),typSpoistego);
                }else{
                    gruntSpoisty =new GruntSpoisty(nazwaSpoistegoGruntu,(25f+35f*((float)value)/100f),typSpoistego);
                }
            } else//kat tarcia wew
            {
                display3b.setForeground(Color.BLACK);
                display11b.setForeground(Color.BLACK);
                display4b.setForeground(changed);
                if (typSpoistego==TypSpoistego.A){
                    gruntSpoisty=new GruntSpoisty((12f+13f*((float)value)/100f),nazwaSpoistegoGruntu,typSpoistego);
                }else if (typSpoistego==TypSpoistego.B){
                    gruntSpoisty=new GruntSpoisty((8f+13f*((float)value)/100f),nazwaSpoistegoGruntu,typSpoistego);
                }else if (typSpoistego==TypSpoistego.C){
                    gruntSpoisty=new GruntSpoisty((6f+12f*((float)value)/100f),nazwaSpoistegoGruntu,typSpoistego);
                }else{
                    gruntSpoisty=new GruntSpoisty((3f+10f*((float)value)/100f),nazwaSpoistegoGruntu,typSpoistego);
                }
            }
            display3b.setStr("" + gruntSpoisty.getStPlastGruntu());
            display4b.setStr("" + gruntSpoisty.getKatTarciaWew());
            display5b.setStr("" + gruntSpoisty.getGestWlasc());
            display6b.setStr("" + gruntSpoisty.getGestObj());
            display7b.setStr("" + gruntSpoisty.getWilgNat());
            display8b.setStr("" + gruntSpoisty.getModulEo());
            display9b.setStr("" + gruntSpoisty.getModulMo());
            display10b.setStr("" +gruntSpoisty.getModulM());
            display11b.setStr(""+gruntSpoisty.getSpojnoscGruntu());
        }
        repaint();
    }
}

class SomeString extends JComponent {
    private static final int MESSAGE_X = 30;
    private static final int MESSAGE_Y = 22;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 30;
    private String str;

    void setStr(String s) {
        str = s;
    }


    SomeString(String string) {
        str = string;
    }

    public void paintComponent(Graphics g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.drawString(str, MESSAGE_X, MESSAGE_Y);
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}

