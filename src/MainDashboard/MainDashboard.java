/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package MainDashboard;

/**
 *
 * @author hunor
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import newpackage.EditionEditor;
import newpackage.CharacterEditor;
import newpackage.ComicBookEditor;
import newpackage.CreatorEditor;
import newpackage.PublisherEditor;
import newpackage.DataHelper;
import newpackage.DataHelper.ComicDataContainer;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser; // For picking the file
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import ro.madarash.kepregeny_project.*;


public class MainDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainDashboard.class.getName());

    
    private List<ComicBook> allComicBooks = new ArrayList<>();
    private List<ComicCharacter> allCharacters = new ArrayList<>();
    private List<Writer> allWriters = new ArrayList<>();
    private List<Artist> allArtists = new ArrayList<>();
    private List<Publisher> allPublishers = new ArrayList<>();
    
    private List<Object> allItemsInTable = new ArrayList<>();
    
    // --- NEW: Table Models for each tab ---
    // We will store a reference to each table's model
    private DefaultTableModel comicBooksModel;
    private DefaultTableModel charactersModel;
    private DefaultTableModel writersModel;
    private DefaultTableModel artistsModel;
    private DefaultTableModel publishersModel;
    /**
     * Creates new form MainDashboard
     */
    
    public MainDashboard() {
        initComponents();
        
        this.setSize(1050, 700);
        
        // Set the window to appear in the center of the screen
        setLocationRelativeTo(null);
        
        setupDataAndTable();
        
        // --- ADD THIS CODE TO SAVE ON EXIT ---
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Call our new save method
                saveDataOnExit();
            }
        });
    }
    
    
    /**
     * Initializes the application state, loads initial data,
     * and sets up listeners.
     */
    private void setupDataAndTable() {
        // Set the window to appear in the center of the screen
        setLocationRelativeTo(null);
        
        // --- NEW: Get the models from the 5 tables you created ---
        comicBooksModel = (DefaultTableModel) comicBooksTable.getModel();
        charactersModel = (DefaultTableModel) charactersTable.getModel();
        writersModel = (DefaultTableModel) writersTable.getModel();
        artistsModel = (DefaultTableModel) artistsTable.getModel();
        publishersModel = (DefaultTableModel) publishersTable.getModel();

        // --- NEW: Add a selection listener to EVERY table ---
        // All tables will call the SAME method when their selection changes.
        comicBooksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelectionChanged();
        });
        charactersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelectionChanged();
        });
        writersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelectionChanged();
        });
        artistsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelectionChanged();
        });
        publishersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onTableSelectionChanged();
        });
        
        // --- NEW: Add a listener to the tab pane itself ---
        // This helps us clear selections when the user changes tabs
        mainTabbedPane.addChangeListener(e -> {
            clearAllTableSelections();
            onTableSelectionChanged(); // Update buttons for the new tab
        });

        // Load some initial data to start
        loadInitialData(); // This will now call refreshAllTables()

        // Set the initial state of the buttons
        onTableSelectionChanged();
    }
    
    private void clearAllTableSelections() {
        comicBooksTable.clearSelection();
        charactersTable.clearSelection();
        writersTable.clearSelection();
        artistsTable.clearSelection();
        publishersTable.clearSelection();
    }
    
    /**
     * MODIFIED: This method now loads data from a hardcoded JSON file
     * automatically on startup.
     */
    private void loadInitialData() {
        
        // --- Call the DataHelper ---
        // It will look for "KepregenyAdatok.json" in your project's root folder
        logger.info("Loading data from KepregenyAdatok.json...");
        ComicDataContainer data = DataHelper.loadDataFromJSON(); // No path needed

        if (data != null) {
            // Assign the loaded lists to this dashboard's master lists
            this.allPublishers = data.publishers;
            this.allWriters = data.writers;
            this.allArtists = data.artists;
            this.allCharacters = data.characters;
            this.allComicBooks = data.comicBooks;
            
            logger.info("Data loaded successfully!");
            
            // This call is in setupDataAndTable, but we call it
            // again here to be safe, since loading is complete.
            refreshAllTables();
            
        } else {
            logger.severe("Failed to load data from JSON.");
            // This is the error message you saw, which will appear if the
            // file is missing or has an error.
            JOptionPane.showMessageDialog(this, 
                    "Could not load data from 'KepregenyAdatok.json'.\n" +
                    "Please make sure the file exists in the project's root folder.", 
                    "Load Error", 
                    JOptionPane.ERROR_MESSAGE);
            // The app will continue with empty lists
        }
    }
    
    /**
     * Gathers all master data lists and tells DataHelper to save them.
     */
    private void saveDataOnExit() {
        logger.info("Saving data to JSON file...");
        
        // 1. Create a container to hold all our lists
        ComicDataContainer dataToSave = new ComicDataContainer();
        
        // 2. Populate the container with our master lists
        dataToSave.publishers = this.allPublishers;
        dataToSave.writers = this.allWriters;
        dataToSave.artists = this.allArtists;
        dataToSave.characters = this.allCharacters;
        dataToSave.comicBooks = this.allComicBooks;
        
        // 3. Call the static save method in DataHelper
        DataHelper.saveDataToJSON(dataToSave);
        
        logger.info("Data saved. Exiting.");
    }
    
    /**
     * This method is called whenever the table selection changes.
     * It enables/disables buttons based on the selected item.
     */
    private void onTableSelectionChanged() {
        Object selectedObject = getSelectedObject();
        
        // Enable/disable the Edit and Delete buttons
        editButton.setEnabled(selectedObject != null);
        deleteButton.setEnabled(selectedObject != null);
        
        // ONLY enable the "Add Edition" button if a ComicBook is selected
        addEditionButton.setEnabled(selectedObject instanceof ComicBook);
        
        // --- NEW Details Panel Logic ---
        if (selectedObject == null) {
            detailsTextArea.setText("Select an item to see details.");
        } else if (selectedObject instanceof ComicBook) {
            // Call the helper for Comic Books
            detailsTextArea.setText(buildComicBookDetails((ComicBook) selectedObject));
        } else if (selectedObject instanceof ComicCharacter) {
            // Call the helper for Characters
            detailsTextArea.setText(buildCharacterDetails((ComicCharacter) selectedObject));
        } else if (selectedObject instanceof Writer) {
            // Call the generic helper for Creators
            detailsTextArea.setText(buildCreatorDetails(selectedObject));
        } else if (selectedObject instanceof Artist) {
            // Call the generic helper for Creators
            detailsTextArea.setText(buildCreatorDetails(selectedObject));
        } else if (selectedObject instanceof Publisher) {
            // Call the helper for Publishers
            detailsTextArea.setText(buildPublisherDetails((Publisher) selectedObject));
        } else {
            detailsTextArea.setText("Unknown item selected.");
        }
        
        // Reset scrollbar to the top after changing text
        detailsTextArea.setCaretPosition(0);
    }
    
    // --- NEW HELPER METHODS (Add these to your class) ---

    /**
     * Builds a formatted string for a ComicBook object.
     * @param comic The ComicBook to display.
     * @return A formatted string of its details.
     */
    private String buildComicBookDetails(ComicBook comic) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("--- COMIC BOOK ---\n");
        sb.append("Title: ").append(comic.getTitle()).append("\n");
        sb.append("Genre: ").append(comic.getGenre()).append("\n");
        
        sb.append("\nWRITERS:\n");
        if (comic.getWriters().isEmpty()) {
            sb.append("  (None listed)\n");
        } else {
            for (Writer w : comic.getWriters()) {
                sb.append("  - ").append(w.getName()).append("\n");
            }
        }
        
        sb.append("\nARTISTS:\n");
        if (comic.getArtists().isEmpty()) {
            sb.append("  (None listed)\n");
        } else {
            for (Artist a : comic.getArtists()) {
                sb.append("  - ").append(a.getName()).append("\n");
            }
        }
        
        sb.append("\nEDITIONS:\n");
        if (comic.getEditions().isEmpty()) {
            sb.append("  (None listed)\n");
        } else {
            for (Edition e : comic.getEditions()) {
                sb.append("  - ").append(e.getEditionName());
                sb.append(" (").append(e.getPublisher().getName()).append(")\n");
            }
        }

        return sb.toString();
    }
    
    /**
     * Builds a formatted string for a ComicCharacter object.
     * @param character The ComicCharacter to display.
     * @return A formatted string of its details.
     */
    private String buildCharacterDetails(ComicCharacter character) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("--- CHARACTER ---\n");
        sb.append("Alias: ").append(character.getDisplayName()).append("\n");
        sb.append("Real Name: ").append(character.getRealName()).append("\n");
        sb.append("Type: ").append(character.getClass().getSimpleName()).append("\n");
        
        sb.append("\nORIGIN:\n");
        sb.append(character.getOrigin().isEmpty() ? "  (N/A)" : "  " + character.getOrigin()).append("\n");
        
        // Handle powers for Superheroes and Villains
        if (character instanceof Superhero) {
            Superhero s = (Superhero) character;
            sb.append("\nPOWERS:\n");
            if (s.getPowers().isEmpty()) {
                sb.append("  (None listed)\n");
            } else {
                for (String power : s.getPowers()) {
                    sb.append("  - ").append(power).append("\n");
                }
            }
        } else if (character instanceof Villain) {
            Villain v = (Villain) character;
            sb.append("\nPOWERS:\n");
            if (v.getPowers().isEmpty()) {
                sb.append("  (None listed)\n");
            } else {
                for (String power : v.getPowers()) {
                    sb.append("  - ").append(power).append("\n");
                }
            }
        }

        sb.append("\nAPPEARANCES:\n");
        if (character.getComicBookAppearances().isEmpty()) {
            sb.append("  (None listed)\n");
        } else {
            for (ComicBook c : character.getComicBookAppearances()) {
                sb.append("  - ").append(c.getTitle()).append("\n");
            }
        }
        
        // --- NEW: Display Affiliations (Team Names) ---
        sb.append("\nAFFILIATIONS:\n");
        List<String> teamAffiliations = null;
        if (character instanceof Superhero) {
            teamAffiliations = ((Superhero) character).getAffiliations();
        } else if (character instanceof Villain) {
            teamAffiliations = ((Villain) character).getAffiliations();
        }

        if (teamAffiliations == null || teamAffiliations.isEmpty()) {
            sb.append("  (None listed)\n");
        } else {
            for (String team : teamAffiliations) {
                sb.append("  - ").append(team).append("\n");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Builds a formatted string for a Writer or Artist object.
     * @param creator The Writer or Artist object.
     * @return A formatted string of their details.
     */
    private String buildCreatorDetails(Object creator) {
        StringBuilder sb = new StringBuilder();
        
        if (creator instanceof Writer) {
            Writer w = (Writer) creator;
            sb.append("--- WRITER ---\n");
            sb.append("Name: ").append(w.getName()).append("\n");
            sb.append("Nationality: ").append(w.getNationality()).append("\n");
        } else if (creator instanceof Artist) {
            Artist a = (Artist) creator;
            sb.append("--- ARTIST ---\n");
            sb.append("Name: ").append(a.getName()).append("\n");
            sb.append("Nationality: ").append(a.getNationality()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Builds a formatted string for a Publisher object.
     * @param pub The Publisher to display.
     * @return A formatted string of its details.
     */
    private String buildPublisherDetails(Publisher pub) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("--- PUBLISHER ---\n");
        sb.append("Name: ").append(pub.getName()).append("\n");
        sb.append("Country: ").append(pub.getCountry()).append("\n");
        
        return sb.toString();
    }
    
    
    /**
     * Helper method to get a displayable publisher name for a ComicBook.
     */
    private String getPublisherNameForComic(ComicBook comic) {
        if (comic.getEditions() != null && !comic.getEditions().isEmpty()) {
            // Get the first edition
            Edition firstEdition = comic.getEditions().get(0);
            if (firstEdition != null && firstEdition.getPublisher() != null) {
                return firstEdition.getPublisher().getName();
            }
        }
        return "N/A";
    }
    
    /**
     * Helper method to get the currently selected object from the table.
     * @return The selected object, or null if no selection.
     */
    private Object getSelectedObject() {
        // Find out which tab is currently selected
        int selectedTabIndex = mainTabbedPane.getSelectedIndex();
        
        switch (selectedTabIndex) {
            case 0: // Comic Books Tab
                int comicRow = comicBooksTable.getSelectedRow();
                if (comicRow >= 0 && comicRow < allComicBooks.size()) {
                    return allComicBooks.get(comicRow);
                }
                break;
            case 1: // Characters Tab
                int charRow = charactersTable.getSelectedRow();
                if (charRow >= 0 && charRow < allCharacters.size()) {
                    return allCharacters.get(charRow);
                }
                break;
            case 2: // Writers Tab
                int writerRow = writersTable.getSelectedRow();
                if (writerRow >= 0 && writerRow < allWriters.size()) {
                    return allWriters.get(writerRow);
                }
                break;
            case 3: // Artists Tab
                int artistRow = artistsTable.getSelectedRow();
                if (artistRow >= 0 && artistRow < allArtists.size()) {
                    return allArtists.get(artistRow);
                }
                break;
            case 4: // Publishers Tab
                int pubRow = publishersTable.getSelectedRow();
                if (pubRow >= 0 && pubRow < allPublishers.size()) {
                    return allPublishers.get(pubRow);
                }
                break;
        }
        
        return null; // No tab selected or no item selected in the active tab
    }
    
    /**
     * --- PUBLIC METHODS FOR EDITORS ---
     * These methods are called by the editor dialogs to add
     * new data to the main application lists.
     */
    
    public void addPublisher(Publisher publisher) {
        this.allPublishers.add(publisher);
        refreshPublishersTable(); // More efficient
    }
    
    public void addWriter(Writer writer) {
        this.allWriters.add(writer);
        refreshWritersTable(); // More efficient
    }
    
    public void addArtist(Artist artist) {
        this.allArtists.add(artist);
        refreshArtistsTable(); // More efficient
    }
    
    public void addCharacter(ComicCharacter character) {
        this.allCharacters.add(character);
        refreshCharactersTable(); // More efficient
    }
    
    public void addComicBook(ComicBook comic) {
        this.allComicBooks.add(comic);
        refreshComicBooksTable(); // More efficient
    }
    
    
    public void refreshAllTables() {
        refreshPublishersTable();
        refreshWritersTable();
        refreshArtistsTable();
        refreshCharactersTable();
        refreshComicBooksTable();
    }
    
    // --- NEW: Specific refresh methods for each table ---
    
    public void refreshPublishersTable() {
        publishersModel.setRowCount(0); // Clear table
        for (Publisher pub : allPublishers) {
            publishersModel.addRow(new Object[]{
                pub.getName(),
                pub.getCountry()
            });
        }
    }
    
    public void refreshWritersTable() {
        writersModel.setRowCount(0); // Clear table
        for (Writer writer : allWriters) {
            writersModel.addRow(new Object[]{
                writer.getName(),
                writer.getNationality()
            });
        }
    }
    
    public void refreshArtistsTable() {
        artistsModel.setRowCount(0); // Clear table
        for (Artist artist : allArtists) {
            artistsModel.addRow(new Object[]{
                artist.getName(),
                artist.getNationality()
            });
        }
    }

    public void refreshCharactersTable() {
        charactersModel.setRowCount(0); // Clear table
        for (ComicCharacter character : allCharacters) {
            charactersModel.addRow(new Object[]{
                character.getDisplayName(),
                character.getRealName(), // Assuming getRealName() exists
                character.getClass().getSimpleName()
            });
        }
    }
    
    public void refreshComicBooksTable() {
        comicBooksModel.setRowCount(0); // Clear table
        for (ComicBook comic : allComicBooks) {
            comicBooksModel.addRow(new Object[]{
                comic.getTitle(),
                comic.getGenre(),
                getPublisherNameForComic(comic)
            });
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

        mainPanel = new javax.swing.JPanel();
        buttomButtonPanel = new javax.swing.JPanel();
        addCharacterButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        addComicBookButton = new javax.swing.JButton();
        addEditionButton = new javax.swing.JButton();
        mainTabbedPane = new javax.swing.JTabbedPane();
        comicBookPanel = new javax.swing.JPanel();
        comicBookTableScrollPanel = new javax.swing.JScrollPane();
        comicBooksTable = new javax.swing.JTable();
        characterPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        charactersTable = new javax.swing.JTable();
        writersPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        writersTable = new javax.swing.JTable();
        artistsPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        artistsTable = new javax.swing.JTable();
        publishersPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        publishersTable = new javax.swing.JTable();
        detailsPanel = new javax.swing.JPanel();
        detailsScrollPane = new javax.swing.JScrollPane();
        detailsTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuItemExit = new javax.swing.JMenuItem();
        addMenu = new javax.swing.JMenu();
        menuItemAddComic = new javax.swing.JMenuItem();
        menuItemAddCharacter = new javax.swing.JMenuItem();
        menuItemAddCreator = new javax.swing.JMenuItem();
        menuItemAddPublisher = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Comic Book Database Dashboard");
        setMinimumSize(new java.awt.Dimension(700, 500));

        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanel.setLayout(new java.awt.BorderLayout());

        buttomButtonPanel.setLayout(new java.awt.GridBagLayout());

        addCharacterButton.setText("Add New Character");
        addCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCharacterButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 36, 0);
        buttomButtonPanel.add(addCharacterButton, gridBagConstraints);
        addCharacterButton.getAccessibleContext().setAccessibleName("jAddNewCharacterButton");

        editButton.setText("Edit Selected Item");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 36, 0);
        buttomButtonPanel.add(editButton, gridBagConstraints);
        editButton.getAccessibleContext().setAccessibleName("jEditSelectedItemButton");

        deleteButton.setText("Delete Selected Item");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 36, 6);
        buttomButtonPanel.add(deleteButton, gridBagConstraints);
        deleteButton.getAccessibleContext().setAccessibleName("jDeleteSelectedItemButton");

        addComicBookButton.setText("Add New Comic Book");
        addComicBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addComicBookButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 36, 0);
        buttomButtonPanel.add(addComicBookButton, gridBagConstraints);
        addComicBookButton.getAccessibleContext().setAccessibleName("jComicBookEditorButton");

        addEditionButton.setText("AddEditon");
        addEditionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEditionButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 6, 36, 0);
        buttomButtonPanel.add(addEditionButton, gridBagConstraints);

        mainPanel.add(buttomButtonPanel, java.awt.BorderLayout.SOUTH);

        comicBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title", "Genre", "Publisher"
            }
        ));
        comicBookTableScrollPanel.setViewportView(comicBooksTable);

        javax.swing.GroupLayout comicBookPanelLayout = new javax.swing.GroupLayout(comicBookPanel);
        comicBookPanel.setLayout(comicBookPanelLayout);
        comicBookPanelLayout.setHorizontalGroup(
            comicBookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(comicBookTableScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
        );
        comicBookPanelLayout.setVerticalGroup(
            comicBookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(comicBookTableScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Comic Books", comicBookPanel);

        charactersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Alias", "Real Name", "Type"
            }
        ));
        jScrollPane1.setViewportView(charactersTable);

        javax.swing.GroupLayout characterPanelLayout = new javax.swing.GroupLayout(characterPanel);
        characterPanel.setLayout(characterPanelLayout);
        characterPanelLayout.setHorizontalGroup(
            characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
        );
        characterPanelLayout.setVerticalGroup(
            characterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Characters", characterPanel);

        writersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Nationality"
            }
        ));
        jScrollPane2.setViewportView(writersTable);

        javax.swing.GroupLayout writersPanelLayout = new javax.swing.GroupLayout(writersPanel);
        writersPanel.setLayout(writersPanelLayout);
        writersPanelLayout.setHorizontalGroup(
            writersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
        );
        writersPanelLayout.setVerticalGroup(
            writersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Writers", writersPanel);

        artistsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Nationality"
            }
        ));
        jScrollPane3.setViewportView(artistsTable);

        javax.swing.GroupLayout artistsPanelLayout = new javax.swing.GroupLayout(artistsPanel);
        artistsPanel.setLayout(artistsPanelLayout);
        artistsPanelLayout.setHorizontalGroup(
            artistsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, artistsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
                .addContainerGap())
        );
        artistsPanelLayout.setVerticalGroup(
            artistsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Artists", artistsPanel);

        publishersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Country"
            }
        ));
        jScrollPane4.setViewportView(publishersTable);

        javax.swing.GroupLayout publishersPanelLayout = new javax.swing.GroupLayout(publishersPanel);
        publishersPanel.setLayout(publishersPanelLayout);
        publishersPanelLayout.setHorizontalGroup(
            publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
        );
        publishersPanelLayout.setVerticalGroup(
            publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        mainTabbedPane.addTab("Publishers", publishersPanel);

        mainPanel.add(mainTabbedPane, java.awt.BorderLayout.CENTER);

        detailsPanel.setLayout(new java.awt.BorderLayout());

        detailsTextArea.setEditable(false);
        detailsTextArea.setColumns(20);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setRows(5);
        detailsTextArea.setWrapStyleWord(true);
        detailsScrollPane.setViewportView(detailsTextArea);

        detailsPanel.add(detailsScrollPane, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });

        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        fileMenu.add(menuItemExit);

        menuBar.add(fileMenu);
        fileMenu.getAccessibleContext().setAccessibleName("jExit");

        addMenu.setText("Add New");

        menuItemAddComic.setText("Add Comic");
        menuItemAddComic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddComicActionPerformed(evt);
            }
        });
        addMenu.add(menuItemAddComic);

        menuItemAddCharacter.setText("Add Character");
        menuItemAddCharacter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddCharacterActionPerformed(evt);
            }
        });
        addMenu.add(menuItemAddCharacter);

        menuItemAddCreator.setText("Add Creator");
        menuItemAddCreator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddCreatorActionPerformed(evt);
            }
        });
        addMenu.add(menuItemAddCreator);

        menuItemAddPublisher.setText("Add Publisher");
        menuItemAddPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddPublisherActionPerformed(evt);
            }
        });
        addMenu.add(menuItemAddPublisher);

        menuBar.add(addMenu);
        addMenu.getAccessibleContext().setAccessibleName("jAddNew");

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        mainPanel.getAccessibleContext().setAccessibleName("jPanel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCharacterButtonActionPerformed
        logger.info("Opening Character Editor...");
        
        // This opens the CharacterEditor.
        
        CharacterEditor charEditor = new CharacterEditor(
            this, 
            true, 
            allCharacters,  // <-- This is what you asked for
            allWriters,       // <-- Also pass writers
            allArtists,     // <-- Also pass artists
            allComicBooks   // <-- Also pass comic books
        );
        charEditor.setVisible(true);
        
        // The dialog will call public method addCharacter() on save,
        // which automatically calls refreshTable().
    }//GEN-LAST:event_addCharacterButtonActionPerformed

    private void addComicBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addComicBookButtonActionPerformed
        logger.info("Opening Comic Book Editor...");
        
        // This is the correct way to open the ComicBookEditor,
        // passing all the required data lists to its constructor.
        ComicBookEditor comicEditor = new ComicBookEditor(
            this, 
            true, 
            allWriters, 
            allArtists,
            allCharacters,
            allPublishers
        );
        
        comicEditor.setVisible(true);
    }//GEN-LAST:event_addComicBookButtonActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemAddComicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddComicActionPerformed
        // TODO add your handling code here:
        addComicBookButtonActionPerformed(evt);
    }//GEN-LAST:event_menuItemAddComicActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        Object selectedObject = getSelectedObject();
        if (selectedObject == null) {
            return; // Nothing selected
        }

        // --- NEW EDIT LOGIC ---
        
        // 1. Determine which editor to open
        if (selectedObject instanceof Publisher) {
            // Open PublisherEditor in "edit mode"
            PublisherEditor pubEditor = new PublisherEditor(this, true, (Publisher) selectedObject);
            pubEditor.setVisible(true);
            
        } else if (selectedObject instanceof Writer) {
            // Open CreatorEditor in "edit mode" for a Writer
            CreatorEditor creatorEditor = new CreatorEditor(this, true, (Writer) selectedObject);
            creatorEditor.setVisible(true);
            
        } else if (selectedObject instanceof Artist) {
            // Open CreatorEditor in "edit mode" for an Artist
            CreatorEditor creatorEditor = new CreatorEditor(this, true, (Artist) selectedObject);
            creatorEditor.setVisible(true);
            
        } else if (selectedObject instanceof ComicCharacter) {
            // Open CharacterEditor in "edit mode"
            CharacterEditor charEditor = new CharacterEditor(
                this, 
                true, 
                allCharacters,
                allWriters,
                allArtists,
                allComicBooks,
                (ComicCharacter) selectedObject // <-- Pass the character to edit
            );
            charEditor.setVisible(true);
            
        } else if (selectedObject instanceof ComicBook) {
            // Open ComicBookEditor in "edit mode"
            ComicBookEditor comicEditor = new ComicBookEditor(
                this, 
                true, 
                allWriters, 
                allArtists,
                allCharacters,
                allPublishers,
                (ComicBook) selectedObject // <-- Pass the comic to edit
            );
            comicEditor.setVisible(true);
        }
        
        // 2. After the editor dialog closes, refresh all tables
        // This will show any changes that were saved.
        refreshAllTables();
        
        // 3. Refresh the details panel to show the updated info
        onTableSelectionChanged();
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        Object selectedObject = getSelectedObject();
        if (selectedObject == null) return;
        
        // Get the name for the confirmation dialog
        String objectName = "this item";
        if (selectedObject instanceof Publisher) objectName = ((Publisher)selectedObject).getName();
        else if (selectedObject instanceof Writer) objectName = ((Writer)selectedObject).getName();
        else if (selectedObject instanceof Artist) objectName = ((Artist)selectedObject).getName();
        else if (selectedObject instanceof ComicCharacter) objectName = ((ComicCharacter)selectedObject).getDisplayName();
        else if (selectedObject instanceof ComicBook) objectName = ((ComicBook)selectedObject).getTitle();
        
        int choice = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to delete '" + objectName + "'?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Remove the object from the correct list
            // AND call the correct refresh method
            if (selectedObject instanceof Publisher) {
                allPublishers.remove(selectedObject);
                refreshPublishersTable();
            } else if (selectedObject instanceof Writer) {
                allWriters.remove(selectedObject);
                refreshWritersTable();
            } else if (selectedObject instanceof Artist) {
                allArtists.remove(selectedObject);
                refreshArtistsTable();
            } else if (selectedObject instanceof ComicCharacter) {
                allCharacters.remove(selectedObject);
                refreshCharactersTable();
            } else if (selectedObject instanceof ComicBook) {
                allComicBooks.remove(selectedObject);
                refreshComicBooksTable();
            }
            
            logger.info("Deleted object: " + objectName);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void menuItemAddCharacterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddCharacterActionPerformed
        // TODO add your handling code here:
        addCharacterButtonActionPerformed(evt);
    }//GEN-LAST:event_menuItemAddCharacterActionPerformed

    private void menuItemAddCreatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddCreatorActionPerformed
        // TODO add your handling code here:
        logger.info("Opening Creator Editor...");
        CreatorEditor creatorEditor = new CreatorEditor(this, true);
        creatorEditor.setVisible(true);
    }//GEN-LAST:event_menuItemAddCreatorActionPerformed

    private void menuItemAddPublisherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddPublisherActionPerformed
        // TODO add your handling code here:
        logger.info("Opening Publisher Editor...");
        PublisherEditor publisherEditor = new PublisherEditor(this, true);
        publisherEditor.setVisible(true);
    }//GEN-LAST:event_menuItemAddPublisherActionPerformed

    private void addEditionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEditionButtonActionPerformed
        // TODO add your handling code here:
        
        Object selectedObject = getSelectedObject();
        
        // Double-check that a ComicBook is selected
        if (selectedObject instanceof ComicBook) {
            logger.info("Opening Edition Editor...");
            ComicBook selectedComic = (ComicBook) selectedObject;
            
            // Open the EditionEditor, passing the selected comic and all publishers
            EditionEditor editionEditor = new EditionEditor(
                this, 
                true, 
                selectedComic, 
                allPublishers
            );
            editionEditor.setVisible(true);
            
            // The EditionEditor itself will call refreshTable() on save.
        } else {
            // This should not happen if the button is enabled/disabled correctly
            JOptionPane.showMessageDialog(this, 
                    "Please select a Comic Book to add an edition to.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addEditionButtonActionPerformed

    
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

        /* Create and display the form */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCharacterButton;
    private javax.swing.JButton addComicBookButton;
    private javax.swing.JButton addEditionButton;
    private javax.swing.JMenu addMenu;
    private javax.swing.JPanel artistsPanel;
    private javax.swing.JTable artistsTable;
    private javax.swing.JPanel buttomButtonPanel;
    private javax.swing.JPanel characterPanel;
    private javax.swing.JTable charactersTable;
    private javax.swing.JPanel comicBookPanel;
    private javax.swing.JScrollPane comicBookTableScrollPanel;
    private javax.swing.JTable comicBooksTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JScrollPane detailsScrollPane;
    private javax.swing.JTextArea detailsTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuItemAddCharacter;
    private javax.swing.JMenuItem menuItemAddComic;
    private javax.swing.JMenuItem menuItemAddCreator;
    private javax.swing.JMenuItem menuItemAddPublisher;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JPanel publishersPanel;
    private javax.swing.JTable publishersTable;
    private javax.swing.JPanel writersPanel;
    private javax.swing.JTable writersTable;
    // End of variables declaration//GEN-END:variables
}
