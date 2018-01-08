/**
 * A token bucket based rate-limiter.
 *
 * This class should implement a "soft" rate limiter by adding maxBytesPerSecond tokens to the bucket every second,
 * or a "hard" rate limiter by resetting the bucket to maxBytesPerSecond tokens every second.
 */
public class RateLimiter implements Runnable {
    private final TokenBucket tokenBucket;
    private final Long maxBytesPerSecond;

    RateLimiter(TokenBucket tokenBucket, Long maxBytesPerSecond) {
        this.tokenBucket = tokenBucket;
        this.maxBytesPerSecond = maxBytesPerSecond;
    }

    @Override
    public void run() {
        //TODO
        
        /* Soft rate limiter - use Tread.sleep() to time the tokens */
        
        while(true){
            tokenBucket.set(maxBytesPerSecond);
            
            try {
                Thread.sleep(1000);
            } catch (Exception e){
                
            }
        }
        
    }
}
