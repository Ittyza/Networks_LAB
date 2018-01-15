import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Describes a file's metadata: URL, file name, size, and which parts already downloaded to disk.
 *
 * The metadata (or at least which parts already downloaded to disk) is constantly stored safely in disk.
 * When constructing a new metadata object, we first check the disk to load existing metadata.
 *
 * CHALLENGE: try to avoid metadata disk footprint of O(n) in the average case
 * HINT: avoid the obvious bitmap solution, and think about ranges...
 */

class DownloadableMetadata {
    private final String metadataFilename;
    private String filename;
    private String url; 

    public double FileSize;
    public static Range[] AllRanges;
    
    private int lastRange = 0;
    public static Range[] RangesInWorker;
    public File metadataFile;
    RandomAccessFile outfile;
    public static  int [] bigArray;
    public Semaphore semi;
    
    DownloadableMetadata(String url , Range [] ranges) {
        this.url = url;
        this.filename = getName(url);
        this.metadataFilename = getMetadataName(filename);
        this.metadataFile = new File(this.metadataFilename);
        AllRanges = ranges;
        RangesInWorker = new Range[ranges.length];
        
        bigArray = new int[(int)IdcDm.fileLength];
        initRangesInFile();
    }
    
    
    //TODO NOTE THIS APPROACH MAY HAVE ISSUES
    private void initRangesInFile(){
    	try {
			 outfile = new RandomAccessFile(filename + ".txt", "rw");
			 for (int i = 0; i < AllRanges.length; i++) {
					outfile.writeBytes(AllRanges[i].getStart() + " " + AllRanges[i].getEnd()  + " "  + AllRanges[i].getLength() + "\n");
					//outfile.writeBytes(AllRanges[i].getStart() + "\n");
					//outfile.writeBytes(AllRanges[i].index + "\n");
			
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }


    private static String getMetadataName(String filename) {
        return filename + ".metadata";
    }

    private static String getName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    void addRange(Range range) throws IOException, InterruptedException {
    
    	outfile.seek(0);
    	for (int i = 0; i < range.index; i++) {
			outfile.readLine();
		}
    	outfile.writeChar('d');
  
    }

    String getFilename() {
        return filename;
    }

    boolean isCompleted() throws IOException {
    	
    	//TODO : TEST IF WORKS 
    	outfile.seek(0);
    	
    	for (int i = 0; i < AllRanges.length; i++) {
			String line = outfile.readLine();
			if(line.charAt(0) != 'd'){
				System.out.println(line.charAt(0));
				return false;
			}
		}
    	
        return true;
    }

    void delete() {
        //TODO delete metadata file 
    	// only called once isComplete come true
    }
    
    public String rangeToString(){
    	
    	//TODO NOT SURE IF WE NEED THIS BUT RETURN STRING FOR METADATA FROM RANGE 
    	
    return null;
    }
    
    public Range stringToRange(){
    	
    	//TODO: RETURN A RANGE MADE FROM THE METADATA STRING 
    	
    	return null;
    }

    Range getMissingRange() {
    	
    	// TODO : RETUEN A LIST OF NON MARKED RANGES IN METADATA FILE
    	
//    	for (int i = lastRange; i < AllRanges.length; i++) {
//			
//    		if(!AllRanges[i].isWritten){
//    			return AllRanges[i];
//    		} else {
//    			lastRange = i;
//    		}
//    		
////    		if(!AllRanges[i].inWorker && !AllRanges[i].isWritten){
////    			return AllRanges[i];
////    		} else if(AllRanges[i].inWorker && !AllRanges[i].isWritten ){
////    			return AllRanges[i];
////    		}
//    		
//		}
		return null;
    }

    String getUrl() {
        return url;
    }

    
    
    
    
}
