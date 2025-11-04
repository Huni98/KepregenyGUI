/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader; // Used for reading the file
import java.io.FileWriter;   // Used for writing the file
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Type; // Needed for TypeToken

// Import Gson libraries
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

// Import all your project's data models
import ro.madarash.kepregeny_project.*;

// Import for parsing dates
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException; // Added for the try/catch

/**
 *
 * @author hunor
 */
public class DataHelper {

    // This is your absolute path to the file
    private static final String DATA_FILE = "C:\\HUNI98\\GIT Projektek\\OOP-labor-feladatok\\Kepregeny_GUI\\KepregenyGUI\\KepregenyAdatok.json";
    
    /**
     * A simple public class to hold all the data lists loaded from the JSON.
     */
    public static class ComicDataContainer {
        public List<Publisher> publishers = new ArrayList<>();
        public List<Writer> writers = new ArrayList<>();
        public List<Artist> artists = new ArrayList<>();
        public List<ComicCharacter> characters = new ArrayList<>();
        public List<ComicBook> comicBooks = new ArrayList<>();
    }

    
    /**
     * --- FULLY CORRECTED GSON LOADER ---
     * Loads all comic book data from the hardcoded JSON file.
     * @return A ComicDataContainer object holding all the lists.
     */
    public static ComicDataContainer loadDataFromJSON() {
        ComicDataContainer data = new ComicDataContainer();
        Gson gson = new Gson(); // Create a Gson object

        // Create Maps to link objects by their NAME or TITLE
        Map<String, Publisher> publisherMap = new HashMap<>();
        Map<String, Object> creatorMap = new HashMap<>(); // Holds both Writers and Artists
        Map<String, ComicBook> comicBookMap = new HashMap<>();
        Map<String, ComicCharacter> characterMap = new HashMap<>(); // We will use realName as the key

        try (FileReader reader = new FileReader(DATA_FILE)) { // Use FileReader
            
            // 1. Define the type we're parsing into: a Map of Strings to Objects
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            
            // 2. Parse the entire JSON file into the 'root' map
            Map<String, Object> root = gson.fromJson(reader, mapType);

            // --- PASS 1: Create all objects ---
            
            // 3. Create Publishers
            List<Map<String, Object>> publishersArray = (List<Map<String, Object>>) root.get("publishers");
            if (publishersArray != null) {
                for (Map<String, Object> p : publishersArray) {
                    String name = (String) p.get("name");
                    String country = (String) p.get("country");
                    Publisher publisher = new Publisher(name, country);
                    
                    data.publishers.add(publisher);
                    publisherMap.put(publisher.getName(), publisher);
                }
            }

            // 4. Create Writers
            List<Map<String, Object>> writersArray = (List<Map<String, Object>>) root.get("writers");
            if (writersArray != null) {
                for (Map<String, Object> w : writersArray) {
                    String name = (String) w.get("name");
                    String nationality = (String) w.get("nationality");
                    Writer writer = new Writer(name, nationality);
                    
                    data.writers.add(writer);
                    creatorMap.put(writer.getName(), writer);
                }
            }

            // 5. Create Artists
            List<Map<String, Object>> artistsArray = (List<Map<String, Object>>) root.get("artists");
            if (artistsArray != null) {
                for (Map<String, Object> a : artistsArray) {
                    String name = (String) a.get("name");
                    String nationality = (String) a.get("nationality");
                    Artist artist = new Artist(name, nationality);
                    
                    data.artists.add(artist);
                    creatorMap.put(artist.getName(), artist);
                }
            }

            // 6. Create ComicBooks (Base Object)
            List<Map<String, Object>> comicBooksArray = (List<Map<String, Object>>) root.get("comicBooks");
            if (comicBooksArray != null) {
                for (Map<String, Object> cb : comicBooksArray) {
                    String title = (String) cb.get("title");
                    String genre = (String) cb.get("genre");
                    ComicBook comic = new ComicBook(title, genre);
                    
                    data.comicBooks.add(comic);
                    comicBookMap.put(comic.getTitle(), comic);
                }
            }

            // 7. Create Characters (Base Object)
            List<Map<String, Object>> charactersArray = (List<Map<String, Object>>) root.get("characters");
            if (charactersArray != null) {
                for (Map<String, Object> c : charactersArray) {
                    String realName = (String) c.get("realName");
                    String alias = (String) c.get("alias");
                    String origin = (String) c.get("origin");
                    String type = (String) c.get("type"); // "SUPERHERO", "VILLAIN", "CIVILIAN"

                    ComicCharacter character;
                    // Handle "N/A" or "OTHER" types by defaulting to Civilian
                    switch (type.toUpperCase()) {
                        case "SUPERHERO":
                        case "ANTIHERO": // Treat Anti-Hero as Superhero
                        case "ANTI-HERO":
                            character = new Superhero(realName, origin, alias);
                            break;
                        case "VILLAIN":
                            character = new Villain(realName, origin, alias);
                            break;
                        case "CIVILIAN":
                        case "OTHER":
                        default:
                            character = new Civilian(realName, origin);
                            break;
                    }
                    data.characters.add(character);
                    characterMap.put(character.getRealName(), character);
                }
            }

            // --- PASS 2: Link all objects ---

            // 8. Link ComicBooks (Writers, Artists, Editions)
            if (comicBooksArray != null) {
                for (Map<String, Object> cb : comicBooksArray) {
                    String title = (String) cb.get("title");
                    ComicBook comic = comicBookMap.get(title);
                    if (comic == null) continue; // Skip if comic wasn't created

                    // --- THIS IS THE FIX ---
                    // Link Writers
                    Object writerNamesObj = cb.get("writerNames");
                    if (writerNamesObj instanceof List) {
                        List<String> writerNames = (List<String>) writerNamesObj;
                        for (String wName : writerNames) {
                            Object creator = creatorMap.get(wName);
                            // Check if it's a Writer before casting
                            if (creator instanceof Writer) {
                                comic.addWriter((Writer) creator);
                            }
                        }
                    }
                    
                    // --- THIS IS THE FIX ---
                    // Link Artists
                    Object artistNamesObj = cb.get("artistNames");
                    if (artistNamesObj instanceof List) {
                        List<String> artistNames = (List<String>) artistNamesObj;
                        for (String aName : artistNames) {
                            Object creator = creatorMap.get(aName);
                            // Check if it's an Artist before casting
                            if (creator instanceof Artist) {
                                comic.addArtist((Artist) creator);
                            }
                        }
                    }
                    
                    // Add Editions
                    Object editionsObj = cb.get("editions");
                    if (editionsObj instanceof List) {
                        List<Map<String, Object>> editions = (List<Map<String, Object>>) editionsObj;
                        for (Map<String, Object> e : editions) {
                            String editionName = (String) e.get("editionName");
                            String dateStr = (String) e.get("publicationDate");
                            String isbn = (String) e.get("isbn");
                            String publisherName = (String) e.get("publisherName"); 
        
                            Publisher p = publisherMap.get(publisherName);
                            
                            // FIX FOR DATE N/A
                            Date pubDate;
                            if (dateStr == null || dateStr.equals("N/A")) {
                                pubDate = new Date(0); // Use a default epoch date
                            } else {
                                pubDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                            }
        
                            if (p != null) {
                                Edition edition = new Edition(editionName, pubDate, isbn, p, comic);
                                comic.addEdition(edition);
                            }
                        }
                    }
                }
            }

            // 9. Link Characters (Powers, Creators, Affiliations, Appearances)
            if (charactersArray != null) {
                for (Map<String, Object> c : charactersArray) {
                    String realName = (String) c.get("realName");
                    ComicCharacter character = characterMap.get(realName);
                    if (character == null) continue; // Skip if character wasn't created
    
                    // Add Powers
                    if (character instanceof Superhero || character instanceof Villain) {
                        // FIX FOR LIST N/A
                        Object powersObj = c.get("powers");
                        if (powersObj instanceof List) {
                            List<String> powers = (List<String>) powersObj;
                            for (String pStr : powers) {
                                if (character instanceof Superhero) {
                                    ((Superhero) character).addPower(pStr);
                                } else {
                                    ((Villain) character).addPower(pStr);
                                }
                            }
                        }
                    }
                    
                    // Link Creators
                    Object creatorsObj = c.get("creators");
                    if (creatorsObj instanceof List) { 
                        List<Map<String, Object>> creators = (List<Map<String, Object>>) creatorsObj;
                        for(Map<String, Object> cr : creators) {
                            String creatorName = (String) cr.get("creatorName");
                            String role = (String) cr.get("role");
                            
                            Object creator = creatorMap.get(creatorName);
                            if (creator instanceof Writer) {
                                character.addCreator((Writer) creator, role);
                            } else if (creator instanceof Artist) {
                                character.addCreator((Artist) creator, role);
                            }
                        }
                    }
                    
                    // Link Affiliations (Team Names)
                    Object affObj = c.get("affiliations");
                    if (affObj instanceof List) {
                        // This is the correct cast for ["Justice League"]
                        List<String> affiliations = (List<String>) affObj;
                        for(String teamName : affiliations) {
                            if (character instanceof Superhero) {
                                ((Superhero) character).addAffiliation(teamName);
                            } else if (character instanceof Villain) {
                                ((Villain) character).addAffiliation(teamName);
                            }
                        }
                    }
                    
                    // --- THIS IS THE MISSING FIX ---
                    // Link Character Affiliations (Relationships)
                    Object charAffObj = c.get("characterAffiliations");
                    if (charAffObj instanceof List) {
                        List<Map<String, Object>> charAffs = (List<Map<String, Object>>) charAffObj;
                        for (Map<String, Object> af : charAffs) {
                            String charRealName = (String) af.get("characterRealName");
                            String relationship = (String) af.get("relationship");
                            
                            ComicCharacter other = characterMap.get(charRealName);
                            if(other != null) {
                                // Use the renamed method
                                character.addCharacterAffiliation(other, relationship);
                            }
                        }
                    }
                    // -----------------------------
                    
                    // Link Appearances
                    Object appObj = c.get("appearances");
                     if (appObj instanceof List) { 
                        List<String> appearances = (List<String>) appObj;
                        for(String comicTitle : appearances) {
                            ComicBook comic = comicBookMap.get(comicTitle);
                            if(comic != null) {
                                character.addAppearance(comic);
                            }
                        }
                    }
                }
            }
            return data;

        } catch (FileNotFoundException e) {
            System.err.println("****************************************************************");
            System.err.println("ERROR: File not found: " + DATA_FILE);
            System.err.println("Please check that the absolute path is correct and the file exists.");
            System.err.println("****************************************************************");
        } catch (IOException | ParseException e) { // Catch IO for FileReader, Parse for Date
            System.err.println("JSON parsing, Date parsing, or File error: " + e.getMessage());
            e.printStackTrace(); // Print the full error to see where it fails
        }
        return null; // Return null if loading failed
    }

    
    /**
     * --- FULLY CORRECTED GSON SAVER ---
     * Saves all the application's data back to the JSON file
     * in a "pretty-printed" (indented) format using Gson.
     * @param data A ComicDataContainer holding the 5 master lists from MainDashboard.
     */
    public static void saveDataToJSON(ComicDataContainer data) {
        File file = new File(DATA_FILE);
        // Use LinkedHashMap to preserve the order of the keys (publishers, writers, etc.)
        Map<String, Object> root = new LinkedHashMap<>();
        
        // 2. Convert Publishers List
        List<Map<String, Object>> publishersArray = new ArrayList<>();
        if (data.publishers != null) {
            for (Publisher p : data.publishers) {
                Map<String, Object> pObj = new LinkedHashMap<>();
                pObj.put("name", p.getName());
                pObj.put("country", p.getCountry());
                publishersArray.add(pObj);
            }
        }
        root.put("publishers", publishersArray);

        // 3. Convert Writers List
        List<Map<String, Object>> writersArray = new ArrayList<>();
        if (data.writers != null) {
            for (Writer w : data.writers) {
                Map<String, Object> wObj = new LinkedHashMap<>();
                wObj.put("name", w.getName());
                wObj.put("nationality", w.getNationality());
                writersArray.add(wObj);
            }
        }
        root.put("writers", writersArray);

        // 4. Convert Artists List
        List<Map<String, Object>> artistsArray = new ArrayList<>();
        if (data.artists != null) {
            for (Artist a : data.artists) {
                Map<String, Object> aObj = new LinkedHashMap<>();
                aObj.put("name", a.getName());
                aObj.put("nationality", a.getNationality());
                artistsArray.add(aObj);
            }
        }
        root.put("artists", artistsArray);

        // 5. Convert ComicBooks List (Complex)
        List<Map<String, Object>> comicBooksArray = new ArrayList<>();
        if (data.comicBooks != null) {
            for (ComicBook c : data.comicBooks) {
                Map<String, Object> cObj = new LinkedHashMap<>();
                cObj.put("title", c.getTitle());
                cObj.put("genre", c.getGenre());
                
                List<String> writerNames = new ArrayList<>();
                if (c.getWriters() != null) { // Add null check
                    for (Writer w : c.getWriters()) {
                        writerNames.add(w.getName());
                    }
                }
                cObj.put("writerNames", writerNames);
                
                List<String> artistNames = new ArrayList<>();
                if (c.getArtists() != null) { // Add null check
                    for (Artist a : c.getArtists()) {
                        artistNames.add(a.getName());
                    }
                }
                cObj.put("artistNames", artistNames);
                
                List<Map<String, Object>> editionsArray = new ArrayList<>();
                if (c.getEditions() != null) { // Add null check
                    for (Edition e : c.getEditions()) {
                        Map<String, Object> eObj = new LinkedHashMap<>();
                        eObj.put("editionName", e.getEditionName());
                        // Format date, handle default date
                        Date pubDate = e.getPublicationDate();
                        if (pubDate == null || pubDate.getTime() == 0) { // Check for null or default epoch date
                             eObj.put("publicationDate", "N/A");
                        } else {
                            eObj.put("publicationDate", new SimpleDateFormat("yyyy-MM-dd").format(pubDate));
                        }
                        eObj.put("isbn", e.getIsbn());
                        eObj.put("publisherName", e.getPublisher().getName());
                        editionsArray.add(eObj);
                    }
                }
                cObj.put("editions", editionsArray);
                
                comicBooksArray.add(cObj);
            }
        }
        root.put("comicBooks", comicBooksArray);

        // 6. Convert Characters List (Most Complex)
        List<Map<String, Object>> charactersArray = new ArrayList<>();
        if (data.characters != null) {
            for (ComicCharacter ch : data.characters) {
                Map<String, Object> chObj = new LinkedHashMap<>();
                chObj.put("type", ch.getClass().getSimpleName().toUpperCase());
                chObj.put("realName", ch.getRealName());
                chObj.put("alias", ch.getDisplayName()); 
                chObj.put("origin", ch.getOrigin());
    
                List<String> powersArray = new ArrayList<>();
                if (ch instanceof Superhero && ((Superhero) ch).getPowers() != null) {
                    powersArray.addAll(((Superhero) ch).getPowers());
                } else if (ch instanceof Villain && ((Villain) ch).getPowers() != null) {
                    powersArray.addAll(((Villain) ch).getPowers());
                }
                chObj.put("powers", powersArray);
    
                List<Map<String, Object>> creatorsArray = new ArrayList<>();
                if (ch.getCreatorWriters() != null) {
                    for (java.util.Map.Entry<Writer, String> entry : ch.getCreatorWriters().entrySet()) {
                        Map<String, Object> crObj = new LinkedHashMap<>();
                        crObj.put("creatorName", entry.getKey().getName());
                        crObj.put("role", entry.getValue());
                        creatorsArray.add(crObj);
                    }
                }
                if (ch.getCreatorArtists() != null) {
                    for (java.util.Map.Entry<Artist, String> entry : ch.getCreatorArtists().entrySet()) {
                        Map<String, Object> crObj = new LinkedHashMap<>();
                        crObj.put("creatorName", entry.getKey().getName());
                        crObj.put("role", entry.getValue());
                        creatorsArray.add(crObj);
                    }
                }
                chObj.put("creators", creatorsArray);
    
                // Save affiliations (Team Names)
                List<String> affArray = new ArrayList<>();
                if (ch instanceof Superhero && ((Superhero) ch).getAffiliations() != null) {
                    affArray.addAll(((Superhero) ch).getAffiliations());
                } else if (ch instanceof Villain && ((Villain) ch).getAffiliations() != null) {
                    affArray.addAll(((Villain) ch).getAffiliations());
                }
                chObj.put("affiliations", affArray);

                // Save character affiliations (Relationships)
                List<Map<String, Object>> charAffArray = new ArrayList<>();
                if (ch.getCharacterAffiliations() != null) {
                    for (java.util.Map.Entry<ComicCharacter, String> entry : ch.getCharacterAffiliations().entrySet()) {
                        Map<String, Object> afObj = new LinkedHashMap<>();
                        afObj.put("characterRealName", entry.getKey().getRealName());
                        afObj.put("relationship", entry.getValue());
                        charAffArray.add(afObj);
                    }
                }
                chObj.put("characterAffiliations", charAffArray);
    
                List<String> appArray = new ArrayList<>();
                if (ch.getComicBookAppearances() != null) {
                    for (ComicBook book : ch.getComicBookAppearances()) {
                        appArray.add(book.getTitle());
                    }
                }
                chObj.put("appearances", appArray);
                
                charactersArray.add(chObj);
            }
        }
        root.put("characters", charactersArray);

        // 7. Write the file using Gson
        try (FileWriter fileWriter = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(root);
            fileWriter.write(jsonOutput);
            fileWriter.flush();
            System.out.println("Data saved with pretty-printing!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // Example test in main()
    public static void main(String[] args) {
        System.out.println("Attempting to load data from " + DATA_FILE + "...");
        ComicDataContainer data = loadDataFromJSON(); 
        if (data != null) {
            System.out.println("\n--- Data loaded successfully! ---");
            System.out.println("Publishers: " + data.publishers.size());
            System.out.println("Writers: " + data.writers.size());
            System.out.println("Artists: " + data.artists.size());
            System.out.println("Characters: " + data.characters.size());
            System.out.println("Comic Books: " + data.comicBooks.size());
            
            if (!data.characters.isEmpty()) {
                System.out.println("\n--- Testing first character ---");
                ComicCharacter firstChar = data.characters.get(0);
                System.out.println("Name: " + firstChar.getDisplayName());
                System.out.println("Appears in: " + firstChar.getComicBookAppearances().size() + " comics");
                if(!firstChar.getComicBookAppearances().isEmpty()) {
                    System.out.println("  - " + firstChar.getComicBookAppearances().get(0).getTitle());
                }
            }
        } else {
            System.out.println("Could not load comic data. See error above.");
        }
    }
}
