/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


// Import all your project's data models
import ro.madarash.kepregeny_project.*;

// Import for parsing dates
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author hunor
 */
public class DataHelper {

    private static final String DATA_FILE = "KepregenyAdatok.json";
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

    
    public static ComicDataContainer loadDataFromJSON() {
        JSONParser parser = new JSONParser();
        ComicDataContainer data = new ComicDataContainer();

        // Create Maps to link objects by their NAME or TITLE
        Map<String, Publisher> publisherMap = new HashMap<>();
        Map<String, Object> creatorMap = new HashMap<>(); // Holds both Writers and Artists
        Map<String, ComicBook> comicBookMap = new HashMap<>();
        Map<String, ComicCharacter> characterMap = new HashMap<>(); // We will use realName as the key

        try {
            // 1. Read JSON file
            File file = new File(DATA_FILE);
            
            Scanner sc = new Scanner(file);
            StringBuilder jsonContent = new StringBuilder();
            while (sc.hasNextLine()) {
                jsonContent.append(sc.nextLine());
            }
            sc.close();

            // 2. Parse the entire JSON file
            JSONObject root = (JSONObject) parser.parse(jsonContent.toString());

            // --- PASS 1: Create all objects ---
            

            // 3. Create Publishers
            JSONArray publishersArray = (JSONArray) root.get("publishers");
            for (Object pObj : publishersArray) {
                JSONObject p = (JSONObject) pObj;
                String name = (String) p.get("name");
                String country = (String) p.get("country");
                Publisher publisher = new Publisher(name, country);
                
                data.publishers.add(publisher);
                publisherMap.put(publisher.getName(), publisher);
            }

            // 4. Create Writers
            JSONArray writersArray = (JSONArray) root.get("writers");
            for (Object wObj : writersArray) {
                JSONObject w = (JSONObject) wObj;
                String name = (String) w.get("name");
                String nationality = (String) w.get("nationality");
                Writer writer = new Writer(name, nationality);
                
                data.writers.add(writer);
                creatorMap.put(writer.getName(), writer); // Add to the generic creator map
            }

            // 5. Create Artists
            JSONArray artistsArray = (JSONArray) root.get("artists");
            for (Object aObj : artistsArray) {
                JSONObject a = (JSONObject) aObj;
                String name = (String) a.get("name");
                String nationality = (String) a.get("nationality");
                Artist artist = new Artist(name, nationality);
                
                data.artists.add(artist);
                creatorMap.put(artist.getName(), artist); // Add to the generic creator map
            }

            // 6. Create ComicBooks (Base Object)
            JSONArray comicBooksArray = (JSONArray) root.get("comicBooks");
            for (Object cbObj : comicBooksArray) {
                JSONObject cb = (JSONObject) cbObj;
                String title = (String) cb.get("title");
                String genre = (String) cb.get("genre");
                ComicBook comic = new ComicBook(title, genre);
                
                data.comicBooks.add(comic);
                comicBookMap.put(comic.getTitle(), comic);
            }

            // 7. Create Characters (Base Object)
            JSONArray charactersArray = (JSONArray) root.get("characters");
            for (Object cObj : charactersArray) {
                JSONObject c = (JSONObject) cObj;
                String realName = (String) c.get("realName");
                String alias = (String) c.get("alias");
                String origin = (String) c.get("origin");
                String type = (String) c.get("type"); // "SUPERHERO", "VILLAIN", "CIVILIAN"

                ComicCharacter character;
                switch (type.toUpperCase()) {
                    case "SUPERHERO":
                        character = new Superhero(realName, origin, alias);
                        break;
                    case "VILLAIN":
                        character = new Villain(realName, origin, alias);
                        break;
                    case "CIVILIAN":
                    default:
                        character = new Civilian(realName, origin);
                        break;
                }
                data.characters.add(character);
                characterMap.put(character.getRealName(), character);
            }

            // --- PASS 2: Link all objects ---
            // --- THIS SECTION CONTAINS THE PARSING FIXES ---

            // 8. Link ComicBooks (Writers, Artists, Editions)
            for (Object cbObj : comicBooksArray) {
                JSONObject cb = (JSONObject) cbObj;
                String title = (String) cb.get("title");
                ComicBook comic = comicBookMap.get(title);

                // Link Writers
                JSONArray writerNames = (JSONArray) cb.get("writerNames");
                for (Object wName : writerNames) {
                    Writer w = (Writer) creatorMap.get((String) wName);
                    if (w != null) comic.addWriter(w);
                }

                // Link Artists
                JSONArray artistNames = (JSONArray) cb.get("artistNames");
                for (Object aName : artistNames) {
                    Artist a = (Artist) creatorMap.get((String) aName);
                    if (a != null) comic.addArtist(a);
                }

                // Add Editions
                JSONArray editions = (JSONArray) cb.get("editions");
                for (Object eObj : editions) {
                    JSONObject e = (JSONObject) eObj;
                    String editionName = (String) e.get("editionName");
                    String dateStr = (String) e.get("publicationDate");
                    String isbn = (String) e.get("isbn");
                    
                    // --- PARSING FIX 2 ---
                    // Was "publisherId", changed to "publisherName"
                    String publisherName = (String) e.get("publisherName"); 

                    // Use the name to get the object from the map
                    Publisher p = publisherMap.get(publisherName);
                    Date pubDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

                    if (p != null) {
                        Edition edition = new Edition(editionName, pubDate, isbn, p, comic);
                        comic.addEdition(edition);
                    }
                }
            }

            // 9. Link Characters (Powers, Creators, Affiliations, Appearances)
            for (Object cObj : charactersArray) {
                JSONObject c = (JSONObject) cObj;
                String realName = (String) c.get("realName");
                ComicCharacter character = characterMap.get(realName);

                // Add Powers (only for Superhero/Villain)
                if (character instanceof Superhero || character instanceof Villain) {
                    JSONArray powers = (JSONArray) c.get("powers");
                    if (powers != null) { // Added null check for safety
                        for (Object pStr : powers) {
                            if (character instanceof Superhero) {
                                ((Superhero) character).addPower((String) pStr);
                            } else {
                                ((Villain) character).addPower((String) pStr);
                            }
                        }
                    }
                }
                
                // Link Creators
                JSONArray creators = (JSONArray) c.get("creators");
                if (creators != null) { // Added null check for safety
                    for(Object crObj : creators) {
                        JSONObject cr = (JSONObject) crObj;
                        
                        // --- PARSING FIX 3 ---
                        // Was "creatorId", changed to "creatorName"
                        String creatorName = (String) cr.get("creatorName");
                        String role = (String) cr.get("role");
                        
                        // Use the name to get the object from the map
                        Object creator = creatorMap.get(creatorName);
                        if (creator instanceof Writer) {
                            character.addCreator((Writer) creator, role);
                        } else if (creator instanceof Artist) {
                            character.addCreator((Artist) creator, role);
                        }
                    }
                }
                
                // Link Affiliations
                JSONArray affiliations = (JSONArray) c.get("affiliations");
                if (affiliations != null) { // Added null check for safety
                    for(Object afObj : affiliations) {
                        JSONObject af = (JSONObject) afObj;
                        
                        // --- PARSING FIX 4 ---
                        // Was "charId", changed to "characterRealName"
                        String charRealName = (String) af.get("characterRealName");
                        String relationship = (String) af.get("relationship");
                        
                        // Use the real name to get the object from the map
                        ComicCharacter other = characterMap.get(charRealName);
                        if(other != null) {
                            character.addAffiliation(other, relationship);
                        }
                    }
                }
                
                // Link Appearances
                JSONArray appearances = (JSONArray) c.get("appearances");
                if (appearances != null) { // Added null check for safety
                    for(Object appObj : appearances) {
                        
                        // --- PARSING FIX 5 ---
                        // Changed variable name for clarity
                        String comicTitle = (String) appObj;
                        
                        // Use the title to get the object from the map
                        ComicBook comic = comicBookMap.get(comicTitle);
                        if(comic != null) {
                            character.addAppearance(comic);
                        }
                    }
                }
            }

            return data;

        } catch (FileNotFoundException e) {
            // This error is more helpful now
            System.err.println("****************************************************************");
            System.err.println("ERROR: File not found: " + DATA_FILE);
            System.err.println("Please check that the absolute path is correct and the file exists.");
            System.err.println("****************************************************************");
        } catch (ParseException | java.text.ParseException e) {
            System.err.println("JSON parsing or Date parsing error: " + e.getMessage());
            e.printStackTrace(); // Print the full error to see where it fails
        }
        return null; // Return null if loading failed
    }
    
    
    /**
     * Saves all the application's data back to the JSON file
     * in a "pretty-printed" (indented) format.
     * @param data A ComicDataContainer holding the 5 master lists from MainDashboard.
     */
    public static void saveDataToJSON(ComicDataContainer data) {
        // This is the file we will write to
        File file = new File(DATA_FILE);
        
        // 1. Create the root JSON object (still using json-simple)
        // This part is identical to your old method.
        JSONObject root = new JSONObject();
        
        // 2. Convert Publishers List
        JSONArray publishersArray = new JSONArray();
        for (Publisher p : data.publishers) {
            JSONObject pObj = new JSONObject();
            pObj.put("name", p.getName());
            pObj.put("country", p.getCountry());
            publishersArray.add(pObj);
        }
        root.put("publishers", publishersArray);

        // 3. Convert Writers List
        JSONArray writersArray = new JSONArray();
        for (Writer w : data.writers) {
            JSONObject wObj = new JSONObject();
            wObj.put("name", w.getName());
            wObj.put("nationality", w.getNationality());
            writersArray.add(wObj);
        }
        root.put("writers", writersArray);

        // 4. Convert Artists List
        JSONArray artistsArray = new JSONArray();
        for (Artist a : data.artists) {
            JSONObject aObj = new JSONObject();
            aObj.put("name", a.getName());
            aObj.put("nationality", a.getNationality());
            artistsArray.add(aObj);
        }
        root.put("artists", artistsArray);

        // 5. Convert ComicBooks List (Complex)
        JSONArray comicBooksArray = new JSONArray();
        for (ComicBook c : data.comicBooks) {
            JSONObject cObj = new JSONObject();
            cObj.put("title", c.getTitle());
            cObj.put("genre", c.getGenre());
            
            JSONArray writerNames = new JSONArray();
            for (Writer w : c.getWriters()) {
                writerNames.add(w.getName());
            }
            cObj.put("writerNames", writerNames);
            
            JSONArray artistNames = new JSONArray();
            for (Artist a : c.getArtists()) {
                artistNames.add(a.getName());
            }
            cObj.put("artistNames", artistNames);
            
            JSONArray editionsArray = new JSONArray();
            for (Edition e : c.getEditions()) {
                JSONObject eObj = new JSONObject();
                eObj.put("editionName", e.getEditionName());
                eObj.put("publicationDate", new SimpleDateFormat("yyyy-MM-dd").format(e.getPublicationDate()));
                eObj.put("isbn", e.getIsbn());
                eObj.put("publisherName", e.getPublisher().getName());
                editionsArray.add(eObj);
            }
            cObj.put("editions", editionsArray);
            
            comicBooksArray.add(cObj);
        }
        root.put("comicBooks", comicBooksArray);

        // 6. Convert Characters List (Most Complex)
        JSONArray charactersArray = new JSONArray();
        for (ComicCharacter ch : data.characters) {
            JSONObject chObj = new JSONObject();
            chObj.put("type", ch.getClass().getSimpleName().toUpperCase());
            chObj.put("realName", ch.getRealName());
            chObj.put("origin", ch.getOrigin());

            if (ch instanceof Superhero) {
                chObj.put("alias", ((Superhero) ch).getDisplayName());
            } else if (ch instanceof Villain) {
                chObj.put("alias", ((Villain) ch).getDisplayName());
            } else {
                chObj.put("alias", ""); 
            }

            JSONArray powersArray = new JSONArray();
            if (ch instanceof Superhero) {
                powersArray.addAll(((Superhero) ch).getPowers());
            } else if (ch instanceof Villain) {
                powersArray.addAll(((Villain) ch).getPowers());
            }
            chObj.put("powers", powersArray);

            JSONArray creatorsArray = new JSONArray();
            for (java.util.Map.Entry<Writer, String> entry : ch.getCreatorWriters().entrySet()) {
                JSONObject crObj = new JSONObject();
                crObj.put("creatorName", entry.getKey().getName());
                crObj.put("role", entry.getValue());
                creatorsArray.add(crObj);
            }
            for (java.util.Map.Entry<Artist, String> entry : ch.getCreatorArtists().entrySet()) {
                JSONObject crObj = new JSONObject();
                crObj.put("creatorName", entry.getKey().getName());
                crObj.put("role", entry.getValue());
                creatorsArray.add(crObj);
            }
            chObj.put("creators", creatorsArray);

            JSONArray affArray = new JSONArray();
            for (java.util.Map.Entry<ComicCharacter, String> entry : ch.getAffiliations().entrySet()) {
                JSONObject afObj = new JSONObject();
                afObj.put("characterRealName", entry.getKey().getRealName());
                afObj.put("relationship", entry.getValue());
                affArray.add(afObj);
            }
            chObj.put("affiliations", affArray);

            JSONArray appArray = new JSONArray();
            for (ComicBook book : ch.getComicBookAppearances()) {
                appArray.add(book.getTitle());
            }
            chObj.put("appearances", appArray);
            
            charactersArray.add(chObj);
        }
        root.put("characters", charactersArray);

        // 7. --- THIS IS THE ONLY PART THAT CHANGES ---
        //    We use Gson to format and write the file.
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(file)) {
            
            // 1. Create a Gson object with pretty printing enabled
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            
            // 2. Convert the 'root' (a json-simple object) to a beautiful string
            String jsonOutput = gson.toJson(root);
            
            // 3. Write that beautiful string to the file
            fileWriter.write(jsonOutput);
            fileWriter.flush();
            
            System.out.println("Data saved with pretty-printing!");

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Example test in main()
    public static void main(String[] args) {
        // No longer asks for input
        System.out.println("Attempting to load data from " + DATA_FILE + "...");

        ComicDataContainer data = loadDataFromJSON(); // <-- Changed call

        if (data != null) {
            System.out.println("\n--- Data loaded successfully! ---");
            System.out.println("Publishers: " + data.publishers.size());
            System.out.println("Writers: " + data.writers.size());
            System.out.println("Artists: " + data.artists.size());
            System.out.println("Characters: " + data.characters.size());
            System.out.println("Comic Books: " + data.comicBooks.size());
            
            // Test a complex object
            if (!data.characters.isEmpty()) {
                System.out.println("\n--- Testing first character ---");
                ComicCharacter firstChar = data.characters.get(0);
                System.out.println("Name: " + firstChar.getDisplayName());
                
                // --- PARSING FIX 6 ---
                // Was "getComicBookAppearances", changed to "getAppearances"
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
