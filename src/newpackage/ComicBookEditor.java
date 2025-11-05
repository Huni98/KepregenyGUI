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
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import ro.madarash.kepregeny_project.*;
import javax.swing.*;
import java.awt.Component;
import java.util.List; // For List
import java.util.ArrayList; // For main method test

public class ComicBookEditor extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ComicBookEditor.class.getName());

    // --- Master Lists ---
    private List<Writer> allWriters;
    private List<Artist> allArtists;
    private List<ComicCharacter> allCharacters;
    private List<Publisher> allPublishers;
    
    // --- NEW: Field to store the comic being edited ---
    private ComicBook comicToEdit;
    
    
    /**
     * --- "CREATE NEW" Constructor ---
     */
    public ComicBookEditor(java.awt.Frame parent, boolean modal,
                           List<Writer> allWriters, List<Artist> allArtists,
                           List<ComicCharacter> allCharacters, List<Publisher> allPublishers) {
        super(parent, modal);
        
        // Store the passed-in lists
        this.allWriters = allWriters;
        this.allArtists = allArtists;
        this.allCharacters = allCharacters;
        this.allPublishers = allPublishers;
        
        // "Create" mode
        this.comicToEdit = null;
        
        initComponents();
        this.setSize(700, 600); // Set standard size
        
        setLocationRelativeTo(parent);
        
        setupListModels();
        populateAvailableLists();
        setupRenderers();
    }
    
    /**
     * --- NEW: "EDIT MODE" Constructor ---
     */
    public ComicBookEditor(java.awt.Frame parent, boolean modal,
                           List<Writer> allWriters, List<Artist> allArtists,
                           List<ComicCharacter> allCharacters, List<Publisher> allPublishers,
                           ComicBook comicToEdit) { // <-- Extra parameter
        
        // Call the "Create" constructor
        this(parent, modal, allWriters, allArtists, allCharacters, allPublishers);
        
        // Set the comic to edit
        this.comicToEdit = comicToEdit;
        
        // Change window title
        setTitle("Edit Comic Book: " + this.comicToEdit.getTitle());
        
        // Load this comic's data into the form
        loadDataForEdit();
    }
    
    private void setupListModels() {
        // Create the models
        allWritersModel = new DefaultListModel<>();
        thisComicBookWriterModel = new DefaultListModel<>();
        allArtistsModel = new DefaultListModel<>();
        thisComicBookArtistModel = new DefaultListModel<>();
        allCharactersModel = new DefaultListModel<>();
        featuredCharactersModel = new DefaultListModel<>();
        
        // Set the models to their JLists
        // This REPLACES the "Item 1, Item 2..." placeholder models
        allWritersList.setModel(allWritersModel);
        thisComicBookWriterList.setModel(thisComicBookWriterModel);
        allArtistsList.setModel(allArtistsModel);
        thisComicBookArtistList.setModel(thisComicBookArtistModel);
        allCharactersList.setModel(allCharactersModel);
        featuredCharactersList.setModel(featuredCharactersModel);
        
        // Set up the Publisher ComboBox
        publisherComboBox.setModel(new DefaultComboBoxModel<>(
                allPublishers.toArray(new Publisher[0])
        ));
    }
    
    private void populateAvailableLists() {
        // Clear any old data
        allWritersModel.removeAllElements();
        allArtistsModel.removeAllElements();
        allCharactersModel.removeAllElements();
        
        // Add all items from the main lists
        for (Writer w : allWriters) {
            allWritersModel.addElement(w);
        }
        for (Artist a : allArtists) {
            allArtistsModel.addElement(a);
        }
        for (ComicCharacter c : allCharacters) {
            allCharactersModel.addElement(c);
        }
    }
    
    private void setupRenderers() {
        ComicObjectRenderer renderer = new ComicObjectRenderer();
        
        // Set renderer for all JLists
        allWritersList.setCellRenderer(renderer);
        thisComicBookWriterList.setCellRenderer(renderer);
        allArtistsList.setCellRenderer(renderer);
        thisComicBookArtistList.setCellRenderer(renderer);
        allCharactersList.setCellRenderer(renderer);
        featuredCharactersList.setCellRenderer(renderer);
        
        // Set renderer for the ComboBox
        publisherComboBox.setRenderer(renderer);
    }
    
    /**
     * --- NEW: Helper method to populate all fields for editing ---
     */
    private void loadDataForEdit() {
        // 1. Populate simple text fields
        titleField.setText(comicToEdit.getTitle());
        genreField.setText(comicToEdit.getGenre());
        
        // 2. Set ComboBox and Edition field
        // This will load the *first* edition's details
        if (comicToEdit.getEditions() != null && !comicToEdit.getEditions().isEmpty()) {
            Edition firstEdition = comicToEdit.getEditions().get(0);
            editionField.setText(firstEdition.getEditionName());
            publisherComboBox.setSelectedItem(firstEdition.getPublisher());
        }
        
        // 3. Populate "Selected" lists
        
        // -- Writers --
        if (comicToEdit.getWriters() != null) {
            List<Writer> writers = new ArrayList<>(comicToEdit.getWriters());
            for (Writer w : writers) {
                if (allWritersModel.contains(w)) {
                    allWritersModel.removeElement(w);
                    thisComicBookWriterModel.addElement(w);
                }
            }
        }
        
        // -- Artists --
        if (comicToEdit.getArtists() != null) {
            List<Artist> artists = new ArrayList<>(comicToEdit.getArtists());
            for (Artist a : artists) {
                if (allArtistsModel.contains(a)) {
                    allArtistsModel.removeElement(a);
                    thisComicBookArtistModel.addElement(a);
                }
            }
        }
        
        // -- Characters --
        if (comicToEdit.getFeaturedCharacters() != null) {
            List<ComicCharacter> characters = new ArrayList<>(comicToEdit.getFeaturedCharacters());
            for (ComicCharacter c : characters) {
                if (allCharactersModel.contains(c)) {
                    allCharactersModel.removeElement(c);
                    featuredCharactersModel.addElement(c);
                }
            }
        }
    }
    
    private <T> void moveItems(JList<T> sourceList, JList<T> destList) {
        // Get the models for both lists
        DefaultListModel<T> sourceModel = (DefaultListModel<T>) sourceList.getModel();
        DefaultListModel<T> destModel = (DefaultListModel<T>) destList.getModel();
        
        // Get all selected items from the source list
        List<T> selectedItems = sourceList.getSelectedValuesList();
        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        topInfoPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        genreLabel = new javax.swing.JLabel();
        genreField = new javax.swing.JTextField();
        publisherLabel = new javax.swing.JLabel();
        publisherComboBox = new javax.swing.JComboBox<>();
        editionLabel = new javax.swing.JLabel();
        editionField = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        writersPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        allWritersList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        thisComicBookWriterList = new javax.swing.JList<>();
        writersButtonPanel = new javax.swing.JPanel();
        addWriterButton = new javax.swing.JButton();
        removeWriterButton = new javax.swing.JButton();
        artistsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        allArtistsList = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        thisComicBookArtistList = new javax.swing.JList<>();
        artistsButtonPanel = new javax.swing.JPanel();
        addArtistButton = new javax.swing.JButton();
        removeArtistButton = new javax.swing.JButton();
        featuredCharactersPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        allCharactersList = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        featuredCharactersList = new javax.swing.JList<>();
        powersButtonPanel2 = new javax.swing.JPanel();
        addCharacterButton = new javax.swing.JButton();
        removeCharacterButton = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        titleLabel.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(titleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(titleField, gridBagConstraints);

        genreLabel.setText("Genre:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(genreLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(genreField, gridBagConstraints);

        publisherLabel.setText("Publisher:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(publisherLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(publisherComboBox, gridBagConstraints);

        editionLabel.setText("Edition:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(editionLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        topInfoPanel.add(editionField, gridBagConstraints);

        mainPanel.add(topInfoPanel, java.awt.BorderLayout.NORTH);

        writersPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 100));

        allWritersList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Writers"));
        jScrollPane1.setViewportView(allWritersList);

        writersPanel.add(jScrollPane1, java.awt.BorderLayout.WEST);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(250, 100));

        thisComicBookWriterList.setBorder(javax.swing.BorderFactory.createTitledBorder("This Comic Books Writers"));
        jScrollPane2.setViewportView(thisComicBookWriterList);

        writersPanel.add(jScrollPane2, java.awt.BorderLayout.EAST);

        writersButtonPanel.setLayout(new java.awt.BorderLayout());

        addWriterButton.setText("->");
        addWriterButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addWriterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWriterButtonActionPerformed(evt);
            }
        });
        writersButtonPanel.add(addWriterButton, java.awt.BorderLayout.NORTH);

        removeWriterButton.setText("<-");
        removeWriterButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeWriterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeWriterButtonActionPerformed(evt);
            }
        });
        writersButtonPanel.add(removeWriterButton, java.awt.BorderLayout.SOUTH);

        writersPanel.add(writersButtonPanel, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Writers", writersPanel);

        artistsPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setPreferredSize(new java.awt.Dimension(250, 100));

        allArtistsList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Artists"));
        jScrollPane4.setViewportView(allArtistsList);

        artistsPanel.add(jScrollPane4, java.awt.BorderLayout.WEST);

        jScrollPane5.setPreferredSize(new java.awt.Dimension(250, 100));

        thisComicBookArtistList.setBorder(javax.swing.BorderFactory.createTitledBorder("This Comic Books Artists"));
        jScrollPane5.setViewportView(thisComicBookArtistList);

        artistsPanel.add(jScrollPane5, java.awt.BorderLayout.EAST);

        artistsButtonPanel.setLayout(new java.awt.BorderLayout());

        addArtistButton.setText("->");
        addArtistButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addArtistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addArtistButtonActionPerformed(evt);
            }
        });
        artistsButtonPanel.add(addArtistButton, java.awt.BorderLayout.NORTH);

        removeArtistButton.setText("<-");
        removeArtistButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeArtistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeArtistButtonActionPerformed(evt);
            }
        });
        artistsButtonPanel.add(removeArtistButton, java.awt.BorderLayout.SOUTH);

        artistsPanel.add(artistsButtonPanel, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Artists", artistsPanel);

        featuredCharactersPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane6.setPreferredSize(new java.awt.Dimension(250, 100));

        allCharactersList.setBorder(javax.swing.BorderFactory.createTitledBorder("All Characters"));
        jScrollPane6.setViewportView(allCharactersList);

        featuredCharactersPanel.add(jScrollPane6, java.awt.BorderLayout.WEST);

        jScrollPane7.setPreferredSize(new java.awt.Dimension(250, 100));

        featuredCharactersList.setBorder(javax.swing.BorderFactory.createTitledBorder("Featured Characters"));
        jScrollPane7.setViewportView(featuredCharactersList);

        featuredCharactersPanel.add(jScrollPane7, java.awt.BorderLayout.EAST);

        powersButtonPanel2.setLayout(new java.awt.BorderLayout());

        addCharacterButton.setText("->");
        addCharacterButton.setPreferredSize(new java.awt.Dimension(70, 25));
        addCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCharacterButtonActionPerformed(evt);
            }
        });
        powersButtonPanel2.add(addCharacterButton, java.awt.BorderLayout.NORTH);

        removeCharacterButton.setText("<-");
        removeCharacterButton.setPreferredSize(new java.awt.Dimension(70, 25));
        removeCharacterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCharacterButtonActionPerformed(evt);
            }
        });
        powersButtonPanel2.add(removeCharacterButton, java.awt.BorderLayout.SOUTH);

        featuredCharactersPanel.add(powersButtonPanel2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Feautred Characters", featuredCharactersPanel);

        mainPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * --- MODIFIED: Handles both Create and Update ---
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // 1. Get data from the top form
        String title = titleField.getText();
        String genre = genreField.getText();
        String editionName = editionField.getText();
        Publisher selectedPublisher = (Publisher) publisherComboBox.getSelectedItem();
        
        // 2. Validate data
        if (title.isBlank()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedPublisher == null) {
            JOptionPane.showMessageDialog(this, "You must select a Publisher.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (editionName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Edition name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Check mode (Edit or Create)
        if (comicToEdit != null) {
            // --- EDIT MODE ---
            logger.info("Updating comic: " + comicToEdit.getTitle());
            
            // Update simple properties
            comicToEdit.setTitle(title);
            comicToEdit.setGenre(genre); // (Need to add setGenre() to ComicBook.java)
            
            // Update the *first* edition's details
            if (comicToEdit.getEditions() != null && !comicToEdit.getEditions().isEmpty()) {
                Edition firstEdition = comicToEdit.getEditions().get(0);
                firstEdition.setEditionName(editionName); // (Need to add setEditionName())
                firstEdition.setPublisher(selectedPublisher); // (Need to add setPublisher())
            }
            
            // Update lists (clear old, add new)
            comicToEdit.getWriters().clear();
            for (int i = 0; i < thisComicBookWriterModel.getSize(); i++) {
                comicToEdit.addWriter(thisComicBookWriterModel.getElementAt(i));
            }
            
            comicToEdit.getArtists().clear();
            for (int i = 0; i < thisComicBookArtistModel.getSize(); i++) {
                comicToEdit.addArtist(thisComicBookArtistModel.getElementAt(i));
            }
            
            comicToEdit.getFeaturedCharacters().clear(); // (Need to add getCharacters() and clear() to ComicBook.java)
            for (int i = 0; i < featuredCharactersModel.getSize(); i++) {
                comicToEdit.addCharacter(featuredCharactersModel.getElementAt(i));
            }
            
        } else {
            // --- CREATE MODE ---
            ComicBook newComic = new ComicBook(title, genre);
            
            // Create a default Edition
            Edition newEdition = new Edition(editionName, new java.util.Date(), "N/A", selectedPublisher, newComic);
            newComic.addEdition(newEdition);
            
            // Add all selected items from the lists
            for (int i = 0; i < thisComicBookWriterModel.getSize(); i++) {
                newComic.addWriter(thisComicBookWriterModel.getElementAt(i));
            }
            for (int i = 0; i < thisComicBookArtistModel.getSize(); i++) {
                newComic.addArtist(thisComicBookArtistModel.getElementAt(i));
            }
            for (int i = 0; i < featuredCharactersModel.getSize(); i++) {
                newComic.addCharacter(featuredCharactersModel.getElementAt(i));
            }
            
            // Add the new comic to the main list
            ((MainDashboard) getParent()).addComicBook(newComic);
            logger.info("New Comic Created: " + newComic.getTitle());
        }

        // 7. Close the dialog
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addWriterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWriterButtonActionPerformed
        // TODO add your handling code here:
         moveItems(allWritersList, thisComicBookWriterList);
    }//GEN-LAST:event_addWriterButtonActionPerformed

    private void removeWriterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeWriterButtonActionPerformed
        // TODO add your handling code here:
        moveItems(thisComicBookWriterList, allWritersList);
    }//GEN-LAST:event_removeWriterButtonActionPerformed

    private void addArtistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addArtistButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allArtistsList, thisComicBookArtistList);
    }//GEN-LAST:event_addArtistButtonActionPerformed

    private void removeArtistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeArtistButtonActionPerformed
        // TODO add your handling code here:
        moveItems(thisComicBookArtistList, allArtistsList);
    }//GEN-LAST:event_removeArtistButtonActionPerformed

    private void addCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCharacterButtonActionPerformed
        // TODO add your handling code here:
        moveItems(allCharactersList, featuredCharactersList);
    }//GEN-LAST:event_addCharacterButtonActionPerformed

    private void removeCharacterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCharacterButtonActionPerformed
        // TODO add your handling code here:
         moveItems(featuredCharactersList, allCharactersList);
    }//GEN-LAST:event_removeCharacterButtonActionPerformed

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
                // --- NEW: Dummy data for testing the dialog directly ---
                List<Writer> testWriters = new ArrayList<>();
                testWriters.add(new Writer("Stan Lee", "USA"));
                
                List<Artist> testArtists = new ArrayList<>();
                testArtists.add(new Artist("Steve Ditko", "USA"));
                
                List<ComicCharacter> testChars = new ArrayList<>();
                testChars.add(new Civilian("J. Jonah Jameson", "..."));
                
                List<Publisher> testPublishers = new ArrayList<>();
                testPublishers.add(new Publisher("Marvel Comics", "USA"));

                ComicBookEditor dialog = new ComicBookEditor(new javax.swing.JFrame(), true,
                        testWriters, testArtists, testChars, testPublishers);
                
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
            } else if (value instanceof Publisher) {
                setText(((Publisher) value).getName());
            }
            
            return c;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addArtistButton;
    private javax.swing.JButton addCharacterButton;
    private javax.swing.JButton addWriterButton;
    private javax.swing.JList<Artist> allArtistsList;
    private javax.swing.JList<ComicCharacter> allCharactersList;
    private javax.swing.JList<Writer> allWritersList;
    private javax.swing.JPanel artistsButtonPanel;
    private javax.swing.JPanel artistsPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField editionField;
    private javax.swing.JLabel editionLabel;
    private javax.swing.JList<ComicCharacter> featuredCharactersList;
    private javax.swing.JPanel featuredCharactersPanel;
    private javax.swing.JTextField genreField;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel powersButtonPanel2;
    private javax.swing.JComboBox<Publisher> publisherComboBox;
    private javax.swing.JLabel publisherLabel;
    private javax.swing.JButton removeArtistButton;
    private javax.swing.JButton removeCharacterButton;
    private javax.swing.JButton removeWriterButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JList<Artist> thisComicBookArtistList;
    private javax.swing.JList<Writer> thisComicBookWriterList;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel topInfoPanel;
    private javax.swing.JPanel writersButtonPanel;
    private javax.swing.JPanel writersPanel;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel<Writer> allWritersModel;
    private DefaultListModel<Writer> thisComicBookWriterModel;
    private DefaultListModel<Artist> allArtistsModel;
    private DefaultListModel<Artist> thisComicBookArtistModel;
    private DefaultListModel<ComicCharacter> allCharactersModel;
    private DefaultListModel<ComicCharacter> featuredCharactersModel;
    
}
