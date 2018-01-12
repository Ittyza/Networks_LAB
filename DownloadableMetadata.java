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
 
    // added by me 
    private Range firstRange;
    private double size;
    private ArrayList<Range> downloadedRanges;
    
    private int[] rangeMap; /// might be needed
    
    DownloadableMetadata(String url) {
        this.url = url;
        this.filename = getName(url);
        this.metadataFilename = getMetadataName(filename);
        
        this.firstRange = null;
        this.size = 0; 
        downloadedRanges = new ArrayList<Range>();
    }

    private static String getMetadataName(String filename) {
        return filename + ".metadata";
    }

    private static String getName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    void addRange(Range range) {
        //TODO
    	
    	if(range.getStart() == 0){
    		this.firstRange = range;
    	} else if(firstRange == null){
    		this.firstRange = range;
    	} else if(range.getStart() < firstRange.getStart()){
    		this.firstRange = range;
    	}
    	
    	
    	this.downloadedRanges.add(range);
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
        
    	long rangeSize = 1024; // no idea what this number is supposed to be 
    	//TODO figure out what number to put here 
    	
    	// it might be worth it to sort the ranges. not sure
    	
    	
    	if(this.firstRange == null){
    		return new Range(0l,rangeSize);
    	}
    	
    	long startindex = -1;
    	long endIndex = -1;
    	
    	
    	Range lastcompleteRange = firstRange;
    	
    	for (int i = 0; i < downloadedRanges.size(); i++) {
			
    		Range currentRange = downloadedRanges.get(i);
    		
    		Range prev = this.getPrevRange(currentRange); 
    		
    		if(prev == null){ // then its the first one
    			
    		}
    		
    		
		}
    	
    		
		
    	
    	
    	return null;
    }

    String getUrl() {
        return url;
    }
    
    /** Added by me **/
    
    // NOTE : these functions assume the ranges dont overlap
    
    /**
     * return the range before the input range. if its there is no range before this one return null
     * 
     * @return prev range or null is there is no prev range 
     */
    private Range getPrevRange(Range i_Range){
    	
    	long currentStartIndex = i_Range.getStart();
    	long closestEndIndex = -1; 
    	Range prevRange = null;
    	for (int i = 0; i < downloadedRanges.size(); i++) {
			
    		Range current = downloadedRanges.get(i);
    		long endIndex = current.getEnd();
    		if(endIndex < currentStartIndex && endIndex > closestEndIndex){
    			prevRange = current;
    			closestEndIndex = endIndex;
    		}	
		}
    	
    	
    	return closestEndIndex == -1 ? null : prevRange;
    }
    
    /**
     * returns the next range closest to this one in the array list of downloaded ranges 
     * @return next range or null if there is no next range 
     */
    private Range getNextRange(Range i_Range){
    	
    	long currentEndIndex = i_Range.getEnd();
    	long closestStartIndex = Long.MAX_VALUE; 
    	Range nextRange = null;
    	
    	for (int i = 0; i < downloadedRanges.size(); i++) {
			
    		Range current = downloadedRanges.get(i);
    		long startIndex = current.getStart();
    		
    			if(startIndex > currentEndIndex && startIndex < closestStartIndex){
    					nextRange = current;
    					closestStartIndex = startIndex;
    			}	
		}
    	
    	if(nextRange == null){
    		return null;
    	} else {
    		return nextRange;
    	}
    }
    
    
    
    
}
