package newpackage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */

/**
 *
 * @author hunor
 */

import MainDashboard.MainDashboard;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import ro.madarash.kepregeny_project.*;

public class CharacterEditor extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CharacterEditor.class.getName());

    
    // --- NEW: Add fields to store the master lists ---
    private List<ComicCharacter> allCharacters;
    private List<Writer> allWriters;
    private List<Artist> allArtists;
    private List<ComicBook> allComicBooks;
    
    
    /**
     * Creates new form CharacterEditor
     */
    public CharacterEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setupListModels();
        
        loadAvailableListsData();
        
        setLocationRelativeTo(parent);
    }
    
    public CharacterEditor(java.awt.Frame parent, boolean modal,
                           List<ComicCharacter> allCharacters,
                           List<Writer> allWriters,
                           List<Artist> allArtists,
                           List<ComicBook> allComicBooks) {
        
        super(parent, modal);
        
        // --- NEW: Save the passed-in lists ---
        this.allCharacters = allCharacters;
        this.allWriters = allWriters;
        this.allArtists = allArtists;
        this.allComicBooks = allComicBooks;
        
        initComponents();
        
        setupListModels();
        
        // This will now use the real data
        loadAvailableListsData();
        
        setLocationRelativeTo(parent);
    }
    
    private void setupListModels() {
        // Create models
        allPowersModel = new DefaultListModel<>();
        charPowersModel = new DefaultListModel<>();
        allAffiliationsModel = new DefaultListModel<>();
        charAffiliationsModel = new DefaultListModel<>();
        allCreatorsModel = new DefaultListModel<>();
        charCreatorsModel = new DefaultListModel<>();
        allAppearancesModel = new DefaultListModel<>();
        charAppearancesModel = new DefaultListModel<>();
        
        // Set models to lists
        allPowersList.setModel(allPowersModel);
        charPowersList.setModel(charPowersModel);
        allAffiliationsList.setModel(allAffiliationsModel);
        charAffiliationsList.setModel(charAffiliationsModel);
        allCreatorsList.setModel(allCreatorsModel);
        charCreatorsList.setModel(charCreatorsModel);
        allAppearancesList.setModel(allAppearancesModel);
        charAppearancesList.setModel(charAppearancesModel);
        
        // --- NEW: Set the custom renderer ---
        // This tells the lists HOW to display your objects
        ComicObjectRenderer renderer = new ComicObjectRenderer();
        allAffiliationsList.setCellRenderer(renderer);
        charAffiliationsList.setCellRenderer(renderer);
        allCreatorsList.setCellRenderer(renderer);
        charCreatorsList.setCellRenderer(renderer);
        allAppearancesList.setCellRenderer(renderer);
        charAppearancesList.setCellRenderer(renderer);
    }
    
    private void loadAvailableListsData() {
        // --- Dummy Power Data ---
        allPowersModel.clear();
        
        allPowersModel.addElement("Acid Spit");
        allPowersModel.addElement("Agility (Superhuman)");
        allPowersModel.addElement("Animal Communication");
        allPowersModel.addElement("Astral Projection");
        allPowersModel.addElement("Atmokinesis (Weather Control)");
        allPowersModel.addElement("Camouflage/Invisibility");
        allPowersModel.addElement("Cryokinesis (Ice Control)");
        allPowersModel.addElement("Density Control");
        allPowersModel.addElement("Durability (Superhuman)");
        allPowersModel.addElement("Elasticity");
        allPowersModel.addElement("Electrokinesis (Electricity Control)");
        allPowersModel.addElement("Energy Absorption");
        allPowersModel.addElement("Energy Blasts");
        allPowersModel.addElement("Explosion Manipulation");
        allPowersModel.addElement("Flight");
        allPowersModel.addElement("Force Fields");
        allPowersModel.addElement("Gadgets (High-Tech)");
        allPowersModel.addElement("Geokinesis (Earth Control)");
        allPowersModel.addElement("Gravity Manipulation");
        allPowersModel.addElement("Healing Factor (Regeneration)");
        allPowersModel.addElement("Heat Vision");
        allPowersModel.addElement("Hydrokinesis (Water Control)");
        allPowersModel.addElement("Illusion Casting");
        allPowersModel.addElement("Intangibility (Phasing)");
        allPowersModel.addElement("Invisibility");
        allPowersModel.addElement("Invulnerability");
        allPowersModel.addElement("Light Manipulation (Photokinesis)");
        allPowersModel.addElement("Magic/Sorcery");
        allPowersModel.addElement("Magnetism Manipulation");
        allPowersModel.addElement("Marksmanship (Superhuman)");
        allPowersModel.addElement("Mind Control");
        allPowersModel.addElement("Pheromone Control");
        allPowersModel.addElement("Plant Control");
        allPowersModel.addElement("Power Absorption");
        allPowersModel.addElement("Power Mimicry");
        allPowersModel.addElement("Precognition (Future Sight)");
        allPowersModel.addElement("Psychic Blasts");
        allPowersModel.addElement("Pyrokinesis (Fire Control)");
        allPowersModel.addElement("Reality Warping");
        allPowersModel.addElement("Reflexes (Superhuman)");
        allPowersModel.addElement("Shadow Manipulation (Umbrakinesis)");
        allPowersModel.addElement("Shapeshifting");
        allPowersModel.addElement("Shrinking / Size Alteration");
        allPowersModel.addElement("Sonic Scream");
        allPowersModel.addElement("Stamina (Superhuman)");
        allPowersModel.addElement("Super Senses");
        allPowersModel.addElement("Super Speed");
        allPowersModel.addElement("Super Strength");
        allPowersModel.addElement("Technopathy (Machine Control)");
        allPowersModel.addElement("Telekinesis");
        allPowersModel.addElement("Telepathy");
        allPowersModel.addElement("Teleportation");
        allPowersModel.addElement("Time Manipulation");
        allPowersModel.addElement("Toxicity/Poison Generation");
        allPowersModel.addElement("Wall-Crawling");
        allPowersModel.addElement("X-Ray Vision");
        
        
        allAffiliationsModel.clear();
        if (allCharacters != null) {
            for (ComicCharacter character : allCharacters) {
                // TODO: Add a check here if you are in "Edit" mode
                // to prevent a character from being affiliated with itself.
                // if (characterToEdit != null && character.equals(characterToEdit)) {
                //    continue; // Skip
                // }
                allAffiliationsModel.addElement(character);
            }
        }
        
        
        allCreatorsModel.clear();
        if (allWriters != null) {
            for (Writer writer : allWriters) {
                allCreatorsModel.addElement(writer);
            }
        }
        if (allArtists != null) {
            for (Artist artist : allArtists) {
                allCreatorsModel.addElement(artist);
            }
        }
        
        
        allAppearancesModel.clear(); 
        if (allComicBooks != null) {
            for (ComicBook comic : allComicBooks) {
                allAppearancesModel.addElement(comic);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        topInfoPanel = new javax.swing.JPanel();
        realNameLabel = new javax.swing.JLabel();
        realNameField = new javax.swing.JTextField();
        aliasLabel = new javax.swing.JLabel();
        aliasField = new javax.swing.JTextField();
        aligmentLabel = new javax.swing.JLabel();
        alignmentComboBox = new javax.swing.JComboBox<>();
        originLabel = new javax.swing.JLabel();
        originScrollPane = new javax.swing.JScrollPane();
        originTextArea = new javax.swing.JTextArea();
        tabbedPane = new javax.swing.JTabbedPane();
        powersPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        allPowersList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        charPowersList = new javax.swing.JList<>();
        powersButtonPanel = new javax.swing.JPanel();
        addPowerButton = new javax.swing.JButton();
        removePowerButton = new javax.swing.JButton();
        affiliationsPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        allAffiliationsList = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        charAffiliationsList = new javax.swing.JList<>();
        affiliationsButtonPanel = new javax.swing.JPanel();
        addAffiliationButton = new javax.swing.JButton();
        removeAffiliationButton = new javax.swing.JButton();
        creatorsPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        allCreatorsList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        charCreatorsList = new javax.swing.JList<>();
        creatorsButtonPanel = new javax.swing.JPanel();
        addCreatorButton = new javax.swing.JButton();
        removeCreatorButton = new javax.swing.JButton();
        appearancesPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        allAppearancesList = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        charAppearancesList = new javax.swing.JList<>();
        appearancesButtonPanel = new javax.swing.JPanel();
        addAppearanceButton = new javax.swing.JButton();
        removeAppearanceButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Character Editor");

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(saveButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        mainPanel.setLayout(new java.awt.BorderLayout());

        topInfoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topInfoPanel.setLayout(new java.awt.GridBagLayout());

        realNameLabel.setText("Real Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(realNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(realNameField, gridBagConstraints);

        aliasLabel.setText("Alias:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(aliasLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(aliasField, gridBagConstraints);

        aligmentLabel.setText("Alignment:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(aligmentLabel, gridBagConstraints);

        alignmentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SUPERHERO", "VILLAIN", "CIVILIAN" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(alignmentComboBox, gridBagConstraints);

        originLabel.setText("Origin Story:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(originLabel, gridBagConstraints);

        originTextArea.setColumns(20);
        originTextArea.setLineWrap(true);
        originTextArea.setRows(5);
        originTextArea.setWrapStyleWord(true);
        originScrollPane.setViewportView(originTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(originScrollPane, gridBagConstraints);

        mainPanel.add(topInfoPanel, java.awt.BorderLayout.NORTH);

        powersPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 100));

        allPowersList.setBorder(javax.swing.BorderFactory.createTitledBorder("Available Powers"));
        jScrollPane1.setViewportView(allPowersList);

        powersPanel.add(jScrollPane1, java.awt.BorderLayout.WEST);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(250, 100));

        charPowersList.setBorder(javax.swing.BorderFactory.createTitledBorder("Character Powers"));
        jScrollPane2.setViewportView(charPowersList);

        powersPanel.add(jScrollPane2, java.awt.BorderLayout.EAST);

        addPowerButton.setText("->");
        addPowerButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addPowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPowerButtonActionPerformed(evt);
            }
        });

        removePowerButton.setText("<-");
        removePowerButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removePowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePowerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout powersButtonPanelLayout = new javax.swing.GroupLayout(powersButtonPanel);
        powersButtonPanel.setLayout(powersButtonPanelLayout);
        powersButtonPanelLayout.setHorizontalGroup(
            powersButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(powersButtonPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(powersButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removePowerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addPowerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        powersButtonPanelLayout.setVerticalGroup(
            powersButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, powersButtonPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(addPowerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(removePowerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        powersPanel.add(powersButtonPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Powers", powersPanel);

        affiliationsPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setPreferredSize(new java.awt.Dimension(250, 100));

        allAffiliationsList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Characters"));
        jScrollPane3.setViewportView(allAffiliationsList);

        affiliationsPanel.add(jScrollPane3, java.awt.BorderLayout.WEST);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(250, 100));

        charAffiliationsList.setBorder(javax.swing.BorderFactory.createTitledBorder("Affiliated Characters"));
        jScrollPane4.setViewportView(charAffiliationsList);

        affiliationsPanel.add(jScrollPane4, java.awt.BorderLayout.EAST);

        addAffiliationButton.setText("->");
        addAffiliationButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addAffiliationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAffiliationButtonActionPerformed(evt);
            }
        });

        removeAffiliationButton.setText("<-");
        removeAffiliationButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeAffiliationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAffiliationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout affiliationsButtonPanelLayout = new javax.swing.GroupLayout(affiliationsButtonPanel);
        affiliationsButtonPanel.setLayout(affiliationsButtonPanelLayout);
        affiliationsButtonPanelLayout.setHorizontalGroup(
            affiliationsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(affiliationsButtonPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(affiliationsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removeAffiliationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addAffiliationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        affiliationsButtonPanelLayout.setVerticalGroup(
            affiliationsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, affiliationsButtonPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(addAffiliationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(removeAffiliationButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        affiliationsPanel.add(affiliationsButtonPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Affiliations", affiliationsPanel);

        creatorsPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane5.setPreferredSize(new java.awt.Dimension(250, 100));

        allCreatorsList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Writers / Artists"));
        jScrollPane5.setViewportView(allCreatorsList);

        creatorsPanel.add(jScrollPane5, java.awt.BorderLayout.LINE_START);

        jScrollPane6.setPreferredSize(new java.awt.Dimension(250, 100));

        charCreatorsList.setBorder(javax.swing.BorderFactory.createTitledBorder("Creators"));
        jScrollPane6.setViewportView(charCreatorsList);

        creatorsPanel.add(jScrollPane6, java.awt.BorderLayout.LINE_END);

        addCreatorButton.setText("->");
        addCreatorButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addCreatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCreatorButtonActionPerformed(evt);
            }
        });

        removeCreatorButton.setText("<-");
        removeCreatorButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeCreatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCreatorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout creatorsButtonPanelLayout = new javax.swing.GroupLayout(creatorsButtonPanel);
        creatorsButtonPanel.setLayout(creatorsButtonPanelLayout);
        creatorsButtonPanelLayout.setHorizontalGroup(
            creatorsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(creatorsButtonPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(creatorsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removeCreatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCreatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        creatorsButtonPanelLayout.setVerticalGroup(
            creatorsButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, creatorsButtonPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(addCreatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(removeCreatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        creatorsPanel.add(creatorsButtonPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Creators", creatorsPanel);

        appearancesPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane7.setPreferredSize(new java.awt.Dimension(250, 100));

        allAppearancesList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Comic Books"));
        jScrollPane7.setViewportView(allAppearancesList);

        appearancesPanel.add(jScrollPane7, java.awt.BorderLayout.LINE_START);

        jScrollPane8.setPreferredSize(new java.awt.Dimension(250, 100));

        charAppearancesList.setBorder(javax.swing.BorderFactory.createTitledBorder("Appearances"));
        jScrollPane8.setViewportView(charAppearancesList);

        appearancesPanel.add(jScrollPane8, java.awt.BorderLayout.LINE_END);

        addAppearanceButton.setText("->");
        addAppearanceButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addAppearanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAppearanceButtonActionPerformed(evt);
            }
        });

        removeAppearanceButton.setText("<-");
        removeAppearanceButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeAppearanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAppearanceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout appearancesButtonPanelLayout = new javax.swing.GroupLayout(appearancesButtonPanel);
        appearancesButtonPanel.setLayout(appearancesButtonPanelLayout);
        appearancesButtonPanelLayout.setHorizontalGroup(
            appearancesButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appearancesButtonPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(appearancesButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removeAppearanceButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addAppearanceButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        appearancesButtonPanelLayout.setVerticalGroup(
            appearancesButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, appearancesButtonPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(addAppearanceButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(removeAppearanceButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        appearancesPanel.add(appearancesButtonPanel, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab("Appearances", appearancesPanel);

        mainPanel.add(tabbedPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // 1. Get data from the top form
        String realName = realNameField.getText();
        String alias = aliasField.getText();
        String alignmentString = (String) alignmentComboBox.getSelectedItem();
        String origin = originTextArea.getText();

        // 2. Validate data
        if (realName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Real Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (alignmentString.equals("SUPERHERO") || alignmentString.equals("VILLAIN")) {
            if (alias.isBlank()) {
                JOptionPane.showMessageDialog(this, "Alias cannot be empty for a Superhero or Villain.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 3. Create the correct Character subclass
        ComicCharacter newCharacter = null; 
        
        switch (alignmentString) {
            case "SUPERHERO":
                newCharacter = new Superhero(realName, origin, alias);
                System.out.println("Saving new Superhero: " + alias);
                break;
            case "VILLAIN":
                newCharacter = new Villain(realName, origin, alias);
                System.out.println("Saving new Villain: " + alias);
                break;
            case "CIVILIAN":
            default:
                newCharacter = new Civilian(realName, origin);
                System.out.println("Saving new Civilian: " + realName);
                break;
        }

        // 4. --- NEW: Save the list data using the 'newCharacter' object ---
        
        // -- Save Powers --
        // Powers are only on Superhero/Villain, so we must cast
        if (newCharacter instanceof Superhero) {
            for (int i = 0; i < charPowersModel.getSize(); i++) {
                String powerName = charPowersModel.getElementAt(i);
                ((Superhero) newCharacter).addPower(powerName);
            }
        } else if (newCharacter instanceof Villain) {
            for (int i = 0; i < charPowersModel.getSize(); i++) {
                String powerName = charPowersModel.getElementAt(i);
                ((Villain) newCharacter).addPower(powerName);
            }
        }
        
        // -- Save Affiliations --
        for (int i = 0; i < charAffiliationsModel.getSize(); i++) {
            ComicCharacter affiliatedChar = charAffiliationsModel.getElementAt(i);
            newCharacter.addAffiliation(affiliatedChar, "Ally"); // Hardcoded "Ally"
        }
        
        // -- Save Creators --
        for (int i = 0; i < charCreatorsModel.getSize(); i++) {
            Object creator = charCreatorsModel.getElementAt(i);
            if (creator instanceof Writer) {
                newCharacter.addCreator((Writer) creator, "Co-creator"); // Hardcoded "Co-creator"
            } else if (creator instanceof Artist) {
                newCharacter.addCreator((Artist) creator, "Co-creator"); // Hardcoded "Co-creator"
            }
        }
        
        // -- Save Appearances --
        for (int i = 0; i < charAppearancesModel.getSize(); i++) {
            ComicBook comic = charAppearancesModel.getElementAt(i);
            newCharacter.addAppearance(comic);
        }
        
        // 5. TODO: Add the 'newCharacter' to your main data list in MainDashboard
        // ((MainDashboard) getParent()).addCharacterToList(newCharacter);
        ((MainDashboard) getParent()).addCharacter(newCharacter);
        
        // 6. Close the dialog
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private <T> void moveItems(JList<T> sourceList, JList<T> destList) {
        // Get the models for both lists
        DefaultListModel<T> sourceModel = (DefaultListModel<T>) sourceList.getModel();
        DefaultListModel<T> destModel = (DefaultListModel<T>) destList.getModel();
        
        // Get all selected items from the source list
        java.util.List<T> selectedItems = sourceList.getSelectedValuesList();
        
        if (selectedItems.isEmpty()) {
            return; // Nothing selected, do nothing
        }

        // Add items to the destination model
        for (T item : selectedItems) {
            destModel.addElement(item);
        }
        
        // Remove items from the source model (in reverse to avoid index issues)
        for (int i = selectedItems.size() - 1; i >= 0; i--) {
            sourceModel.removeElement(selectedItems.get(i));
        }
    }
    
    private void removeAppearanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAppearanceButtonActionPerformed
        // TODO add your handling code here:
        moveItems(charAppearancesList, allAppearancesList);
    }//GEN-LAST:event_removeAppearanceButtonActionPerformed

    private void addAppearanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAppearanceButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allAppearancesList, charAppearancesList);
    }//GEN-LAST:event_addAppearanceButtonActionPerformed

    private void removeCreatorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCreatorButtonActionPerformed
        // TODO add your handling code here:
        moveItems(charCreatorsList, allCreatorsList);
    }//GEN-LAST:event_removeCreatorButtonActionPerformed

    private void addCreatorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCreatorButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allCreatorsList, charCreatorsList);
    }//GEN-LAST:event_addCreatorButtonActionPerformed

    private void removeAffiliationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAffiliationButtonActionPerformed
        // TODO add your handling code here:
        moveItems(charAffiliationsList, allAffiliationsList);
    }//GEN-LAST:event_removeAffiliationButtonActionPerformed

    private void addAffiliationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAffiliationButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allAffiliationsList, charAffiliationsList);
    }//GEN-LAST:event_addAffiliationButtonActionPerformed

    private void removePowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePowerButtonActionPerformed
        // TODO add your handling code here:
        moveItems(charPowersList, allPowersList);
    }//GEN-LAST:event_removePowerButtonActionPerformed

    private void addPowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPowerButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allPowersList, charPowersList);
    }//GEN-LAST:event_addPowerButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CharacterEditor dialog = new CharacterEditor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    class ComicObjectRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            // Get the default component (a JLabel)
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            // Check the type of the object and set the text accordingly
            if (value instanceof Writer) {
                setText(((Writer) value).getName());
            } else if (value instanceof Artist) {
                setText(((Artist) value).getName());
            } else if (value instanceof ComicCharacter) {
                setText(((ComicCharacter) value).getDisplayName());
            } else if (value instanceof ComicBook) {
                setText(((ComicBook) value).getTitle());
            }
            // (It already handles String, so 'powers' list will work fine)
            
            return c;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAffiliationButton;
    private javax.swing.JButton addAppearanceButton;
    private javax.swing.JButton addCreatorButton;
    private javax.swing.JButton addPowerButton;
    private javax.swing.JPanel affiliationsButtonPanel;
    private javax.swing.JPanel affiliationsPanel;
    private javax.swing.JTextField aliasField;
    private javax.swing.JLabel aliasLabel;
    private javax.swing.JLabel aligmentLabel;
    private javax.swing.JComboBox<String> alignmentComboBox;
    private javax.swing.JList<ComicCharacter> allAffiliationsList;
    private javax.swing.JList<ComicBook> allAppearancesList;
    private javax.swing.JList<Object> allCreatorsList;
    private javax.swing.JList<String> allPowersList;
    private javax.swing.JPanel appearancesButtonPanel;
    private javax.swing.JPanel appearancesPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JList<ComicCharacter> charAffiliationsList;
    private javax.swing.JList<ComicBook> charAppearancesList;
    private javax.swing.JList<Object> charCreatorsList;
    private javax.swing.JList<String> charPowersList;
    private javax.swing.JPanel creatorsButtonPanel;
    private javax.swing.JPanel creatorsPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel originLabel;
    private javax.swing.JScrollPane originScrollPane;
    private javax.swing.JTextArea originTextArea;
    private javax.swing.JPanel powersButtonPanel;
    private javax.swing.JPanel powersPanel;
    private javax.swing.JTextField realNameField;
    private javax.swing.JLabel realNameLabel;
    private javax.swing.JButton removeAffiliationButton;
    private javax.swing.JButton removeAppearanceButton;
    private javax.swing.JButton removeCreatorButton;
    private javax.swing.JButton removePowerButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel topInfoPanel;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel<String> allPowersModel;
    private DefaultListModel<String> charPowersModel;
    private DefaultListModel<ComicCharacter> allAffiliationsModel;
    private DefaultListModel<ComicCharacter> charAffiliationsModel;
    private DefaultListModel<Object> allCreatorsModel;
    private DefaultListModel<Object> charCreatorsModel;
    private DefaultListModel<ComicBook> allAppearancesModel;
    private DefaultListModel<ComicBook> charAppearancesModel;
}
