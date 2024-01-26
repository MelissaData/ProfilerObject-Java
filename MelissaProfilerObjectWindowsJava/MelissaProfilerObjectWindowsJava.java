import com.melissadata.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MelissaProfilerObjectWindowsJava {

  public static void main(String args[]) throws IOException {
    // Variables
    String[] arguments = ParseArguments(args);
    String license = arguments[0];
    String testFile = arguments[1];
    String dataPath = arguments[2];

    RunAsConsole(license, testFile, dataPath);
  }

  public static String[] ParseArguments(String[] args) {
    String license = "", testFile = "", dataPath = "";
    List<String> argumentStrings = Arrays.asList("--license", "-l", "--file", "-f", "--dataPath", "-d");
    for (int i = 0; i < args.length - 1; i++) {
      if ((args[i].equals("--license") || args[i].equals("-l")) && (!argumentStrings.contains(args[i + 1]))) {
        if (args[i + 1] != null) {
          license = args[i + 1];
        }
      }
      if ((args[i].equals("--file") || args[i].equals("-f")) && (!argumentStrings.contains(args[i + 1]))) {
        if (args[i + 1] != null) {
          testFile = args[i + 1];
        }
      }

      if ((args[i].equals("--dataPath") || args[i].equals("-d")) && (!argumentStrings.contains(args[i + 1]))) {
        if (args[i + 1] != null) {
          dataPath = args[i + 1];
        }
      }
    }

    return new String[] { license, testFile, dataPath };

  }

  public static void RunAsConsole(String license, String testFile, String dataPath) throws IOException {
    System.out
        .println("\n\n==================== WELCOME TO MELISSA PROFILER OBJECT WINDOWS JAVA ====================\n");
    ProfilerObject profilerObject = new ProfilerObject(license, dataPath);

    boolean shouldContinueRunning = true;

    if (!profilerObject.mdProfilerObj.GetInitializeErrorString().equals("No error.")) {
      shouldContinueRunning = false;
    }

    while (shouldContinueRunning) {
      DataContainer dataContainer = new DataContainer();
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

      if (testFile.isEmpty()) {
        System.out.println("\nFill in each value to see the Profiler Object results");

        System.out.print("File Path: ");
        dataContainer.InputFilePath = stdin.readLine();

      } else {
        dataContainer.InputFilePath = testFile;
      }

      // Print user input
      System.out
          .println("\n========================================= INPUTS ========================================\n");

      List<String> sections = dataContainer.GetWrapped(dataContainer.InputFilePath, 50);

      System.out.println("\t                Input File: " + sections.get(0));

      for (int i = 1; i < sections.size(); i++) {
        if ((i == sections.size() - 1) && sections.get(i).endsWith("/")) {
          sections.set(i, sections.get(i).substring(0, sections.get(i).length() - 1));
        }
        System.out.println("\t                            " + sections.get(i));
      }

      // Execute Profiler Object
      profilerObject.ExecuteObjectAndResultCodes(dataContainer);

      // Print output
      System.out
          .println("\n========================================= OUTPUT ========================================\n");

      System.out.println("\n                 Profiler Object Information:");
      System.out.println("\n                       TABLE STATISTICS\n\n");
      System.out
          .println("                        TableRecordCount           :  " + profilerObject.mdProfilerObj.GetTableRecordCount());
      System.out.println("                        ColumnCount                :  " + profilerObject.mdProfilerObj.GetColumnCount());
      System.out.println("");
      System.out.println(
          "                        ExactMatchDistinctCount    :  " + profilerObject.mdProfilerObj.GetTableExactMatchDistinctCount());
      System.out.println(
          "                        ExactMatchDupesCount       :  " + profilerObject.mdProfilerObj.GetTableExactMatchDupesCount());
      System.out.println(
          "                        ExactMatchLargestGroup     :  " + profilerObject.mdProfilerObj.GetTableExactMatchLargestGroup());
      System.out.println("");
      System.out.println(
          "                        ContactMatchDistinctCount  :  " + profilerObject.mdProfilerObj.GetTableContactMatchDistinctCount());
      System.out.println(
          "                        ContactMatchDupesCount     :  " + profilerObject.mdProfilerObj.GetTableContactMatchDupesCount());
      System.out.println(
          "                        ContactMatchLargestGroup   :  " + profilerObject.mdProfilerObj.GetTableContactMatchLargestGroup());
      System.out.println("");
      System.out.println("                        HouseholdMatchDistinctCount:  "
          + profilerObject.mdProfilerObj.GetTableHouseholdMatchDistinctCount());
      System.out.println(
          "                        HouseholdMatchDupesCount   :  " + profilerObject.mdProfilerObj.GetTableHouseholdMatchDupesCount());
      System.out.println("                        HouseholdMatchLargestGroup :  "
          + profilerObject.mdProfilerObj.GetTableHouseholdMatchLargestGroup());
      System.out.println("");
      System.out.println(
          "                        AddressMatchDistinctCount  :  " + profilerObject.mdProfilerObj.GetTableAddressMatchDistinctCount());
      System.out.println(
          "                        AddressMatchDupesCount     :  " + profilerObject.mdProfilerObj.GetTableAddressMatchDupesCount());
      System.out.println(
          "                        AddressMatchLargestGroup   :  " + profilerObject.mdProfilerObj.GetTableAddressMatchLargestGroup());

      System.out.println("\n\n                       COLUMN STATISTICS\n\n");

      // STATE Iterator Example
      System.out.println("                        STATE Value                 Count");
      profilerObject.mdProfilerObj.StartDataFrequency("state", mdProfiler.Order.OrderCountAscending);
      while (profilerObject.mdProfilerObj.GetNextDataFrequency("state") == 1) {
        System.out.printf("                             %-24s%-10s%n",
            profilerObject.mdProfilerObj.GetDataFrequencyValue("state"),
            profilerObject.mdProfilerObj.GetDataFrequencyCount("state"));
      }
      System.out.println("");

      // POSTAL Iterator Example
      System.out.println("                        POSTAL Pattern              Count");
      profilerObject.mdProfilerObj.StartPatternFrequency("zip", mdProfiler.Order.OrderCountAscending);
      do {
        System.out.printf("                             %-24s%-10s%n",
            profilerObject.mdProfilerObj.GetPatternFrequencyValue("zip"),
            profilerObject.mdProfilerObj.GetPatternFrequencyCount("zip"));
      } while (profilerObject.mdProfilerObj.GetNextPatternFrequency("zip") == 1);

      boolean isValid = false;
      if (!testFile.isEmpty()) {
        isValid = true;
        shouldContinueRunning = false;
      }
      while (!isValid) {
        System.out.println("\nTest another file? (Y/N)");
        String testAnotherResponse = stdin.readLine();

        if (!testAnotherResponse.isEmpty()) {
          testAnotherResponse = testAnotherResponse.toLowerCase();
          if (testAnotherResponse.equals("y")) {
            isValid = true;
          } else if (testAnotherResponse.equals("n")) {
            isValid = true;
            shouldContinueRunning = false;
          } else {
            System.out.print("Invalid Response, please respond 'Y' or 'N'");
          }
        }
      }     
    }

    // Java programs should explicitly call this function to destroy the
    // Profiler object. Do not rely on Java's GC to do this.
    profilerObject.mdProfilerObj.delete();
    System.out
        .println("\n========================= THANK YOU FOR USING MELISSA JAVA OBJECT =======================\n");
  }
}

