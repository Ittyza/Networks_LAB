import java.io.File;
import java.util.ArrayList;

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
    
    
    public static Range[] RangesInWorker;
    public File metadataFile;
    
    public static  int [] bigArray;
    
    DownloadableMetadata(String url , Range [] ranges) {
        this.url = url;
        this.filename = getName(url);
        this.metadataFilename = getMetadataName(filename);
        this.metadataFile = new File(this.metadataFilename);
        AllRanges = ranges;
        RangesInWorker = new Range[ranges.length];
        
        bigArray = new int[(int)IdcDm.fileLength];
    }


    private static String getMetadataName(String filename) {
        return filename + ".metadata";
    }

    private static String getName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    void addRange(Range range) {
    	//this.downloadedRanges.add(range);
    }

    String getFilename() {
        return filename;
    }

    boolean isCompleted() {
        return true;
    }

    void delete() {
        
    }

    Range getMissingRange() {
    	for (int i = 0; i < AllRanges.length; i++) {
			
    		if(!AllRanges[i].isWritten){
    			return AllRanges[i];
    		}
    		
//    		if(!AllRanges[i].inWorker && !AllRanges[i].isWritten){
//    			return AllRanges[i];
//    		} else if(AllRanges[i].inWorker && !AllRanges[i].isWritten ){
//    			return AllRanges[i];
//    		}
    		
		}
		return null;
    }

    String getUrl() {
        return url;
    }

    
    
    
    
}
