import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv){
        //OracleConnector.setCredentials("n8937250", "n8937250");
        //convertElectionDatabase("conversion.txt");
        //indexElectionDatabase("indexes.txt");

        //testInsertion();
        //testFind();
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

    public static void testInsertion(){
        PerformanceManager pm;

        // Not Indexed
        pm = new PerformanceManager("TEST_TABLE");
        pm.insertionTest(100);
        pm.insertionTest(1000);
        pm.insertionTest(10000);
        pm.insertionTest(100000);

        // Indexed
        // db.TEST_TABLE_INDEXED.createIndex({ "nome1": "text" });
        pm = new PerformanceManager("TEST_TABLE_INDEXED");
        pm.insertionTest(100);
        pm.insertionTest(1000);
        pm.insertionTest(10000);
        pm.insertionTest(100000);
    }

    public static void testFind(){
        PerformanceManager pm;

        // Not Indexed
        pm = new PerformanceManager("TEST_TABLE");
        pm.findTest(100);
        pm.findTest(1000);
        pm.findTest(10000);
        pm.findTest(100000);
        pm.findTest(1000000);

        // Indexed
        pm = new PerformanceManager("TEST_TABLE_INDEXED");
        pm.findTest(100);
        pm.findTest(1000);
        pm.findTest(10000);
        pm.findTest(100000);
        pm.findTest(1000000);
    }

    // Performance results below:
    /*
     * ########## MONGODB ##########
     * INSERTIONS
     * -- No index
     *    100 insertions: 0.62 seconds!
     *   1000 insertions: 5.59 seconds!
     *  10000 insertions: 26.42 seconds!
     * 100000 insertions: 285.51 seconds!
     *
     * -- Indexed
     *    100 insertions: 0.63 seconds!
     *   1000 insertions: 2.01 seconds!
     *  10000 insertions: 33.50 seconds!
     * 100000 insertions: 219.06 seconds!
     *
     * FINDS
     * -- No index
     *     100 finds: 0.02 seconds!
     *    1000 finds: 0.00 seconds!
     *   10000 finds: 0.01 seconds!
     *  100000 finds: 0.03 seconds!
     * 1000000 finds: 0.12 seconds!
     *
     * -- Indexed
     *     100 finds: 0.00 seconds!
     *    1000 finds: 0.00 seconds!
     *   10000 finds: 0.00 seconds!
     *  100000 finds: 0.01 seconds!
     * 1000000 finds: 0.15 seconds!
     *
     * ########## ORACLE ##########
     * INSERTIONS
     * -- No index
     *    100 insertions: 7.79 seconds!
     *   1000 insertions: 223.83 seconds!
     *  10000 insertions: 954.21 seconds!
     * 100000 insertions: 5545.3 seconds!
     *
     * -- Indexed
     *    100 insertions: 7.34 seconds!
     *   1000 insertions: 112.26 seconds!
     *  10000 insertions: 1029.89 seconds!
     * 100000 insertions: 12093.42 seconds!
     *
     * SELECTS
     * -- No index
     *     100 selects: 0.14 seconds!
     *    1000 selects: 0.22 seconds!
     *   10000 selects: 0.47 seconds!
     *  100000 selects: 5.31 seconds!
     * 1000000 selects: 52.73 seconds!
     *
     * -- Indexed
     *     100 selects: 1.37 seconds!
     *    1000 selects: 0.73 seconds!
     *   10000 selects: 0.47 seconds!
     *  100000 selects: 5.37 seconds!
     * 1000000 selects: 54.19 seconds!
     */
}