class ProfilerObject {
  // Path to Profiler Object data files (.dat, etc)
  String dataFilePath;

  // Create instance of Melissa Profiler Object
  mdProfiler mdProfilerObj = new mdProfiler();

  public ProfilerObject(String license, String dataPath) {
    // Set license string and set path to data files (.dat, etc)
    mdProfilerObj.SetLicenseString(license);
    mdProfilerObj.SetFileName("testFile.prf");
    mdProfilerObj.SetAppendMode(mdProfiler.AppendMode.Overwrite);
    dataFilePath = dataPath;
    mdProfilerObj.SetPathToProfilerDataFiles(dataFilePath);

    mdProfilerObj.SetSortAnalysis(1); // the default is 1
    mdProfilerObj.SetMatchUpAnalysis(1); // the default is 1
    mdProfilerObj.SetRightFielderAnalysis(1); // the default is 1
    mdProfilerObj.SetDataAggregation(1); // the default is 1

    // If you see a different date than expected, check your license string and
    // either download the new data files or use the Melissa Updater program to
    // update your data files.
    mdProfiler.ProgramStatus pStatus = mdProfilerObj.InitializeDataFiles();

    if (pStatus != mdProfiler.ProgramStatus.ErrorNone) {
      // Problem during initialization
      System.out.println("Failed to Initialize Object.");
      System.out.println(pStatus);
      return;
    }
    System.out.println("                             DataBase Date: " + mdProfilerObj.GetDatabaseDate());
    System.out.println("                           Expiration Date: " + mdProfilerObj.GetLicenseExpirationDate());

    /**
     * This number should match with the file properties of the Melissa Object
     * binary file.
     * If TEST appears with the build number, there may be a license key issue.
     */
    System.out.println("                            Object Version: " + mdProfilerObj.GetBuildNumber());
    System.out.println();

  }

