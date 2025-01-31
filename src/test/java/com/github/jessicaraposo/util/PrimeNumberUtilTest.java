package com.github.jessicaraposo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimeNumberUtilTest {

    @Test
    void shouldReturnFalseForNumbersLessThanTwo() {
        assertFalse(PrimeNumberUtil.isPrime(-5));
        assertFalse(PrimeNumberUtil.isPrime(0));
        assertFalse(PrimeNumberUtil.isPrime(1));
    }

    @Test
    void shouldReturnTrueForPrimeNumbers() {
        assertTrue(PrimeNumberUtil.isPrime(2));
        assertTrue(PrimeNumberUtil.isPrime(3));
        assertTrue(PrimeNumberUtil.isPrime(5));
        assertTrue(PrimeNumberUtil.isPrime(7));
        assertTrue(PrimeNumberUtil.isPrime(11));
        assertTrue(PrimeNumberUtil.isPrime(13));
        assertTrue(PrimeNumberUtil.isPrime(17));
        assertTrue(PrimeNumberUtil.isPrime(19));
    }

    @Test
    void shouldReturnFalseForNonPrimeNumbers() {
        assertFalse(PrimeNumberUtil.isPrime(4));
        assertFalse(PrimeNumberUtil.isPrime(6));
        assertFalse(PrimeNumberUtil.isPrime(8));
        assertFalse(PrimeNumberUtil.isPrime(9));
        assertFalse(PrimeNumberUtil.isPrime(10));
        assertFalse(PrimeNumberUtil.isPrime(12));
        assertFalse(PrimeNumberUtil.isPrime(15));
        assertFalse(PrimeNumberUtil.isPrime(18));
    }

    @Test
    void shouldReturnTrueForLargePrimeNumbers() {
        assertTrue(PrimeNumberUtil.isPrime(101));
        assertTrue(PrimeNumberUtil.isPrime(103));
        assertTrue(PrimeNumberUtil.isPrime(107));
        assertTrue(PrimeNumberUtil.isPrime(109));
    }

    @Test
    void shouldReturnFalseForLargeNonPrimeNumbers() {
        assertFalse(PrimeNumberUtil.isPrime(100));
        assertFalse(PrimeNumberUtil.isPrime(102));
        assertFalse(PrimeNumberUtil.isPrime(104));
        assertFalse(PrimeNumberUtil.isPrime(110));
    }
}
