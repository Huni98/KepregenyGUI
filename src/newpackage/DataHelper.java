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
     * Loads all comic book data from a single JSON file.
     * @param filePath The path to the comics_data.json file.
     * @return A ComicDataContainer object holding all the lists.
     */
    public static ComicDataContainer loadDataFromJSON(String filePath) {
        JSONParser parser = new JSONParser();
        ComicDataContainer data = new ComicDataContainer();

        // Create Maps to link objects by their ID
        Map<String, Publisher> publisherMap = new HashMap<>();
        Map<String, Object> creatorMap = new HashMap<>(); // Holds both Writers and Artists
        Map<String, ComicBook> comicBookMap = new HashMap<>();
        Map<String, ComicCharacter> characterMap = new HashMap<>();

        try {
            // 1. Read JSON file
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            StringBuilder jsonContent = new StringBuilder();
            while (sc.hasNextLine()) {
                jsonContent.append(sc.nextLine());
            }
            sc.close();

            // 2. Parse the entire JSON file
            JSONObject root = (JSONObject) parser.parse(jsonContent.toString());

            // --- PASS 1: Create all objects ---
            // First, we create every object with its base properties (name, title, etc.)
            // and store them in maps. This lets us link them in Pass 2.

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
            // Now that all objects exist in our maps, we loop through the JSON
            // again to build their relationships.

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
                    String publisherId = (String) e.get("publisherId");

                    Publisher p = publisherMap.get(publisherId);
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
                    for (Object pStr : powers) {
                        if (character instanceof Superhero) {
                            ((Superhero) character).addPower((String) pStr);
                        } else {
                            ((Villain) character).addPower((String) pStr);
                        }
                    }
                }
                
                // Link Creators
                JSONArray creators = (JSONArray) c.get("creators");
                for(Object crObj : creators) {
                    JSONObject cr = (JSONObject) crObj;
                    String creatorId = (String) cr.get("creatorId");
                    String role = (String) cr.get("role");
                    
                    Object creator = creatorMap.get(creatorId);
                    if (creator instanceof Writer) {
                        character.addCreator((Writer) creator, role);
                    } else if (creator instanceof Artist) {
                        character.addCreator((Artist) creator, role);
                    }
                }
                
                // Link Affiliations
                JSONArray affiliations = (JSONArray) c.get("affiliations");
                for(Object afObj : affiliations) {
                    JSONObject af = (JSONObject) afObj;
                    String charId = (String) af.get("characterId");
                    String relationship = (String) af.get("relationship");
                    
                    ComicCharacter other = characterMap.get(charId);
                    if(other != null) {
                        character.addAffiliation(other, relationship);
                    }
                }
                
                // Link Appearances
                JSONArray appearances = (JSONArray) c.get("appearances");
                for(Object appObj : appearances) {
                    String comicId = (String) appObj;
                    ComicBook comic = comicBookMap.get(comicId);
                    if(comic != null) {
                        character.addAppearance(comic);
                    }
                }
            }

            return data;

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (ParseException | java.text.ParseException e) {
            System.out.println("JSON parsing or Date parsing error: " + e.getMessage());
        }
        return null;
    }

    // Example test in main()
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter path to JSON file: ");
        // Example path: C:/Users/hunor/Desktop/comics_data.json
        String filePath = input.nextLine();

        ComicDataContainer data = loadDataFromJSON(filePath);

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
                System.out.println("Appears in: " + firstChar.getComicBookAppearances().size() + " comics");
                if(!firstChar.getComicBookAppearances().isEmpty()) {
                    System.out.println("  - " + firstChar.getComicBookAppearances().get(0).getTitle());
                }
            }

        } else {
            System.out.println("Could not load comic data.");
        }
    }
}