  // This will call the lookup function to process the input file as well as
  // generate the result codes
  public void ExecuteObjectAndResultCodes(DataContainer data) {
    mdProfilerObj.AddColumn("first", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeFirstName);
    mdProfilerObj.AddColumn("last", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeLastName);
    mdProfilerObj.AddColumn("address", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeAddress);
    mdProfilerObj.AddColumn("city", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeCity);
    mdProfilerObj.AddColumn("state", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeStateOrProvince);
    mdProfilerObj.AddColumn("zip", mdProfiler.ProfilerColumnType.ColumnTypeVariableUnicodeString,
        mdProfiler.ProfilerDataType.DataTypeZipOrPostalCode);

    List<String> records = new ArrayList<String>();
    try {
      records = Files.readAllLines(Paths.get(data.InputFilePath), StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("Error: Unable to open the input file");
      System.exit(1);
    }

    mdProfilerObj.StartProfiling();

    // Inputting the records to the Profiler Object
    for (String record : records) {
      String[] fields = record.trim().split(",");
      mdProfilerObj.SetColumn("first", fields[0]);
      mdProfilerObj.SetColumn("last", fields[1]);
      mdProfilerObj.SetColumn("address", fields[2]);
      mdProfilerObj.SetColumn("city", fields[3]);
      mdProfilerObj.SetColumn("state", fields[4]);
      mdProfilerObj.SetColumn("zip", fields[5]);

      mdProfilerObj.AddRecord();
    }

    mdProfilerObj.ProfileData();

    // ResultsCodes explain any issues Profiler Object has with the object.
    // List of result codes for Profiler Object
    // https://wiki.melissadata.com/index.php?title=Result_Code_Details#Profiler_Object

  }
}

class DataContainer {
  public String InputFilePath = "";
  public String ResultCodes = "";

  public List<String> GetWrapped(String path, int maxLineLength) {
    File file = new File(path);
    String filePath = file.getAbsolutePath();
    String[] lines = filePath.split("\\\\");
    String currentLine = "";
    List<String> wrappedString = new ArrayList<>();
    for (String section : lines) {
      if ((currentLine + section).length() > maxLineLength) {
        wrappedString.add(currentLine.trim());
        currentLine = "";
      }
      if (section.contains(path)) {
        currentLine += section;
      } else {
        currentLine += section + "\\";
      }
    }
    if (currentLine.length() > 0) {
      wrappedString.add(currentLine.trim());
    }
    return wrappedString;
  }

}
