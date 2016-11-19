import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv){
        OracleConnector.setCredentials("n8937250", "n8937250");

        convertElectionDatabase("conversion.txt");
        indexElectionDatabase("indexes.txt");
    }

    /**
     * This function converts the specific election database used in our class for most of the semester
     * to a script that is ready to be executed in MongoDB.
     *
     * If you want to convert another database, you need to rearrange the function calls and
     * change the function arguments.
     */
    public static void convertElectionDatabase(String fileName){
        MongoConverter mc = new MongoConverter("db");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            ArrayList<ArrayList<String>> buffer = new ArrayList<>();

            System.out.println("Converting database...");
            long startTime = System.nanoTime();

            buffer.add(mc.buildCollection("ESTADO"));
            buffer.add(mc.buildCollection("CIDADE"));
            buffer.add(mc.buildCollection("ZONA"));
            buffer.add(mc.buildCollection("BAIRRO"));
            buffer.add(mc.buildCollection("URNA"));
            buffer.add(mc.buildCollection("SESSAO"));
            buffer.add(mc.buildCollection("CANDIDATO", "PARTIDO"));
            buffer.add(mc.buildCollection("CARGO"));
            buffer.add(mc.buildCollection("CANDIDATURA"));
            buffer.add(mc.buildCollection("PESQUISA"));

            buffer.add(mc.buildManyToManyRelationships("PLEITO"));
            buffer.add(mc.buildManyToManyRelationships("INTENCAODEVOTO"));

            // Writes all the information to the file
            for (ArrayList<String> commandList : buffer) {
                for (String command : commandList) {
                    writer.write(command);
                    writer.newLine();
                }
            }

            long endTime = System.nanoTime();
            System.out.printf("Database successfully converted in %.2f seconds!\n", (endTime - startTime) / 1000000000.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function builds the MongoDB indexes for the specific election
     * database used in our class for most of the semester.
     *
     * If you want to index another database, you need to rearrange the function calls and
     * change the function arguments.
     */
    public static void indexElectionDatabase(String fileName){
        MongoConverter mc = new MongoConverter("db");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            ArrayList<ArrayList<String>> buffer = new ArrayList<>();

            System.out.println("Indexing database...");
            long startTime = System.nanoTime();

            buffer.add(mc.buildIndexes("ESTADO"));
            buffer.add(mc.buildIndexes("CIDADE"));
            buffer.add(mc.buildIndexes("ZONA"));
            buffer.add(mc.buildIndexes("BAIRRO"));
            buffer.add(mc.buildIndexes("URNA"));
            buffer.add(mc.buildIndexes("SESSAO"));
            buffer.add(mc.buildIndexes("CANDIDATO"));
            buffer.add(mc.buildIndexes("CARGO"));
            buffer.add(mc.buildIndexes("CANDIDATURA"));
            buffer.add(mc.buildIndexes("PESQUISA"));

            // Writes all the information to the file
            for (ArrayList<String> commandList : buffer) {
                for (String command : commandList) {
                    writer.write(command);
                    writer.newLine();
                }
            }

            long endTime = System.nanoTime();
            System.out.printf("Database successfully indexed in %.2f seconds!\n", (endTime - startTime) / 1000000000.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
