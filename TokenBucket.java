/**
 * A Token Bucket (https://en.wikipedia.org/wiki/Token_bucket)
 *
 * This thread-safe bucket should support the following methods:
 *
 * - take(n): remove n tokens from the bucket (blocks until n tokens are available and taken)
 * - set(n): set the bucket to contain n tokens (to allow "hard" rate limiting)
 * - add(n): add n tokens to the bucket (to allow "soft" rate limiting)
 * - terminate(): mark the bucket as terminated (used to communicate between threads)
 * - terminated(): return true if the bucket is terminated, false otherwise
 *
 */

import java.util.concurrent.atomic.*;
import java.util.concurrent.Semaphore;


class TokenBucket {
	
	private AtomicLong m_Tokens;
	private AtomicBoolean m_IsTerminated;

    TokenBucket() {
    	this.m_IsTerminated = new AtomicBoolean(false);
    	this.m_Tokens = new AtomicLong();
    }

    synchronized void take(long tokens) {
    	long currentAmount = this.m_Tokens.get();
    	this.m_Tokens.set(currentAmount - tokens); 
    	
    }

    void terminate() {
    	this.m_IsTerminated.set(true);
    }

    boolean terminated() {
    	return this.m_IsTerminated.get();
    }

    void set(long tokens) {
        this.m_Tokens.set(tokens);
    }
    
    void add(long tokens) {
    	if(this.m_Tokens.addAndGet(tokens) > 0){
    	}
    }
    
}
